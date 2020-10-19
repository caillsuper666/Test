package com.cll.test.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cll.test.entity.admin.AnswerQuestion;


/**
 *  ‘æÌ¥Ã‚service¿‡
 * @author Administrator
 *
 */
@Service
public interface AnswerQuestionService {
	public int add(AnswerQuestion answerQuestion);
	public int edit(AnswerQuestion answerQuestion);
	public List<AnswerQuestion> findList(Map<String, Object> queryMap);
	public int delete(Long id);
	public Integer getTotal(Map<String, Object> queryMap);
	public List<AnswerQuestion> findListByUser(Map<String, Object> queryMap);
	public int submitAnswer(AnswerQuestion answerQuestion);
}
