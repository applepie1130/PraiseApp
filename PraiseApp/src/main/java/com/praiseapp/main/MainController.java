package com.praiseapp.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.ibatis.reflection.SystemMetaObject;
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
import com.praiseapp.comm.StringUtil;

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
	 * @Desc	: 메인페이지
	 * @Author	: 김성준
	 * @Create	: 2015년 08월 02일 
	 * @stereotype Action
	 */
	@RequestMapping(value = {"", "/"})
	public String findMainPage(@RequestParam Map<String, Object> paramMap, Model model, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		String sPageType = ObjectUtils.toString(paramMap.get("pageType"));
		
		List findUserList = mainSvc.findPageList(paramMap);
		
		// writeFile
		String sFile = "/batch/data/PraiseApp/main/rUserList.json";
		fileSvc.writeFileToJSONList(sFile, findUserList);
		
		// readFile
		sFile = "/batch/data/PraiseApp/main/rUserList.json";
		findUserList = fileSvc.readFileFromJSONList(sFile);
		
		// 즐겨찾기 쿠키정보 가져오기
		String sFavoriteVal = GLIO.getFavoriteCookieInfo();
		
		model.addAttribute("rData", findUserList);
		model.addAttribute("pageType", sPageType);
		model.addAttribute("favoriteVal", sFavoriteVal);
		
		return "/main/main";
	}
	
	/**
	 * @Desc	: 뷰페이지
	 * @Author	: 김성준
	 * @Create	: 2015년 08월 02일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/view")
	public String findViewPage(@RequestParam Map<String, Object> paramMap, Model model, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		model.addAttribute("pageType", "view");
		
		return "/main/view";
	}
	
	/**
	 * @Desc	: 즐겨찾기 페이지
	 * @Author	: 김성준
	 * @Create	: 2015년 08월 02일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/favorite")
	public String findFavoritePage(@RequestParam Map<String, Object> paramMap, Model model, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		String sPageType = ObjectUtils.toString(paramMap.get("pageType"));
		
		// 즐겨찾기 쿠키정보 가져오기
		String sFavoriteVal = GLIO.getFavoriteCookieInfo();
		
		model.addAttribute("pageType", sPageType);
		model.addAttribute("favoriteVal", sFavoriteVal);
		
		return "/main/favorite";
	}
	
	/**
	 * @Desc	: 즐겨찾기 쿠키등록
	 * @Author	: 김성준
	 * @Create	: 2015년 08월 04일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/saveFavorite", method = RequestMethod.POST)
	public Map saveFavorite(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		String sToken = ",";
		Map mCookieData = new HashMap<String, Object>();
		Map mRtnData = new HashMap<String, Object>();
		
		// 즐겨찾기 값
		String sCookieValue = ObjectUtils.toString(paramMap.get("cookieValue"));
		
		// 쿠키 기본 값 설정
		paramMap.put("cookieName", "MY_FAVORITE");
		paramMap.put("cookieExpire", 20 * 12 * 30 * 24 * 60 * 60);
		paramMap.put("cookiePath", "/");
		
		// 쿠키정보 가져오기
		String sFavoriteVal = GLIO.getFavoriteCookieInfo();
		
		// 쿠키값 확인 및 중복시(이미등록) 쿠키 값 제거
		String[] rSplited = sFavoriteVal.split(sToken);
		int nChkIndex = -1;
		int nLength = rSplited.length;
		for ( int i=0; i<nLength; i++ ) {
			if ( sCookieValue.equals( rSplited[i] ) ) {
				rSplited[i] = null;
				nChkIndex++;
			}
		}
		
		// 쿠키정보 셋팅
		if ( nChkIndex > -1 ) {		// 이미 즐겨찾기 추가된 경우
			sFavoriteVal = "";
			for ( int i=0; i<nLength; i++ ) {
				if ( !StringUtil.isEmpty(rSplited[i]) ) {
					sFavoriteVal += rSplited[i] + sToken;
				}
			}
			
			// 쿠키정보가 1개인경우 쿠키삭제
			if ( rSplited.length == 1 ) {
				paramMap.put("cookieExpire", 0);
				paramMap.put("cookieValue", "-1");
			} else { 
				paramMap.put("cookieValue", sFavoriteVal);
			}
			
		} else {					// 신규 즐겨찾기 등록의 경우
			sFavoriteVal += sCookieValue + sToken;
			
			paramMap.put("cookieValue", sFavoriteVal);
		}
		
		// 최종 쿠키생성
		if ( !RequestUtil.setCookie(paramMap, request, response) ) {
			throw new BusinessException("쿠키생성 중 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
		}
		
		mRtnData.put("cookieValue", paramMap.get("cookieValue"));
		
		return mRtnData;
	}
}