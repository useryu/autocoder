package cn.agilecode.autocoder.generator;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.agilecode.autocoder.util.StringUtils;

import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.util.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Model 生成器
 */
public class ModelGenerator {
	
	protected String modelPackageName;
	protected String modelOutputDir;
	protected boolean generateDaoInModel = true;
	private String templateName;
	private boolean delOldFile;
	private String commonModelPackageName;
	
	public ModelGenerator(String commonModelPackageName, String modelPackageName, String modelOutputDir, String templateName, boolean delOldFile) {
		if (StringUtils.isEmpty(modelPackageName))
			throw new IllegalArgumentException("modelPackageName can not be blank.");
		if (modelPackageName.contains("/") || modelPackageName.contains("\\"))
			throw new IllegalArgumentException("modelPackageName error : " + modelPackageName);
		if (StringUtils.isEmpty(modelOutputDir))
			throw new IllegalArgumentException("modelOutputDir can not be blank.");
		
		this.commonModelPackageName = commonModelPackageName;
		this.modelPackageName = modelPackageName;
		this.modelOutputDir = modelOutputDir;
		this.templateName = templateName;
		this.delOldFile = delOldFile;
	}
	
	public void generate(List<TableMeta> tableMetas) throws Exception {
		System.out.println("Generate model ...");
		for (TableMeta tableMeta : tableMetas) {
			if(tableMeta.isNoNeedModel()) {
				continue;
			}
			genModelContent(tableMeta);
			String fullDirPath = modelOutputDir + FileUtils.genPackagePath(this.modelPackageName);
			FileUtils.wirtToFile(fullDirPath, tableMeta.getModelName(), tableMeta.getModelContent(), this.delOldFile);
		}
	}
	
	protected void genModelContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/"+templateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(templateName);
		
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("commonModelPackageName", this.commonModelPackageName);
		vars.put("packageName", this.modelPackageName);
		vars.put("table", tableMeta);
		vars.put("fields", tableMeta.getColumnMetas());		
		StringWriter out = new StringWriter();
		template.process(vars, out);
        out.flush();
		tableMeta.setModelContent(out.toString());
	}
}


