package com.luxuryadmin.service.pro.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProClassifySub;
import com.luxuryadmin.entity.pro.ProSeriesModel;
import com.luxuryadmin.entity.pro.ProSubSeries;
import com.luxuryadmin.mapper.pro.ProSeriesModelMapper;
import com.luxuryadmin.mapper.pro.ProSubSeriesMapper;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProClassifySubService;
import com.luxuryadmin.service.pro.ProPublicService;
import com.luxuryadmin.service.pro.ProSeriesModelService;
import com.luxuryadmin.service.pro.ProSubSeriesService;
import com.luxuryadmin.vo.pro.*;
import org.apache.commons.lang.StringUtils;
import org.jacoco.agent.rt.internal_1f1cc91.core.internal.flow.IFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 *商品分类系列表 serverImpl
 *@author zhangsai
 *@Date 2021-08-04 13:45:01
 */
@Service
@Transactional
public class ProSubSeriesServiceImpl   implements ProSubSeriesService {


	/**
	 * 注入dao
	 */
	@Resource
	private ProSubSeriesMapper proSubSeriesMapper;
	@Autowired
	private ProSeriesModelService proSeriesModelService;
	@Autowired
	private ProClassifySubService proClassifySubService;
	@Resource
	private ProSeriesModelMapper proSeriesModelMapper;
	@Autowired
	private ProPublicService proPublicService;
	@Override
	public List<VoProSubSeries> listSubSeriesPage(ParamProClassifySubSunQuery classifySubSunQuery) {
		List<VoProSubSeries> subSeriesList = proSubSeriesMapper.listSubSeriesPage(classifySubSunQuery);
		if (LocalUtils.isEmptyAndNull(subSeriesList)){
			subSeriesList =new ArrayList<>();
			VoProSubSeries voProSubSeries = new VoProSubSeries();
			voProSubSeries.setSubSeriesName("其它");
			voProSubSeries.setClassifySubName(classifySubSunQuery.getClassifySubName());
			subSeriesList.add(voProSubSeries);
		}
		return subSeriesList;
	}

	@Override
	public void addSubSeries(ParamProClassifySubSunAdd classifySubSunAdd) {
		ProSubSeries proSubSeries = proSubSeriesMapper.getSubSeriesByNameAndSub(classifySubSunAdd.getName(), classifySubSunAdd.getClassifySubName());
		if(!LocalUtils.isEmptyAndNull(proSubSeries)){
			return;
		}
		ProSubSeries subSeries = new ProSubSeries();
		Integer shopId = classifySubSunAdd.getShopId();
		Integer userId = classifySubSunAdd.getUserId();
		String type = "1";
		if (LocalUtils.isEmptyAndNull(shopId)){
			shopId = -9;
			type = "0";
		}
		subSeries.setFkShpShopId(shopId);
		if (LocalUtils.isEmptyAndNull(userId)){
			userId = -9;
		}
		subSeries.setInsertAdmin(userId);
		subSeries.setFkProClassifySubName(classifySubSunAdd.getClassifySubName());
		subSeries.setName(classifySubSunAdd.getName());
		subSeries.setDel("0");
		subSeries.setState(1);
		subSeries.setType(type);
		subSeries.setInsertTime(new Date());
		subSeries.setSort(classifySubSunAdd.getSort());
//		subSeries.setSeriesModel(classifySubSunAdd.getServiceModelName());
		proSubSeriesMapper.saveObject(subSeries);
	}

	@Override
	public void updateSubSeries(ParamProClassifySubSunUpdate classifySubSunUpdate) {
		ProSubSeries subSeries = new ProSubSeries();
		subSeries.setId(Integer.parseInt(classifySubSunUpdate.getId()));
		subSeries.setName(classifySubSunUpdate.getName());
		Integer userId = classifySubSunUpdate.getUserId();
		if (LocalUtils.isEmptyAndNull(userId)){
			userId = -9;
		}
		subSeries.setUpdateAdmin(userId);
		subSeries.setUpdateTime(new Date());
//		subSeries.setSeriesModel(classifySubSunUpdate.getServiceModelName());
		proSubSeriesMapper.updateObject(subSeries);
	}

	@Override
	public void deleteSubSeries(String id) {
		ProSubSeries subSeriesOld =(ProSubSeries)proSubSeriesMapper.getObjectById(id);
		ProSubSeries subSeries = new ProSubSeries();
		subSeries.setDel("1");
		subSeries.setId(Integer.parseInt(id));
		proSubSeriesMapper.updateObject(subSeries);
		if (subSeriesOld != null){
			proSeriesModelMapper.deleteSeriesModelClassifySub(null,subSeriesOld.getName());
		}

	}

	@Override
	public VoProSubSeries getSubSeriesById(String id) {
		return proSubSeriesMapper.getSubSeriesById(Integer.parseInt(id));
	}

