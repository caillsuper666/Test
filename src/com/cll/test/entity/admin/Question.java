package com.cll.test.entity.admin;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * ����ʵ����
 * @author Administrator
 *
 */
@Component
public class Question {
	
	//�������Ͷ���
	public static int QUESTION_TYPE_SINGLE = 0;//��ѡ����
	public static int QUESTION_TYPE_MUILT = 1;//��ѡ����
	public static int QUESTION_TYPE_CHARGE = 2;//�ж�����
	//�����ֵ���壬�����������Ͷ����ֵ
	public static int QUESTION_TYPE_SINGLE_SCORE = 5;//��ѡ���ͣ�ÿ��5��
	public static int QUESTION_TYPE_MUILT_SCORE = 10;//��ѡ����,ÿ��10��
	public static int QUESTION_TYPE_CHARGE_SCORE = 5;//�ж�����,ÿ��5��
	
	private Long id;
	private Long subjectId;//ѧ��רҵ����
	private int questionType;//��������
	private String title;//��Ŀ
	private int score;//��ֵ
	private String optionA;//ѡ��A
	private String optionB;//ѡ��B
	private String optionC;//ѡ��C
	private String optionD;//ѡ��D
	private String rightanswer;//��ȷ��
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOptionA() {
		return optionA;
	}
	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}
	public String getOptionB() {
		return optionB;
	}
	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}
	public String getOptionC() {
		return optionC;
	}
	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}
	public String getOptionD() {
		return optionD;
	}
	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}
	public String getRightanswer() {
		return rightanswer;
	}
	public void setRightanswer(String rightanswer) {
		this.rightanswer = rightanswer;
	}
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * ���������������÷�ֵ
	 */
	public void setScoreByType(){
		if(questionType == QUESTION_TYPE_SINGLE)score = QUESTION_TYPE_SINGLE_SCORE;
		if(questionType == QUESTION_TYPE_MUILT)score = QUESTION_TYPE_MUILT_SCORE;
		if(questionType == QUESTION_TYPE_CHARGE)score = QUESTION_TYPE_CHARGE_SCORE;
	}
}
