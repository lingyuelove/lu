package com.luxuryadmin.service.impl;

import com.luxuryadmin.mapper.MpUnionVerifyMapper;
import com.luxuryadmin.service.MpUnionVerifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 *商家联盟审核表 serverImpl
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@Service
@Transactional
public class MpUnionVerifyServiceImpl   implements MpUnionVerifyService {


	/**
	 * 注入dao
	 */
	@Resource
	private MpUnionVerifyMapper mpUnionVerifyMapper;


}
