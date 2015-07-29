package com.praiseapp.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.praiseapp.comm.BusinessException;
import com.praiseapp.comm.CommonController;
import com.praiseapp.comm.FileService;
import com.praiseapp.comm.RequestUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/")
public class MainController extends CommonController {
	
	/*
	 * Define Service Variables
	 */
	@Autowired (required=false)
	private MainService mainSvc;
	
	@Autowired (required=false)
	private FileService fileSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	/**
	 * @Desc	: 메인페이지 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = {"", "/"})
	public String findMainView(@RequestParam Map<String, Object> paramMap, Model model, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		List findUserList = mainSvc.findPageList(paramMap);
		
		// writeFile
		String sFile = "/batch/data/PraiseApp/main/rUserList.json";
		fileSvc.writeFileToJSONList(sFile, findUserList);
		
		// readFile
		sFile = "/batch/data/PraiseApp/main/rUserList.json";
		findUserList = fileSvc.readFileFromJSONList(sFile);
				
		model.addAttribute("rData", findUserList);
		
		// redirect
		return "/main/index";
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
}