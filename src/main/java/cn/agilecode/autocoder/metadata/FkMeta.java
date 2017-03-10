package cn.agilecode.autocoder.metadata;

public class FkMeta {

	private String columnName;
	private String fkTable;
	private String fkTableColumn;
	private String fkName;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getFkTable() {
		return fkTable;
	}

	public void setFkTable(String fkTable) {
		this.fkTable = fkTable;
	}

	public String getFkTableColumn() {
		return fkTableColumn;
	}

	public void setFkTableColumn(String fkTableColumn) {
		this.fkTableColumn = fkTableColumn;
	}

	public String getFkName() {
		return fkName;
	}

	public void setFkName(String fkName) {
		this.fkName = fkName;
	}

	@Override
	public String toString() {
		return "FkMeta [columnName=" + columnName + ", fkTable=" + fkTable + ", fkTableColumn=" + fkTableColumn
				+ ", fkName=" + fkName + "]";
	}

}