	@Override
	public VoProSubSeries getSubSeriesByName(String name, Integer shopId) {
		return proSubSeriesMapper.getSubSeriesByName(name,shopId);
	}

	@Override
	public void addSubSeriesForAdmin(ParamSubSeriesForAdminAdd subSeriesForAdminAdd) {

		if (!LocalUtils.isEmptyAndNull(subSeriesForAdminAdd.getName())){
			ParamProClassifySubSunAdd classifySubSunAdd =new ParamProClassifySubSunAdd();
			classifySubSunAdd.setClassifySubName(subSeriesForAdminAdd.getClassifySubName());
			classifySubSunAdd.setName(subSeriesForAdminAdd.getName());
		    classifySubSunAdd.setServiceModelName(subSeriesForAdminAdd.getServiceModelName());
			addSubSeries(classifySubSunAdd);
		}
		if (!LocalUtils.isEmptyAndNull(subSeriesForAdminAdd.getServiceModelName())){
//			String[] serviceModelName =LocalUtils.splitString(subSeriesForAdminAdd.getServiceModelName(), ",");
//			根据逗号分隔转化为list
			List<String> serviceModelName = Arrays.asList(subSeriesForAdminAdd.getServiceModelName().split(","));
			ParamProClassifySubSonAdd classifySubSonAdd =new ParamProClassifySubSonAdd();
			classifySubSonAdd.setSubSeriesName(subSeriesForAdminAdd.getName());
			classifySubSonAdd.setClassifySubName(subSeriesForAdminAdd.getClassifySubName());
			serviceModelName.forEach(s -> {
				classifySubSonAdd.setName(s);
				proSeriesModelService.addSeriesModel(classifySubSonAdd);
			});
		}
	}

	@Override
	public VoProSubSeriesByPageForAdmin getSubSeriesByPageForAdmin(ParamProClassifySubSunQuery paramProClassifySubSunQuery) {
		if (LocalUtils.isEmptyAndNull(paramProClassifySubSunQuery.getPageNum())){
			paramProClassifySubSunQuery.setPageNum("1");
		}
		//添加分页
		PageHelper.startPage(Integer.parseInt(paramProClassifySubSunQuery.getPageNum()), paramProClassifySubSunQuery.getPageSize());
		List<VoProSubSeriesForAdmin> subSeriesForAdmins = proSubSeriesMapper.listSubSeriesForAdminPage(paramProClassifySubSunQuery);
		VoProSubSeriesByPageForAdmin subSeriesByPageForAdmin = new VoProSubSeriesByPageForAdmin();
		if (LocalUtils.isEmptyAndNull(subSeriesForAdmins)){
			return subSeriesByPageForAdmin;
		}
		//遍历赋值品牌类型
		subSeriesForAdmins.forEach(subSeriesForAdmin -> {
			subSeriesForAdmin.setClassifyCodeName(proClassifySubService.getClassifyCodeName(subSeriesForAdmin.getClassifyCode()));
			subSeriesForAdmin.setSeriesModelName(getSeriesModel(subSeriesForAdmin.getClassifySubName(),subSeriesForAdmin.getSubSeriesName()));
		});
		PageInfo pageInfo = new PageInfo(subSeriesForAdmins);
		subSeriesByPageForAdmin.setPageNum(pageInfo.getPageNum());
		subSeriesByPageForAdmin.setPageSize(pageInfo.getPageSize());
		if (pageInfo.getNextPage() > 0) {
			subSeriesByPageForAdmin.setHasNextPage(true);
		} else {
			subSeriesByPageForAdmin.setHasNextPage(false);
		}
		subSeriesByPageForAdmin.setList(subSeriesForAdmins);
		subSeriesByPageForAdmin.setTotal(pageInfo.getTotal());
		return subSeriesByPageForAdmin;
	}

