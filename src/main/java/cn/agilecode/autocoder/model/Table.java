package cn.agilecode.autocoder.model;

import java.util.List;

public class Table {

	private String name;
	private String chname;
	private List<Column> columns;
	private Listui listui;
	private Editui editui;
	
	public Editui getEditui() {
		return editui;
	}
	public void setEditui(Editui editui) {
		this.editui = editui;
	}
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
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public Listui getListui() {
		return listui;
	}
	public void setListui(Listui listui) {
		this.listui = listui;
	}
	
}
