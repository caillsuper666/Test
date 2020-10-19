package com.cll.test.entity.admin;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * 试卷信息实体
 * @author Administrator
 *
 */
@Component
public class TestPaper {
	
	private Long id;
	private Long testId;//所属考试ID
	private Long studentId;//所属学生ID
	private int status = 0;//试卷状态：0：未考，1：已考
	private int count;//总分
	private int grade;//得分
	private Date startTestTime;//开始考试时间
	private Date endTestTime;//结束考试时间
	private int timeConsuming;//考试用时
	private Test test;//考试实体
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
