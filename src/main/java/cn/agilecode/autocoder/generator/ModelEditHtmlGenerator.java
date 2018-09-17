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
public class ModelEditHtmlGenerator {
	
	protected String modelOutputDir;
	private String templateName;
	private boolean delOldFile;
	
	public ModelEditHtmlGenerator(String modelOutputDir, String templateName, boolean delOldFile) {
		this.modelOutputDir = modelOutputDir;
		this.templateName = templateName;
		this.delOldFile = delOldFile;
	}
	
	public void generate(List<TableMeta> tableMetas) throws Exception {
		System.out.println("Generate model edit html ...");
		for (TableMeta tableMeta : tableMetas) {
			if(tableMeta.isNoNeedModel()) {
				continue;
			}
			genHtmlContent(tableMeta);
			String fullDirPath = modelOutputDir+File.separatorChar+StrKit.firstCharToLowerCase(tableMeta.getModelName());
			FileUtils.wirtToFile(fullDirPath, StrKit.firstCharToLowerCase(tableMeta.getModelName())+"_edit", tableMeta.getModelContent(), this.delOldFile, "html");
		}
	}
	
	protected void genHtmlContent(TableMeta tableMeta) throws Exception {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/"+templateName).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(templateName);
		
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("modelRemarks", tableMeta.getRemarks());
		vars.put("modelName", StrKit.firstCharToLowerCase(tableMeta.getModelName()));
		vars.put("fields", this.removeNoNeedEditCol(tableMeta.getColumnMetas()));
		vars.put("objFields", this.getObjectCol(tableMeta.getColumnMetas()));
		StringWriter out = new StringWriter();
		template.process(vars, out);
        out.flush();
		tableMeta.setModelContent(out.toString());
	}

	private List<ColumnMeta> getObjectCol(List<ColumnMeta> columnMetas) {
		List<ColumnMeta> objCols = new ArrayList<ColumnMeta>();
		for(ColumnMeta col:columnMetas) {
			if(col.isObjectCol()) {
				objCols.add(col);
			}
		}
		return objCols;
	}
	
	private List<ColumnMeta> removeNoNeedEditCol(List<ColumnMeta> columnMetas) {
		List<ColumnMeta> noIdcols = new ArrayList<ColumnMeta>();
		for(ColumnMeta col:columnMetas) {
			if(!col.getName().equalsIgnoreCase("id") && !col.isSetCol() && !col.isObjectCol()) {
				noIdcols.add(col);
			}
		}
		return noIdcols;
	}
}


