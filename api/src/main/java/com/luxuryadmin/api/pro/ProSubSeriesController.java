package com.luxuryadmin.api.pro;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.entity.pro.ProSubSeries;
import com.luxuryadmin.param.pro.ParamProClassifySubQuery;
import com.luxuryadmin.param.pro.ParamProClassifySubSunAdd;
import com.luxuryadmin.param.pro.ParamProClassifySubSunQuery;
import com.luxuryadmin.param.pro.ParamShareDelete;
import com.luxuryadmin.service.pro.ProSubSeriesService;

import com.luxuryadmin.vo.pro.VoProSubSeries;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *商品分类系列表 controller
 *@author zhangsai
 *@Date 2021-08-04 13:45:00
 */
@Slf4j
@RequestMapping(value = "/shop/user/classify/sub", method = RequestMethod.POST)
@RestController
@Api(tags = {"E002.2【二级分类】系列模块----2.6.2"}, description = "/shop/user/classify/sub |二级分类模块相关")
public class ProSubSeriesController  extends BaseController {


	@Autowired
	private ProSubSeriesService proSubSeriesService;
	@ApiOperation(value = "加载系列分类---2.6.2",
			notes = "加载系列分类；根据商铺及品牌分类加载",
			httpMethod = "POST"
	)
	@RequestMapping("listSubSeriesPage")
	public BaseResult<List<VoProSubSeries>> listSubSeriesPage(@Valid ParamProClassifySubSunQuery classifySubSunQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);
		classifySubSunQuery.setShopId(getShopId());
		//查询店铺和一级分类下所有二级分类，按首字母进行匹配
		List<VoProSubSeries> subSeriesList = proSubSeriesService.listSubSeriesPage(classifySubSunQuery);
		return BaseResult.okResult(subSeriesList);
	}

	@ApiOperation(
			value = "删除商品系列分类;----2.6.2",
			notes = "删除商品系列分类;----2.6.2",
			httpMethod = "POST")

	@RequestMapping(value = "/deleteSubSeries", method = RequestMethod.POST)
	public BaseResult deleteSeriesModel(@Valid ParamShareDelete shareDelete, BindingResult result){
		servicesUtil.validControllerParam(result);
		VoProSubSeries subSeries = proSubSeriesService.getSubSeriesById(shareDelete.getId());
		if (subSeries == null){
			return BaseResult.errorResult("暂无此系列分类");
		}
		if (subSeries != null && "0".equals(subSeries.getType())){
			return BaseResult.errorResult("此系列分类为系统设置，所以不可删");
		}
		proSubSeriesService.deleteSubSeries(shareDelete.getId());
		return BaseResult.okResult();
	}

	@ApiOperation(
			value = "添加商品系列分类;----2.6.2",
			notes = "添加商品系列分类;----2.6.2",
			httpMethod = "POST")

	@RequestMapping(value = "/addSubSeries", method = RequestMethod.POST)
	public BaseResult addSeriesModel(@Valid ParamProClassifySubSunAdd classifySubSunAdd , BindingResult result){
		servicesUtil.validControllerParam(result);
		//对emoji表情的处理
		classifySubSunAdd.setName(EmojiParser.parseToAliases(classifySubSunAdd.getName()));
		//根据名称查询分类是否存在
		VoProSubSeries subSeries = proSubSeriesService.getSubSeriesByName(classifySubSunAdd.getName(),getShopId());
		if (subSeries!=null){
			return BaseResult.defaultErrorWithMsg("该系列分类已存在!");
		}
//        EmojiParser.parseToAliases(paramAdd.getName());

		classifySubSunAdd.setShopId(getShopId());
		classifySubSunAdd.setUserId(getUserId());
		proSubSeriesService.addSubSeries(classifySubSunAdd);
		return BaseResult.okResult();
	}
}
