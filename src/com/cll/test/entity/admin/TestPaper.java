package com.cll.test.entity.admin;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * �Ծ���Ϣʵ��
 * @author Administrator
 *
 */
@Component
public class TestPaper {
	
	private Long id;
	private Long testId;//��������ID
	private Long studentId;//����ѧ��ID
	private int status = 0;//�Ծ�״̬��0��δ����1���ѿ�
	private int count;//�ܷ�
	private int grade;//�÷�
	private Date startTestTime;//��ʼ����ʱ��
	private Date endTestTime;//��������ʱ��
	private int timeConsuming;//������ʱ
	private Test test;//����ʵ��
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public Date getStartTestTime() {
		return startTestTime;
	}
	public void setStartTestTime(Date startTestTime) {
		this.startTestTime = startTestTime;
	}
	public Date getEndTestTime() {
		return endTestTime;
	}
	public void setEndTestTime(Date endTestTime) {
		this.endTestTime = endTestTime;
	}
	public int getTimeConsuming() {
		return timeConsuming;
	}
	public void setTimeConsuming(int timeConsuming) {
		this.timeConsuming = timeConsuming;
	}
	public Test getTest() {
		return test;
	}
	public void setTest(Test test) {
		this.test = test;
	}
	
}
