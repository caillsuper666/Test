package com.cll.test.entity.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * 考试实体
 * @author Administrator
 *
 */
@Component
public class Test {
	
	private Long id;
	private String name;//考试名称
	private Long subjectId;//所属学科专业ID
	private Date beginTime;//考试开始时间
	private Date endTime;//考试结束时间
	private int usableTime;//可用时间
	private int questionNum;//试题总数
	private int count;//总分
	private int singleQuestionNum;//单选题数量
	private int muiltQuestionNum;//多选题数量
	private int chargeQuestionNum;//判断题数量
	
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
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			this.beginTime = sdf.parse(beginTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			this.endTime = sdf.parse(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getUsableTime() {
		return usableTime;
	}
	public void setUsableTime(int usableTime) {
		this.usableTime = usableTime;
	}
	public int getQuestionNum() {
		return questionNum;
	}
	public void setQuestionNum(int questionNum) {
		this.questionNum = questionNum;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public int getSingleQuestionNum() {
		return singleQuestionNum;
	}
	public void setSingleQuestionNum(int singleQuestionNum) {
		this.singleQuestionNum = singleQuestionNum;
	}
	public int getMuiltQuestionNum() {
		return muiltQuestionNum;
	}
	public void setMuiltQuestionNum(int muiltQuestionNum) {
		this.muiltQuestionNum = muiltQuestionNum;
	}
	public int getChargeQuestionNum() {
		return chargeQuestionNum;
	}
	public void setChargeQuestionNum(int chargeQuestionNum) {
		this.chargeQuestionNum = chargeQuestionNum;
	}
}
