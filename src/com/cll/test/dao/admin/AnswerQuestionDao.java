package com.cll.test.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cll.test.entity.admin.AnswerQuestion;



/**
 * ÊÔ¾ídao²ã
 * @author Administrator
 *
 */
@Repository
public interface AnswerQuestionDao {
	public int add(AnswerQuestion answerQuestion);
	public int edit(AnswerQuestion answerQuestion);
	public List<AnswerQuestion> findList(Map<String, Object> queryMap);
	public int delete(Long id);
	public Integer getTotal(Map<String, Object> queryMap);
	public List<AnswerQuestion> findListByUser(Map<String, Object> queryMap);
	public int submitAnswer(AnswerQuestion answerQuestion);
}
