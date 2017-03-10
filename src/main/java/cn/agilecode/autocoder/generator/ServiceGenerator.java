package cn.agilecode.autocoder.generator;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.agilecode.autocoder.util.StringUtils;

import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.util.FileUtils;
import cn.agilecode.autocoder.util.StrKit;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Service 生成器
 */
public class ServiceGenerator {

	protected String servicePackageName;
	protected String modelPackageName;
	protected String baseServicePackageName;
	protected String daoPackageName;
	protected String serviceOutputDir;
	protected boolean generateServiceInModel = true;
	private String serviceInfTemplateName;
	private String serviceImplTemplateName;
	private boolean delOldFile;
	private String coreBasePackage;

	/**
	 * @param servicePackageName
	 * @param modelPackageName
	 * @param baseServicePackageName
	 * @param serviceOutputDir
	 * @param serviceInfTemplateName
	 * @param serviceImplTemplateName
	 * @param delOldFile
	 */
	public ServiceGenerator(String servicePackageName, String modelPackageName, String baseServicePackageName, 
			String daoPackageName, String coreBasePackage, String serviceOutputDir,
			String serviceInfTemplateName, String serviceImplTemplateName, boolean delOldFile) {
		if (StringUtils.isEmpty(servicePackageName))
			throw new IllegalArgumentException("modelPackageName can not be blank.");
		if (servicePackageName.contains("/") || servicePackageName.contains("\\"))
			throw new IllegalArgumentException("modelPackageName error : " + servicePackageName);
		if (StringUtils.isEmpty(serviceOutputDir))
			throw new IllegalArgumentException("modelOutputDir can not be blank.");

		this.coreBasePackage = coreBasePackage;
		this.servicePackageName = servicePackageName;
		this.modelPackageName = modelPackageName;
		this.baseServicePackageName = baseServicePackageName;
		this.daoPackageName = daoPackageName;
		this.serviceOutputDir = serviceOutputDir;
		this.serviceInfTemplateName = serviceInfTemplateName;
		this.serviceImplTemplateName = serviceImplTemplateName;
		this.delOldFile = delOldFile;
	}

	public void generate(List<TableMeta> tableMetas) throws Exception {
		System.out.println("Generate Service ...");
		for (TableMeta tableMeta : tableMetas) {
			if (tableMeta.isNoNeedModel()) {
				continue;
			}
			String ServiceInfText = genServiceInfContent(tableMeta);
			String ServiceImplText = genServiceImplContent(tableMeta);
			String fullDirPath = serviceOutputDir + FileUtils.genPackagePath(this.servicePackageName);
			FileUtils.wirtToFile(fullDirPath, "I" + tableMeta.getModelName() + "Service", ServiceInfText, this.delOldFile);
			FileUtils.wirtToFile(fullDirPath, tableMeta.getModelName() + "Service", ServiceImplText, this.delOldFile);
		}
	}

	protected String genServiceInfContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/" + serviceInfTemplateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(serviceInfTemplateName);

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("servicePackageName", this.servicePackageName);
		vars.put("baseServicePackageName", this.baseServicePackageName);
		vars.put("modelPackageName", this.modelPackageName);
		vars.put("modelName", tableMeta.getModelName());
		vars.put("coreBasePackage", this.coreBasePackage);
		StringWriter out = new StringWriter();
		template.process(vars, out);
		out.flush();
		return out.toString();
	}
	
	protected String genServiceImplContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/" + serviceImplTemplateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(serviceImplTemplateName);
		
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("servicePackageName", this.servicePackageName);
		vars.put("baseServicePackageName", this.baseServicePackageName);
		vars.put("daoPackageName", this.daoPackageName);
		vars.put("modelPackageName", this.modelPackageName);
		vars.put("modelName", tableMeta.getModelName());
		vars.put("daoFieldName", genDaoFieldName(tableMeta.getModelName()));
		vars.put("coreBasePackage", this.coreBasePackage);
		StringWriter out = new StringWriter();
		template.process(vars, out);
		out.flush();
		return out.toString();
	}

	private String genDaoFieldName(String modelName) {
		return StrKit.firstCharToLowerCase(modelName+"Dao");
	}
}
