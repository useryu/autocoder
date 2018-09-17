package cn.agilecode.autocoder.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import cn.agilecode.autocoder.dialect.Dialect;
import cn.agilecode.autocoder.dialect.MssqlDialect;
import cn.agilecode.autocoder.dialect.OracleDialect;
import cn.agilecode.autocoder.util.StrKit;

/**
 * MetaBuilder
 */
public class MetaBuilder {

	protected DataSource dataSource;
	protected Dialect dialect;
	protected Set<String> excludedTables = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	protected Connection conn = null;
	protected DatabaseMetaData dbMeta = null;

	protected String[] removedTableNamePrefixes = null;
	
	protected String includeTableNamePrefixe = null;

	protected TypeMapping typeMapping = new TypeMapping();

	public MetaBuilder(DataSource dataSource) {
		if (dataSource == null) {
			throw new IllegalArgumentException("dataSource can not be null.");
		}
		this.dataSource = dataSource;
		this.fetchDbMeta();
	}

	private void fetchDbMeta() {
		try {
			conn = dataSource.getConnection();
			dbMeta = conn.getMetaData();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
		}
	}

	public void addExcludedTable(String... excludedTables) {
		if (excludedTables != null) {
			for (String table : excludedTables) {
				this.excludedTables.add(table);
			}
		}
	}

