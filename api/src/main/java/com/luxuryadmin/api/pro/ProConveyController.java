package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProConvey;
import com.luxuryadmin.entity.pro.ProTemp;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProConveyService;
import com.luxuryadmin.vo.pro.VoConvey;
import com.luxuryadmin.vo.pro.VoConveyForApp;
import com.luxuryadmin.vo.pro.VoProTemp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;


/**
 *商品传送表 controller
 *@author zhangsai
 *@Date 2021-11-22 15:12:44
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro/convey")
@Api(tags = {"C003.【商品】模块--商品传送--2.6.7❤"})
public class ProConveyController  extends BaseController {

	@Autowired
	private ProConveyService proConveyService;
	/**
	 * 加载【商品传送】页面
	 *
	 * @param paramConveyQuery 前端参数
	 * @return Result
	 */
	@ApiOperation(
			value = "商品传送--集合显示",
			notes = "商品传送--集合显示",
			httpMethod = "POST")
	@RequestMapping("/listConvey")
	public BaseResult<VoConveyForApp> listConvey(@Valid ParamConveyQuery paramConveyQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);
		PageHelper.startPage(getPageNum(), PAGE_SIZE_20);
		Integer shopId = getShopId();
		paramConveyQuery.setShopId(shopId);
		List<VoConvey> conveyList = proConveyService.listConvey(paramConveyQuery);
		VoConveyForApp voConveyForApp =new VoConveyForApp();
		//集合显示
		voConveyForApp.setConveys(conveyList);
		if (Objects.equals(paramConveyQuery.getType(),"send")){
			String vid = LocalUtils.getUUID();
			String proTempKey = ConstantRedisKey.getCreateProConveyKey(shopId, getUserId());
			redisUtil.setExMINUTES(proTempKey, vid, 120);
			//新增的vid的设置
			voConveyForApp.setVid(vid);
		}

		return BaseResult.okResult(voConveyForApp);
	}

	/**
	 * 创建商品传送
	 *
	 * @param conveyAdd 前端参数
	 * @return Result
	 */
	@ApiOperation(
			value = "创建商品传送",
			notes = "创建商品传送",
			httpMethod = "POST")
	@RequestMapping("/addConvey")
	public BaseResult addConvey(@Valid ParamConveyAdd conveyAdd, BindingResult result) {
		servicesUtil.validControllerParam(result);
//		String proTempKey = ConstantRedisKey.getCreateProConveyKey(getShopId(), getUserId());
//		servicesUtil.validVid(proTempKey, conveyAdd.getVid());
		conveyAdd.setShopId(getShopId());
		conveyAdd.setUserId(getUserId());
		proConveyService.addConvey(conveyAdd);
//		redisUtil.delete(proTempKey);
		return BaseResult.okResult();
	}
	@ApiOperation(
			value = "提取商品传送",
			notes = "提取商品传送",
			httpMethod = "POST")
	@RequestMapping("/receiveConvey")
	public BaseResult receiveConvey(@Valid ParamConveyUpdate conveyUpdate, BindingResult result) {
		servicesUtil.validControllerParam(result);
		conveyUpdate.setShopId(getShopId());
		conveyUpdate.setUserId(getUserId());
		proConveyService.receiveConvey(conveyUpdate);
		return BaseResult.okResult();
	}
	@ApiOperation(
			value = "删除商品传送",
			notes = "删除商品传送",
			httpMethod = "POST")
	@RequestMapping("/deleteConvey")
	public BaseResult deleteConvey(@Valid ParamConveyDelete conveyDelete, BindingResult result) {
		servicesUtil.validControllerParam(result);
		proConveyService.deleteConvey(conveyDelete);
		return BaseResult.okResult();
	}
}
