package cn.agilecode.autocoder.generator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import cn.agilecode.autocoder.dialect.MssqlDialect;
import cn.agilecode.autocoder.metadata.ColumnMeta;
import cn.agilecode.autocoder.metadata.MetaBuilder;
import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.util.FileUtils;
import cn.agilecode.autocoder.util.StrKit;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * DataDictionary 数据字典生成器
 */
public class WordDictionaryGenerator {

	protected DataSource dataSource;
	protected String dataDictionaryOutputDir;
	protected String dataDictionaryFileName = "DataDictionary.xml";
	protected String templateFile;

	public WordDictionaryGenerator(DataSource dataSource, String dataDictionaryOutputDir, String templateFile) {
		this.dataSource = dataSource;
		this.dataDictionaryOutputDir = dataDictionaryOutputDir;
		this.templateFile = templateFile;
	}
	
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		properties.load(new FileReader("d:/temp/jdbc.txt"));
		DataSource ds = BasicDataSourceFactory.createDataSource(properties);
		MetaBuilder metaBuilder = new MetaBuilder(ds);
		metaBuilder.setDialect(new MssqlDialect(metaBuilder.getDbMeta()));
		WordDictionaryGenerator g = new WordDictionaryGenerator(ds, "d:/temp/", "db_dict_template.xml");
		List<TableMeta> tableMetas = metaBuilder.build();
		g.generate(tableMetas);
	}

	public void generate(List<TableMeta> tableMetas) throws TemplateException, IOException {
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);
		String templateDirStr = ModelGenerator.class.getResource("/template/"+templateFile).getFile();
		File templateDir = new File(templateDirStr);
		config.setDirectoryForTemplateLoading(templateDir.getParentFile());
		Template template = config.getTemplate(templateFile);
		
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("tables", tableMetas);
		StringWriter out = new StringWriter();
		template.process(vars, out);
        out.flush();
        FileUtils.wirtToFile(dataDictionaryOutputDir, dataDictionaryFileName, out.toString(), true, "xml");
	}
	
}
