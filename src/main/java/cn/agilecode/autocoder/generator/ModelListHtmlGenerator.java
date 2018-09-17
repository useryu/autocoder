package cn.agilecode.autocoder.generator;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.agilecode.autocoder.metadata.ColumnMeta;
import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.util.FileUtils;
import cn.agilecode.autocoder.util.StrKit;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成模型的列表页面
 */
public class ModelListHtmlGenerator {
	
	protected String modelOutputDir;
	private String serverPaggingTemplateName;
	private String clientPaggingTemplateName;
	private boolean delOldFile;
	
	public ModelListHtmlGenerator(String modelOutputDir, String clientPaggingTemplateName, String serverPaggingTemplateName, boolean delOldFile) {
		this.modelOutputDir = modelOutputDir;
		this.clientPaggingTemplateName = clientPaggingTemplateName;
		this.serverPaggingTemplateName = serverPaggingTemplateName;
		this.delOldFile = delOldFile;
	}
	
	public void generate(List<TableMeta> tableMetas) throws Exception {
		System.out.println("Generate model list html ...");
		for (TableMeta tableMeta : tableMetas) {
			if(tableMeta.isNoNeedModel()) {
				continue;
			}
			if(tableMeta.isPagingInServer()) {
				genServerPaggingHtmlContent(tableMeta);
			}else {
				genClientPaggingHtmlContent(tableMeta);
			}
			String fullDirPath = modelOutputDir+File.separatorChar+StrKit.firstCharToLowerCase(tableMeta.getModelName());
			FileUtils.wirtToFile(fullDirPath, StrKit.firstCharToLowerCase(tableMeta.getModelName())+"_list", tableMeta.getModelContent(), this.delOldFile, "html");
		}
	}
	
	protected void genServerPaggingHtmlContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/"+clientPaggingTemplateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(serverPaggingTemplateName);
		
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("modelRemarks", tableMeta.getRemarks());
		vars.put("modelName", StrKit.firstCharToLowerCase(tableMeta.getModelName()));
		vars.put("fieldHeaders", this.genHeaders(tableMeta.getColumnMetas()));
		vars.put("fieldNames", this.genFieldNames(tableMeta.getColumnMetas()));
		vars.put("fields", tableMeta.getColumnMetas());
		StringWriter out = new StringWriter();
		template.process(vars, out);
        out.flush();
		tableMeta.setModelContent(out.toString());
	}
	
	protected void genClientPaggingHtmlContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/"+clientPaggingTemplateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(clientPaggingTemplateName);
		
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("modelRemarks", tableMeta.getRemarks());
		vars.put("modelName", StrKit.firstCharToLowerCase(tableMeta.getModelName()));
		vars.put("fieldHeaders", this.genHeaders(tableMeta.getColumnMetas()));
		vars.put("fieldNames", this.genFieldNames(tableMeta.getColumnMetas()));
		StringWriter out = new StringWriter();
		template.process(vars, out);
        out.flush();
		tableMeta.setModelContent(out.toString());
	}

	private List<String> genFieldNames(List<ColumnMeta> columnMetas) {
		List<String> names = new ArrayList<String>();
		for(ColumnMeta col:columnMetas) {
			if(col.isSetCol() || !col.isShowInList()) {
				continue;
			}
			names.add(StrKit.firstCharToLowerCase(col.getAttrName()));
		}
		return names;
	}

	private List<String> genHeaders(List<ColumnMeta> columnMetas) {
		List<String> headers = new ArrayList<String>();
		for(ColumnMeta col:columnMetas) {
			if(col.isSetCol() || !col.isShowInList()) {
				continue;
			}
			headers.add(col.getRemarks());
		}
		return headers;
	}
}


