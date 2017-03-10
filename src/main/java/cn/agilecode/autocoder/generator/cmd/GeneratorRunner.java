package cn.agilecode.autocoder.generator.cmd;

import java.io.FileReader;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import cn.agilecode.autocoder.dialect.MssqlDialect;
import cn.agilecode.autocoder.generator.ControllerGenerator;
import cn.agilecode.autocoder.generator.DaoGenerator;
import cn.agilecode.autocoder.generator.DataDictionaryGenerator;
import cn.agilecode.autocoder.generator.Generator;
import cn.agilecode.autocoder.generator.ModelEditHtmlGenerator;
import cn.agilecode.autocoder.generator.ModelGenerator;
import cn.agilecode.autocoder.generator.ModelListHtmlGenerator;
import cn.agilecode.autocoder.generator.ServiceGenerator;
import cn.agilecode.autocoder.metadata.MetaBuilder;

public class GeneratorRunner {

	private static String jdbcFile;
	private static String baseDir;
	private static String outputDir;
	private static String htmlOutputDir;
	private static String coreBasePackage;
	private static String projectSpecifyName;
	private static String includeTableNamePrefixe;
	private static String removedTableNamePrefixes;
	private static boolean delOldFile;

	public static String getJdbcFile() {
		return jdbcFile;
	}

	public static void setJdbcFile(String jdbcFile) {
		GeneratorRunner.jdbcFile = jdbcFile;
	}

	public static String getBaseDir() {
		return baseDir;
	}

	public static void setBaseDir(String baseDir) {
		GeneratorRunner.baseDir = baseDir;
	}

	public static String getOutputDir() {
		return outputDir;
	}

	public static void setOutputDir(String outputDir) {
		GeneratorRunner.outputDir = outputDir;
	}

	public static String getHtmlOutputDir() {
		return htmlOutputDir;
	}

	public static void setHtmlOutputDir(String htmlOutputDir) {
		GeneratorRunner.htmlOutputDir = htmlOutputDir;
	}

	public static String getCoreBasePackage() {
		return coreBasePackage;
	}

	public static void setCoreBasePackage(String coreBasePackage) {
		GeneratorRunner.coreBasePackage = coreBasePackage;
	}

	public static String getProjectSpecifyName() {
		return projectSpecifyName;
	}

	public static void setProjectSpecifyName(String projectSpecifyName) {
		GeneratorRunner.projectSpecifyName = projectSpecifyName;
	}

	public static String getIncludeTableNamePrefixe() {
		return includeTableNamePrefixe;
	}

	public static void setIncludeTableNamePrefixe(String includeTableNamePrefixe) {
		GeneratorRunner.includeTableNamePrefixe = includeTableNamePrefixe;
	}

	public static String getRemovedTableNamePrefixes() {
		return removedTableNamePrefixes;
	}

	public static void setRemovedTableNamePrefixes(String removedTableNamePrefixes) {
		GeneratorRunner.removedTableNamePrefixes = removedTableNamePrefixes;
	}

	public static boolean isDelOldFile() {
		return delOldFile;
	}

	public static void setDelOldFile(boolean delOldFile) {
		GeneratorRunner.delOldFile = delOldFile;
	}

//	public static void main(String[] args) throws Exception {
//		jdbcFile = "d:/temp/jdbc.txt";
//		baseDir = "D:/temp";
//		outputDir = baseDir + "/java/";
//		htmlOutputDir = baseDir + "/html/";
//		coreBasePackage = "com.fisher";
//		projectSpecifyName = "workshop";
//		includeTableNamePrefixe = "gf_";
//		removedTableNamePrefixes = "";
//		delOldFile = true;
//		
//		String projectBasePackage = coreBasePackage+"."+projectSpecifyName;//这个和每个工程相关
//		Properties properties = new Properties();
//		properties.load(new FileReader(jdbcFile));
//		String[] excludedTables = ((String)properties.get("excludedTables")).split(",");
//		//以下如果按规范，则不用做任何修改
//		DataSource ds = BasicDataSourceFactory.createDataSource(properties);
//		DataDictionaryGenerator dictGenerator = new DataDictionaryGenerator(ds,
//				outputDir);
//		String modelPackageName = coreBasePackage + "." + "model";
//		String commonModelPackageName = coreBasePackage + "." + "common.model";
//		ModelGenerator modelGenerator = new ModelGenerator(
//				commonModelPackageName, modelPackageName, outputDir,
//				"model.ftl", delOldFile);
//		MetaBuilder metaBuilder = new MetaBuilder(ds);
//		metaBuilder.setDialect(new MssqlDialect(metaBuilder.getDbMeta()));
//		metaBuilder.addExcludedTable(excludedTables);
//		metaBuilder.setRemovedTableNamePrefixes(removedTableNamePrefixes);
//		metaBuilder.setIncludeTableNamePrefixes(includeTableNamePrefixe);
//		
//		String daoPackageName = coreBasePackage + "." + "dao";
//		String baseDaoPackageName = coreBasePackage + "." + "common.dao";
//		DaoGenerator daoGenerator = new DaoGenerator(coreBasePackage, daoPackageName,
//				modelPackageName, baseDaoPackageName, outputDir, "daoInf.ftl",
//				"daoImpl.ftl", delOldFile);
//		String servicePackageName = projectBasePackage + "." + "service";
//		String baseServicePackageName = coreBasePackage + "." + "common.service";
//		ServiceGenerator serviceGenerator = new ServiceGenerator(
//				servicePackageName, modelPackageName, baseServicePackageName,
//				daoPackageName, coreBasePackage, outputDir, "serviceInf.ftl", "serviceImpl.ftl",
//				delOldFile);
//		String controllerPackageName = projectBasePackage + "." + "web.controller";
//		ControllerGenerator controllerGenerator = new ControllerGenerator(
//				controllerPackageName, projectBasePackage, coreBasePackage, outputDir,
//				"controller.ftl", delOldFile);
//		ModelListHtmlGenerator listGenerator = new ModelListHtmlGenerator(
//				htmlOutputDir, "client_pagging_mode_list.ftl", "server_pagging_mode_list.ftl", delOldFile);
//		ModelEditHtmlGenerator editGenerator = new ModelEditHtmlGenerator(
//				htmlOutputDir, "model_edit.ftl", delOldFile);
//		Generator g = new Generator(metaBuilder, modelGenerator, daoGenerator,
//				serviceGenerator, dictGenerator, controllerGenerator,
//				listGenerator, editGenerator, delOldFile);
//		g.generate();
//	}

}
