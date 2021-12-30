package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.entity.shp.ShpShopConfig;
import com.luxuryadmin.mapper.shp.ShpShopConfigMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.param.shp.ParamShopConfigUpdate;
import com.luxuryadmin.service.shp.ShpShopConfigService;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


/**
 *店铺配置表 serverImpl
 *@author zhangsai
 *@Date 2021-07-01 11:16:02
 */
@Service
public class ShpShopConfigServiceImpl implements ShpShopConfigService {

	/**
	 * 注入dao
	 */
	@Resource
	private ShpShopConfigMapper shpShopConfigMapper;
	@Resource
	private ShpShopMapper shpShopMapper;
	@Override
	public void updateShopConfig(ParamShopConfigUpdate shopConfigUpdate) {

		ShpShopConfig shpShopConfig = shpShopConfigMapper.getShopConfigByShopId(shopConfigUpdate.getShopId());
		if (shpShopConfig == null){
			this.addShopConfig(shopConfigUpdate);
		}else {
			shpShopConfig.setOpenShareUser(shopConfigUpdate.getOpenShareUser());
			shpShopConfigMapper.updateObject(shpShopConfig);
		}

	}
	public void addShopConfig(ParamShopConfigUpdate shopConfigUpdate) {
		ShpShop shpShop =(ShpShop)shpShopMapper.getObjectById(shopConfigUpdate.getShopId());
		ShpShopConfig shpShopConfig =new ShpShopConfig();
		shpShopConfig.setFkShpShopId(shopConfigUpdate.getShopId());
		shpShopConfig.setInsertTime(new Date());
		if (shopConfigUpdate.getOpenShareUser() != null){
			shpShopConfig.setOpenShareUser(shopConfigUpdate.getOpenShareUser());
		}else {
			shpShopConfig.setOpenShareUser("0");
		}
		shpShopConfig.setShopNumber(shpShop.getNumber());
		shpShopConfig.setInsertAdmin(-9);
		shpShopConfigMapper.saveObject(shpShopConfig);
	}

	@Override
	public ShpShopConfig getShopConfigByShopId(Integer shopId) {
		return shpShopConfigMapper.getShopConfigByShopId(shopId);
	}
}
