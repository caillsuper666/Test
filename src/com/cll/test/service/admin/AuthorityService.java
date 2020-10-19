package com.cll.test.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cll.test.entity.admin.Authority;

/**
 * 权限service接口
 * @author llq
 *
 */
@Service
public interface AuthorityService {
	public int add(Authority authority);
	public int deleteByRoleId(Long roleId);
	public List<Authority> findListByRoleId(Long roleId);
}
