package com.praiseapp.comm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonService extends SuperDelegationAdapter {
	
	@Autowired
	private SqlSession session;
	
	private static final Logger logger = LoggerFactory.getLogger(CommonService.class);
	
	/**
	 * @Desc	: DB Table Select Query
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public List queryForListData ( String queryId, Map condition ) {
		List rResultList = null;
		rResultList = session.selectList(queryId, condition);
		return rResultList;
	}
	
	/**
	 * @Desc	: DB Table Update Query
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public int update ( String queryId, Map condition ) {
		int nInsert = session.insert(queryId, condition);
		return nInsert;
	}
}
