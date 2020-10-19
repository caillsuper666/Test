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

import com.cll.test.entity.admin.TestPaper;
import com.cll.test.page.admin.Page;


import com.cll.test.service.admin.StudentService;
import com.cll.test.service.admin.TestPaperService;
import com.cll.test.service.admin.TestService;

/**
 * �Ծ�����̨������
 * @author Administrator
 *
 */
@RequestMapping("/admin/testPaper")
@Controller
public class TestPaperController {
	
	@Autowired
	private TestPaperService testPaperService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TestService testService;
	
	/**
	 * �Ծ��б�ҳ��
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
		model.setViewName("testPaper/list");
		return model;
	}
	
	/**
	 * ģ��������ҳ��ʾ�б��ѯ
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(
			@RequestParam(name="testId",required=false) Long testId,
			@RequestParam(name="studentId",required=false) Long studentId,
			@RequestParam(name="status",required=false) Integer status,
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
		if(status != null){
			queryMap.put("status", status);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", testPaperService.findList(queryMap));
		ret.put("total", testPaperService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * ����Ծ�
	 * @param examPaper
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(TestPaper testPaper){
		Map<String, String> ret = new HashMap<String, String>();
		if(testPaper == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ���Ծ���Ϣ!");
			return ret;
		}
		if(testPaper.getTestId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ���Ծ���������!");
			return ret;
		}
		if(testPaper.getStudentId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ������ѧ��!");
			return ret;
		}
		if(testPaperService.add(testPaper) <= 0){
			ret.put("type", "error");
			ret.put("msg", "���ʧ�ܣ�����ϵ����Ա!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "��ӳɹ�!");
		return ret;
	}
	
	/**
	 * �༭�Ծ�
	 * @param examPaper
	 * @return
	 */
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(TestPaper testPaper){
		Map<String, String> ret = new HashMap<String, String>();
		if(testPaper == null){
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ���Ծ���Ϣ!");
			return ret;
		}
		if(testPaper.getTestId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ���Ծ���������!");
			return ret;
		}
		if(testPaper.getStudentId() == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ������ѧ��!");
			return ret;
		}if(testPaperService.edit(testPaper) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�༭ʧ�ܣ�����ϵ����Ա!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�༭�ɹ�!");
		return ret;
	}
	
	/**
	 * ɾ���Ծ�
	 * @param id
	 * @return
	 */
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(Long id){
		Map<String, String> ret = new HashMap<String, String>();
		if(id == null){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��Ҫɾ��������!");
			return ret;
		}
		try {
			if(testPaperService.delete(id) <= 0){
				ret.put("type", "error");
				ret.put("msg", "ɾ��ʧ�ܣ�����ϵ����Ա!");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type", "error");
			ret.put("msg", "���Ծ��´��ڴ�����Ϣ������ɾ��!");
			return ret;
		}
		
		ret.put("type", "success");
		ret.put("msg", "ɾ���ɹ�!");
		return ret;
	}
	
}
