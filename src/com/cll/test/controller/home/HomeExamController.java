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
 * 前台用户考试控制器
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
	 * 开始考试前检查合法性，随机生成试题
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
			ret.put("msg", "考试信息不存在!");
			return ret;
		}
		if(test.getEndTime().getTime() < new Date().getTime()){
			ret.put("type", "error");
			ret.put("msg", "该考试已结束!");
			return ret;
		}
		Student student = (Student)request.getSession().getAttribute("student");
		/*if(test.getSubjectId().longValue() != student.getSubjectId().longValue()){
			ret.put("type", "error");
			ret.put("msg", "科目不同，不能考试!");
			return ret;
		}*/
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("testId", test.getId());
		queryMap.put("studentId", student.getId());
		TestPaper find = testPaperService.find(queryMap);
		if(find != null){
			if(find.getStatus() == 1){
				//表示已经考过
				ret.put("type", "error");
				ret.put("msg", "您已经考过这门考试了!");
				return ret;
			}
			//走到这里表示试卷已经生成，但是没有提交考试，可以开始重新考试
			ret.put("type", "success");
			ret.put("msg", "可以开始考试");
			return ret;
		}
		//此时，说明符合考试条件，随机生成试卷试题
		//要做判断，看是否满足生成试卷的条件
		//获取单选题总数
		Map<String, Long> qMap = new HashMap<String, Long>();
		qMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_SINGLE));
		qMap.put("subjectId", test.getSubjectId());
		int singleQuestionTotalNum = questionService.getQuestionNumByType(qMap);
		if(test.getSingleQuestionNum() > singleQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "单选题数量超过题库单选题总数，无法生成试卷!");
			return ret;
		}
		//获取多选题总数
		qMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_MUILT));
		int muiltQuestionTotalNum = questionService.getQuestionNumByType(qMap);
		if(test.getMuiltQuestionNum() > muiltQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "多选题数量超过题库多选题总数，无法生成试卷!");
			return ret;
		}
		//获取判断题总数
		qMap.put("questionType", Long.valueOf(Question.QUESTION_TYPE_CHARGE));
		int chargeQuestionTotalNum = questionService.getQuestionNumByType(qMap);
		if(test.getChargeQuestionNum() > chargeQuestionTotalNum){
			ret.put("type", "error");
			ret.put("msg", "判断题数量超过题库判断题总数，无法生成试卷!");
			return ret;
		}
		//所有条件都满足，开始创建试卷，随机生成试题
		TestPaper testPaper = new TestPaper();
		testPaper.setTestId(testId);
		testPaper.setStatus(0);
		testPaper.setStudentId(student.getId());
		testPaper.setCount(test.getCount());
		testPaper.setTimeConsuming(0);
		if(testPaperService.add(testPaper) <= 0){
			ret.put("type", "error");
			ret.put("msg", "试卷生成失败，请联系管理员!");
			return ret;
		}
		//试卷已经正确生成，现在开始随机生成试题
		Map<String, Object> queryQuestionMap = new HashMap<String, Object>();
		queryQuestionMap.put("questionType", Question.QUESTION_TYPE_SINGLE);
		queryQuestionMap.put("subjectId", test.getSubjectId());
		queryQuestionMap.put("offset", 0);
		queryQuestionMap.put("pageSize", 99999);
		if(test.getSingleQuestionNum() > 0){
			//考试规定单选题数量大于0
			//获取所有的单选题
			List<Question> singleQuestionList = questionService.findList(queryQuestionMap);
			//随机选取考试规定数量的单选题，插入到数据库中
			List<Question> selectedSingleQuestionList = getRandomList(singleQuestionList, test.getSingleQuestionNum());
			insertQuestionAnswer(selectedSingleQuestionList, testId, testPaper.getId(), student.getId());
		}
		if(test.getMuiltQuestionNum() > 0){
			queryQuestionMap.put("questionType", Question.QUESTION_TYPE_MUILT);
			//获取所有的多选题
			List<Question> muiltQuestionList = questionService.findList(queryQuestionMap);
			//随机选取考试规定数量的多选题，插入到数据库中
			List<Question> selectedMuiltQuestionList = getRandomList(muiltQuestionList, test.getMuiltQuestionNum());
			insertQuestionAnswer(selectedMuiltQuestionList, testId, testPaper.getId(), student.getId());
			
		}
		if(test.getChargeQuestionNum() > 0){
			//获取所有的判断题
			queryQuestionMap.put("questionType", Question.QUESTION_TYPE_CHARGE);
			List<Question> chargeQuestionList = questionService.findList(queryQuestionMap);
			//随机选取考试规定数量的判断题，插入到数据库中
			List<Question> selectedChargeQuestionList = getRandomList(chargeQuestionList, test.getChargeQuestionNum());
			insertQuestionAnswer(selectedChargeQuestionList, testId,testPaper.getId(), student.getId());
		}
		
		ret.put("type", "success");
		ret.put("msg", "试卷生成成功!");
		return ret;
	}
	
	/**
	 * 开始进行考试
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
			model.addObject("msg", "当前考试不存在!");
			return model;
		}
		if(test.getEndTime().getTime() < new Date().getTime()){
			model.setViewName("/home/test/error");
			model.addObject("msg", "当前考试时间已过期!");
			return model;
		}
		if(test.getSubjectId().longValue() != student.getSubjectId().longValue()){
			model.setViewName("/home/test/error");
			model.addObject("msg", "您所属科目与考试科目不符合，不能进行考试!");
			return model;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("testId", testId);
		queryMap.put("studentId", student.getId());
		//根据考试信息和学生信息获取试卷
		TestPaper testPaper = testPaperService.find(queryMap);
		if(testPaper == null){
			model.setViewName("/home/test/error");
			model.addObject("msg", "当前考试不存在试卷");
			return model;
		}
		if(testPaper.getStatus() == 1){
			model.setViewName("/home/test/error");
			model.addObject("msg", "您已经考过这门考试了！");
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
			//表示时间已经耗尽，按零分处理
			testPaper.setGrade(0);
			testPaper.setStartTestTime(startTestTime);
			testPaper.setStatus(1);
			testPaper.setTimeConsuming(passedTime);
			testPaperService.submitPaper(testPaper);
			model.setViewName("/home/test/error");
			model.addObject("msg", "当前考试时间已耗尽，还未提交试卷，做0分处理！");
			return model;
		}
		Integer hour = (test.getUsableTime()-passedTime)/60;
		Integer minitute = (test.getUsableTime()-passedTime)%60;
		Integer second = (test.getUsableTime()*60-(int)(now.getTime() - startTestTime.getTime())/1000)%60;
		queryMap.put("testPaperId", testPaper.getId());
		List<AnswerQuestion> findListByUser = answerQuestionService.findListByUser(queryMap);
		model.addObject("title", test.getName()+"-开始考试");
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
	 * 用户选择答题提交单个答案
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
			ret.put("msg", "请正确操作!");
			return ret;
		}
		Test test = testService.findById(answerQuestion.getTestId());
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "考试信息不存在!");
			return ret;
		}
		if(test.getEndTime().getTime() < new Date().getTime()){
			ret.put("type", "error");
			ret.put("msg", "该考试已结束!");
			return ret;
		}
		Student student = (Student)request.getSession().getAttribute("student");
		if(test.getSubjectId().longValue() != student.getSubjectId().longValue()){
			ret.put("type", "error");
			ret.put("msg", "学科不同，无权进行考试!");
			return ret;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("testId", test.getId());
		queryMap.put("studentId", student.getId());
		TestPaper findTestPaper =testPaperService.find(queryMap);
		if(findTestPaper == null){
			ret.put("type", "error");
			ret.put("msg", "不存在试卷!");
			return ret;
		}
		if(findTestPaper.getId().longValue() != answerQuestion.getTestPaperId().longValue()){
			ret.put("type", "error");
			ret.put("msg", "考试试卷不正确，请规范操作!");
			return ret;
		}
		Question question = questionService.findById(answerQuestion.getQuestionId());
		if(question == null){
			ret.put("type", "error");
			ret.put("msg", "试题不存在，请规范操作!");
			return ret;
		}
		//此时，可以将答案插入到数据库中
		answerQuestion.setStudentId(student.getId());
		if(question.getRightanswer().equals(answerQuestion.getAnswer())){
			answerQuestion.setIsCorrect(1);
		}
		if(answerQuestionService.submitAnswer(answerQuestion) <= 0){
			ret.put("type", "error");
			ret.put("msg", "答题出错，请联系管理员!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "答题成功!");
		return ret;
	}
	
	@RequestMapping(value="/submit_exam",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> submitExam(Long testId,Long testPaperId,HttpServletRequest request){
		Map<String, String> ret = new HashMap<String, String>();
		Test test = testService.findById(testId);
		if(test == null){
			ret.put("type", "error");
			ret.put("msg", "考试不存在，请正确操作!");
			return ret;
		}
		if(test.getEndTime().getTime() < new Date().getTime()){
			ret.put("type", "error");
			ret.put("msg", "该考试已结束!");
			return ret;
		}
		Student student = (Student)request.getSession().getAttribute("student");
		if(test.getSubjectId().longValue() != student.getSubjectId().longValue()){
			ret.put("type", "error");
			ret.put("msg", "学科不同，无权进行操作!");
			return ret;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("testId", test.getId());
		queryMap.put("studentId", student.getId());
		TestPaper findTestPaper = testPaperService.find(queryMap);
		if(findTestPaper == null){
			ret.put("type", "error");
			ret.put("msg", "不存在试卷!");
			return ret;
		}
		/*if(findTestPaper.getId().longValue() != testPaperId.longValue()){
			ret.put("type", "error");
			ret.put("msg", "考试试卷不正确，请规范操作!");
			return ret;
		}*/
		if(findTestPaper.getStatus() == 1){
			ret.put("type", "error");
			ret.put("msg", "请勿重复交卷!");
			return ret;
		}
		//此时计算考试得分
		queryMap.put("testPaperId", testPaperId);
		List<AnswerQuestion> answerQuestionList = answerQuestionService.findListByUser(queryMap);
		//此时，可以将答案插入到数据库中
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
		//更新统计结果
		testService.edit(test);
		ret.put("type", "success");
		ret.put("msg", "提交成功!");
		return ret;
	}
	
	/**
	 * 随机从给定的list中选取给定数量的元素
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
	 * 插入指定数量的试题到答题详情表
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
	}	
	