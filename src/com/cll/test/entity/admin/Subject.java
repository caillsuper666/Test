package com.cll.test.entity.admin;

import org.springframework.stereotype.Component;

/**
 * ѧ��רҵʵ����
 * @author Administrator
 *
 */
@Component
public class Subject {
	
	private Long id;
	private String name;//ѧ������

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
