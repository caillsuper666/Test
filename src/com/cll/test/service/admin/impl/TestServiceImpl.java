package com.cll.test.service.admin.impl;
/**
 * øº ‘service µœ÷¿‡
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cll.test.dao.admin.TestDao;
import com.cll.test.entity.admin.Test;
import com.cll.test.service.admin.TestService;
@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestDao testDao;

	@Override
	public int add(Test test) {
		// TODO Auto-generated method stub
		return testDao.add(test);
	}

	@Override
	public int edit(Test test) {
		// TODO Auto-generated method stub
		return testDao.edit(test);
	}

	@Override
	public List<Test> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testDao.findList(queryMap);
	}

	@Override
	public int delete(String ids) {
		// TODO Auto-generated method stub
		return testDao.delete(ids);
	}

	@Override
	public Integer getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testDao.getTotal(queryMap);
	}

	@Override
	public List<Test> findListByUser(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testDao.findListByUser(queryMap);
	}

	@Override
	public Integer getTotalByUser(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testDao.getTotalByUser(queryMap);
	}

	@Override
	public Test findById(Long id) {
		// TODO Auto-generated method stub
		return testDao.findById(id);
	}
	
}
