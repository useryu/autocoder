package cn.agilecode.autocoder.dialect;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * OracleDialect.
 */
public class OracleDialect extends Dialect {
	
	public OracleDialect(DatabaseMetaData dbMeta) {
		super(dbMeta);
	}

	@Override
	public String getShemaPattern() {
		try {
			return dbMeta.getUserName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String forTableBuilderDoBuild(String tableName) {
		return "select * from " + tableName + " where rownum < 1";
	}
	
	public String forDbFindById(String tableName, String[] pKeys) {
		tableName = tableName.trim();
		trimPrimaryKeys(pKeys);
		
		StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(" where ");
		for (int i=0; i<pKeys.length; i++) {
			if (i > 0) {
				sql.append(" and ");
			}
			sql.append(pKeys[i]).append(" = ?");
		}
		return sql.toString();
	}
	
	public String forDbDeleteById(String tableName, String[] pKeys) {
		tableName = tableName.trim();
		trimPrimaryKeys(pKeys);
		
		StringBuilder sql = new StringBuilder("delete from ").append(tableName).append(" where ");
		for (int i=0; i<pKeys.length; i++) {
			if (i > 0) {
				sql.append(" and ");
			}
			sql.append(pKeys[i]).append(" = ?");
		}
		return sql.toString();
	}
	
	
	
	public String forPaginate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		int start = (pageNumber - 1) * pageSize;
		int end = pageNumber * pageSize;
		StringBuilder ret = new StringBuilder();
		ret.append("select * from ( select row_.*, rownum rownum_ from (  ");
		ret.append(select).append(" ").append(sqlExceptSelect);
		ret.append(" ) row_ where rownum <= ").append(end).append(") table_alias");
		ret.append(" where table_alias.rownum_ > ").append(start);
		return ret.toString();
	}
	
	public boolean isOracle() {
		return true;
	}
	
	public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
		for (int i=0, size=paras.size(); i<size; i++) {
			Object value = paras.get(i);
			if (value instanceof java.sql.Date) {
				pst.setDate(i + 1, (java.sql.Date)value);
			} else if (value instanceof java.sql.Timestamp) {
				pst.setTimestamp(i + 1, (java.sql.Timestamp)value);
			} else {
				pst.setObject(i + 1, value);
			}
		}
	}
	
	public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
		for (int i=0; i<paras.length; i++) {
			Object value = paras[i];
			if (value instanceof java.sql.Date) {
				pst.setDate(i + 1, (java.sql.Date)value);
			} else if (value instanceof java.sql.Timestamp) {
				pst.setTimestamp(i + 1, (java.sql.Timestamp)value);
			} else {
				pst.setObject(i + 1, value);
			}
		}
	}
	
	public String getDefaultPrimaryKey() {
		return "ID";
	}

	@Override
	public boolean isToLowerCase() {
		return true;
	}
}
