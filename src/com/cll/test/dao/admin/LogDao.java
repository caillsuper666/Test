package com.cll.test.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cll.test.entity.admin.Authority;
import com.cll.test.entity.admin.Log;

/**
 * ϵͳ��־��dao
 * @author llq
 *
 */
@Repository
public interface LogDao {
	public int add(Log log);
	public List<Log> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
	public int delete(String ids);
}
