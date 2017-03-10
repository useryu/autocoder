package cn.agilecode.autocoder.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import cn.agilecode.autocoder.metadata.ColumnMeta;
import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.util.StrKit;

/**
 * DataDictionary 数据字典生成器
 */
public class DataDictionaryGenerator {

	protected DataSource dataSource;
	protected String dataDictionaryOutputDir;
	protected String dataDictionaryFileName = "_DataDictionary.txt";

	public DataDictionaryGenerator(DataSource dataSource, String dataDictionaryOutputDir) {
		this.dataSource = dataSource;
		this.dataDictionaryOutputDir = dataDictionaryOutputDir;
	}

	public void setDataDictionaryOutputDir(String dataDictionaryOutputDir) {
		if (StrKit.notBlank(dataDictionaryOutputDir))
			this.dataDictionaryOutputDir = dataDictionaryOutputDir;
	}

	public void setDataDictionaryFileName(String dataDictionaryFileName) {
		if (StrKit.notBlank(dataDictionaryFileName))
			this.dataDictionaryFileName = dataDictionaryFileName;
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate DataDictionary file ...");
		rebuildColumnMetas(tableMetas);

		StringBuilder ret = new StringBuilder();
		for (TableMeta tableMeta : tableMetas)
			generateTable(tableMeta, ret);

		wirtToFile(ret);
	}

	protected void generateTable(TableMeta tableMeta, StringBuilder ret) {
		ret.append("Table: ").append(tableMeta.getName());
		if (StrKit.notBlank(tableMeta.getRemarks()))
			ret.append("\tRemarks: ").append(tableMeta.getRemarks());
		ret.append("\n");

		String sparateLine = genSeparateLine(tableMeta);
		ret.append(sparateLine);
		genTableHead(tableMeta, ret);
		ret.append(sparateLine);
		for (ColumnMeta columnMeta : tableMeta.getColumnMetas())
			genColumn(tableMeta, columnMeta, ret);
		ret.append(sparateLine);
		ret.append("\n");
	}

	/*
	 * -----------+---------+------+-----+---------+---------------- Field |
	 * Type | Null | Key | Default | Remarks
	 * -----------+---------+------+-----+---------+---------------- id |
	 * int(11) | NO | PRI | NULL | remarks here
	 */
	protected void genCell(int columnMaxLen, String preChar, String value, String fillChar, String postChar,
			StringBuilder ret) {
		ret.append(preChar);
		ret.append(value);
		for (int i = 0, n = columnMaxLen - value.length() + 1; i < n; i++)
			ret.append(fillChar); // 值后的填充字符，值为 " "、"-"
		ret.append(postChar);
	}

	protected String genSeparateLine(TableMeta tm) {
		StringBuilder ret = new StringBuilder();
		genCell(tm.getColNameMaxLen(), "-", "---", "-", "+", ret);
		genCell(tm.getColTypeMaxLen(), "-", "---", "-", "+", ret);
		genCell("Null".length(), "-", "---", "-", "+", ret);
		genCell("Key".length(), "-", "---", "-", "+", ret);
		genCell(tm.getColDefaultValueMaxLen(), "-", "---", "-", "+", ret);
		genCell("Remarks".length(), "-", "---", "-", "", ret);
		ret.append("\n");
		return ret.toString();
	}

	protected void genTableHead(TableMeta tm, StringBuilder ret) {
		genCell(tm.getColNameMaxLen(), " ", "Field", " ", "|", ret);
		genCell(tm.getColTypeMaxLen(), " ", "Type", " ", "|", ret);
		genCell("Null".length(), " ", "Null", " ", "|", ret);
		genCell("Key".length(), " ", "Key", " ", "|", ret);
		genCell(tm.getColDefaultValueMaxLen(), " ", "Default", " ", "|", ret);
		genCell("Remarks".length(), " ", "Remarks", " ", "", ret);
		ret.append("\n");
	}

