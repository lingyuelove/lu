package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProSeriesModel;
import com.luxuryadmin.mapper.pro.ProSeriesModelMapper;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProSeriesModelService;
import com.luxuryadmin.vo.pro.VoProSubSeries;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *商品分类型号表 serverImpl
 *@author zhangsai
 *@Date 2021-08-04 13:45:01
 */
@Service
@Transactional
public class ProSeriesModelServiceImpl   implements ProSeriesModelService {


	/**
	 * 注入dao
	 */
	@Resource
	private ProSeriesModelMapper proSeriesModelMapper;

	@Override
	public List<VoProSubSeries> listSeriesModelPage(ParamProClassifySubSonQuery classifySubSonQuery) {
		List<VoProSubSeries> subSeriesList = proSeriesModelMapper.listSeriesModelPage(classifySubSonQuery);
		if (LocalUtils.isEmptyAndNull(subSeriesList)){
			subSeriesList =new ArrayList<>();
			VoProSubSeries voProSubSeries = new VoProSubSeries();
			voProSubSeries.setSeriesModelName("其它");
			voProSubSeries.setClassifySubName(classifySubSonQuery.getSubSeriesName());
			subSeriesList.add(voProSubSeries);
		}
		return subSeriesList;
	}

	@Override
	public void addSeriesModel(ParamProClassifySubSonAdd classifySubSonAdd) {
		String subSeriesName = classifySubSonAdd.getSubSeriesName();
		String classifySubName =classifySubSonAdd.getClassifySubName();
		ProSeriesModel seriesModelOld = proSeriesModelMapper.getSeriesModel(classifySubSonAdd.getName(),classifySubName,subSeriesName);
		if (!LocalUtils.isEmptyAndNull(seriesModelOld)){
			return;
		}
		ProSeriesModel seriesModel = new ProSeriesModel();
		Integer shopId = classifySubSonAdd.getShopId();
		Integer userId = classifySubSonAdd.getUserId();
		String type = "1";
		if (LocalUtils.isEmptyAndNull(shopId)){
			shopId = -9;
			type = "0";
		}
		seriesModel.setFkShpShopId(shopId);
		if (LocalUtils.isEmptyAndNull(userId)){
			userId = -9;
		}
		seriesModel.setInsertAdmin(userId);

		seriesModel.setFkProSubSeriesName(subSeriesName);
		seriesModel.setFkProClassifySubName(classifySubName);
		seriesModel.setName(classifySubSonAdd.getName());
		seriesModel.setDel("0");
		seriesModel.setState(1);
		seriesModel.setType(type);
		seriesModel.setInsertTime(new Date());
		seriesModel.setSort(classifySubSonAdd.getSort());
		proSeriesModelMapper.saveObject(seriesModel);
	}

	@Override
	public void updateSeriesModel(ParamProClassifySubSunUpdate classifySubSunUpdate) {
		ProSeriesModel seriesModel = new ProSeriesModel();
		seriesModel.setId(Integer.parseInt(classifySubSunUpdate.getId()));
		seriesModel.setName(classifySubSunUpdate.getName());
		Integer userId = classifySubSunUpdate.getUserId();
		if (LocalUtils.isEmptyAndNull(userId)){
			userId = -9;
		}
		seriesModel.setUpdateAdmin(userId);
		seriesModel.setUpdateTime(new Date());
		seriesModel.setDel("0");
		proSeriesModelMapper.updateObject(seriesModel);
	}

	@Override
	public void deleteSeriesModel(String id) {
		ProSeriesModel seriesModel = new ProSeriesModel();
		seriesModel.setDel("1");
		seriesModel.setId(Integer.parseInt(id));
		proSeriesModelMapper.updateObject(seriesModel);
	}

	@Override
	public VoProSubSeries getSeriesModelById(String id) {
		return proSeriesModelMapper.getSeriesModelById(Integer.parseInt(id));
	}

	@Override
	public VoProSubSeries getSeriesModelByName(String name, Integer shopId) {
		return proSeriesModelMapper.getSeriesModelByName(name,shopId);
	}
}
