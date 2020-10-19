package com.cll.test.entity.admin;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * 试题实体类
 * @author Administrator
 *
 */
@Component
public class Question {
	
	//试题类型定义
	public static int QUESTION_TYPE_SINGLE = 0;//单选题型
	public static int QUESTION_TYPE_MUILT = 1;//多选题型
	public static int QUESTION_TYPE_CHARGE = 2;//判断题型
	//试题分值定义，根据试题类型定义分值
	public static int QUESTION_TYPE_SINGLE_SCORE = 5;//单选题型，每题5分
	public static int QUESTION_TYPE_MUILT_SCORE = 10;//多选题型,每题10分
	public static int QUESTION_TYPE_CHARGE_SCORE = 5;//判断题型,每题5分
	
	private Long id;
	private Long subjectId;//学科专业类型
	private int questionType;//试题类型
	private String title;//题目
	private int score;//分值
	private String optionA;//选项A
	private String optionB;//选项B
	private String optionC;//选项C
	private String optionD;//选项D
	private String rightanswer;//正确答案
	
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
	 * 根据试题类型设置分值
	 */
	public void setScoreByType(){
		if(questionType == QUESTION_TYPE_SINGLE)score = QUESTION_TYPE_SINGLE_SCORE;
		if(questionType == QUESTION_TYPE_MUILT)score = QUESTION_TYPE_MUILT_SCORE;
		if(questionType == QUESTION_TYPE_CHARGE)score = QUESTION_TYPE_CHARGE_SCORE;
	}
}
