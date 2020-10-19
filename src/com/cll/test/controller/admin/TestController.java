package com.cll.test.controller.admin;

import java.io.Console;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.cll.test.entity.admin.Question;
import com.cll.test.entity.admin.Subject;
import com.cll.test.entity.admin.Test;
import com.cll.test.page.admin.Page;
import com.cll.test.service.admin.QuestionService;
import com.cll.test.service.admin.SubjectService;
import com.cll.test.service.admin.TestService;

/**
 * 考试管理后台控制器
 * @author Administrator
 *
 */
@RequestMapping("/test")
@Controller
public class TestController {
	
	@Autowired
	private TestService testService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private SubjectService subjectService;
	
	/**
	 * 考试列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model){
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offset",0);
		queryMap.put("pageSize", 99999);
		List<Subject> findList = subjectService.findList(queryMap);
/*		model.addObject("subjectList", subjectService.findList(queryMap));*/
		model.addObject("singleScore", Question.QUESTION_TYPE_SINGLE_SCORE);
		model.addObject("muiltScore", Question.QUESTION_TYPE_MUILT_SCORE);
		model.addObject("chargeScore", Question.QUESTION_TYPE_CHARGE_SCORE);
		model.addObject("subjectList",findList );
		model.addObject("subjectListJson",JSONArray.fromObject(findList));
		model.setViewName("test/list");
		return model;
	}
	
	/**
	 * 模糊搜索分页显示列表查询
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/get_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(
			@RequestParam(name="name",defaultValue="") String name,
			@RequestParam(name="subjectId",required=false) Long subjectId,
			Page page
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", name);
		if(subjectId != null){
			queryMap.put("subjectId", subjectId);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", testService.findList(queryMap));
		ret.put("total", testService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * 添加考试
	 * @param exam
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Test test){
		Map<String, String> ret = new HashMap<String, String>();
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "请填写正确的考试信息!");
			return ret;
		}
		if(StringUtils.isEmpty(test.getName())){
			ret.put("type", "error");
			ret.put("msg", "请填写考试名称!");
			return ret;
		}
		if(test.getSubjectId() == null){
			ret.put("type", "error");
			ret.put("msg", "请选择所属科目!");
			return ret;
		}
		if(test.getUsableTime()== 0){
			ret.put("type", "error");
			ret.put("msg", "请填写可用时间!");
			return ret;
		}
		if(test.getSingleQuestionNum() == 0 && test.getMuiltQuestionNum() == 0 && test.getChargeQuestionNum() == 0){
			ret.put("type", "error");
			ret.put("msg", "单选题、多选题、判断题至少有一种类型");
			return ret;
		}
		//此时去查询所填写的题型数量是否满足
		//获取单选题总数
		Map<String, Long> queryMap = new HashMap<String, Long>();
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_SINGLE));
		queryMap.put("subjectId", test.getSubjectId());
		int singleQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getSingleQuestionNum() > singleQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "单选题数量超过题库单选题");
			return ret;
		}
		//获取多选题总数
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_MUILT));
		int muiltQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getMuiltQuestionNum() > muiltQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "多选题数量超过题库多选题");
			return ret;
		}
		//获取判断题总数
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_CHARGE));
		int chargeQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getChargeQuestionNum() > chargeQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "判断题数量超过题库判断题!");
			return ret;
		}
		test.setQuestionNum(test.getSingleQuestionNum() + test.getMuiltQuestionNum() + test.getChargeQuestionNum());
		test.setCount(test.getSingleQuestionNum() * Question.QUESTION_TYPE_SINGLE_SCORE + test.getMuiltQuestionNum() * Question.QUESTION_TYPE_MUILT_SCORE + test.getChargeQuestionNum() * Question.QUESTION_TYPE_CHARGE_SCORE);
		if(testService.add(test) <= 0){
			ret.put("type", "error");
			ret.put("msg", "添加失败，请联系管理员!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "添加成功!");
		return ret;
	}
	
	/**
	 * 编辑考试
	 * @param exam
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Test test){
		Map<String, String> ret = new HashMap<String, String>();
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "请选择正确的考试信息进行编辑!");
			return ret;
		}
		if(StringUtils.isEmpty(test.getName())){
			ret.put("type", "error");
			ret.put("msg", "请填写考试名称!");
			return ret;
		}
		if(test.getSubjectId() == null){
			ret.put("type", "error");
			ret.put("msg", "请选择所属科目!");
			return ret;
		}
		/*if(test.getSingleQuestionNum() == 0 && test.getMuiltQuestionNum() == 0 && test.getChargeQuestionNum() == 0){
			ret.put("type", "error");
			ret.put("msg", "单选题、多选题、判断题至少有一种类型的题目!");
			return ret;
		}*/
		//此时去查询所填写的题型数量是否满足
		//获取单选题总数
		Map<String, Long> queryMap = new HashMap<String, Long>();
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_SINGLE));
		queryMap.put("subjectId", test.getSubjectId());
		int singleQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getSingleQuestionNum() > singleQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "单选题数量超过题库单选题总数，请修改!");
			return ret;
		}
		//获取多选题总数
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_MUILT));
		int muiltQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getMuiltQuestionNum() > muiltQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "多选题数量超过题库多选题总数，请修改!");
			return ret;
		}
		//获取判断题总数
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_CHARGE));
		int chargeQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getChargeQuestionNum() > chargeQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "判断题数量超过题库判断题总数，请修改!");
			return ret;
		}
		test.setQuestionNum(test.getSingleQuestionNum() + test.getMuiltQuestionNum() + test.getChargeQuestionNum());
		test.setCount(test.getSingleQuestionNum() * Question.QUESTION_TYPE_SINGLE_SCORE + test.getMuiltQuestionNum() * Question.QUESTION_TYPE_MUILT_SCORE + test.getChargeQuestionNum() * Question.QUESTION_TYPE_CHARGE_SCORE);
		if(testService.edit(test) <= 0){
			ret.put("type", "error");
			ret.put("msg", "编辑失败，请联系管理员!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "编辑成功!");
		return ret;
	}
	
	/**
	 * 删除考试
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(@RequestParam(value="ids[]",required=true) Long[] ids
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
			try {
				if(testService.delete(idsString) <= 0){
					ret.put("type", "error");
					ret.put("msg", "删除失败，请联系管理员!");
					return ret;
				}
			} catch (Exception e) {
				// TODO: handle exception
				ret.put("type", "error");
				ret.put("msg", "该考试下存在试卷或考试记录信息，不能删除!");
				return ret;
			}
			ret.put("type", "success");
			ret.put("msg", "修改成功!");
			return ret;
		}
	

	


}