	protected void genColumn(TableMeta tableMeta, ColumnMeta columnMeta, StringBuilder ret) {
		genCell(tableMeta.getColNameMaxLen(), " ", columnMeta.getName(), " ", "|", ret);
		genCell(tableMeta.getColTypeMaxLen(), " ", columnMeta.getType(), " ", "|", ret);
		genCell("Null".length(), " ", columnMeta.getIsNullable(), " ", "|", ret);
		genCell("Key".length(), " ", columnMeta.getIsPrimaryKey(), " ", "|", ret);
		genCell(tableMeta.getColDefaultValueMaxLen(), " ", columnMeta.getDefaultValue(), " ", "|", ret);
		genCell("Remarks".length(), " ", columnMeta.getRemarks(), " ", "", ret);
		ret.append("\n");
	}

	protected void rebuildColumnMetas(List<TableMeta> tableMetas) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			for (TableMeta tableMeta : tableMetas) {
				// 重建整个 TableMeta.columnMetas
				tableMeta.setColumnMetas(new ArrayList<ColumnMeta>());
				ResultSet rs = dbMeta.getColumns(conn.getCatalog(), null, tableMeta.getName(), null);
				while (rs.next()) {
					ColumnMeta columnMeta = new ColumnMeta();
					columnMeta.setName(rs.getString("COLUMN_NAME")); // 名称

					columnMeta.setType(rs.getString("TYPE_NAME")); // 类型
					if (columnMeta.getType() == null)
						columnMeta.setType("未知");

					int columnSize = rs.getInt("COLUMN_SIZE"); // 长度
					if (columnSize > 0) {
						columnMeta.setType(columnMeta.getType() + "(" + columnSize);
						int decimalDigits = rs.getInt("DECIMAL_DIGITS"); // 小数位数
						if (decimalDigits > 0) {
							columnMeta.setType(columnMeta.getType() + "," + decimalDigits);
						}
						columnMeta.setType(columnMeta.getType() + ")");
					}

					columnMeta.setIsNullable(rs.getString("IS_NULLABLE")); // 是否允许
																			// NULL
																			// 值
					if (columnMeta.getIsNullable() == null)
						columnMeta.setIsNullable("true");

					columnMeta.setIsPrimaryKey("   ");
					String[] keys = tableMeta.getPrimaryKey().split(",");
					for (String key : keys) {
						if (key.equalsIgnoreCase(columnMeta.getName())) {
							columnMeta.setIsPrimaryKey("PRI");
							break;
						}
					}

					columnMeta.setDefaultValue(rs.getString("COLUMN_DEF")); // 默认值
					if (columnMeta.getDefaultValue() == null)
						columnMeta.setDefaultValue("");

					columnMeta.setRemarks(rs.getString("REMARKS")); // 备注
					if (columnMeta.getRemarks() == null)
						columnMeta.setRemarks("");

					if (tableMeta.getColNameMaxLen() < columnMeta.getName().length())
						tableMeta.setColNameMaxLen(columnMeta.getName().length());
					if (tableMeta.getColTypeMaxLen() < columnMeta.getType().length())
						tableMeta.setColTypeMaxLen(columnMeta.getType().length());
					if (tableMeta.getColDefaultValueMaxLen() < columnMeta.getDefaultValue().length())
						tableMeta.setColDefaultValueMaxLen(columnMeta.getDefaultValue().length());

					tableMeta.getColumnMetas().add(columnMeta);
				}
				rs.close();
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
	 * _DataDictionary.txt 覆盖写入
	 */
	protected void wirtToFile(StringBuilder ret) {
		FileWriter fw = null;
		try {
			File dir = new File(dataDictionaryOutputDir);
			if (!dir.exists())
				dir.mkdirs();

			String target = dataDictionaryOutputDir + File.separator + dataDictionaryFileName;
			fw = new FileWriter(target);
			fw.write(ret.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
