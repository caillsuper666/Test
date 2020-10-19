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
 * ���Թ����̨������
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
	 * �����б�ҳ��
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
	 * ģ��������ҳ��ʾ�б��ѯ
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
	 * ��ӿ���
	 * @param exam
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Test test){
		Map<String, String> ret = new HashMap<String, String>();
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ�Ŀ�����Ϣ!");
			return ret;
		}
		if(StringUtils.isEmpty(test.getName())){
			ret.put("type", "error");
			ret.put("msg", "����д��������!");
			return ret;
		}
		if(test.getSubjectId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��������Ŀ!");
			return ret;
		}
		if(test.getUsableTime()== 0){
			ret.put("type", "error");
			ret.put("msg", "����д����ʱ��!");
			return ret;
		}
		if(test.getSingleQuestionNum() == 0 && test.getMuiltQuestionNum() == 0 && test.getChargeQuestionNum() == 0){
			ret.put("type", "error");
			ret.put("msg", "��ѡ�⡢��ѡ�⡢�ж���������һ������");
			return ret;
		}
		//��ʱȥ��ѯ����д�����������Ƿ�����
		//��ȡ��ѡ������
		Map<String, Long> queryMap = new HashMap<String, Long>();
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_SINGLE));
		queryMap.put("subjectId", test.getSubjectId());
		int singleQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getSingleQuestionNum() > singleQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "��ѡ������������ⵥѡ��");
			return ret;
		}
		//��ȡ��ѡ������
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_MUILT));
		int muiltQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getMuiltQuestionNum() > muiltQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��������������ѡ��");
			return ret;
		}
		//��ȡ�ж�������
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_CHARGE));
		int chargeQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getChargeQuestionNum() > chargeQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "�ж���������������ж���!");
			return ret;
		}
		test.setQuestionNum(test.getSingleQuestionNum() + test.getMuiltQuestionNum() + test.getChargeQuestionNum());
		test.setCount(test.getSingleQuestionNum() * Question.QUESTION_TYPE_SINGLE_SCORE + test.getMuiltQuestionNum() * Question.QUESTION_TYPE_MUILT_SCORE + test.getChargeQuestionNum() * Question.QUESTION_TYPE_CHARGE_SCORE);
		if(testService.add(test) <= 0){
			ret.put("type", "error");
			ret.put("msg", "���ʧ�ܣ�����ϵ����Ա!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "��ӳɹ�!");
		return ret;
	}
	
	/**
	 * �༭����
	 * @param exam
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Test test){
		Map<String, String> ret = new HashMap<String, String>();
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ����ȷ�Ŀ�����Ϣ���б༭!");
			return ret;
		}
		if(StringUtils.isEmpty(test.getName())){
			ret.put("type", "error");
			ret.put("msg", "����д��������!");
			return ret;
		}
		if(test.getSubjectId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��������Ŀ!");
			return ret;
		}
		/*if(test.getSingleQuestionNum() == 0 && test.getMuiltQuestionNum() == 0 && test.getChargeQuestionNum() == 0){
			ret.put("type", "error");
			ret.put("msg", "��ѡ�⡢��ѡ�⡢�ж���������һ�����͵���Ŀ!");
			return ret;
		}*/
		//��ʱȥ��ѯ����д�����������Ƿ�����
		//��ȡ��ѡ������
		Map<String, Long> queryMap = new HashMap<String, Long>();
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_SINGLE));
		queryMap.put("subjectId", test.getSubjectId());
		int singleQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getSingleQuestionNum() > singleQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "��ѡ������������ⵥѡ�����������޸�!");
			return ret;
		}
		//��ȡ��ѡ������
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_MUILT));
		int muiltQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getMuiltQuestionNum() > muiltQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��������������ѡ�����������޸�!");
			return ret;
		}
		//��ȡ�ж�������
		queryMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_CHARGE));
		int chargeQuestionTotalNum = questionService.getQuestionNumByType(queryMap);
		if(test.getChargeQuestionNum() > chargeQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "�ж���������������ж������������޸�!");
			return ret;
		}
		test.setQuestionNum(test.getSingleQuestionNum() + test.getMuiltQuestionNum() + test.getChargeQuestionNum());
		test.setCount(test.getSingleQuestionNum() * Question.QUESTION_TYPE_SINGLE_SCORE + test.getMuiltQuestionNum() * Question.QUESTION_TYPE_MUILT_SCORE + test.getChargeQuestionNum() * Question.QUESTION_TYPE_CHARGE_SCORE);
		if(testService.edit(test) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�༭ʧ�ܣ�����ϵ����Ա!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�༭�ɹ�!");
		return ret;
	}
	
	/**
	 * ɾ������
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
				ret.put("msg", "��ѡ��Ҫɾ��������!");
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
					ret.put("msg", "ɾ��ʧ�ܣ�����ϵ����Ա!");
					return ret;
				}
			} catch (Exception e) {
				// TODO: handle exception
				ret.put("type", "error");
				ret.put("msg", "�ÿ����´����Ծ���Լ�¼��Ϣ������ɾ��!");
				return ret;
			}
			ret.put("type", "success");
			ret.put("msg", "�޸ĳɹ�!");
			return ret;
		}
	

	


}