	public String getSeriesModel(String classifySubName,String subSeriesName){
		ParamProClassifySubSonQuery classifySubSonQuery =new ParamProClassifySubSonQuery();
		classifySubSonQuery.setClassifySubName(classifySubName);
		classifySubSonQuery.setSubSeriesName(subSeriesName);
		List<VoProSubSeries> subSeriesList = proSeriesModelMapper.listSeriesModelPage(classifySubSonQuery);
		if (LocalUtils.isEmptyAndNull(subSeriesList)){
			return null;
		}
		List<String> name = new ArrayList<>();
		subSeriesList.forEach(subSeries ->{
			name.add(subSeries.getSeriesModelName());
		});
		String nmeList =  StringUtils.join(name,",");
		return nmeList;
	}
	@Override
	public void updateSubSeriesForAdmin(ParamSubSeriesForAdminUpdate subSeriesForAdminUpdate) {
		ProSubSeries subSeriesOld =(ProSubSeries)proSubSeriesMapper.getObjectById(subSeriesForAdminUpdate.getId());
		ParamProClassifySubSunUpdate classifySubSunUpdate = new ParamProClassifySubSunUpdate();
		classifySubSunUpdate.setId(subSeriesForAdminUpdate.getId());
		classifySubSunUpdate.setName(subSeriesForAdminUpdate.getName());
		updateSubSeries(classifySubSunUpdate);
		if (!LocalUtils.isEmptyAndNull(subSeriesForAdminUpdate.getServiceModelName())){
			proSeriesModelMapper.deleteSeriesModelClassifySub(null,subSeriesOld.getName());
//			String[] serviceModelName =LocalUtils.splitString(subSeriesForAdminAdd.getServiceModelName(), ",");
			//根据逗号分隔转化为list
			List<String> serviceModelName = Arrays.asList(subSeriesForAdminUpdate.getServiceModelName().split(","));
			ParamProClassifySubSonAdd classifySubSonAdd =new ParamProClassifySubSonAdd();
			classifySubSonAdd.setSubSeriesName(subSeriesForAdminUpdate.getName());
			classifySubSonAdd.setClassifySubName(subSeriesForAdminUpdate.getClassifySubName());
			proSeriesModelMapper.deleteSeriesModelClassifySub(null,subSeriesOld.getName());
			serviceModelName.forEach(s -> {
				VoProSubSeries proSubSeries=proSeriesModelService.getSeriesModelByName(s,null);
				if (proSubSeries ==null){
					classifySubSonAdd.setName(s);
					proSeriesModelService.addSeriesModel(classifySubSonAdd);
				}else {
					ParamProClassifySubSunUpdate paramProClassifySubSunUpdate = new ParamProClassifySubSunUpdate();
					paramProClassifySubSunUpdate.setId(proSubSeries.getId().toString());
					proSeriesModelService.updateSeriesModel(paramProClassifySubSunUpdate);
				}
			});
		}
	}

	@Override
	public List<VoProSubSeriesForAdmin> listSubSeriesForAdmin(ParamProClassifySubSunQuery paramProClassifySubSunQuery) {
		List<VoProSubSeriesForAdmin> subSeriesForAdmins = proSubSeriesMapper.listSubSeriesForAdminPage(paramProClassifySubSunQuery);
		if (LocalUtils.isEmptyAndNull(subSeriesForAdmins)){
			return subSeriesForAdmins;
		}
		//遍历赋值品牌类型
		subSeriesForAdmins.forEach(subSeriesForAdmin -> {
			subSeriesForAdmin.setClassifyCodeName(proClassifySubService.getClassifyCodeName(subSeriesForAdmin.getClassifyCode()));
//			subSeriesForAdmin.setSeriesModelName(getSeriesModel(subSeriesForAdmin.getClassifySubName(),subSeriesForAdmin.getSubSeriesName()));
		});
		return subSeriesForAdmins;
	}

	@Override
	public void addSubSeriesList() {
		ParamProClassifySubQuery paramQuery =new ParamProClassifySubQuery();
		List<VoProClassifySub> classifySubs = proClassifySubService.listProClassifySubPage(paramQuery);
		if (LocalUtils.isEmptyAndNull(classifySubs)){
			return;
		}
		classifySubs.forEach(voProClassifySub -> {
			//根据品牌查询系列
			List<VoProPublic> serialNoList = proPublicService.querySerialNo(voProClassifySub.getName());
			this.addSubServiceList(serialNoList,voProClassifySub.getName());
		});
	}
	public void addSubServiceList(List<VoProPublic> serialNoList ,String name){
		if (LocalUtils.isEmptyAndNull(serialNoList)){
			return;
		}
		ParamProClassifySubSunAdd classifySubSunAdd =new ParamProClassifySubSunAdd();
		serialNoList.forEach(serialNo -> {
			//根据品牌系列查询型号
			List<VoProPublic> typeNoList = proPublicService.queryTypeNo(name, serialNo.getSerialNo());
			this.addServiceModel(typeNoList,serialNo.getSerialNo(),name);
			//新增系列
			classifySubSunAdd.setClassifySubName(name);
			classifySubSunAdd.setName(serialNo.getSerialNo());
			addSubSeries(classifySubSunAdd);
		});
	}

	/**
	 *
	 * @param serialNoList
	 * @param serialNo
	 * @param name
	 */
	public void addServiceModel(List<VoProPublic> serialNoList ,String serialNo,String name){
		if (LocalUtils.isEmptyAndNull(serialNoList)){
			return;
		}
		ParamProClassifySubSonAdd classifySubSonAdd =new ParamProClassifySubSonAdd();
		serialNoList.forEach(typeNo -> {
			//新增型号
			classifySubSonAdd.setClassifySubName(name);
			classifySubSonAdd.setSubSeriesName(serialNo);
			classifySubSonAdd.setName(typeNo.getTypeNo());
			proSeriesModelService.addSeriesModel(classifySubSonAdd);
		});
	}
}
