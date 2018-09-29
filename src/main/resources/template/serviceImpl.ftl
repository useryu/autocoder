package ${servicePackageName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.agilecode.common.AppException;
import cn.agilecode.common.AdvBeanUtils;
import cn.agilecode.common.service.BaseService;
import ${daoPackageName}.${modelName}Repository;
import ${modelPackageName}.${modelName};

@Service("${modelName}Service")
@Transactional
public class ${modelName}Service extends BaseService<${modelName}, Long> implements I${modelName}Service{
	
	private ${modelName}Repository ${daoFieldName};
	
	@Autowired
	@Qualifier("${modelName}Repository")
	public void set${modelName}Repository(${modelName}Repository ${daoFieldName}) {
		this.${daoFieldName} = ${daoFieldName};
		setBaseDao(${daoFieldName});
	}
	
	
	@Override
	public void update(${modelName} ${modelName?uncap_first}) throws AppException {
		${modelName} db${modelName} = this.${daoFieldName}.getOne(${modelName?uncap_first}.getId());
		AdvBeanUtils.copyPropertiesIgnoreNull(${modelName?uncap_first}, db${modelName});
		this.${daoFieldName}.save(db${modelName});
	}

}