package cn.agilecode.autocoder.generator;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.util.FileUtils;
import cn.agilecode.autocoder.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Dao 生成器
 */
public class DaoGenerator {

	protected String coreBasePackage;
	protected String daoPackageName;
	protected String modelPackageName;
	protected String baseDaoPackageName;
	protected String daoOutputDir;
	protected boolean generateDaoInModel = true;
	private String daoInfTemplateName;
	private String daoImplTemplateName;
	private boolean delOldFile;

	/**
	 * @param daoPackageName
	 * @param modelPackageName
	 * @param baseDaoPackageName
	 * @param daoOutputDir
	 * @param daoInfTemplateName
	 * @param daoImplTemplateName
	 * @param delOldFile
	 */
	public DaoGenerator(String coreBasePackage, String daoPackageName, String modelPackageName, String baseDaoPackageName, String daoOutputDir,
			String daoInfTemplateName, String daoImplTemplateName, boolean delOldFile) {
		if (StringUtils.isEmpty(daoPackageName))
			throw new IllegalArgumentException("modelPackageName can not be blank.");
		if (daoPackageName.contains("/") || daoPackageName.contains("\\"))
			throw new IllegalArgumentException("modelPackageName error : " + daoPackageName);
		if (StringUtils.isEmpty(daoOutputDir))
			throw new IllegalArgumentException("modelOutputDir can not be blank.");

		this.coreBasePackage = coreBasePackage;
		this.daoPackageName = daoPackageName;
		this.modelPackageName = modelPackageName;
		this.baseDaoPackageName = baseDaoPackageName;
		this.daoOutputDir = daoOutputDir;
		this.daoInfTemplateName = daoInfTemplateName;
		this.daoImplTemplateName = daoImplTemplateName;
		this.delOldFile = delOldFile;
	}

	public void generate(List<TableMeta> tableMetas) throws Exception {
		System.out.println("Generate dao ...");
		for (TableMeta tableMeta : tableMetas) {
			if (tableMeta.isNoNeedModel()) {
				continue;
			}
			String daoInfText = genDaoInfContent(tableMeta);
			String daoImplText = genDaoImplContent(tableMeta);
			String fullDirPath = daoOutputDir + FileUtils.genPackagePath(this.daoPackageName);
			FileUtils.wirtToFile(fullDirPath, "I" + tableMeta.getModelName() + "Dao", daoInfText, this.delOldFile);
			FileUtils.wirtToFile(fullDirPath, tableMeta.getModelName() + "Dao", daoImplText, this.delOldFile);
		}
	}

	protected String genDaoInfContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/" + daoInfTemplateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(daoInfTemplateName);

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("daoPackageName", this.daoPackageName);
		vars.put("baseDaoPackageName", this.baseDaoPackageName);
		vars.put("modelPackageName", this.modelPackageName);
		vars.put("modelName", tableMeta.getModelName());
		StringWriter out = new StringWriter();
		template.process(vars, out);
		out.flush();
		return out.toString();
	}
	
	protected String genDaoImplContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/" + daoImplTemplateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(daoImplTemplateName);
		
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("coreBasePackage", this.coreBasePackage);
		vars.put("daoPackageName", this.daoPackageName);
		vars.put("baseDaoPackageName", this.baseDaoPackageName);
		vars.put("modelPackageName", this.modelPackageName);
		vars.put("modelName", tableMeta.getModelName());
		StringWriter out = new StringWriter();
		template.process(vars, out);
		out.flush();
		return out.toString();
	}
}
