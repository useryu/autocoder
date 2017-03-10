package cn.agilecode.common.web.support;

public class Data {
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "Data [type=" + type + "]";
	}

}