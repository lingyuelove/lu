package com.luxuryadmin.service.biz.impl;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.entity.biz.BizShopSee;
import com.luxuryadmin.mapper.biz.BizLeaguerMapper;
import com.luxuryadmin.mapper.biz.BizShopSeeMapper;
import com.luxuryadmin.param.biz.ParamShopSeeAdd;
import com.luxuryadmin.param.biz.ParamShopSeeSearch;
import com.luxuryadmin.service.biz.BizShopSeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;


/**
 *店铺查看次数表 serverImpl
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
@Service
public class BizShopSeeServiceImpl   implements BizShopSeeService {


	/**
	 * 注入dao
	 */
	@Resource
	private BizShopSeeMapper bizShopSeeMapper;

	@Resource
	private BizLeaguerMapper bizLeaguerMapper;

	@Override
	public void addShopSee(ParamShopSeeAdd shopSeeAdd) {
		BizLeaguer leaguer = bizLeaguerMapper.getBizLeaguerByShopIdAndLeaguerShopId(shopSeeAdd.getShopId(),shopSeeAdd.getBeSeenShopId());
		if (leaguer == null){
			return;
		}
		BizShopSee shopSee = new BizShopSee();
		shopSee.setFkShpShopId(shopSeeAdd.getShopId());
		shopSee.setFkBeSeenShopId(shopSeeAdd.getBeSeenShopId());
		shopSee.setDayCount(1);
		try {
			shopSee.setInsertTime(DateUtil.getStartTimeOfDay(new Date()));
		} catch (ParseException e) {
			throw new MyException("店铺查看次数表"+e);
		}
		shopSee.setInsertAdmin(shopSeeAdd.getUserId());
		bizShopSeeMapper.saveObject(shopSee);
	}

	@Override
	public BizShopSee getBySearch(ParamShopSeeSearch shopSeeSearch) {
		try {
			shopSeeSearch.setInsertTime(DateUtil.getStartTimeOfDay(new Date()));
		} catch (ParseException e) {
			throw new MyException("店铺查看次数表"+e);
		}
		BizShopSee bizShopSee = bizShopSeeMapper.getBySearch(shopSeeSearch);
		return bizShopSee;
	}

	@Override
	public void saveShopSee(ParamShopSeeAdd shopSeeAdd) {
		ParamShopSeeSearch shopSeeSearch = new ParamShopSeeSearch();
		BeanUtils.copyProperties(shopSeeAdd, shopSeeSearch);
		BizShopSee shopSee = this.getBySearch(shopSeeSearch);
		if (shopSee == null){
			this.addShopSee(shopSeeAdd);
		}else {
			shopSee.setDayCount(shopSee.getDayCount()+1);
			bizShopSeeMapper.updateObject(shopSee);
		}
	}
}
