package cn.agilecode.common.web.support;

public class Columns {
	private String id;

	private String datatype;

	private boolean primaryKey;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getDatatype() {
		return this.datatype;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public String toString() {
		return "Columns [id=" + id + ", datatype=" + datatype + ", primaryKey=" + primaryKey + "]";
	}

}