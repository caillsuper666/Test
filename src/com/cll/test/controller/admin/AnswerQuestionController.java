package com.cll.test.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cll.test.page.admin.Page;
import com.cll.test.service.admin.AnswerQuestionService;
import com.cll.test.service.admin.QuestionService;
import com.cll.test.service.admin.StudentService;
import com.cll.test.service.admin.TestService;

/**
 * 试卷答题管理后台控制器
 * @author Administrator
 *
 */
@RequestMapping("/admin/answerQuestion")
@Controller
public class AnswerQuestionController {
	
	@Autowired
	private AnswerQuestionService answerQuestionService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TestService testService;
	@Autowired
	private QuestionService questionService;
	
	/**
	 * 试卷答题列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offset", 0);
		queryMap.put("pageSize", 99999);
		model.addObject("testList", testService.findList(queryMap));
		model.addObject("studentList", studentService.findList(queryMap));
		model.addObject("questionList", questionService.findList(queryMap));
		model.setViewName("answerQuestion/list");
		return model;
	}
	
	/**
	 * 模糊搜索分页显示列表查询
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(
			@RequestParam(name="testId",required=false) Long testId,
			@RequestParam(name="studentId",required=false) Long studentId,
			@RequestParam(name="questionId",required=false) Long questionId,
			Page page
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if(testId != null){
			queryMap.put("testId", testId);
		}
		if(studentId != null){
			queryMap.put("studentId", studentId);
		}
		if(questionId != null){
			queryMap.put("questionId", questionId);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", answerQuestionService.findList(queryMap));
		ret.put("total", answerQuestionService.getTotal(queryMap));
		return ret;
	}
	
	
}
