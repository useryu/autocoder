package cn.agilecode.common.web.support;

import java.util.List;

public class Nodes {
	private String id;

	private String name;

	private String type;
	
	private String query;

	private List<Columns> columns;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setColumns(List<Columns> columns) {
		this.columns = columns;
	}

	public List<Columns> getColumns() {
		return this.columns;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return "Nodes [id=" + id + ", name=" + name + ", type=" + type + ", query=" + query + ", columns=" + columns
				+ "]";
	}

}