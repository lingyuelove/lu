package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.mapper.shp.ShpShareTypeMapper;
import com.luxuryadmin.service.shp.ShpShareTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 *商铺添加时长类型表 serverImpl
 *@author zhangsai
 *@Date 2021-06-08 14:15:43
 */
@Service
@Transactional
public class ShpShareTypeServiceImpl   implements ShpShareTypeService {


	/**
	 * 注入dao
	 */
	@Resource
	private ShpShareTypeMapper shpShareTypeMapper;



}
