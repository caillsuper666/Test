package com.cll.test.entity.admin;

import org.springframework.stereotype.Component;

/**
 * �Ծ������Ϣʵ��
 * @author Administrator
 *
 */
@Component
public class AnswerQuestion {
	
	private Long id;
	private Long studentId;//����ѧ��ID
	private Long testId;//��������ID
	private Long testPaperId;//�����Ծ�ID
	private Long questionId;//��������ID
	private String answer;//�ύ��
	private int isCorrect;//�Ƿ���ȷ
	private Question question;//���⼯
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
