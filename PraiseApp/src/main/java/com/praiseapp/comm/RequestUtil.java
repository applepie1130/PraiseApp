package com.praiseapp.comm;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;

public class RequestUtil {
	
	/**
	 * @Desc	: Request Parameter 재 정의
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public static Map getParameter(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mParam = paramMap;
		
		mParam.put("request", request);
		mParam.put("response", response);
		
		return mParam;
	}
	
	/**
	 * @Desc	: 쿠키 생성
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 23일
	 * @stereotype Utils
	 */
	public static Boolean setCookie(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		
		String sName	= ObjectUtils.toString(paramMap.get("cookieName"));
		String sValue	= ObjectUtils.toString(paramMap.get("cookieValue"));
		int nExpire		= Integer.parseInt(ObjectUtils.toString((paramMap.get("cookieExpire"))));
		String sDomain	= ObjectUtils.toString(paramMap.get("cookieDomain"));
		String sPath	= ObjectUtils.toString(paramMap.get("cookiePath"));
		
		if ( !StringUtil.isEmpty(sName) && !StringUtil.isEmpty(sValue) ) {
			// 쿠키생성
			Cookie cookie = new Cookie(sName, sValue);
			
			if ( nExpire > -1  ) {
				cookie.setMaxAge(nExpire);
			}
			
			if ( !StringUtil.isEmpty(sDomain) ) {
				cookie.setDomain(sDomain);
			}
			
			if ( !StringUtil.isEmpty(sPath) ) {
				cookie.setPath(sPath);
			}
			
			response.addCookie(cookie);
			return true;
		}
			
		return false;
	}
	
	/**
	 * @Desc	: 쿠키 조회
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 23일 
	 * @stereotype Utils
	 */
	public static Map getCookie(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		String sName	= ObjectUtils.toString(paramMap.get("cookieName"));
		Map rRtnData	= new HashMap<String, Object>();
		
		if ( !StringUtil.isEmpty(sName) ) {
			Cookie cookies[] = request.getCookies();
			if ( cookies != null ) {
				for ( Cookie ck : cookies ) {
					if ( sName.equals(ck.getName()) ) {
						rRtnData.put(ck.getName(), ck.getValue());
					}
				}
			}
		}
		
		return rRtnData;
	}
	
	
//	public static Map getParameter(HttpServletRequest request, HttpServletResponse response) {
//	Map<String, String[]> parameters = request.getParameterMap();
//	Map mData = null;
//	List rData = null;
//	
//	if (parameters != null) {
//		mData = new HashMap();
//		rData = new ArrayList<Map>();
//
//		for (String key : parameters.keySet()) {
//			String[] values = parameters.get(key);
//			mData.put(key, values[0]);
//		}
//	}
//	return mData;
//}
}
