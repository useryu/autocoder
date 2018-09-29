package cn.agilecode.autocoder.generator;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.agilecode.autocoder.generator.bean.TemplateBean;
import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.util.FileUtils;
import cn.agilecode.autocoder.util.StrKit;
import cn.agilecode.autocoder.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成器
 */
public class BaseGenerator {

	private TemplateBean[] templateBeans;
	private boolean delOldFile;
	private Map<String, Object> vars;

	public BaseGenerator(TemplateBean[] templateBeans, Map<String, Object> vars, boolean delOldFile) {
		this.delOldFile = delOldFile;
		this.templateBeans = templateBeans;
		this.vars = vars;
	}

	public void generate(List<TableMeta> tableMetas) throws Exception {
		for(TemplateBean templateBean:templateBeans) {
			System.out.println("Generate "+ templateBean.getType() +" ...");
			for (TableMeta tableMeta : tableMetas) {
				if (tableMeta.isNoNeedModel()) {
					continue;
				}
				String content = genContent(tableMeta, templateBean);
				FileUtils.wirtToFile(templateBean.getOutputDir(), 
						templateBean.getPrefix() + tableMeta.getModelName() + templateBean.getSufix(), 
						content, this.delOldFile, templateBean.getExt());
			}
		}
	}
	
	protected String genContent(TableMeta tableMeta, TemplateBean templateBean) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateName = templateBean.getTemplateName();
		String templateDirStr = ModelGenerator.class.getResource("/template/" + templateName ).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(templateName);		
		vars.put("modelName", tableMeta.getModelName());
		vars.put("modelClassName", tableMeta.getModelName());
		vars.put("modelFieldName", StrKit.firstCharToLowerCase(tableMeta.getModelName()));
		vars.put("oneSideModeNames", tableMeta.getOneSideModeNames());
		vars.put("fields", tableMeta.getColumnMetas());	
		vars.put("daoFieldName", genDaoFieldName(tableMeta.getModelName()));
		vars.put("table", tableMeta);
		
		StringWriter out = new StringWriter();
		template.process(vars, out);
		out.flush();
		return out.toString();
	}

	private String genDaoFieldName(String modelName) {
		return StrKit.firstCharToLowerCase(modelName+"Repository");
	}
}
