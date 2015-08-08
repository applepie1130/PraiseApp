package com.praiseapp.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.praiseapp.comm.CommonService;

@Service
@Transactional
public class MainService extends CommonService {
	
	/**
	 * @Desc	: 곡 리스트 조회 (검색용)
	 * @Author	: 김성준
	 * @Create	: 2015년 08월 07일 
	 * @stereotype ServiceMethod
	 */
	@CacheEvict(value="default", allEntries=true)
	public List findMusicSheetListBySearch(Map paramMap) {
		List resultData = queryForListData("mainQry.selectMusicSheetListBySearch", paramMap);
		return resultData;
	}
	
	/**
	 * @Desc	: 초성별 곡 리스트 조회
	 * @Author	: 김성준
	 * @Create	: 2015년 08월 07일 
	 * @stereotype ServiceMethod
	 */
	@CacheEvict(value="default", allEntries=true)
	public List findMusicSheetListByFirstNm(Map paramMap) {
		List resultData = queryForListData("mainQry.selectMusicSheetListByFirstNm", paramMap);
		return resultData;
	}
	
	/**
	 * @Desc	: 곡 전체 등록수 조회
	 * @Author	: 김성준
	 * @Create	: 2015년 08월 07일 
	 * @stereotype ServiceMethod
	 */
	@CacheEvict(value="default", allEntries=true)
	public List findtMusicSheetTotalCount(Map paramMap) {
		List resultData = queryForListData("mainQry.selectMusicSheetTotalCount", paramMap);
		return resultData;
	}
	
	/**
	 * @Desc	: 곡 명 키워드 조회
	 * @Author	: 김성준
	 * @Create	: 2015년 08월 08일 
	 * @stereotype ServiceMethod
	 */
	@CacheEvict(value="default", allEntries=true)
	public String findMusicSheetKeyWord() {
		Map paramMap = new HashMap<String, Object>();
		Map mData = new HashMap<String, Object>();
		List rData = queryForListData("mainQry.selectMusicSheetListBySearch", paramMap);
		String [] rKeyWordData = new String[rData.size() * 2];
		Iterator itr = rData.iterator();
		int i = 0;
		
		// title(제목), sheet_num(장) 조합
		while ( itr.hasNext() ) {
			mData = (Map) itr.next();
			rKeyWordData[i] = ObjectUtils.toString(mData.get("title"));
			rKeyWordData[i+1] = ObjectUtils.toString(mData.get("sheet_num"));
			
			i = i + 2;
		}
		
		return Arrays.toString(rKeyWordData).replace("[", "").replace("]", "").replaceAll(", ", ",");
	}
}
