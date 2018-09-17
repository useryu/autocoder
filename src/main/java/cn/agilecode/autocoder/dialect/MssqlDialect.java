package cn.agilecode.autocoder.dialect;

import java.sql.DatabaseMetaData;

/**
 * MysqlDialect.
 */
public class MssqlDialect extends Dialect {
	
	public MssqlDialect(DatabaseMetaData dbMeta) {
		super(dbMeta);
	}


	public String forTableBuilderDoBuild(String tableName) {
		return "select * from [" + tableName + "] where 1 = 2";
	}
	

	public static String fetchColumnRemarksSql(String tableName) {
		return "SELECT a.name,a.is_nullable,c.name type_name,cast([value] as varchar(500))[value] FROM sys.columns a inner join systypes c on a.user_type_id=c.xusertype left join sys.extended_properties g on (a.object_id = g.major_id AND g.minor_id = a.column_id) "
					+"WHERE object_id in (SELECT object_id FROM sys.tables where name ='"+tableName+"')";
	}
	
	public static String fetchAllTableRemarksSql() {
		return "select a.name,cast([value] as varchar(500))[value] from sys.tables a left join sys.extended_properties g on (a.object_id = g.major_id AND g.minor_id = 0)";
	}
	
	public String forDbFindById(String tableName, String[] pKeys) {
		tableName = tableName.trim();
		trimPrimaryKeys(pKeys);
		
		StringBuilder sql = new StringBuilder("select * from [").append(tableName).append("] where ");
		for (int i=0; i<pKeys.length; i++) {
			if (i > 0) {
				sql.append(" and ");
			}
			sql.append("").append(pKeys[i]).append(" = ?");
		}
		return sql.toString();
	}
	
	public String forDbDeleteById(String tableName, String[] pKeys) {
		tableName = tableName.trim();
		trimPrimaryKeys(pKeys);
		
		StringBuilder sql = new StringBuilder("delete from [").append(tableName).append("] where ");
		for (int i=0; i<pKeys.length; i++) {
			if (i > 0) {
				sql.append(" and ");
			}
			sql.append("").append(pKeys[i]).append(" = ?");
		}
		return sql.toString();
	}
	
	/**
	 * Do not delete the String[] pKeys parameter, the element of pKeys needs to trim()
	 */
	
	public String forPaginate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		int offset = pageSize * (pageNumber - 1);
		StringBuilder ret = new StringBuilder();
		ret.append(select).append(" ").append(sqlExceptSelect);
		ret.append(" limit ").append(offset).append(", ").append(pageSize);	// limit can use one or two '?' to pass paras
		return ret.toString();
	}


	@Override
	public String getShemaPattern() {
		return "dbo";
	}


	/**
	 * sys 打头的表是系统表，不需要生成
	 */
	@Override
	public boolean isSkipTable(String tableName) {
		return tableName.startsWith("sys");
	}

	
	
}
