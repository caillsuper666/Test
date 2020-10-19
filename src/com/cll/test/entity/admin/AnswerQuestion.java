package com.cll.test.entity.admin;

import org.springframework.stereotype.Component;

/**
 * 试卷答题信息实体
 * @author Administrator
 *
 */
@Component
public class AnswerQuestion {
	
	private Long id;
	private Long studentId;//所属学生ID
	private Long testId;//所属考试ID
	private Long testPaperId;//所属试卷ID
	private Long questionId;//所属试题ID
	private String answer;//提交答案
	private int isCorrect;//是否正确
	private Question question;//试题集
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getTestPaperId() {
		return testPaperId;
	}
	public void setTestPaperId(Long testPaperId) {
		this.testPaperId = testPaperId;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getIsCorrect() {
		return isCorrect;
	}
	public void setIsCorrect(int isCorrect) {
		this.isCorrect = isCorrect;
	}
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	
	
}
