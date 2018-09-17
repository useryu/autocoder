package cn.agilecode.autocoder.generator;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.util.FileUtils;
import cn.agilecode.autocoder.util.StrKit;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Controller 生成器
 */
public class ControllerGenerator {
	
	protected String controllerPackage;
	protected String modelOutputDir;
	protected boolean generateDaoInModel = true;
	private String templateName;
	private boolean delOldFile;
	private String projectPackage;
	private String coreBasePackage;
	
	public ControllerGenerator(String controllerPackage, String projectPackage, String coreBasePackage, String modelOutputDir, String templateName, boolean delOldFile) {
		this.controllerPackage = controllerPackage;
		this.projectPackage = projectPackage;
		this.coreBasePackage = coreBasePackage;
		this.modelOutputDir = modelOutputDir;
		this.templateName = templateName;
		this.delOldFile = delOldFile;
	}
	
	public void generate(List<TableMeta> tableMetas) throws Exception {
		System.out.println("Generate Controllers ...");
		for (TableMeta tableMeta : tableMetas) {
			if(tableMeta.isNoNeedModel()) {
				continue;
			}
			genControllerContent(tableMeta);
			String fullDirPath = modelOutputDir + FileUtils.genPackagePath(this.controllerPackage);
			FileUtils.wirtToFile(fullDirPath, tableMeta.getModelName()+"Controller", tableMeta.getModelContent(), this.delOldFile);
		}
	}
	
	protected void genControllerContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/"+templateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(templateName);
		
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("controllerPackage", this.controllerPackage);
		vars.put("projectPackage", this.projectPackage);
		vars.put("coreBasePackage", this.coreBasePackage);
		vars.put("modelClassName", tableMeta.getModelName());
		vars.put("modelFieldName", StrKit.firstCharToLowerCase(tableMeta.getModelName()));
		vars.put("oneSideModeNames", tableMeta.getOneSideModeNames());
		StringWriter out = new StringWriter();
		template.process(vars, out);
        out.flush();
		tableMeta.setModelContent(out.toString());
	}
}


