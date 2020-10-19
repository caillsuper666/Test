package com.cll.test.service.admin.impl;
/**
 *  ‘æÌservice µœ÷¿‡
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cll.test.dao.admin.TestPaperDao;
import com.cll.test.entity.admin.TestPaper;
import com.cll.test.service.admin.TestPaperService;
@Service
public class TestPaperServiceImpl implements TestPaperService {

	@Autowired
	private TestPaperDao testPaperDao;

	

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return testPaperDao.delete(id);
	}

	@Override
	public Integer getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testPaperDao.getTotal(queryMap);
	}

	@Override
	public int add(TestPaper testPaper) {
		// TODO Auto-generated method stub
		return testPaperDao.add(testPaper);
	}

	@Override
	public int edit(TestPaper testPaper) {
		// TODO Auto-generated method stub
		return testPaperDao.edit(testPaper);
	}

	@Override
	public List<TestPaper> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testPaperDao.findList(queryMap);
	}

	@Override
	public List<TestPaper> findHistory(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testPaperDao.findHistory(queryMap);
	}

	@Override
	public Integer getHistoryTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testPaperDao.getHistoryTotal(queryMap);
	}

	@Override
	public TestPaper find(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return testPaperDao.find(queryMap);
	}

	@Override
	public int submitPaper(TestPaper examPaper) {
		// TODO Auto-generated method stub
		return testPaperDao.submitPaper(examPaper);
	}

	@Override
	public List<Map<String,Object>> getTestStats(Long testId) {
		// TODO Auto-generated method stub
		return testPaperDao.getTestStats(testId);
	}


}
