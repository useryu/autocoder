package cn.agilecode.autocoder.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * TableMeta
 */
public class TableMeta {

	private String name; // 表名
	private String tableHibernateDef; // 表名
	private String remarks; // 表备注
	private String primaryKey; // 主键，复合主键以逗号分隔
	private List<FkMeta> fkMetas = new ArrayList<FkMeta>(); // 外键字段
	private List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>(); // 字段
																		// meta
	private List<String> oneSideModeNames = new ArrayList<String>();
	private String modelName; // 生成的 model 名
	private String modelContent; // 生成的 model 内容
	private String funcDesc="";//生成数据字典
	private String relatedModule="";//生成数据字典

	// ---------

	private int colNameMaxLen = "Field".length(); // 字段名最大宽度，用于辅助生成字典文件样式
	private int colTypeMaxLen = "Type".length(); // 字段类型最大宽度，用于辅助生成字典文件样式
	private int colDefaultValueMaxLen = "Default".length(); // 字段默认值最大宽度，用于辅助生成字典文件样式
	
	private boolean pagingInServer = true; //生成的列表在服务端进行分页
	private boolean noNeedModel = false; //关联表，是不需要生成DAO和MODEL的

	public String getTableHibernateDef() {
		return tableHibernateDef;
	}

	public void setTableHibernateDef(String tableHibernateDef) {
		this.tableHibernateDef = tableHibernateDef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<ColumnMeta> getColumnMetas() {
		return columnMetas;
	}

	public void setColumnMetas(List<ColumnMeta> columnMetas) {
		this.columnMetas = columnMetas;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelContent() {
		return modelContent;
	}

	public void setModelContent(String modelContent) {
		this.modelContent = modelContent;
	}

	public int getColNameMaxLen() {
		return colNameMaxLen;
	}

	public void setColNameMaxLen(int colNameMaxLen) {
		this.colNameMaxLen = colNameMaxLen;
	}

	public int getColTypeMaxLen() {
		return colTypeMaxLen;
	}

	public void setColTypeMaxLen(int colTypeMaxLen) {
		this.colTypeMaxLen = colTypeMaxLen;
	}

	public int getColDefaultValueMaxLen() {
		return colDefaultValueMaxLen;
	}

	public void setColDefaultValueMaxLen(int colDefaultValueMaxLen) {
		this.colDefaultValueMaxLen = colDefaultValueMaxLen;
	}

	public ColumnMeta findColByName(String colname) {
		for(ColumnMeta col : this.columnMetas) {
			if(col.getName().equalsIgnoreCase(colname)) {
				return col;
			}
		}
		return null;
	}

	public List<FkMeta> getFkMetas() {
		return fkMetas;
	}

	public void setFkMetas(List<FkMeta> fkMetas) {
		this.fkMetas = fkMetas;
	}

	@Override
	public String toString() {
		return "TableMeta [name=" + name + ", tableHibernateDef=" + tableHibernateDef + ", remarks=" + remarks
				+ ", primaryKey=" + primaryKey + ", fkMetas=" + fkMetas + ", columnMetas=" + columnMetas
				+ ", modelName=" + modelName + ", modelContent=" + modelContent + ", colNameMaxLen=" + colNameMaxLen
				+ ", colTypeMaxLen=" + colTypeMaxLen + ", colDefaultValueMaxLen=" + colDefaultValueMaxLen + "]";
	}

	public boolean isNoNeedModel() {
		return noNeedModel;
	}

	public void setNoNeedModel(boolean noNeedModel) {
		this.noNeedModel = noNeedModel;
	}

	public List<String> getOneSideModeNames() {
		return oneSideModeNames;
	}

	public void setOneSideModeNames(List<String> oneSideModeNames) {
		this.oneSideModeNames = oneSideModeNames;
	}

	public boolean isPagingInServer() {
		return pagingInServer;
	}

	public void setPagingInServer(boolean pagingInServer) {
		this.pagingInServer = pagingInServer;
	}

	public String getFuncDesc() {
		return funcDesc;
	}

	public void setFuncDesc(String funcDesc) {
		this.funcDesc = funcDesc;
	}

	public String getRelatedModule() {
		return relatedModule;
	}

	public void setRelatedModule(String relatedModule) {
		this.relatedModule = relatedModule;
	}


}
