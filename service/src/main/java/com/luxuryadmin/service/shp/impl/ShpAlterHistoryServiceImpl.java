package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.mapper.shp.ShpAlterHistoryMapper;
import com.luxuryadmin.service.shp.ShpAlterHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 *商铺会员变动表 serverImpl
 *@author zhangsai
 *@Date 2021-06-08 13:45:04
 */
@Service
public class ShpAlterHistoryServiceImpl implements ShpAlterHistoryService {


	/**
	 * 注入dao
	 */
	@Resource
	private ShpAlterHistoryMapper shpAlterHistoryMapper;

}
