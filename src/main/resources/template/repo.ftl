package ${daoPackageName};

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ${modelPackageName}.${modelName};

//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
@Repository
public interface ${modelName}Repository extends JpaRepository<${modelName}, Long>, JpaSpecificationExecutor<${modelName}> {
	
}