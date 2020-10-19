package com.cll.test.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


import com.cll.test.entity.admin.TestPaper;

/**
 *  ‘æÌservice¿‡
 * @author Administrator
 *
 */
@Service
public interface TestPaperService {
	public int add(TestPaper testPaper);
	public int edit(TestPaper testPaper);
	public List<TestPaper> findList(Map<String, Object> queryMap);
	public int delete(Long id);
	public Integer getTotal(Map<String, Object> queryMap);
	public List<TestPaper> findHistory(Map<String, Object> queryMap);
	public Integer getHistoryTotal(Map<String, Object> queryMap);
	public TestPaper find(Map<String, Object> queryMap);
	public int submitPaper(TestPaper testPaper);
	public List<Map<String,Object>> getTestStats(Long testId);
}