	/**
	 * 设置需要被移除的表名前缀，仅用于生成 modelName 与 baseModelName 例如表名 "osc_account"，移除前缀
	 * "osc_" 后变为 "account"
	 */
	public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
		this.removedTableNamePrefixes = removedTableNamePrefixes;
	}
	
	/**
	 * 设置要处理的表名前缀，如果设置了，则只会处理带有这些前缀的表
	 */
	public void setIncludeTableNamePrefixes(String includeTableNamePrefixe) {
		this.includeTableNamePrefixe = includeTableNamePrefixe;
	}

	public void setTypeMapping(TypeMapping typeMapping) {
		if (typeMapping != null) {
			this.typeMapping = typeMapping;
		}
	}

	public List<TableMeta> build() {
		System.out.println("Build TableMeta ...");
		try {
			List<TableMeta> tableMetas = new ArrayList<TableMeta>();
			buildTableMetas(tableMetas);
			for (TableMeta tableMeta : tableMetas) {
				buildPrimaryKey(tableMeta);
				buildColumnMetas(tableMeta);
				buildForeignKey(tableMeta);
				buildAditionColumnInfo(tableMeta);
			}
			for (TableMeta tableMeta : tableMetas) {
				processManyToMany(tableMeta, tableMetas);
			}
			for (TableMeta tableMeta : tableMetas) {
				processManyToOne(tableMeta, tableMetas);
			}
			return tableMetas;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					if(!conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * 通过继承并覆盖此方法，跳过一些不希望处理的 table，定制更加灵活的 table 过滤规则
	 * 
	 * @return 返回 true 时将跳过当前 tableName 的处理
	 */
	protected boolean isSkipTable(String tableName) {
		return dialect.isSkipTable(tableName);
	}

	/**
	 * 构造 modelName，mysql 的 tableName 建议使用小写字母，多单词表名使用下划线分隔，不建议使用驼峰命名 oracle 之下的
	 * tableName 建议使用下划线分隔多单词名，无论 mysql还是 oralce，tableName 都不建议使用驼峰命名
	 */
	protected String buildModelName(String tableName) {
		// 移除表名前缀仅用于生成 modelName、baseModelName，而 tableMeta.name 表名自身不能受影响
		if (removedTableNamePrefixes != null) {
			for (String prefix : removedTableNamePrefixes) {
				if (tableName.startsWith(prefix)) {
					tableName = tableName.replaceFirst(prefix, "");
					break;
				}
			}
		}

		// 将 oralce 大写的 tableName 转成小写，再生成 modelName
		if (dialect.isToLowerCase()) {
			tableName = tableName.toLowerCase();
		}

		return StrKit.firstCharToUpperCase(StrKit.toCamelCase(tableName));
	}

	/**
	 * 不同数据库 dbMeta.getTables(...) 的 schemaPattern 参数意义不同 1：oracle 数据库这个参数代表
	 * dbMeta.getUserName() 2：postgresql 数据库中需要在 jdbcUrl中配置 schemaPatter，例如：
	 * jdbc:postgresql://localhost:15432/djpt?currentSchema=public,sys,app
	 * 最后的参数就是搜索schema的顺序， 下测试成功 3：开发者若在其它库中发现工作不正常，可通过继承
	 * MetaBuilder并覆盖此方法来实现功能
	 */
	protected ResultSet getTablesResultSet() throws SQLException {
		String schemaPattern = dialect.getShemaPattern();// instanceof OracleDialect ? dbMeta.getUserName() : null;
		String tableNamePattern = dialect.getTableNamePattern();
		return dbMeta.getTables(conn.getCatalog(), schemaPattern, tableNamePattern, new String[] { "TABLE" });
	}

	protected void buildTableMetas(List<TableMeta> ret) throws SQLException {
		ResultSet rs = getTablesResultSet();
	      
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");

			if (excludedTables.contains(tableName)) {
				System.out.println("Skip excludedTables :" + tableName);
				continue;
			}
			if (isSkipTable(tableName)) {
				System.out.println("Skip table by dialect:" + tableName);
				continue;
			}
			if(this.includeTableNamePrefixe!=null && !tableName.startsWith(this.includeTableNamePrefixe)) {
				continue;
			}

			TableMeta tableMeta = new TableMeta();
			tableMeta.setName(tableName);
			tableMeta.setRemarks(rs.getString("REMARKS"));

			tableMeta.setModelName(buildModelName(tableName));
			String tableHibernateDef = "name = \""+ tableName + "\"";
			tableMeta.setTableHibernateDef(tableHibernateDef );
			if(StrKit.isBlank(tableMeta.getRemarks())) {
				tableMeta.setRemarks(tableMeta.getModelName());
			}
			ret.add(tableMeta);
		}
		rs.close();
		
		//mssql server 只能从特定视图取备注信息，不通过JDBC META API取
		if(this.dialect instanceof MssqlDialect) {
			String fetchAllTableRemarksSql = MssqlDialect.fetchAllTableRemarksSql();
			rs = conn.createStatement().executeQuery(fetchAllTableRemarksSql);
			while(rs.next()) {
				for(TableMeta table:ret) {
					if(rs.getString("name").equalsIgnoreCase(table.getName())) {
						table.setRemarks(rs.getString("value"));
						if(StrKit.isBlank(table.getRemarks())) {
							table.setRemarks(table.getModelName());
						}
						break;
					}
				}
			}
			rs.close();
		}
		
	}

	private void setMssqlColumnRemarks(TableMeta table, String fetchColumnRemarksSql) throws SQLException {
		ResultSet rs = conn.createStatement().executeQuery(fetchColumnRemarksSql);
		while(rs.next()) {
			for(ColumnMeta col : table.getColumnMetas()) {
				if(col.getName().equalsIgnoreCase(rs.getString("name"))) {
					String type = rs.getString("type_name");
					String remarks = rs.getString("value");
					String isNullable = rs.getString("is_nullable");
					if(StrKit.isBlank(remarks)) {
						remarks = col.getAttrName();
					}
					col.setRemarks(remarks);
					col.setType(type);
					col.setIsNullable(isNullable);
					break;
				}
			}
		}
		rs.close();
	}

	protected void buildPrimaryKey(TableMeta tableMeta) throws SQLException {
		ResultSet rs = dbMeta.getPrimaryKeys(conn.getCatalog(), null, tableMeta.getName());

		String primaryKey = "";
		int index = 0;
		while (rs.next()) {
			if (index++ > 0)
				primaryKey += ",";
			primaryKey += rs.getString("COLUMN_NAME");
		}
		tableMeta.setPrimaryKey(primaryKey);
		rs.close();
	}
	
	protected void buildForeignKey(TableMeta tableMeta) throws SQLException {
		ResultSet rs = dbMeta.getImportedKeys(conn.getCatalog(), null, tableMeta.getName());
		while (rs.next()) {
			FkMeta fk = new FkMeta();
			fk.setFkTable(rs.getString("PKTABLE_NAME"));
			fk.setFkTableColumn(rs.getString("PKCOLUMN_NAME"));
			fk.setColumnName(rs.getString("FKCOLUMN_NAME"));
			fk.setFkName(rs.getString("FK_NAME"));
			tableMeta.getFkMetas().add(fk);
		}
		rs.close();
	}
	
	/**
	 * @deprecated 暂时没用上
	 * @param tableMeta
	 * @throws SQLException
	 */
	protected void buildForeignKeyByName(TableMeta tableMeta) throws SQLException {
		for (ColumnMeta col : tableMeta.getColumnMetas()) {
			if(col.getName().endsWith("_id")) {
				String fkTable = col.getName().substring(0,col.getName().lastIndexOf("_id"));
				FkMeta fk = new FkMeta();
				fk.setFkTable(fkTable);
				fk.setFkTableColumn("id");
				fk.setColumnName("id");
				fk.setFkName(col.getName());
				tableMeta.getFkMetas().add(fk);
			}
		}
	}

	protected void processManyToMany(TableMeta tableMeta, List<TableMeta> tableMetas) {
		//一共只有二个字段，有两个外键字段。说明这是一个关联表
		List<FkMeta> fkMetas = tableMeta.getFkMetas();
		List<ColumnMeta> columnMetas = tableMeta.getColumnMetas();
		if(columnMetas.size()==2 && fkMetas.size()==2) {
			//find side1 and side2
			TableMeta side1 = null;
			FkMeta fk1 = null;
			TableMeta side2 = null;
			FkMeta fk2 = null;
			
			for(TableMeta oneSideTable:tableMetas) {
				if(oneSideTable.getName().equalsIgnoreCase(fkMetas.get(0).getFkTable())) {
					side1 = oneSideTable;
					fk1 = fkMetas.get(0);
				}
				if(oneSideTable.getName().equalsIgnoreCase(fkMetas.get(1).getFkTable())) {
					side2 = oneSideTable;
					fk2 = fkMetas.get(1);
				}
			}
			
			if(side1 == null || side2 == null) {
				return;
			}
			
			ColumnMeta setCol1 = new ColumnMeta();
			String fieldName1 = side2.getName()+"s";
			setCol1.setName(fieldName1);
			setCol1.setAttrName(buildAttrName(fieldName1));
			setCol1.setUpperFirstCharName(StrKit.firstCharToUpperCase(setCol1.getAttrName()));
			setCol1.setJavaType("Set<"+buildModelName(side2.getName())+">");
			String initFieldStr1 = " = new HashSet<"+buildModelName(side2.getName())+">(0)";
			setCol1.setInitFieldStr(initFieldStr1);
			setCol1.setRemarks("");
			setCol1.setHibernateDef("@ManyToMany"+System.lineSeparator()
				+"\t@JoinTable(name=\""+tableMeta.getName()+"\", joinColumns={@JoinColumn(name=\""+fk1.getColumnName()+"\")}"
						+ ", inverseJoinColumns={@JoinColumn(name=\""+fk2.getColumnName()+"\")})");
			setCol1.setShowInTostringMethod(false);
			setCol1.setShowInList(false);
			side1.getColumnMetas().add(setCol1);
			
			ColumnMeta setCol2 = new ColumnMeta();
			String fieldName2 = side1.getName()+"s";
			setCol2.setName(fieldName2);
			setCol2.setAttrName(buildAttrName(fieldName2));
			setCol2.setUpperFirstCharName(StrKit.firstCharToUpperCase(setCol2.getAttrName()));
			setCol2.setJavaType("Set<"+buildModelName(side1.getName())+">");
			String initFieldStr2 = " = new HashSet<"+buildModelName(side1.getName())+">(0)";
			setCol2.setInitFieldStr(initFieldStr2);
			setCol2.setRemarks("");
			setCol2.setHibernateDef("@ManyToMany(mappedBy=\""+fieldName1+"\")");
			setCol2.setShowInTostringMethod(false);
			setCol2.setShowInList(false);
			side2.getColumnMetas().add(setCol2);
			
			//清空后，就不会当成ManyToOne再处理一次
			fkMetas.clear();
			tableMeta.setNoNeedModel(true);
		}
	}
	
	protected void processManyToOne(TableMeta tableMeta, List<TableMeta> tableMetas) {
		for(FkMeta fk:tableMeta.getFkMetas()) {
			String fkColumnName = fk.getColumnName();
			ColumnMeta col = tableMeta.findColByName(fkColumnName);
			if(col==null) {
				continue;
			}
			String fkModelName = buildModelName(fk.getFkTable());
			col.setJavaType(fkModelName);
			String colName = StrKit.firstCharToLowerCase(buildAttrName(removeId(fkColumnName)));
			col.setAttrName(colName);
			col.setName(colName);
			col.setUpperFirstCharName(StrKit.firstCharToUpperCase(colName));
			String manyToOneDef = "@ManyToOne(fetch = FetchType.EAGER)"+System.lineSeparator()+"\t@JoinColumn(name = \""+fkColumnName+"\", nullable = false)";
			col.setHibernateDef(manyToOneDef);
			col.setShowInTostringMethod(false);
			col.setShowInList(false);
			col.setObjectCol(true);
			
			//process one side
			for(TableMeta oneSideTable:tableMetas) {
				if(oneSideTable.getName().equalsIgnoreCase(fk.getFkTable())) {
					for(ColumnMeta oneSideCol:oneSideTable.getColumnMetas()) {
						if(oneSideCol.getName().equalsIgnoreCase(fk.getFkTableColumn())) {
							ColumnMeta setCol = new ColumnMeta();
							String fieldName = tableMeta.getName();
							setCol.setName(fieldName);
							String manySideModeName = buildModelName(tableMeta.getName());
							setCol.setAttrName(StrKit.firstCharToLowerCase(buildAttrName(manySideModeName))+"s");
							setCol.setUpperFirstCharName(StrKit.firstCharToUpperCase(setCol.getAttrName()));
							setCol.setJavaType("Set<"+manySideModeName+">");
							String initFieldStr = " = new HashSet<"+manySideModeName+">(0)";
							setCol.setInitFieldStr(initFieldStr );
							setCol.setRemarks("");
							setCol.setHibernateDef("@IndexedEmbedded"+System.lineSeparator()+"\t@OneToMany(fetch = FetchType.LAZY, mappedBy = \""+StrKit.firstCharToLowerCase(oneSideTable.getModelName())+"\")");
							setCol.setShowInTostringMethod(false);
							setCol.setSetCol(true);
							setCol.setShowInList(false);
							oneSideTable.getColumnMetas().add(setCol);
							tableMeta.getOneSideModeNames().add(oneSideTable.getModelName());
							break;
						}
					}
				}
			}
		}
	}

	private String removeId(String fkColumnName) {
		if(fkColumnName.lastIndexOf("_id")==-1) {
			return fkColumnName;
		}
		return fkColumnName.substring(0,fkColumnName.lastIndexOf("_id"));
	}
	
	protected void buildAditionColumnInfo(TableMeta tableMeta) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			ResultSet rs = dbMeta.getColumns(conn.getCatalog(), null, tableMeta.getName(), null);
			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME"); // 名称
				ColumnMeta columnMeta = tableMeta.findColByName(name);
				
				String isNullable = rs.getString("IS_NULLABLE"); // 是否允许
				columnMeta.setIsNullable(isNullable);
				
				String isPrimaryKey = "   ";
				String[] keys = tableMeta.getPrimaryKey().split(",");
				for (String key : keys) {
					if (key.equalsIgnoreCase(columnMeta.getName())) {
						isPrimaryKey = "PRI";
						break;
					}
				}
				columnMeta.setIsPrimaryKey(isPrimaryKey);

				String defaultValue = rs.getString("COLUMN_DEF"); // 默认值
				columnMeta.setDefaultValue(defaultValue);

				String remarks = rs.getString("REMARKS"); // 备注
				columnMeta.setRemarks(remarks == null? columnMeta.getAttrName():remarks);
			}
			rs.close();
			//mssql server 只能从特定视图取备注信息，不通过JDBC META API取
			if(this.dialect instanceof MssqlDialect) {
				this.setMssqlColumnRemarks(tableMeta, MssqlDialect.fetchColumnRemarksSql(tableMeta.getName()));
			}
			
			for(ColumnMeta col:tableMeta.getColumnMetas()) {
				String remarks = col.getRemarks();
				if(remarks!=null && remarks.contains("|")) {
					int cmdIndex = remarks.indexOf("|");
					String cmdStr = remarks.substring(cmdIndex+1);
					col.setRemarks(remarks.substring(0,cmdIndex));
					String[] cmds = cmdStr.split(",");
					for(String cmd:cmds) {
						if(cmd.equalsIgnoreCase("notInList")) {
							col.setShowInList(false);
						}
						// other command process here
					}
					
				}
				
				//字符类型默认参与全局搜索
				if(col.getJavaType().equalsIgnoreCase("java.lang.String")) {
					col.setFullTextSearch(true);
				}
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 文档参考： http://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-
	 * conversions.html
	 * 
	 * JDBC 与时间有关类型转换规则，mysql 类型到 java 类型如下对应关系： DATE java.sql.Date DATETIME
	 * java.sql.Timestamp TIMESTAMP[(M)] java.sql.Timestamp TIME java.sql.Time
	 * 
	 * 对数据库的 DATE、DATETIME、TIMESTAMP、TIME 四种类型注入 new
	 * java.util.Date()对象保存到库以后可以达到“秒精度” 为了便捷性，getter、setter 方法中对上述四种字段类型采用
	 * java.util.Date，可通过定制 TypeMapping 改变此映射规则
	 */
	protected void buildColumnMetas(TableMeta tableMeta) throws SQLException {
		String sql = dialect.forTableBuilderDoBuild(tableMeta.getName());
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			ColumnMeta cm = new ColumnMeta();
			cm.setName(rsmd.getColumnName(i));
			if(cm.getName().equals("birthday")) {
				System.out.println("gender type"+rsmd.getColumnType(i));
			}
			String colClassName = rsmd.getColumnClassName(i);
			String typeStr = typeMapping.getType(colClassName);
			if (typeStr != null) {
				cm.setJavaType(typeStr);
			} else {
				int type = rsmd.getColumnType(i);
				if (type == Types.BINARY || type == Types.VARBINARY) {
					cm.setJavaType("java.lang.Boolean");
				} else if (type == Types.BLOB) {
					cm.setJavaType("byte[]");
				} else if (type == Types.CLOB || type == Types.NCLOB) {
					cm.setJavaType("java.lang.String");
				} else {
					cm.setJavaType("java.lang.String");
				}
			}

			if(cm.getName().startsWith("is_") && cm.getJavaType().equals("java.lang.Integer")) {
				cm.setJavaType("java.lang.Boolean");
			}

			if(cm.getName().startsWith("has_") && cm.getJavaType().equals("java.lang.Integer")) {
				cm.setJavaType("java.lang.Boolean");
			}
			
			if(cm.getName().equalsIgnoreCase("id")) {
				cm.setAttrName("id");
				cm.setJavaType("java.lang.Long");
			}
			
			// 构造字段对应的属性名 attrName
			cm.setAttrName(buildAttrName(cm.getName()));
			cm.setUpperFirstCharName(StrKit.firstCharToUpperCase(cm.getAttrName()));
			this.buildHibernateAnotations(cm);
			tableMeta.getColumnMetas().add(cm);
		}

		rs.close();
		stm.close();
		
	}

	private void buildHibernateAnotations(ColumnMeta cm) {
		StringBuffer def = new StringBuffer();
		String lineBreaker = System.lineSeparator() + "\t";
		if (cm.getName().equals("id")) {
			def.append("@Id").append(lineBreaker).append("@GeneratedValue(strategy = GenerationType.AUTO)")
					.append(lineBreaker).append("@Column(name = \"id\", nullable = false)").append(lineBreaker)
					.append("@DocumentId");
		} else if (cm.getJavaType().equals("java.util.Date")) {
			def.append("@Temporal(TemporalType.DATE)").append(lineBreaker)
					.append("@Column(name = \"" + cm.getName() + "\")");
		} else {
			def.append("@Field").append(lineBreaker).append("@Column(name = \"" + cm.getName() + "\")");
		}
		cm.setHibernateDef(def.toString());
	}

	/**
	 * 构造 colName 所对应的 attrName，mysql 数据库建议使用小写字段名或者驼峰字段名 Oralce
	 * 反射将得到大写字段名，所以不建议使用驼峰命名，建议使用下划线分隔单词命名法
	 */
	protected String buildAttrName(String colName) {
		if (dialect instanceof OracleDialect) {
			colName = colName.toLowerCase();
		}
		return StrKit.toCamelCase(colName);
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public DatabaseMetaData getDbMeta() {
		return dbMeta;
	}

}
