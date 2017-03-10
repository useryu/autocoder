package cn.agilecode.common.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String tologin(HttpServletRequest req) throws IOException {
		return "main/login";
	}

}
