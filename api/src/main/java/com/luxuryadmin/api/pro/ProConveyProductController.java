package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProConvey;
import com.luxuryadmin.entity.pro.ProConveyProduct;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProConveyProductService;
import com.luxuryadmin.service.pro.ProConveyService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.shp.VoEmployee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *pro_convey_product controller
 *@author zhangsai
 *@Date 2021-11-22 15:05:52
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro/convey")
@Api(tags = {"C003.【商品】模块--商品传送--2.6.7❤"})
public class ProConveyProductController  extends ProProductBaseController {

	@Autowired
	private ProConveyProductService proConveyProductService;

	@Autowired
	private ProClassifyService proClassifyService;

	@Autowired
	private ProConveyService proConveyService;
	/**
	 * 初始化上传商品页面
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(
			value = "初始化商品传送页面",
			notes = "初始化商品传送页面;加载分类;",
			httpMethod = "GET")
	@GetMapping(value = "/initConveyUpload")
	public BaseResult<VoConveyProductInit> initUpload(@Valid ParamConveyProductQuery conveyProductQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);
		int shopId = getShopId();
		List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
		String defaultPrice;
		String conveyId =conveyProductQuery.getConveyId().toString();
		//判断redis是否有key值
		if (redisUtil.hasKey(ConstantRedisKey.getProConveyKeyByKey(conveyId))) {
			//获取接收参数---是否为登录后初次请求接口
			defaultPrice = redisUtil.get(ConstantRedisKey.getProConveyKeyByKey(conveyId));
		} else {
			defaultPrice = "tradePrice";
		}
		//获取寄卖传送
		ProConvey convey =proConveyService.getConveyDetail(conveyProductQuery.getConveyId());
		if (convey == null){
			throw new MyException("该寄卖传送不存在");
		}
		VoConveyProductInit conveyProductInit =new VoConveyProductInit();
		conveyProductInit.setClassifyList(voProClassifyList);
		conveyProductInit.setDefaultPrice(defaultPrice);
		conveyProductInit.setReceiveState(convey.getReceiveState());
		conveyProductInit.setSendState(convey.getSendState());
		return BaseResult.okResult(conveyProductInit);
	}

	/**
	 * 加载【商品传送详情】页面
	 *
	 * @param conveyProductQuery 前端参数
	 * @return Result
	 */
	@ApiOperation(
			value = "商品传送--详情显示",
			notes = "商品传送--详情显示",
			httpMethod = "POST")
	@RequestMapping("/listConveyProduct")
	public BaseResult<VoConveyProduct> listConveyProduct(@Valid ParamConveyProductQuery conveyProductQuery, BindingResult result) {
		servicesUtil.validControllerParam(result);
		String type =conveyProductQuery.getType();
		if (LocalUtils.isEmptyAndNull(conveyProductQuery.getType())){
			throw new MyException("寄卖传送标识不能为空");
		}
		ProConvey convey =proConveyService.getConveyDetail(conveyProductQuery.getConveyId());
		if (convey == null){
			throw new MyException("该传送不存在");
		}
		if (LocalUtils.isEmptyAndNull(convey.getReceiveState())){
			convey.setReceiveState("0");
		}
		conveyProductQuery.setReceiveState(convey.getReceiveState());
		//未签收接收方商品列表设置为传送方
		if("receive".equals(conveyProductQuery.getType()) && "0".equals(convey.getReceiveState())){
			conveyProductQuery.setType("send");
		}
		if(!LocalUtils.isEmptyAndNull(conveyProductQuery.getProName())){
			conveyProductQuery.setUniqueCode(conveyProductQuery.getProName());
		}
		PageHelper.startPage(getPageNum(), PAGE_SIZE_20);
		Integer shopId = getShopId();
		conveyProductQuery.setShopId(shopId);
		VoConveyProduct productList = proConveyProductService.listConveyProduct(conveyProductQuery);
		List<VoProductLoad> productLoads = productList.getProductLoads();
		if (LocalUtils.isEmptyAndNull(productLoads)){
			return BaseResult.okResult(productList);
		}
		String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
		productLoads.forEach(voProductLoad -> {

			formatVoProductLoad(conveyProductQuery.getAppVersion(), voProductLoad, true, userPerms, "normal");
			if (!LocalUtils.isEmptyAndNull(voProductLoad.getShowTime())){
				if("receive".equals(type)){
					voProductLoad.setShowTime("提取时间 "+voProductLoad.getShowTime());
				}else {
					voProductLoad.setShowTime("更新时间 "+voProductLoad.getShowTime());
				}

			}
			getConveyPrice(conveyProductQuery.getConveyId().toString(),voProductLoad);
		});
		productList.setProductLoads(productLoads);
		
		return BaseResult.okResult(productList);
	}

	@ApiOperation(
			value = "商品传送--删除接口",
			notes = "商品传送--删除接口",
			httpMethod = "POST")
	@RequestMapping("/deleteConveyProduct")
	public BaseResult deleteConveyProduct(@Valid ParamConveyProductUpdate conveyProductUpdate, BindingResult result) {
		servicesUtil.validControllerParam(result);
		conveyProductUpdate.setShopId(getShopId());
		conveyProductUpdate.setUserId(getUserId());
		proConveyProductService.deleteConveyProduct(conveyProductUpdate);
		return BaseResult.okResult();
	}

	@ApiOperation(
			value = "商品传送--新增",
			notes = "商品传送--新增",
			httpMethod = "POST")
	@RequestMapping("/addConveyProduct")
	public BaseResult addConveyProduct(@Valid ParamConveyProductAdd conveyProductAdd, BindingResult result) {
		servicesUtil.validControllerParam(result);
		conveyProductAdd.setShopId(getShopId());
		conveyProductAdd.setUserId(getUserId());
		proConveyProductService.addConveyProduct(conveyProductAdd);

		return BaseResult.okResult();
	}

	@ApiOperation(
			value = "商品传送--编辑",
			notes = "商品传送--编辑",
			httpMethod = "POST")
	@RequestMapping("/updateConveyProduct")
	public BaseResult updateConveyProduct(@Valid ParamConveyProductUpdate conveyProductUpdate, BindingResult result) {
		servicesUtil.validControllerParam(result);
		conveyProductUpdate.setShopId(getShopId());
		conveyProductUpdate.setUserId(getUserId());
		proConveyProductService.updateConveyProduct(conveyProductUpdate);
		return BaseResult.okResult();
	}
}
