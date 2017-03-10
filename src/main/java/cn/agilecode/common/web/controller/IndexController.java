package cn.agilecode.common.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.agilecode.common.web.support.ProjectBean;

@Controller
public class IndexController {
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String tologin(HttpServletRequest req) throws IOException {
		return "main/login";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(HttpServletRequest req) throws IOException {
		return "main/main";
	}


	@RequestMapping(value = "/project/getDemoProject.json", produces = "application/json")
	@ResponseBody
	public ProjectBean getDemoProject(HttpServletRequest req) {
		ObjectMapper objectMapper = new ObjectMapper();
		Resource resource = new ClassPathResource("test_schema.json");
		ProjectBean bean = null;
		try {
			bean = objectMapper.readValue(resource.getFile() , ProjectBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	@RequestMapping(value = "/project/save", produces = "application/json")
	@ResponseBody
	public ProjectBean save(HttpServletRequest req) {
		String jsonStr = req.getParameter("jsonStr");
		ObjectMapper objectMapper = new ObjectMapper();
		Resource resource = new ClassPathResource("test_schema.json");
		ProjectBean bean = null;
		
		return bean;
	}
}
