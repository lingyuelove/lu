package com.luxuryadmin.api.biz;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.biz.ParamShopSeeAdd;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.luxuryadmin.entity.biz.BizShopSee;
import com.luxuryadmin.service.biz.BizShopSeeService;


/**
 *店铺查看次数表 controller
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/biz/see", method = RequestMethod.POST)
@Api(tags = {"G002.【友商】模块"}, description = "/shop/user/biz/see |【友商】模块相关")
public class BizShopSeeController extends BaseController {

	@Autowired
	private BizShopSeeService bizShopSeeService;



	/**
	 *  保存 (主键为空则增加否则修改)-> 店铺查看次数表
	 */
	@PostMapping("save")
	@ApiImplicitParams({
			@ApiImplicitParam(
					paramType = "query", required = true, dataType = "String",
					name = "token", value = "登录token")
	})
	@ApiOperation(value="新增友商店铺观看记录", notes="新增友商店铺观看记录" ,httpMethod="POST")
	public BaseResult save(ParamShopSeeAdd shopSeeAdd, BindingResult result){
		servicesUtil.validControllerParam(result);
		bizShopSeeService.saveShopSee(shopSeeAdd);
		return BaseResult.okResult();

	}



}
