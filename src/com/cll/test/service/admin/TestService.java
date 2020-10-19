package com.cll.test.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cll.test.entity.admin.Test;

/**
 * øº ‘service¿‡
 * @author Administrator
 *
 */
@Service
public interface TestService {
	public int add(Test test);
	public int edit(Test test);
	public List<Test> findList(Map<String, Object> queryMap);
	public int delete(String ids);
	public Integer getTotal(Map<String, Object> queryMap);
	public List<Test> findListByUser(Map<String, Object> queryMap);
	public Integer getTotalByUser(Map<String, Object> queryMap);
	public Test findById(Long id);


}
