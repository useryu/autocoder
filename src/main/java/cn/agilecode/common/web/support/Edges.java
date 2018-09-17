package cn.agilecode.common.web.support;

public class Edges {
	private String source;

	private String target;

	private Data data;

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return this.data;
	}

	@Override
	public String toString() {
		return "Edges [source=" + source + ", target=" + target + ", data=" + data + "]";
	}

}