package ${daoPackageName};


import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.agilecode.common.util.StrKit;
import ${baseDaoPackageName}.BaseHibernateDao;
import ${coreBasePackage}.common.model.FullTextSearch;
import ${coreBasePackage}.common.pagination.PageContext;
import ${coreBasePackage}.common.web.support.AdvSearchCondition;
import ${coreBasePackage}.common.web.support.AutoPropertyEditorRegistrySupport;
import ${daoPackageName}.I${modelName}Dao;
import ${modelPackageName}.${modelName};



@Repository("${modelName}Dao")
public class ${modelName}Dao extends BaseHibernateDao<${modelName}, Long> implements I${modelName}Dao {

	@Override
	public PageContext<${modelName}> getPageOfDataAll(PageContext<${modelName}> pageInfo) {
		String searchString = pageInfo.getSearchString();
		String advSearchJson = pageInfo.getAdvSearchJson();
		if (!StrKit.isBlank(searchString)) {
			Criteria criteria = this.createFullSearch(pageInfo);
			return super.getPageOfData(criteria, pageInfo);
		} else if(!StrKit.isBlank(advSearchJson)) {
			Criteria criteria = this.createAdvSearch(pageInfo);
			return super.getPageOfData(criteria, pageInfo);
		}
		return super.getPageOfDataAll(pageInfo);
		
	}

	private Criteria createAdvSearch(PageContext<${modelName}> pageInfo) {
		Criteria base = super.getBaseCriteria();
		String advSearchJson = pageInfo.getAdvSearchJson();
		AdvSearchCondition[] conditions = parseAdvSearchJson(advSearchJson);
		for(AdvSearchCondition condition:conditions) {
			Criterion attrCriterion = null;
			if(condition.getOper().endsWith("eq")) {
				attrCriterion = Restrictions.eq(condition.getField(), getFieldTypeValue(condition,${modelName}.class));
			} else if(condition.getOper().endsWith("gt")) {
				attrCriterion = Restrictions.gt(condition.getField(), getFieldTypeValue(condition,${modelName}.class));
			}  else if(condition.getOper().endsWith("ge")) {
				attrCriterion = Restrictions.ge(condition.getField(), getFieldTypeValue(condition,${modelName}.class));
			}  else if(condition.getOper().endsWith("lt")) {
				attrCriterion = Restrictions.lt(condition.getField(), getFieldTypeValue(condition,${modelName}.class));
			}  else if(condition.getOper().endsWith("le")) {
				attrCriterion = Restrictions.le(condition.getField(), getFieldTypeValue(condition,${modelName}.class));
			}
			base.add(attrCriterion);
		}
		return base;
	}

	private Object getFieldTypeValue(AdvSearchCondition condition,
			Class<${modelName}> modelClass) {
		Field[] fields = modelClass.getDeclaredFields();
		Field f = null;
		for(Field field:fields) {
			if(field.getName().equalsIgnoreCase(condition.getField())) {
				f = field;
				break;
			}
		}
		if(f==null) {
			throw new RuntimeException("没有找到属性"+condition.getField());
		}
		AutoPropertyEditorRegistrySupport ps = new AutoPropertyEditorRegistrySupport();
		Class<?> type = f.getType();
		if(type.getName().equals("java.lang.String")) {
			return condition.getValue();
		}
		PropertyEditor foundedEditor = ps.getDefaultEditor(type);
		if(foundedEditor==null) {
			foundedEditor = ps.findCustomEditor(type, null);
		}
		if(foundedEditor==null) {
			throw new RuntimeException("没有找到合适的类型转换器，请注册一个"+type);
		}
		foundedEditor.setAsText(condition.getValue());
		return foundedEditor.getValue();
	}

	private Criteria createFullSearch(PageContext<${modelName}> pageInfo) {
		Criteria base = super.getBaseCriteria();
		String searchString = "%"+pageInfo.getSearchString()+"%";
		Field[] fields = ${modelName}.class.getDeclaredFields();
		List<Criterion> attrCriterions = new ArrayList<Criterion>();
		for(Field field:fields) {
			if(field.isAnnotationPresent(FullTextSearch.class)) {
				Criterion attrCriterion = Restrictions.like(field.getName(), searchString);
				attrCriterions.add(attrCriterion);
			}
		}
		base.add(Restrictions.or(attrCriterions.toArray(new Criterion[attrCriterions.size()])));
		return base;
	}
}
