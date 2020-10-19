package com.cll.test.controller.home;
import java.io.Console;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cll.test.service.admin.TestService;
import com.cll.test.service.admin.TestPaperService;
import com.cll.test.entity.admin.AnswerQuestion;
import com.cll.test.entity.admin.Log;
import com.cll.test.entity.admin.Question;
import com.cll.test.entity.admin.Student;
import com.cll.test.entity.admin.Test;
import com.cll.test.entity.admin.TestPaper;
import com.cll.test.service.admin.AnswerQuestionService;
import com.cll.test.service.admin.QuestionService;

/**
 * ǰ̨�û����Կ�����
 * @author Administrator
 *
 */
@RequestMapping("/home/test")
@Controller
public class HomeExamController {
	
	
	@Autowired
	private TestService testService;
	@Autowired
	private TestPaperService testPaperService;
	@Autowired
	private AnswerQuestionService answerQuestionService;
	@Autowired
	private QuestionService questionService;
	/**
	 * ��ʼ����ǰ���Ϸ��ԣ������������
	 * @param testId
	 * @return
	 */
	@RequestMapping(value="/statr_test",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> startTest(Long testId,HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		Test test=testService.findById(testId);
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "������Ϣ������!");
			return ret;
		}
		if(test.getEndTime().getTime() < new Date().getTime()){
			ret.put("type", "error");
			ret.put("msg", "�ÿ����ѽ���!");
			return ret;
		}
		Student student = (Student)request.getSession().getAttribute("student");
		/*if(test.getSubjectId().longValue() != student.getSubjectId().longValue()){
			ret.put("type", "error");
			ret.put("msg", "��Ŀ��ͬ�����ܿ���!");
			return ret;
		}*/
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("testId", test.getId());
		queryMap.put("studentId", student.getId());
		TestPaper find = testPaperService.find(queryMap);
		if(find != null){
			if(find.getStatus() == 1){
				//��ʾ�Ѿ�����
				ret.put("type", "error");
				ret.put("msg", "���Ѿ��������ſ�����!");
				return ret;
			}
			//�ߵ������ʾ�Ծ��Ѿ����ɣ�����û���ύ���ԣ����Կ�ʼ���¿���
			ret.put("type", "success");
			ret.put("msg", "���Կ�ʼ����");
			return ret;
		}
		//��ʱ��˵�����Ͽ�����������������Ծ�����
		//Ҫ���жϣ����Ƿ����������Ծ������
		//��ȡ��ѡ������
		Map<String, Long> qMap = new HashMap<String, Long>();
		qMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_SINGLE));
		qMap.put("subjectId", test.getSubjectId());
		int singleQuestionTotalNum = questionService.getQuestionNumByType(qMap);
		if(test.getSingleQuestionNum() > singleQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "��ѡ������������ⵥѡ���������޷������Ծ�!");
			return ret;
		}
		//��ȡ��ѡ������
		qMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_MUILT));
		int muiltQuestionTotalNum = questionService.getQuestionNumByType(qMap);
		if(test.getMuiltQuestionNum() > muiltQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "��ѡ��������������ѡ���������޷������Ծ�!");
			return ret;
		}
		//��ȡ�ж�������
		qMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_CHARGE));
		int chargeQuestionTotalNum = questionService.getQuestionNumByType(qMap);
		if(test.getChargeQuestionNum() > chargeQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "�ж���������������ж����������޷������Ծ�!");
			return ret;
		}
		//�������������㣬��ʼ�����Ծ������������
		TestPaper testPaper = new TestPaper();
		testPaper.setTestId(testId);
		testPaper.setStatus(0);
		testPaper.setStudentId(student.getId());
		testPaper.setCount(test.getCount());
		testPaper.setTimeConsuming(0);
		if(testPaperService.add(testPaper) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�Ծ�����ʧ�ܣ�����ϵ����Ա!");
			return ret;
		}
		//�Ծ��Ѿ���ȷ���ɣ����ڿ�ʼ�����������
		Map<String, Object> queryQuestionMap = new HashMap<String, Object>();
		queryQuestionMap.put("questionType", Question.QUESTION_TYPE_SINGLE);
		queryQuestionMap.put("subjectId", test.getSubjectId());
		queryQuestionMap.put("offset", 0);
		queryQuestionMap.put("pageSize", 99999);
		if(test.getSingleQuestionNum() > 0){
			//���Թ涨��ѡ����������0
			//��ȡ���еĵ�ѡ��
			List<Question> singleQuestionList = questionService.findList(queryQuestionMap);
			//���ѡȡ���Թ涨�����ĵ�ѡ�⣬���뵽���ݿ���
			List<Question> selectedSingleQuestionList = getRandomList(singleQuestionList, test.getSingleQuestionNum());
			insertQuestionAnswer(selectedSingleQuestionList, testId, testPaper.getId(), student.getId());
		}
		if(test.getMuiltQuestionNum() > 0){
			queryQuestionMap.put("questionType", Question.QUESTION_TYPE_MUILT);
			//��ȡ���еĶ�ѡ��
			List<Question> muiltQuestionList = questionService.findList(queryQuestionMap);
			//���ѡȡ���Թ涨�����Ķ�ѡ�⣬���뵽���ݿ���
			List<Question> selectedMuiltQuestionList = getRandomList(muiltQuestionList, test.getMuiltQuestionNum());
			insertQuestionAnswer(selectedMuiltQuestionList, testId, testPaper.getId(), student.getId());
			
		}
		if(test.getChargeQuestionNum() > 0){
			//��ȡ���е��ж���
			queryQuestionMap.put("questionType", Question.QUESTION_TYPE_CHARGE);
			List<Question> chargeQuestionList = questionService.findList(queryQuestionMap);
			//���ѡȡ���Թ涨�������ж��⣬���뵽���ݿ���
			List<Question> selectedChargeQuestionList = getRandomList(chargeQuestionList, test.getChargeQuestionNum());
			insertQuestionAnswer(selectedChargeQuestionList, testId,testPaper.getId(), student.getId());
		}
		
		ret.put("type", "success");
		ret.put("msg", "�Ծ����ɳɹ�!");
		return ret;
	}
	
	/**
	 * ��ʼ���п���
	 * @param model
	 * @param examId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/testing",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model,Long testId,HttpServletRequest request){
		Student student = (Student)request.getSession().getAttribute("student");
		Test test =testService.findById(testId);
		if(test == null){
			model.setViewName("/home/test/error");
			model.addObject("msg", "��ǰ���Բ�����!");
			return model;
		}
		if(test.getEndTime().getTime() < new Date().getTime()){
			model.setViewName("/home/test/error");
			model.addObject("msg", "��ǰ����ʱ���ѹ���!");
			return model;
		}
		if(test.getSubjectId().longValue() != student.getSubjectId().longValue()){
			model.setViewName("/home/test/error");
			model.addObject("msg", "��������Ŀ�뿼�Կ�Ŀ�����ϣ����ܽ��п���!");
			return model;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("testId", testId);
		queryMap.put("studentId", student.getId());
		//���ݿ�����Ϣ��ѧ����Ϣ��ȡ�Ծ�
		TestPaper testPaper = testPaperService.find(queryMap);
		if(testPaper == null){
			model.setViewName("/home/test/error");
			model.addObject("msg", "��ǰ���Բ������Ծ�");
			return model;
		}
		if(testPaper.getStatus() == 1){
			model.setViewName("/home/test/error");
			model.addObject("msg", "���Ѿ��������ſ����ˣ�");
			return model;
		}
		Date now = new Date();
		Object attributeStartTime = request.getSession().getAttribute("startTestTime");
		if(attributeStartTime == null){
			request.getSession().setAttribute("startTestTime", now);
		}
		Date startTestTime = (Date)request.getSession().getAttribute("startTestTime");
		int passedTime = (int)(now.getTime() - startTestTime.getTime())/1000/60;
		if(passedTime >= test.getUsableTime()){
			//��ʾʱ���Ѿ��ľ�������ִ���
			testPaper.setGrade(0);
			testPaper.setStartTestTime(startTestTime);
			testPaper.setStatus(1);
			testPaper.setTimeConsuming(passedTime);
			testPaperService.submitPaper(testPaper);
			model.setViewName("/home/test/error");
			model.addObject("msg", "��ǰ����ʱ���Ѻľ�����δ�ύ�Ծ���0�ִ���");
			return model;
		}
		Integer hour = (test.getUsableTime()-passedTime)/60;
		Integer minitute = (test.getUsableTime()-passedTime)%60;
		Integer second = (test.getUsableTime()*60-(int)(now.getTime() - startTestTime.getTime())/1000)%60;
		queryMap.put("testPaperId", testPaper.getId());
		List<AnswerQuestion> findListByUser = answerQuestionService.findListByUser(queryMap);
		model.addObject("title", test.getName()+"-��ʼ����");
		model.addObject("singleQuestionList", getAnswerQuestionList(findListByUser, Question.QUESTION_TYPE_SINGLE));
		model.addObject("muiltQuestionList", getAnswerQuestionList(findListByUser, Question.QUESTION_TYPE_MUILT));
		model.addObject("chargeQuestionList", getAnswerQuestionList(findListByUser, Question.QUESTION_TYPE_CHARGE));
		model.addObject("hour",hour);
		model.addObject("minitute",minitute);
		model.addObject("second",second);
		model.addObject("test", test);
		model.addObject("testPaper",testPaper);
		model.addObject("singleScore", Question.QUESTION_TYPE_SINGLE_SCORE);
		model.addObject("muiltScore", Question.QUESTION_TYPE_MUILT_SCORE);
		model.addObject("chargeScore", Question.QUESTION_TYPE_CHARGE_SCORE);
		model.addObject("singleQuestion", Question.QUESTION_TYPE_SINGLE);
		model.addObject("muiltQuestion", Question.QUESTION_TYPE_MUILT);
		model.addObject("chargeQuestion", Question.QUESTION_TYPE_CHARGE);
		model.setViewName("/home/test/testing");
		return model;
	}
	
	/**
	 * �û�ѡ������ύ������
	 * @param examPaperAnswer
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/submit_answer",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> submitAnswer(AnswerQuestion answerQuestion,HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		if(answerQuestion == null){
			ret.put("type", "error");
			ret.put("msg", "����ȷ����!");
			return ret;
		}
		Test test = testService.findById(answerQuestion.getTestId());
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "������Ϣ������!");
			return ret;
		}
		if(test.getEndTime().getTime() < new Date().getTime()){
			ret.put("type", "error");
			ret.put("msg", "�ÿ����ѽ���!");
			return ret;
		}
		Student student = (Student)request.getSession().getAttribute("student");
		if(test.getSubjectId().longValue() != student.getSubjectId().longValue()){
			ret.put("type", "error");
			ret.put("msg", "ѧ�Ʋ�ͬ����Ȩ���п���!");
			return ret;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("testId", test.getId());
		queryMap.put("studentId", student.getId());
		TestPaper findTestPaper =testPaperService.find(queryMap);
		if(findTestPaper == null){
			ret.put("type", "error");
			ret.put("msg", "�������Ծ�!");
			return ret;
		}
		if(findTestPaper.getId().longValue() != answerQuestion.getTestPaperId().longValue()){
			ret.put("type", "error");
			ret.put("msg", "�����Ծ���ȷ����淶����!");
			return ret;
		}
		Question question = questionService.findById(answerQuestion.getQuestionId());
		if(question == null){
			ret.put("type", "error");
			ret.put("msg", "���ⲻ���ڣ���淶����!");
			return ret;
		}
		//��ʱ�����Խ��𰸲��뵽���ݿ���
		answerQuestion.setStudentId(student.getId());
		if(question.getRightanswer().equals(answerQuestion.getAnswer())){
			answerQuestion.setIsCorrect(1);
		}
		if(answerQuestionService.submitAnswer(answerQuestion) <= 0){
			ret.put("type", "error");
			ret.put("msg", "�����������ϵ����Ա!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "����ɹ�!");
		return ret;
	}
	
	@RequestMapping(value="/submit_exam",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> submitExam(Long testId,Long testPaperId,HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		Test test = testService.findById(testId);
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "���Բ����ڣ�����ȷ����!");
			return ret;
		}
		if(test.getEndTime().getTime() < new Date().getTime()){
			ret.put("type", "error");
			ret.put("msg", "�ÿ����ѽ���!");
			return ret;
		}
		Student student = (Student)request.getSession().getAttribute("student");
		if(test.getSubjectId().longValue() != student.getSubjectId().longValue()){
			ret.put("type", "error");
			ret.put("msg", "ѧ�Ʋ�ͬ����Ȩ���в���!");
			return ret;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("testId", test.getId());
		queryMap.put("studentId", student.getId());
		TestPaper findTestPaper = testPaperService.find(queryMap);
		if(findTestPaper == null){
			ret.put("type", "error");
			ret.put("msg", "�������Ծ�!");
			return ret;
		}
		/*if(findTestPaper.getId().longValue() != testPaperId.longValue()){
			ret.put("type", "error");
			ret.put("msg", "�����Ծ���ȷ����淶����!");
			return ret;
		}*/
		if(findTestPaper.getStatus() == 1){
			ret.put("type", "error");
			ret.put("msg", "�����ظ�����!");
			return ret;
		}
		//��ʱ���㿼�Ե÷�
		queryMap.put("testPaperId", testPaperId);
		List<AnswerQuestion> answerQuestionList = answerQuestionService.findListByUser(queryMap);
		//��ʱ�����Խ��𰸲��뵽���ݿ���
		int score = 0;
		for(AnswerQuestion answerQuestion:answerQuestionList){
			if(answerQuestion.getIsCorrect() == 1){
				score += answerQuestion.getQuestion().getScore();
			}
		}
		findTestPaper.setEndTestTime(new Date());
		findTestPaper.setGrade(score);
		findTestPaper.setStartTestTime((Date)request.getSession().getAttribute("startTestTime"));
		findTestPaper.setStatus(1);
		findTestPaper.setTimeConsuming((int)(findTestPaper.getEndTestTime().getTime()-findTestPaper.getStartTestTime().getTime())/1000/60);
		testPaperService.submitPaper(findTestPaper);
		
		request.getSession().setAttribute("startTestTime", null);
		//����ͳ�ƽ��
		testService.edit(test);
		ret.put("type", "success");
		ret.put("msg", "�ύ�ɹ�!");
		return ret;
	}
	
	/**
	 * ����Ӹ�����list��ѡȡ����������Ԫ��
	 * @param questionList
	 * @param n
	 * @return
	 */
	private List<Question> getRandomList(List<Question> questionList,int n){
		if(questionList.size() <= n)return questionList;
		Map<Integer, String> selectedMap = new HashMap<Integer, String>();
		List<Question> selectedList = new ArrayList<Question>();
		while(selectedList.size() < n){
			for(Question question:questionList){
				int index = (int)(Math.random() * questionList.size());
				if(!selectedMap.containsKey(index)){
					selectedMap.put(index, "");
					selectedList.add(questionList.get(index));
					if(selectedList.size() >= n)break;
				}
			}
		}
		return selectedList;
	}

	/**
	 * ����ָ�����������⵽���������
	 * @param questionList
	 * @param examId
	 * @param examPaperId
	 * @param studentId
	 */
	private void insertQuestionAnswer(List<Question> questionList,Long testId,Long testPaperId,Long studentId){
		for(Question question:questionList){
			AnswerQuestion answerQuestion = new AnswerQuestion();
			answerQuestion.setTestId(testId);
			answerQuestion.setTestPaperId(testPaperId);
			answerQuestion.setIsCorrect(0);
			answerQuestion.setQuestionId(question.getId());
			answerQuestion.setStudentId(studentId);
			answerQuestionService.add(answerQuestion);
		}
		}
	
		/**
		 * ����ָ�����͵�����
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
	}	
	