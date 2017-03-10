package cn.agilecode.autocoder.dialect;

import java.sql.DatabaseMetaData;

/**
 * MysqlDialect.
 */
public class MysqlDialect extends Dialect {
	
	public MysqlDialect(DatabaseMetaData dbMeta) {
		super(dbMeta);
	}


	public String forTableBuilderDoBuild(String tableName) {
		return "select * from `" + tableName + "` where 1 = 2";
	}
	
	
	public String forDbFindById(String tableName, String[] pKeys) {
		tableName = tableName.trim();
		trimPrimaryKeys(pKeys);
		
		StringBuilder sql = new StringBuilder("select * from `").append(tableName).append("` where ");
		for (int i=0; i<pKeys.length; i++) {
			if (i > 0) {
				sql.append(" and ");
			}
			sql.append("`").append(pKeys[i]).append("` = ?");
		}
		return sql.toString();
	}
	
	public String forDbDeleteById(String tableName, String[] pKeys) {
		tableName = tableName.trim();
		trimPrimaryKeys(pKeys);
		
		StringBuilder sql = new StringBuilder("delete from `").append(tableName).append("` where ");
		for (int i=0; i<pKeys.length; i++) {
			if (i > 0) {
				sql.append(" and ");
			}
			sql.append("`").append(pKeys[i]).append("` = ?");
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
}
