package com.cll.test.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cll.test.entity.admin.Role;
import com.cll.test.entity.admin.User;
import com.cll.test.page.admin.Page;
import com.cll.test.service.admin.RoleService;
import com.cll.test.service.admin.UserService;

/**
 * 用户管理控制器
 * @author llq
 *
 */
@RequestMapping("/user")
@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	/**
	 * 用户列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		model.setViewName("user/list");
		return model;
	}
	
	/**
	 * 获取用户列表
	 * @param page
	 * @param username
	 * @param roleId
	 * @param sex
	 * @return
	 */
	@RequestMapping(value="/get_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(Page page,
			@RequestParam(name="username",required=false,defaultValue="") String username,
			@RequestParam(name="sex",required=false) Integer sex
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", username);
		queryMap.put("sex", sex);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", userService.findList(queryMap));
		ret.put("total", userService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null){
			ret.put("type", "error");
			ret.put("msg", "请填写正确的用户信息！");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())){
			ret.put("type", "error");
			ret.put("msg", "请填写用户名！");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())){
			ret.put("type", "error");
			ret.put("msg", "请填写密码！");
			return ret;
		}
		if(isExist(user.getUsername(), 0l)){
			ret.put("type", "error");
			ret.put("msg", "该用户名已经存在，请重新输入！");
			return ret;
		}
		if(userService.add(user) <= 0){
			ret.put("type", "error");
			ret.put("msg", "用户添加失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "角色添加成功！");
		return ret;
	}
	
	/**
	 * 编辑用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null){
			ret.put("type", "error");
			ret.put("msg", "请填写正确的用户信息！");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())){
			ret.put("type", "error");
			ret.put("msg", "请填写用户名！");
			return ret;
		}
//		if(StringUtils.isEmpty(user.getPassword())){
//			ret.put("type", "error");
//			ret.put("msg", "请填写密码！");
//			return ret;
//		}
		
		if(isExist(user.getUsername(), user.getId())){
			ret.put("type", "error");
			ret.put("msg", "该用户名已经存在，请重新输入！");
			return ret;
		}
		if(userService.edit(user) <= 0){
			ret.put("type", "error");
			ret.put("msg", "用户添加失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "角色修改成功！");
		return ret;
	}
	
	/**
	 * 批量删除用户
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(
			@RequestParam(value="ids[]",required=true) Long[] ids
		){
		Map<String, String> ret = new HashMap<String, String>();
		if(ids == null){
			ret.put("type", "error");
			ret.put("msg", "请选择要删除的数据!");
			return ret;
		}
		String idsString = "";
		for(Long id:ids){
			idsString += id + ",";
		}
		idsString = idsString.substring(0,idsString.length()-1);
		if(userService.delete(idsString) <= 0){
			ret.put("type", "error");
			ret.put("msg", "删除失败!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "修改成功!");
		return ret;
	}
	
	
	/**
	 * 判断该用户名是否存在
	 * @param username
	 * @param id
	 * @return
	 */
	private boolean isExist(String username,Long id){
		User user = userService.findByUsername(username);
		if(user == null)return false;
		if(user.getId().longValue() == id.longValue())return false;
		return true;
	}
}
