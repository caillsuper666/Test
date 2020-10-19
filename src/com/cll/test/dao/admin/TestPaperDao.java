package com.cll.test.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cll.test.entity.admin.TestPaper;

/**
 * ÊÔ¾ídao²ã
 * @author Administrator
 *
 */
@Repository
public interface TestPaperDao {
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
