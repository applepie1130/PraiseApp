package com.praiseapp.main;

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
public class MainService extends CommonService {
	
	/**
	 * @Desc	: 사용자 조회 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype ServiceMethod
	 */
	@CacheEvict(value="default", allEntries=true)
	public List findPageList(Map paramMap) {
		List resultData = queryForListData("mainQry.selectPageList", paramMap);
		return resultData;
	}
}
