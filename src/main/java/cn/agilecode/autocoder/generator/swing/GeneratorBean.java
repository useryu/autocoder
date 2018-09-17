package cn.agilecode.autocoder.generator.swing;


public class GeneratorBean {

	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private String excludedTables;
	
	private String baseDir;
	private String outputDir;
	private String htmlOutputDir;
	private String coreBasePackage;
	private String projectSpecifyName;
	private String includeTableNamePrefixe;
	private String removedTableNamePrefixes;
	private boolean delOldFile;

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getHtmlOutputDir() {
		return htmlOutputDir;
	}

	public void setHtmlOutputDir(String htmlOutputDir) {
		this.htmlOutputDir = htmlOutputDir;
	}

	public String getCoreBasePackage() {
		return coreBasePackage;
	}

	public void setCoreBasePackage(String coreBasePackage) {
		this.coreBasePackage = coreBasePackage;
	}

	public String getProjectSpecifyName() {
		return projectSpecifyName;
	}

	public void setProjectSpecifyName(String projectSpecifyName) {
		this.projectSpecifyName = projectSpecifyName;
	}

	public String getIncludeTableNamePrefixe() {
		return includeTableNamePrefixe;
	}

	public void setIncludeTableNamePrefixe(String includeTableNamePrefixe) {
		this.includeTableNamePrefixe = includeTableNamePrefixe;
	}

	public String getRemovedTableNamePrefixes() {
		return removedTableNamePrefixes;
	}

	public void setRemovedTableNamePrefixes(String removedTableNamePrefixes) {
		this.removedTableNamePrefixes = removedTableNamePrefixes;
	}

	public boolean isDelOldFile() {
		return delOldFile;
	}

	public void setDelOldFile(boolean delOldFile) {
		this.delOldFile = delOldFile;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExcludedTables() {
		return excludedTables;
	}

	public void setExcludedTables(String excludedTables) {
		this.excludedTables = excludedTables;
	}

}
