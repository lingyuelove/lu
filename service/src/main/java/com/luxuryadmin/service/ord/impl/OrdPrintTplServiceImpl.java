package com.luxuryadmin.service.ord.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.ord.OrdPrintTpl;
import com.luxuryadmin.mapper.ord.OrdPrintTplMapper;
import com.luxuryadmin.param.ord.ParamPrintTplUpload;
import com.luxuryadmin.service.ord.OrdPrintTplService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 *订单打印模板表 serverImpl
 *@author zhangsai
 *@Date 2021-09-26 16:00:53
 */
@Service
@Transactional
public class OrdPrintTplServiceImpl implements OrdPrintTplService {


	/**
	 * 注入dao
	 */
	@Resource
	private OrdPrintTplMapper ordPrintTplMapper;

	@Override
	public void saveOrUpPrintTpl(ParamPrintTplUpload paramPrintTplUpload) {
		Integer shopId =paramPrintTplUpload.getShopId();
		Integer userId =paramPrintTplUpload.getUserId();
		OrdPrintTpl ordPrintTplOld = ordPrintTplMapper.getTplByShopId(shopId);
		if (LocalUtils.isEmptyAndNull(ordPrintTplOld)){
			OrdPrintTpl ordPrintTpl = new OrdPrintTpl();
			ordPrintTpl.setContext(paramPrintTplUpload.getContext());
			ordPrintTpl.setInsertAdmin(userId);
			ordPrintTpl.setFkShpShopId(shopId);
			ordPrintTpl.setInsertTime(new Date());
			ordPrintTplMapper.saveObject(ordPrintTpl);
		}else {
			ordPrintTplOld.setUpdateAdmin(userId);
			ordPrintTplOld.setUpdateTime(new Date());
			ordPrintTplOld.setContext(paramPrintTplUpload.getContext());
			ordPrintTplMapper.updateObject(ordPrintTplOld);
		}

	}

	@Override
	public OrdPrintTpl getTplByShopId(Integer shopId) {
		OrdPrintTpl ordPrintTpl = ordPrintTplMapper.getTplByShopId(shopId);
		return ordPrintTpl;
	}

	@Override
	public List<OrdPrintTpl> listTplByShopId(Integer shopId) {
		return ordPrintTplMapper.listTplByShopId(shopId);
	}
}
