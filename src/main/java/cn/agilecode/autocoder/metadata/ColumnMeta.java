package cn.agilecode.autocoder.metadata;

/**
 * ColumnMeta
 */
public class ColumnMeta {
	
	private String name;				// 字段名
	private String upperFirstCharName; // 首字母大写的字段名
	private String javaType;			// 字段对应的 java 类型
	private String attrName;			// 字段对应的属性名
	private String initFieldStr="";			//初始化字段用的字串
	private String hibernateDef;			//hibernate anotation定义，用于使用anotation做表映射时
	
	// ---------
	
	/*
	-----------+---------+------+-----+---------+----------------
	 Field     | Type    | Null | Key | Default | Remarks
	-----------+---------+------+-----+---------+----------------
	 id		   | int(11) | NO	| PRI | NULL	| remarks here	
	*/
	private String type="";				// 字段类型(附带字段长度与小数点)，例如：decimal(11,2)
	private String isNullable="";		// 是否允许空值
	private String isPrimaryKey="";		// 是否主键
	private String defaultValue="无";		// 默认值
	private String remarks;			// 字段备注
	private boolean showInTostringMethod = true;
	private boolean setCol = false;  //onetomany 中many的字段
	private boolean objectCol = false; //onetomany 中one的字段
	private boolean showInList = true;
	private boolean fullTextSearch = false;
	//以下为数据字典专用
	private boolean isSystemData = false;
	private boolean isBusinessData = true;
	private String description="";
	private String mainDataSource="";
	private String relatedSource="";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUpperFirstCharName() {
		return upperFirstCharName;
	}
	public void setUpperFirstCharName(String upperFirstCharName) {
		this.upperFirstCharName = upperFirstCharName;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsNullable() {
		return isNullable;
	}
	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}
	public String getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(String isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getDefaultValue() {
		return defaultValue==null?"":defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getHibernateDef() {
		return hibernateDef;
	}
	public void setHibernateDef(String hibernateDef) {
		this.hibernateDef = hibernateDef;
	}
	@Override
	public String toString() {
		return "ColumnMeta [name=" + name + ", upperFirstCharName=" + upperFirstCharName + ", javaType=" + javaType
				+ ", attrName=" + attrName + ", hibernateDef=" + hibernateDef + ", type=" + type + ", isNullable="
				+ isNullable + ", isPrimaryKey=" + isPrimaryKey + ", defaultValue=" + defaultValue + ", remarks="
				+ remarks + "]";
	}
	public String getInitFieldStr() {
		return initFieldStr;
	}
	public void setInitFieldStr(String initFieldStr) {
		this.initFieldStr = initFieldStr;
	}
	public boolean isShowInTostringMethod() {
		return showInTostringMethod;
	}
	public void setShowInTostringMethod(boolean showInTostringMethod) {
		this.showInTostringMethod = showInTostringMethod;
	}
	public boolean isSetCol() {
		return setCol;
	}
	public void setSetCol(boolean setCol) {
		this.setCol = setCol;
	}
	public boolean isObjectCol() {
		return objectCol;
	}
	public void setObjectCol(boolean objectCol) {
		this.objectCol = objectCol;
	}
	public boolean isShowInList() {
		return showInList;
	}
	public void setShowInList(boolean showInList) {
		this.showInList = showInList;
	}
	public boolean isFullTextSearch() {
		return fullTextSearch;
	}
	public void setFullTextSearch(boolean fullTextSearch) {
		this.fullTextSearch = fullTextSearch;
	}
	public boolean isSystemData() {
		return "id".equalsIgnoreCase(this.getName());
	}
	public void setSystemData(boolean isSystemData) {
		this.isSystemData = isSystemData;
	}
	public boolean isBusinessData() {
		return !this.isSystemData();
	}
	public void setBusinessData(boolean isBusinessData) {
		this.isBusinessData = isBusinessData;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMainDataSource() {
		return mainDataSource;
	}
	public void setMainDataSource(String mainDataSource) {
		this.mainDataSource = mainDataSource;
	}
	public String getRelatedSource() {
		return relatedSource;
	}
	public void setRelatedSource(String relatedSource) {
		this.relatedSource = relatedSource;
	}
	
	
}

