package com.cll.test.entity.admin;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * ����ʵ����
 * @author Administrator
 *
 */
@Component
public class Student {
	
	private Long id;
	private Long subjectId;//����ѧ��רҵID
	private String name;//�˺�
	private String password;//����
	private String realname;//����
	private String phone;//�ֻ���
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
}
