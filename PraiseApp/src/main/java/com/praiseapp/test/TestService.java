package com.praiseapp.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.praiseapp.comm.BusinessException;
import com.praiseapp.comm.CommonService;
import com.praiseapp.comm.StringUtil;

@Service
@Transactional
public class TestService extends CommonService {
	
	/**
	 * @Desc	: 사용자 조회 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype ServiceMethod
	 */
	@CacheEvict(value="default", allEntries=true)
	public List findUserList(Map paramMap) {
		List resultData = queryForListData("userQry.selectUserList", paramMap);
		return resultData;
	}
	
	/**
	 * @Desc	: 사용자 저장 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype ServiceMethod
	 */
	public Map saveUserInfo(List rParamData, Map paramMap) {
		
		System.out.println(rParamData);
		
		Map<String, Object> mData = new HashMap<String, Object>();
		ListIterator itr = rParamData.listIterator();
		String sRowStatus;
		String sMessage = "Tranjaction is Completed !!";
		int nRtnStatus = -1;

		while (itr.hasNext()) {
			mData = (Map<String, Object>) itr.next();
			sRowStatus = (String) mData.get("status");
			
			try {
				if ("INSERT".equals(sRowStatus.toUpperCase())) {
					System.out.println("Insert!!!!");
					nRtnStatus = update("userQry.insertUserInfo", mData);
				} else if ("UPDATE".equals(sRowStatus.toUpperCase())) {
					System.out.println("Update!!!!");
					nRtnStatus = update("userQry.updateUserInfo", mData);
				} else if ("DELETE".equals(sRowStatus.toUpperCase())) {
					System.out.println("Delete!!!!");
					nRtnStatus = update("userQry.deleteUserInfo", mData);
				} else if ("NORMAL".equals(sRowStatus.toUpperCase())) {
	
				} 
			} catch (Exception e) {
				throw new BusinessException("오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
			}
		}
		
		// Return Massage
		Map mRtnMsg = new HashMap<String, Object>();
		mRtnMsg.put("status", nRtnStatus);
		mRtnMsg.put("message", sMessage);
		
		return mRtnMsg;
	}
	
	/**
	 * @Desc	: RESTful 조회 테스트 (JSON)
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype ServiceMethod
	 */
	public Map findJSONList(Map paramMap) {
		String sUrl = ObjectUtils.toString(paramMap.get("url"));
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> mData = restTemplate.getForObject(sUrl, Map.class);
		
		return mData;
	}
	
	/**
	 * @Desc	: RESTful 조회 테스트 (XML)
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype ServiceMethod
	 */
	public Map findXMLList(Map paramMap) {
		String sUrl = ObjectUtils.toString(paramMap.get("url"));
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> mData = new HashMap<String, Object>();
		
		// Stirng Type XML data -> JsonObject Type JSON data -> Map Type Json data 변환
		try {
			JSONObject objJson = XML.toJSONObject(restTemplate.getForObject(sUrl, String.class));
			mData = new ObjectMapper().readValue(objJson.toString(), new TypeReference<Map<String, Object>>() {});
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return mData;
	}
	
	/**
	 * @Desc	: DOM 클롤링 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype ServiceMethod
	 */
	@Cacheable(value="default")
	public List findNaverRealRankList(Map paramMap) {
		String sPageUrl = ObjectUtils.toString(paramMap.get("url"));
		List rRtnData = new ArrayList();
		
		try {
			Document doc = Jsoup.connect(sPageUrl).get();
			
			// 검색구분자 CSS Selector
			String sRealRankSelector = "#realrank li a";
			Elements rcw = doc.select(sRealRankSelector);
			
			// 검색시간
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	        long nowmills = System.currentTimeMillis();
	        String now = sdf.format(new Date(nowmills));
	        
	        // 실시간검색에 Parsing
	        for (Element el : rcw) {
	        	Map mData = new HashMap();
	        	mData.put("rank", el.parent().attr("value"));
	        	mData.put("title", el.attr("title"));
	        	mData.put("link", el.attr("href"));
	        	mData.put("searchTime", now);
	        	
	        	String sId = el.parent().attr("id");
	        	if ( !"lastrank".equals(sId) ) {
	        		rRtnData.add(mData);
	        	}
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rRtnData;
	}
	
	
	/**
	 * @Desc	: 파일업로드 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 30일 
	 * @stereotype ServiceMethod
	 */
	public Map saveFileUpload(Map paramMap) {
		Map mRtnData = new HashMap<String, Object>();
		MultipartHttpServletRequest request = (MultipartHttpServletRequest) paramMap.get("request");
		MultipartFile file = null;
		Iterator<String> itr =  request.getFileNames();
		String sFilePath = "/batch/data/MyApp/file/";
		
		// 파일 디렉토리가 없으면 디렉토리 생성, 있으면 파일쓰기
		File filePath = new File(sFilePath); 
		
		if ( !filePath.exists() ) {
			filePath.mkdirs();
		}
		
		// 파일검증
		while ( itr.hasNext() ) {
			file = request.getFile(itr.next());
			
			if ( !file.isEmpty() ) {
				// 파일타입 체크 (이미지 파일 제한)
				String sOrgType = file.getContentType();
				Pattern pattern = Pattern.compile("image");
				Matcher matcher = pattern.matcher(sOrgType);

				/*
				if ( !matcher.find() ) {
					throw new BusinessException("이미지파일만 업로드 가능합니다.<br/>다시 시도해주세요.");
				}
				
				
				// 파일사이즈 체크 (5MB이 제한)
				long nFileSize = file.getSize();
				
				if ( nFileSize > 5000000 ) {
					throw new BusinessException("파일 용량은 5MB를 초과할 수 없습니다.<br/>다시 시도해주세요.");
				}
				*/
			}
		}
		
		
		// 파일전송
		itr =  request.getFileNames();
		while ( itr.hasNext() ) {
			file = request.getFile(itr.next());
			
			if ( !file.isEmpty() ) {
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				String sOrgFileNm = file.getOriginalFilename();
				String sOrgType = file.getContentType();
				String sFileName = dateFormat.format(date) + "_" + StringUtil.getUniqueString() + sOrgFileNm.substring(sOrgFileNm.lastIndexOf(".")).toLowerCase();
				long nFileSize = file.getSize();
				
				try {
					// old (Using BufferOutputStream)
//					byte[] bytes = file.getBytes();
//					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(sFilePath + sFileName)));
//					stream.write(bytes);
//					stream.close();
					
					// new (Using MultipartFile transfer)
					file.transferTo(new File(sFilePath + sFileName));
					
				} catch (Exception e) {
					throw new BusinessException("파일업로드 중 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
				}
				
				mRtnData.put("fileType", sOrgType);
				mRtnData.put("fileName", sFileName);
				mRtnData.put("fileSize", nFileSize);
			}
		}
		
		return mRtnData;
	}
}
