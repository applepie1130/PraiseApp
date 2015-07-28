package com.praiseapp.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.praiseapp.comm.BusinessException;
import com.praiseapp.comm.CommonConfig;
import com.praiseapp.comm.CommonController;
import com.praiseapp.comm.FileService;
import com.praiseapp.comm.MailService;
import com.praiseapp.comm.RequestUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/test")
public class TestController extends CommonController {
	
	/*
	 * Define Service Variables
	 */
	@Autowired (required=false)
	private TestService testSvc;
	
	@Autowired (required=false)
	private MailService mailSvc;
	
	@Autowired (required=false)
	private CommonConfig commonConfig;
	
	@Autowired (required=false)
	private FileService fileSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	/**
	 * @Desc	: 메인페이지 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = {"", "/"})
	public String userView(@RequestParam Map<String, Object> paramMap, Model model, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		List findUserList = testSvc.findUserList(paramMap);
		
		// writeFile
		String sFile = "/batch/data/MyApp/test/rUserList.json";
		fileSvc.writeFileToJSONList(sFile, findUserList);
		
		// redirect
		return "redirect:/test/index";
	}
	
	/**
	 * @Desc	: 대표 url 리다이렉션 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/index")
	public String userView2(@RequestParam Map<String, Object> paramMap, Model model, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		// readFile
		String sFile = "/batch/data/MyApp/test/rUserList.json";
		List findUserList = fileSvc.readFileFromJSONList(sFile);
		
		model.addAttribute("rData", findUserList);
		
		return "/test/index";
	}
	
	/**
	 * @Desc	: 사용자 조회 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/findUserList", method = RequestMethod.POST)
	public List findUserList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		List rUserList = testSvc.findUserList(paramMap);
		
		return rUserList;
	}
	
	/**
	 * @Desc	: 사용자 저장 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
	public Map saveUserInfo(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		List rParamData = (List) paramMap.get("rUserInfo");
		Map mRtnData = testSvc.saveUserInfo(rParamData, paramMap);
		
		return mRtnData;
	}

	/**
	 * @Desc	: RESTful 조회 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/findRestTmpList", method = RequestMethod.POST)
	public  Map findRestTmpList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		Map mTmpData = new HashMap();
		Map mRtnData = new HashMap();
		String sFile = "/batch/data/MyApp/test/";
		
		if ( "json".equals(ObjectUtils.toString(paramMap.get("restType"))) ) {
			mTmpData.put("key", testSvc.findJSONList(paramMap));
			sFile += "restTmpFromJson.json";
			
		} else if ( "xml".equals(ObjectUtils.toString(paramMap.get("restType"))) ) { 
			mTmpData.put("key", testSvc.findXMLList(paramMap));
			sFile += "restTmpFromXml.json";
			
		} else {
			new BusinessException("restType을 지정해주세요.");
		}
		
		// writeFile
		fileSvc.writeFileToJSONMap(sFile, mTmpData);
		
		// readFile
		mRtnData = fileSvc.readFileFromJSONMap(sFile);
		
		return mRtnData;
	}
	
	/**
	 * @Desc	: DOM 클롤링 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/findNaverRealRankList", method = RequestMethod.POST)
	public  List findNaverRealRankList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		String sUrl = "http://www.naver.com";
		paramMap.put("url", sUrl);
		
		List rRtnData = testSvc.findNaverRealRankList(paramMap);
		
		return rRtnData;
	}
	
	/**
	 * @Desc	: 관리자 메일 발송
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/saveSendMail", method = RequestMethod.POST)
	public Map saveSendMail(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		Map mRtnData  = mailSvc.sendMail(paramMap);
		
		return mRtnData;
	}
	
	/**
	 * @Desc	: 설정파일 조회 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/findBeanConfigure", method = RequestMethod.POST)
	public Map findBeanConfigure(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		Map mRtnData = new HashMap<String, Object>();
		mRtnData.put("serviceShutDownStartDt", commonConfig.getServiceShutDownStartDt());
		mRtnData.put("serviceShutDownEndDt", commonConfig.getServiceShutDownEndDt());
		
		return mRtnData;
	}
	
	/**
	 * @Desc	: 예외처리 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/findExceptionTest", method = RequestMethod.POST)
	public void findExceptionTest(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		throw new BusinessException("예외테스트 : 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
	}
	
	/**
	 * @Desc	: 쿠키 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 23일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/saveCookieTest", method = RequestMethod.POST)
	public Map saveCookieTest(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		Map mRtnData = new HashMap<String, Object>();
		
		// 쿠키생성
		if ( !RequestUtil.setCookie(paramMap, request, response) ) {
			throw new BusinessException("쿠키생성 중 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
		}
		
		// 쿠키읽기
		mRtnData = RequestUtil.getCookie(paramMap, request, response);
		
		return mRtnData;
	}
	
	/**
	 * @Desc	: 런타임 커맨드 실행 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 23일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/findRuntimeExecTest", method = RequestMethod.POST)
	public Map findRuntimeExecTest(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		Map mRtnData = new HashMap<String, Object>();
		
		// 런타임 커맨드 실행
		String sCommand = ObjectUtils.toString(paramMap.get("command"));
		
		try {
			Process process = Runtime.getRuntime().exec(sCommand);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String sCommandLine = null;
		    
		    while( (sCommandLine=br.readLine()) != null){
		    	System.out.println(sCommandLine);
		    }
		    
		    process.getErrorStream().close();
			process.getOutputStream().close();
		    process.getInputStream().close();
		    process.waitFor();
		    
		} catch (Exception e) {
			throw new BusinessException("커맨드 실행 중 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
		}
		
		return mRtnData;
	}
	
	/**
	 * @Desc	: 파일업로드 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 30일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/saveFileUpload", method = RequestMethod.POST)
	public Map saveFileUpload(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		Map mRtnData = testSvc.saveFileUpload(paramMap);
		
		return mRtnData;
	}
}