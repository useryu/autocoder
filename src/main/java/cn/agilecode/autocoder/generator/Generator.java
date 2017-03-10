package cn.agilecode.autocoder.generator;

import java.util.List;

import cn.agilecode.autocoder.dialect.Dialect;
import cn.agilecode.autocoder.metadata.MetaBuilder;
import cn.agilecode.autocoder.metadata.TableMeta;
import cn.agilecode.autocoder.metadata.TypeMapping;

/**
 * 生成器
 * 1：生成时会强制覆盖 Base model、MappingKit、DataDictionary，建议不要修改三类文件，在数据库有变化重新生成一次便可
 * 2：生成  Model 不会覆盖已经存在的文件，Model 通常会被人为修改和维护
 * 3：MappingKit 文件默认会在生成 Model 文件的同时生成
 * 4：DataDictionary 文件默认不会生成。只有在设置 setGenerateDataDictionary(true)后，会在生成 Model文件的同时生成
 * 5：可以通过继承 BaseModelGenerator、ModelGenerator、MappingKitGenerator、DataDictionaryGenerator
 *   来创建自定义生成器，然后使用 Generator 的 setter 方法指定自定义生成器来生成
 * 6：生成模板文字属性全部为 protected 可见性，方便自定义 Generator 生成符合。。。。
 */
public class Generator {
	
	protected MetaBuilder metaBuilder;
	protected ModelGenerator modelGenerator;
	protected DaoGenerator daoGenerator;
	protected ServiceGenerator serviceGenerator;
	protected DataDictionaryGenerator dataDictionaryGenerator;
	protected ControllerGenerator controllerGenerator;
	protected ModelListHtmlGenerator modelListHtmlGenerator;
	protected boolean generateDataDictionary = false;
	protected ModelEditHtmlGenerator modelEditHtmlGenerator;
	
	
	public Generator(MetaBuilder metaBuilder, ModelGenerator modelGenerator, DaoGenerator daoGenerator,
			ServiceGenerator serviceGenerator,DataDictionaryGenerator dataDictionaryGenerator,
			ControllerGenerator controllerGenerator,ModelListHtmlGenerator modelListHtmlGenerator,
			ModelEditHtmlGenerator modelEditHtmlGenerator,
			boolean generateDataDictionary) {
		super();
		this.metaBuilder = metaBuilder;
		this.modelGenerator = modelGenerator;
		this.daoGenerator = daoGenerator;
		this.serviceGenerator = serviceGenerator;
		this.dataDictionaryGenerator = dataDictionaryGenerator;
		this.generateDataDictionary = generateDataDictionary;
		this.controllerGenerator = controllerGenerator;
		this.modelListHtmlGenerator = modelListHtmlGenerator;
		this.modelEditHtmlGenerator = modelEditHtmlGenerator;
	}

	/**
	 * 设置 MetaBuilder，便于扩展自定义 MetaBuilder
	 */
	public void setMetaBuilder(MetaBuilder metaBuilder) {
		if (metaBuilder != null) {
			this.metaBuilder = metaBuilder;
		}
	}
	
	public void setTypeMapping(TypeMapping typeMapping) {
		this.metaBuilder.setTypeMapping(typeMapping);
	}
	
	/**
	 * 设置 DataDictionaryGenerator，便于扩展自定义 DataDictionaryGenerator
	 */
	public void setDataDictionaryGenerator(DataDictionaryGenerator dataDictionaryGenerator) {
		if (dataDictionaryGenerator != null) {
			this.dataDictionaryGenerator = dataDictionaryGenerator;
		}
	}
	
	/**
	 * 设置数据库方言，默认为 MysqlDialect
	 */
	public void setDialect(Dialect dialect) {
		metaBuilder.setDialect(dialect);
	}
	
	/**
	 * 设置需要被移除的表名前缀，仅用于生成 modelName 与  baseModelName
	 * 例如表名  "osc_account"，移除前缀 "osc_" 后变为 "account"
	 */
	public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
		metaBuilder.setRemovedTableNamePrefixes(removedTableNamePrefixes);
	}
	
	/**
	 * 添加不需要处理的数据表
	 */
	public void addExcludedTable(String... excludedTables) {
		metaBuilder.addExcludedTable(excludedTables);
	}
	
	/**
	 * 设置是否生成数据字典 Dictionary 文件，默认不生成
	 */
	public void setGenerateDataDictionary(boolean generateDataDictionary) {
		this.generateDataDictionary = generateDataDictionary;
	}
	
	/**
	 * 设置数据字典 DataDictionary 文件输出目录，默认与 modelOutputDir 相同
	 */
	public void setDataDictionaryOutputDir(String dataDictionaryOutputDir) {
		if (this.dataDictionaryGenerator != null) {
			this.dataDictionaryGenerator.setDataDictionaryOutputDir(dataDictionaryOutputDir);
		}
	}
	
	/**
	 * 设置数据字典 DataDictionary 文件输出目录，默认值为 "_DataDictionary.txt"
	 */
	public void setDataDictionaryFileName(String dataDictionaryFileName) {
		if (dataDictionaryGenerator != null) {
			dataDictionaryGenerator.setDataDictionaryFileName(dataDictionaryFileName);
		}
	}
	
	public void generate() {
		long start = System.currentTimeMillis();
		List<TableMeta> tableMetas = metaBuilder.build();
		if (tableMetas.size() == 0) {
			System.out.println("TableMeta 数量为 0，不生成任何文件");
			return ;
		}
		
		if (modelGenerator != null) {
			try {
				modelGenerator.generate(tableMetas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (daoGenerator != null) {
			try {
				daoGenerator.generate(tableMetas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (serviceGenerator != null) {
			try {
				serviceGenerator.generate(tableMetas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (controllerGenerator != null) {
			try {
				controllerGenerator.generate(tableMetas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (modelListHtmlGenerator != null) {
			try {
				modelListHtmlGenerator.generate(tableMetas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (modelEditHtmlGenerator != null) {
			try {
				modelEditHtmlGenerator.generate(tableMetas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (dataDictionaryGenerator != null && generateDataDictionary) {
			dataDictionaryGenerator.generate(tableMetas);
		}
		
		long usedTime = (System.currentTimeMillis() - start) / 1000;
		System.out.println("Generate complete in " + usedTime + " seconds.");
	}
}



