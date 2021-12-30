package com.luxuryadmin.api.pro;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProSeriesModelService;

import com.luxuryadmin.vo.pro.VoProSubSeries;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 *商品分类型号表 controller
 *@author zhangsai
 *@Date 2021-08-04 13:45:01
 */
@Slf4j
@RequestMapping(value = "/shop/user/classify/sub", method = RequestMethod.POST)
@RestController
@Api(tags = {"E002.3【二级分类】型号模块----2.6.2"}, description = "/shop/user/classify/sub |二级分类模块相关")
public class ProSeriesModelController  extends BaseController {

	@Autowired
	private ProSeriesModelService proSeriesModelService;


	@ApiOperation(value = "加载型号分类---2.6.2",
			notes = "加载型号分类；根据商铺及系列分类加载",
			httpMethod = "POST"
	)
	@RequestMapping("listSeriesModelPage")
	public BaseResult<List<VoProSubSeries>> listSeriesModelPage(@Valid ParamProClassifySubSonQuery classifySubSonQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);
		classifySubSonQuery.setShopId(getShopId());
		//查询店铺和一级分类下所有二级分类，按首字母进行匹配
		List<VoProSubSeries> subSeriesList = proSeriesModelService.listSeriesModelPage(classifySubSonQuery);
		return BaseResult.okResult(subSeriesList);
	}

	@ApiOperation(
			value = "删除商品型号分类;----2.6.2",
			notes = "删除商品型号分类;----2.6.2",
			httpMethod = "POST")
	@RequestMapping(value = "/deleteSeriesModel", method = RequestMethod.POST)
	public BaseResult deleteSeriesModel(@Valid ParamShareDelete shareDelete, BindingResult result){
		servicesUtil.validControllerParam(result);
		VoProSubSeries subSeries = proSeriesModelService.getSeriesModelById(shareDelete.getId());
		if (subSeries == null){
			return BaseResult.errorResult("暂无此型号分类");
		}
		if (subSeries != null && "0".equals(subSeries.getType())){
			return BaseResult.errorResult("此型号分类为系统设置，所以不可删");
		}
		proSeriesModelService.deleteSeriesModel(shareDelete.getId());
		return BaseResult.okResult();
	}

	@ApiOperation(
			value = "添加商品型号分类;----2.6.2",
			notes = "添加商品型号分类;----2.6.2",
			httpMethod = "POST")
	@RequestMapping(value = "/addSeriesModel", method = RequestMethod.POST)
	public BaseResult addSeriesModel(@Valid ParamProClassifySubSonAdd classifySubSonAdd , BindingResult result){
		servicesUtil.validControllerParam(result);

		//对emoji表情的处理
		classifySubSonAdd.setName(EmojiParser.parseToAliases(classifySubSonAdd.getName()));
		//根据名称查询分类是否存在
		VoProSubSeries subSeries = proSeriesModelService.getSeriesModelByName(classifySubSonAdd.getName(),getShopId());
		if (subSeries!=null){
			return BaseResult.defaultErrorWithMsg("该型号分类已存在!");
		}
		classifySubSonAdd.setShopId(getShopId());
		classifySubSonAdd.setUserId(getUserId());
		proSeriesModelService.addSeriesModel(classifySubSonAdd);
		return BaseResult.okResult();
	}

}
