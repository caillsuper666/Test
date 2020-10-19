package com.cll.test.controller.home;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cll.test.entity.admin.Test;
import com.cll.test.entity.admin.TestPaper;
import com.cll.test.entity.admin.AnswerQuestion;
import com.cll.test.entity.admin.Question;
import com.cll.test.entity.admin.Student;
import com.cll.test.service.admin.AnswerQuestionService;
import com.cll.test.service.admin.TestPaperService;
import com.cll.test.service.admin.TestService;
import com.cll.test.service.admin.StudentService;
import com.cll.test.service.admin.SubjectService;
import com.cll.test.util.DateFormatUtil;

/**
 * 前台考生中心控制器
 * @author Administrator
 *
 */
@RequestMapping("/home/user")
@Controller
public class HomeStudentController {

	@Autowired
	private StudentService studentService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TestService testService;
	@Autowired
	private TestPaperService testPaperService;
	
	@Autowired
	private AnswerQuestionService answerQuestionService;
	
	private int pageSize = 10;
	/**
	 * 考生中心首页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model){
		model.addObject("title", "考生中心");
		model.setViewName("/home/user/index");
		return model;
	}

	
	/**
	 * 获取当前登录用户的用户名
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/get_current",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> getCurrent(HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		Object attribute = request.getSession().getAttribute("student");
		if(attribute == null){
			ret.put("type", "error");
			ret.put("msg", "登录信息失效！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "获取成功！");
		Student student  = (Student)attribute;
		ret.put("username", student.getName());
		ret.put("realname", student.getRealname());
		return ret;
	}
	
	/**
	 * 用户基本信息页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/profile",method = RequestMethod.GET)
	public ModelAndView profile(ModelAndView model,HttpServletRequest request){
		Student student = (Student)request.getSession().getAttribute("student");
		model.addObject("title", "考生信息");
		model.addObject("student", student);
		model.addObject("subject", subjectService.findById(student.getSubjectId()));
		model.setViewName("/home/user/profile");
		return model;
	}
	
	/**
	 * 修改用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/update_info",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> updateInfo(Student student,HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		Student onlineStudent  = (Student)request.getSession().getAttribute("student");
		onlineStudent.setPhone(student.getPhone());
		onlineStudent.setRealname(student.getRealname());
		if(studentService.edit(onlineStudent) <= 0){
			ret.put("type", "error");
			ret.put("msg", "修改失败，请联系管理员！");
			return ret;
		}
		//重置session中的用户信息
		request.getSession().setAttribute("student", onlineStudent);
		ret.put("type", "success");
		ret.put("msg", "获取成功！");
		return ret;
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout",method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
		request.getSession().setAttribute("student", null);
		return "redirect:login";
	}
	
	/**
	 * 用户修改密码
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/password",method = RequestMethod.GET)
	public ModelAndView password(ModelAndView model,HttpServletRequest request){
		Student student = (Student)request.getSession().getAttribute("student");
		model.addObject("student", student);
		model.setViewName("/home/user/password");
		return model;
	}
	
	/**
	 * 修改密码提交
	 * @param student
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/update_password",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> updatePassword(Student student,String oldPassword,HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		Student onlineStudent  = (Student)request.getSession().getAttribute("student");
		if(!onlineStudent.getPassword().equals(oldPassword)){
			ret.put("type", "error");
			ret.put("msg", "旧密码错误！");
			return ret;
		}
		onlineStudent.setPassword(student.getPassword());
		if(studentService.edit(onlineStudent) <= 0){
			ret.put("type", "error");
			ret.put("msg", "修改失败，请联系管理员！");
			return ret;
		}
		//重置session中的用户信息
		request.getSession().setAttribute("student", onlineStudent);
		ret.put("type", "success");
		ret.put("msg", "获取成功！");
		return ret;
	}
	/**
	 * 获取当前学生正在进行的考试信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exam_list",method = RequestMethod.GET)
	public ModelAndView exameList(ModelAndView model,
			@RequestParam(name="name",defaultValue="") String name,
			@RequestParam(name="page",defaultValue="1") Integer page,
			HttpServletRequest request){
		Student student = (Student)request.getSession().getAttribute("student");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("subjectId", student.getSubjectId());
		queryMap.put("name", name);
		queryMap.put("offset", getOffset(page, pageSize));
		queryMap.put("pageSize", pageSize);
		model.addObject("testList", testService.findListByUser(queryMap));
		model.addObject("name", name);
		model.addObject("subject", subjectService.findById(student.getSubjectId()));
		model.setViewName("/home/user/exam_list");
		if(page < 1)page = 1;
		model.addObject("page", page);
		model.addObject("nowTime", System.currentTimeMillis());
		return model;
	}
	/**
	 * 获取当前学生考过的考试信息
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/history_list",method = RequestMethod.GET)
	public ModelAndView historyList(ModelAndView model,
			@RequestParam(name="name",defaultValue="") String name,
			@RequestParam(name="page",defaultValue="1") Integer page,
			HttpServletRequest request){
		Student student = (Student)request.getSession().getAttribute("student");
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", name);
		queryMap.put("studentId", student.getId());
		queryMap.put("offset", getOffset(page, pageSize));
		queryMap.put("pageSize", pageSize);
		model.addObject("historyList", testPaperService.findHistory(queryMap));
		model.addObject("name", name);
		model.addObject("subject", subjectService.findById(student.getSubjectId()));
		model.setViewName("/home/user/history_list");
		if(page < 1)page = 1;
		model.addObject("page", page);
		return model;
	}

	/**
	 * 返回指定类型的试题
	 * @param examPaperAnswers
	 * @param questionType
	 * @return
	 */
	private List<AnswerQuestion> getAnswerQuestionList(List<AnswerQuestion> answerQuestions,int questionType){
		List<AnswerQuestion> newAnswerQuestions = new ArrayList<AnswerQuestion>();
		for(AnswerQuestion answerQuestion:answerQuestions){
			if(answerQuestion.getQuestion().getQuestionType() == questionType){
				newAnswerQuestions.add(answerQuestion);
			}
		}
		return newAnswerQuestions;
	}
	
	/**
	 * 计算数据库查询游标位置
	 * @param page
	 * @param pageSize
	 * @return
	 */
	private int getOffset(int page,int pageSize){
		if(page < 1)page = 1;
		return (page - 1) * pageSize;
	}
}