package com.luxuryadmin.apiadmin.ord;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.ord.OrdPrintTpl;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.ord.ParamPrintTplUpload;
import com.luxuryadmin.service.ord.OrdPrintTplService;

import com.luxuryadmin.vo.biz.VoBizLeaguer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *订单打印模板表 controller
 *@author zhangsai
 *@Date 2021-09-26 16:00:52
 */
@Slf4j
@RestController
@RequestMapping(value = "/ord")
@Api(tags = {"C002.1.【订单管理】模块"}, description = "/ord/order | 订单打印模板 ")
public class OrdPrintTplController extends BaseController {


	@Autowired
	private OrdPrintTplService ordPrintTplService;

	@ApiOperation(
			value = "编辑订单打印模板;",
			notes = "编辑订单打印模板;",
			httpMethod = "POST")
	@RequestMapping("/saveOrUpPrintTpl")
	public BaseResult saveOrUpPrintTpl(@Valid ParamPrintTplUpload paramPrintTplUpload, BindingResult result) {
		servicesUtil.validControllerParam(result);
		paramPrintTplUpload.setUserId(getUserId());
		paramPrintTplUpload.setShopId(getShopId());
		ordPrintTplService.saveOrUpPrintTpl(paramPrintTplUpload);
		return BaseResult.okResult();
	}

	@ApiOperation(
			value = "订单打印模板详情;",
			notes = "订单打印模板详情;",
			httpMethod = "POST")
	@RequestMapping("/getTplByShopId")
	public BaseResult<OrdPrintTpl> getTplByShopId(@Valid ParamToken paramToken, BindingResult result) {
		servicesUtil.validControllerParam(result);
		OrdPrintTpl ordPrintTpl =ordPrintTplService.getTplByShopId(getShopId());
		return BaseResult.okResult(ordPrintTpl);
	}

	@ApiOperation(
			value = "订单打印模板列表;",
			notes = "订单打印模板列表;",
			httpMethod = "POST")
	@RequestMapping("/listTpl")
	public BaseResult<List<OrdPrintTpl>> listTpl(@Valid ParamToken paramToken, BindingResult result) {
		servicesUtil.validControllerParam(result);
		List<OrdPrintTpl> listTplByShopId =ordPrintTplService.listTplByShopId(getShopId());
		return LocalUtils.getBaseResult(listTplByShopId);
	}
}
