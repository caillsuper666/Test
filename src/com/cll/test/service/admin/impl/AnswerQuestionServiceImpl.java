package com.cll.test.service.admin.impl;
/**
 * 试卷答题service实现类
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cll.test.dao.admin.AnswerQuestionDao;
import com.cll.test.entity.admin.AnswerQuestion;
import com.cll.test.service.admin.AnswerQuestionService;

@Service
public class AnswerQuestionServiceImpl implements AnswerQuestionService {

	@Autowired
	private AnswerQuestionDao answerQuestionDao;

	@Override
	public int add(AnswerQuestion answerQuestion) {
		// TODO Auto-generated method stub
		return answerQuestionDao.add(answerQuestion);
	}

	@Override
	public int edit(AnswerQuestion answerQuestion) {
		// TODO Auto-generated method stub
		return answerQuestionDao.edit(answerQuestion);
	}

	@Override
	public List<AnswerQuestion> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return answerQuestionDao.findList(queryMap);
	}

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return answerQuestionDao.delete(id);
	}

	@Override
	public Integer getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return answerQuestionDao.getTotal(queryMap);
	}

	@Override
	public List<AnswerQuestion> findListByUser(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return answerQuestionDao.findListByUser(queryMap);
	}

	@Override
	public int submitAnswer(AnswerQuestion answerQuestion) {
		// TODO Auto-generated method stub
		return answerQuestionDao.submitAnswer(answerQuestion);
	}

	

	


}
