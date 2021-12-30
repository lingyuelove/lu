package com.luxuryadmin.api.pro;


import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProTempProductMoveService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoMoveProductLoad;
import com.luxuryadmin.vo.pro.VoProductLoad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *临时仓商品移动历史表 controller
 *@author zhangsai
 *@Date 2021-09-24 17:46:58
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro")
@Api(tags = {"C004.2【临时仓】移仓模块-----2.6.4---zs"}, description = "/shop/user/pro |临时仓】移仓模块相关")
public class ProTempProductMoveController extends ProProductBaseController {


	@Autowired
	private ProTempProductMoveService proTempProductMoveService;

	@ApiOperation(
			value = "添加商品到临时仓",
			notes = "添加商品到临时仓",
			httpMethod = "POST")
	@PostMapping("/moveProductToTemp")
	public BaseResult moveProductToTemp(@Valid ParamTempMovePro paramTempMovePro, BindingResult result) {
		servicesUtil.validControllerParam(result);
		paramTempMovePro.setUserId(getUserId());
		paramTempMovePro.setShopId(getShopId());
		String message = proTempProductMoveService.moveProductToTemp(paramTempMovePro);
		if (!LocalUtils.isEmptyAndNull(message)){
			return BaseResult.errorResult(EnumCode.OK_MOVE_TEMP);
		}
		return BaseResult.okResult();
	}

	@ApiOperation(
			value = " 加载转入转出页面",
			notes = " 加载转入转出页面 --2.6.4❤--zs",
			httpMethod = "POST")
	@PostMapping("/listMoveProductToTemp")
	public BaseResult listMoveProductToTemp(@Valid ParamTempProductMoveQuery paramTempProductMoveQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);

		if ("enter".equals(paramTempProductMoveQuery.getTempType())){
			paramTempProductMoveQuery.setEnterTempId(paramTempProductMoveQuery.getProTempId());
		}
		if ("remove".equals(paramTempProductMoveQuery.getTempType())){
			paramTempProductMoveQuery.setRemoveTempId(paramTempProductMoveQuery.getProTempId());
		}
		paramTempProductMoveQuery.setShopId(getShopId());
		PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
		List<VoMoveProductLoad> voProductLoadList =proTempProductMoveService.listMoveProductToTemp(paramTempProductMoveQuery);
		if (!LocalUtils.isEmptyAndNull(voProductLoadList)){
			voProductLoadList.forEach(voMoveProductLoad -> {
				formatVoProductLoad(null, voMoveProductLoad, "normal");
			});
		}
		return BaseResult.okResult(voProductLoadList);
	}
}
