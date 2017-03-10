package cn.agilecode.autocoder.model;

/**
 * -----------+---------+------+-----+---------+---------------- Field | Type |
 * Null | Key | Default | Remarks
 * -----------+---------+------+-----+---------+---------------- id | int(11) |
 * NO | PRI | NULL | ch name here
 * 
 * @author lenovo
 *
 */
public class Column {

	private String name;
	private String chname;
	private String type;
	private boolean nullable;
	private int length;
	private boolean primaryKey;
	private String defaultValue;
	private String exampleValue;
	private String note;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChname() {
		return chname;
	}

	public void setChname(String chname) {
		this.chname = chname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getExampleValue() {
		return exampleValue;
	}

	public void setExampleValue(String exampleValue) {
		this.exampleValue = exampleValue;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
