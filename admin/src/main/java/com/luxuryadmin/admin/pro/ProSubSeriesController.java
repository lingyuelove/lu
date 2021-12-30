package com.luxuryadmin.admin.pro;

import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProSeriesModelService;
import com.luxuryadmin.service.pro.ProSubSeriesService;
import com.luxuryadmin.vo.pro.VoProSubSeries;
import com.luxuryadmin.vo.pro.VoProSubSeriesByPageForAdmin;
import com.luxuryadmin.vo.pro.VoProSubSeriesForAdmin;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 *商品分类系列表 controller
 *@author zhangsai
 *@Date 2021-08-04 13:45:00
 */
@Slf4j
@RequestMapping(value = "/shop/user/classify/sub", method = RequestMethod.POST)
@RestController
@Api(tags = {"1.2【二级分类】系列模块----2.6.2"}, description = "/shop/user/classify/sub |二级分类模块相关")
public class ProSubSeriesController extends BaseController {
	@Autowired
	private ProSeriesModelService proSeriesModelService;

	@Autowired
	private ProSubSeriesService proSubSeriesService;
	@ApiOperation(value = "加载系列分类---2.6.2--page",
			notes = "加载系列分类--page；根据商铺及品牌分类加载--page",
			httpMethod = "POST"
	)
	@RequestMapping("listSubSeriesPage")
	@RequiresPerm(value = "public:sub:series")
	public BaseResult<VoProSubSeriesByPageForAdmin> listSubSeriesPage(@Valid ParamProClassifySubSunQuery classifySubSunQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);
		VoProSubSeriesByPageForAdmin subSeriesByPageForAdmin = proSubSeriesService.getSubSeriesByPageForAdmin(classifySubSunQuery);
		return BaseResult.okResult(subSeriesByPageForAdmin);
	}

	@ApiOperation(value = "加载系列分类---2.6.2--list",
			notes = "加载系列分类--list；根据商铺及品牌分类加载--list",
			httpMethod = "POST"
	)
	@RequestMapping("listSubSeries")
	public BaseResult<List<VoProSubSeriesForAdmin>> listSubSeriesForAdmin(@Valid ParamClassifySonForAdminQuery classifySonForAdminQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);
		ParamProClassifySubSunQuery classifySubSonQuery = new ParamProClassifySubSunQuery();
		BeanUtils.copyProperties(classifySonForAdminQuery,classifySubSonQuery);
		List<VoProSubSeriesForAdmin> subSeriesList = proSubSeriesService.listSubSeriesForAdmin(classifySubSonQuery);
		return BaseResult.okResult(subSeriesList);
	}

	@ApiOperation(value = "加载型号分类---2.6.2--list",
			notes = "加载型号分类；根据品牌系列加载型号",
			httpMethod = "POST"
	)
	@RequestMapping("listSeriesModelPage")
	public BaseResult<List<VoProSubSeries>> listSeriesModelPage(@Valid ParamClassifySonForAdminQuery classifySonForAdminQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);
		ParamProClassifySubSonQuery classifySubSonQuery = new ParamProClassifySubSonQuery();
		BeanUtils.copyProperties(classifySonForAdminQuery,classifySubSonQuery);
		if (LocalUtils.isEmptyAndNull(classifySonForAdminQuery.getSubSeriesName())){
			classifySubSonQuery.setSubSeriesName("(未知)");
		}
		List<VoProSubSeries> subSeriesList = proSeriesModelService.listSeriesModelPage(classifySubSonQuery);
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
		proSubSeriesService.deleteSubSeries(shareDelete.getId());
		return BaseResult.okResult();
	}

	@ApiOperation(
			value = "添加商品系列分类;----2.6.2",
			notes = "添加商品系列分类;----2.6.2",
			httpMethod = "POST")
	@RequestMapping(value = "/addSubSeries", method = RequestMethod.POST)
	public BaseResult addSeriesModel(@Valid ParamSubSeriesForAdminAdd subSeriesForAdminAdd , BindingResult result){
		servicesUtil.validControllerParam(result);
		if (LocalUtils.isEmptyAndNull(subSeriesForAdminAdd.getName())){
			subSeriesForAdminAdd.setName("(未知)");
		}
		//根据名称查询分类是否存在
		VoProSubSeries subSeries = proSubSeriesService.getSubSeriesByName(subSeriesForAdminAdd.getName(),null);
		if (subSeries!=null){
			return BaseResult.defaultErrorWithMsg("该系列分类已存在!");
		}
//        EmojiParser.parseToAliases(paramAdd.getName());


		proSubSeriesService.addSubSeriesForAdmin(subSeriesForAdminAdd);
		return BaseResult.okResult();
	}

	@ApiOperation(
			value = "编辑商品系列分类;----2.6.2",
			notes = "编辑商品系列分类;----2.6.2",
			httpMethod = "POST")
	@RequestMapping(value = "/updateSubSeriesForAdmin", method = RequestMethod.POST)
	public BaseResult updateSubSeriesForAdmin(@Valid ParamSubSeriesForAdminUpdate subSeriesForAdminUpdate , BindingResult result){
		servicesUtil.validControllerParam(result);

//		//根据名称查询分类是否存在
//		VoProSubSeries subSeries = proSubSeriesService.getSubSeriesByName(subSeriesForAdminUpdate.getName(),null);
//		if (subSeries!=null){
//			return BaseResult.defaultErrorWithMsg("该系列分类已存在!");
//		}
//        EmojiParser.parseToAliases(paramAdd.getName());

		if (LocalUtils.isEmptyAndNull(subSeriesForAdminUpdate.getName())){
			subSeriesForAdminUpdate.setName("(未知)");
		}
		proSubSeriesService.updateSubSeriesForAdmin(subSeriesForAdminUpdate);
		return BaseResult.okResult();
	}
}
