package com.luxuryadmin.admin.ord;

import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.ord.OrdPrintTpl;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.ord.ParamPrintTplUpload;
import com.luxuryadmin.param.ord.ParamPrintTplUploadForAdmin;
import com.luxuryadmin.service.ord.OrdPrintTplService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 *订单打印模板表 controller
 *@author zhangsai
 *@Date 2021-09-26 16:00:52
 */
@Slf4j
@RestController
@RequestMapping(value = "/ord")
@Api(tags = {"4.1【订单管理】模块"}, description = "/ord/order | 订单打印模板 ")
public class OrdPrintTplController extends BaseController {


	@Autowired
	private OrdPrintTplService ordPrintTplService;

	@ApiOperation(
			value = "编辑订单打印模板;",
			notes = "编辑订单打印模板;",
			httpMethod = "POST")
	@RequestMapping("/saveOrUpPrintTpl")
	public BaseResult saveOrUpPrintTpl(@Valid ParamPrintTplUploadForAdmin paramPrintTplUploadForAdmin, BindingResult result) {
		servicesUtil.validControllerParam(result);
		ParamPrintTplUpload paramPrintTplUpload =new ParamPrintTplUpload();

		paramPrintTplUpload.setUserId(-9);
		paramPrintTplUpload.setShopId(-9);
		paramPrintTplUpload.setContext(paramPrintTplUploadForAdmin.getContext());
		ordPrintTplService.saveOrUpPrintTpl(paramPrintTplUpload);
		return BaseResult.okResult();
	}

	@ApiOperation(
			value = "订单打印模板详情;",
			notes = "订单打印模板详情;",
			httpMethod = "POST")
	@RequestMapping("/getTplDetail")
	@RequiresPerm(value = "system:tpl")
	public BaseResult<OrdPrintTpl> getTplDetail(@Valid ParamToken paramToken, BindingResult result) {
		servicesUtil.validControllerParam(result);
		OrdPrintTpl ordPrintTpl =ordPrintTplService.getTplByShopId(-9);
		return BaseResult.okResult(ordPrintTpl);
	}


}
