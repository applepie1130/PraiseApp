package com.praiseapp.main;

import java.util.List;
import java.util.Map;

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
}
