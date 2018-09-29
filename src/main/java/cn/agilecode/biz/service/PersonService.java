package cn.agilecode.biz.service;

import org.springframework.stereotype.Component;

import cn.agilecode.biz.dto.Person;

@Component
public class PersonService {

	public Person findOne1() {
		Person p = new Person();
		p.setAge(11);
		p.setName("fisher");
		return p;
	}

}
