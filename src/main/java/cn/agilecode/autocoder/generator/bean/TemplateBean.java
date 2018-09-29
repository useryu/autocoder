package cn.agilecode.autocoder.generator.bean;

public class TemplateBean {

	private String type;//model dao serviceInf serviceImpl controller htmlList htmlEdit htmlView
	private String TemplateName;
	private String outputDir;
	private String sufix;
	private String prefix;
	private String ext;	
	
	public TemplateBean(String type, String templateName, String outputDir, String sufix, String ext) {
		super();
		this.type = type;
		TemplateName = templateName;
		this.outputDir = outputDir;
		this.sufix = sufix;
		this.ext = ext;
		this.prefix = "";
	}
	
	public TemplateBean(String type, String templateName, String outputDir, String sufix, String ext, String prefix) {
		super();
		this.type = type;
		TemplateName = templateName;
		this.outputDir = outputDir;
		this.sufix = sufix;
		this.ext = ext;
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTemplateName() {
		return TemplateName;
	}
	public void setTemplateName(String templateName) {
		TemplateName = templateName;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	public String getSufix() {
		return sufix;
	}
	public void setSufix(String sufix) {
		this.sufix = sufix;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	
	
}
