package ${packageName};

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cn.agilecode.common.model.AbstractModel;
import cn.agilecode.common.model.FullTextSearch;
import cn.agilecode.common.model.Idenfitier;
import cn.agilecode.common.model.ShowInList;

@Entity
@Table(${table.tableHibernateDef})
@Indexed
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class ${table.modelName} extends AbstractModel implements Idenfitier<Long>{
	
	private static final long serialVersionUID = 1L;
	
	<#list fields as field>
	//备注:${field.remarks}
	${field.hibernateDef}
	<#if field.showInList>@ShowInList(chname="${field.remarks}")</#if>
	<#if field.fullTextSearch>@FullTextSearch</#if>
	private ${field.javaType} ${field.attrName}${field.initFieldStr};
	
	</#list>
	
	//belows is get and setters
	<#list fields as field>
	
	public ${field.javaType} get${field.upperFirstCharName}(){
		return ${field.attrName};
	}
	
	public void set${field.upperFirstCharName}(${field.javaType} ${field.attrName}){
		this.${field.attrName} = ${field.attrName};
	}
	
	</#list>
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		${table.modelName} other = (${table.modelName}) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "${table.modelName} [<#list fields as field><#if field.showInTostringMethod>,${field.attrName}="+${field.attrName}+" </#if></#list>]";
	}
	
	/*autocode seperator line*/
	
}