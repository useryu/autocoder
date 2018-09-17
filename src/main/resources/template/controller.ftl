package ${controllerPackage};


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.agilecode.common.AppException;
import cn.agilecode.common.pagination.PageContext;
import cn.agilecode.common.web.controller.BaseController;
import cn.agilecode.common.web.support.DatatableReqBean;
import cn.agilecode.common.web.support.DatatableRespBean;

import ${coreBasePackage}.model.${modelClassName};
<#list oneSideModeNames as oneSideModeName> 
import ${projectPackage}.service.I${oneSideModeName}Service;
import ${coreBasePackage}.model.${oneSideModeName};
</#list>
import ${projectPackage}.service.I${modelClassName}Service;




@Controller
public class ${modelClassName}Controller extends BaseController{

	@Autowired
	private I${modelClassName}Service ${modelFieldName}Service;
	<#list oneSideModeNames as oneSideModeName> 
	@Autowired
	private I${oneSideModeName}Service ${oneSideModeName?uncap_first}Service;
	</#list>

	@RequiresPermissions({"${modelFieldName}:list"})
	@RequestMapping(value = "/${modelFieldName}/list", method = { RequestMethod.GET })
	public String list(HttpServletRequest req, Model model) {
		set${modelClassName}ListAttrs(req);
		return "${modelFieldName}/${modelFieldName}_list";
	}

	private void set${modelClassName}ListAttrs(HttpServletRequest req) {
		PageContext<${modelClassName}> pageInfo = new PageContext<${modelClassName}>();
		pageInfo = ${modelFieldName}Service.getPageOfDataAll(pageInfo);
		List<${modelClassName}> items = pageInfo.getItems();
		setAttr(req, "${modelFieldName}s", items);
		setAttr(req, "headers", ${modelClassName}.getHeadersFromModel(${modelClassName}.class));
		super.setMenuActive(req, "${modelFieldName}");
	}

	@RequiresPermissions({"${modelFieldName}:list"})
	@RequestMapping(value = "/${modelFieldName}/list.json", produces = "application/json")
	@ResponseBody
	public DatatableRespBean listjson(HttpServletRequest req, @ModelAttribute DatatableReqBean datatableReqBean) {
		datatableReqBean.setSearchValue(getAttr(req,"search[value]",""));
		datatableReqBean.setSearchRegex(Boolean.parseBoolean(getAttr(req,"search[regex]","false")));
		datatableReqBean.setOrderColumn(getAttr(req,"order[0][column]","0"));
		datatableReqBean.setOrderDir(getAttr(req,"order[0][dir]","asc"));
		PageContext<${modelClassName}> pageInfo = new PageContext<${modelClassName}>();
		pageInfo.setStart(datatableReqBean.getStart());
		pageInfo.setPageSize(datatableReqBean.getLength());
		pageInfo.setSortAsc(datatableReqBean.getOrderDir().equalsIgnoreCase("asc"));
		pageInfo.setSearchString(datatableReqBean.getSearchValue());
		pageInfo.setAdvSearchJson(datatableReqBean.getAdvSearch());
		String sortColumn = ${modelClassName}.getSourColumnFromModel(${modelClassName}.class, datatableReqBean);//
		pageInfo.setSortColumn(sortColumn);
		pageInfo = ${modelFieldName}Service.getPageOfDataAll(pageInfo);
		DatatableRespBean datatableRespBean = new DatatableRespBean();
		datatableRespBean.setDraw(datatableReqBean.getDraw());
		datatableRespBean.setRecordsTotal(pageInfo.getTotalSize());
		datatableRespBean.setRecordsFiltered(pageInfo.getTotalSize());
		List<String[]> data = null;
		try {
			data = genTableDataFromModel(pageInfo, req, "${modelFieldName}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		datatableRespBean.setData(data);
		return datatableRespBean ;
	}
	
	@RequiresPermissions({"${modelFieldName}:view"})
	@RequestMapping(value = "/${modelFieldName}/view/{${modelFieldName}Id}", method = { RequestMethod.GET })
	public String view(@PathVariable Long ${modelFieldName}Id, HttpServletRequest req) {
		setAttr(req, "${modelFieldName}", ${modelFieldName}Service.getById(${modelFieldName}Id));
		return "${modelFieldName}/${modelFieldName}_view";
	}
	
	@RequiresPermissions({"${modelFieldName}:add"})
	@RequestMapping(value = "/${modelFieldName}/toadd", method = { RequestMethod.GET })
	public String toAdd(HttpServletRequest req) {
		setAttr(req, "${modelFieldName}", new ${modelClassName}());
		<#list oneSideModeNames as oneSideModeName>
		setAttr(req, "${oneSideModeName?uncap_first}s", ${oneSideModeName?uncap_first}Service.getAll());
		</#list>
		setMenuActive(req, "${modelFieldName}");
		return "${modelFieldName}/${modelFieldName}_edit";
	}
	
	@RequiresPermissions({"${modelFieldName}:add"})
	@RequestMapping(value = "/${modelFieldName}/add", method = { RequestMethod.POST })
	public String add( HttpServletRequest req, @ModelAttribute("${modelFieldName}") ${modelClassName} ${modelFieldName}) throws AppException {
		${modelFieldName}Service.save(${modelFieldName});
		setAlert(req, "添加成功");
		set${modelClassName}ListAttrs(req);
		return "redirect:/${modelFieldName}/toadd";
	}
	
	@RequiresPermissions({"${modelFieldName}:edit"})
	@RequestMapping(value = "/${modelFieldName}/toedit/{${modelFieldName}Id}", method = { RequestMethod.GET })
	public String toEdit(@PathVariable Long ${modelFieldName}Id, HttpServletRequest req) {
		setAttr(req, "${modelFieldName}", ${modelFieldName}Service.getById(${modelFieldName}Id));
		<#list oneSideModeNames as oneSideModeName> 
		List<${oneSideModeName}> ${oneSideModeName?uncap_first}s = ${oneSideModeName?uncap_first}Service.getAll();
		setAttr(req, "${oneSideModeName?uncap_first}s", ${oneSideModeName?uncap_first}s);
		</#list>
		setMenuActive(req, "${modelFieldName}");
		return "${modelFieldName}/${modelFieldName}_edit";
	}
	
	@RequiresPermissions({"${modelFieldName}:edit"})
	@RequestMapping(value = "/${modelFieldName}/edit", method = { RequestMethod.POST })
	public String edit( HttpServletRequest req, @ModelAttribute("${modelFieldName}") ${modelClassName} ${modelFieldName}) throws AppException {
		${modelFieldName}Service.update(${modelFieldName});
		setAlert(req, "修改成功");
		set${modelClassName}ListAttrs(req);
		return "/${modelFieldName}/${modelFieldName}_list";
	}

	@RequiresPermissions({"${modelFieldName}:delete"})
	@RequestMapping(value = "/${modelFieldName}/delete/{${modelFieldName}Id}", method = { RequestMethod.GET })
	public String delete(@PathVariable Long ${modelFieldName}Id, HttpServletRequest req) throws AppException {
		${modelFieldName}Service.delById(${modelFieldName}Id);
		setAlert(req, "删除成功");
		set${modelClassName}ListAttrs(req);
		return "/${modelFieldName}/${modelFieldName}_list";
	}
	
}
