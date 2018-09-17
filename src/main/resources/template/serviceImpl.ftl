package ${servicePackageName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.agilecode.common.AppException;
import cn.agilecode.common.AdvBeanUtils;
import cn.agilecode.common.service.BaseService;
import ${daoPackageName}.I${modelName}Dao;
import ${modelPackageName}.${modelName};

@Service("${modelName}Service")
@Transactional
public class ${modelName}Service extends BaseService<${modelName}, Long> implements I${modelName}Service{
	
	private I${modelName}Dao ${daoFieldName};
	
	@Autowired
	@Qualifier("${modelName}Dao")
	public void set${modelName}Dao(I${modelName}Dao ${daoFieldName}) {
		this.${daoFieldName} = ${daoFieldName};
		setBaseDao(${daoFieldName});
	}
	
	
	@Override
	public void update(${modelName} ${modelName?uncap_first}) throws AppException {
		${modelName} db${modelName} = this.${daoFieldName}.getById(${modelName?uncap_first}.getId());
		AdvBeanUtils.copyPropertiesIgnoreNull(${modelName?uncap_first}, db${modelName});
		this.${daoFieldName}.update(db${modelName});
	}

}