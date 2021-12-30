package com.luxuryadmin.api.biz;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.biz.BizUnionVerify;
import com.luxuryadmin.param.biz.ParamUnionVerifyAdd;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.oss.ParamImg;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.biz.BizUnionVerifyService;

import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.biz.VoUnionVerify;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *商家联盟审核表 controller
 *@author zhangsai
 *@Date 2021-11-03 17:44:45
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/biz/union", method = RequestMethod.POST)
@Api(tags = {"G002.1.【友商】模块2.6.6 --zs"}, description = "/shop/user/biz/union |【友商】模块相关")
public class BizUnionVerifyController extends BaseController {


	@Autowired
	private BizUnionVerifyService bizUnionVerifyService;
	@Autowired
	private BizShopUnionService bizShopUnionService;

	@ApiOperation(
			value = "新增--商家联盟审核",
			notes = "新增--商家联盟审核",
			httpMethod = "POST")

	@PostMapping("/save")
	public BaseResult addUnionVerify(@Valid ParamUnionVerifyAdd param, BindingResult result) {
		servicesUtil.validControllerParam(result);
		param.setShopId(getShopId());
		param.setUserId(getUserId());
		bizUnionVerifyService.addUnionVerify(param);
		return BaseResult.okResult();
	}
	@ApiOperation(
			value = "获取详情--商家联盟审核",
			notes = "获取详情--商家联盟审核",
			httpMethod = "POST")
	@PostMapping("/getVerifyByShopId")
	public BaseResult<VoUnionVerify> getByShopId() {

		VoUnionVerify unionVerify= bizUnionVerifyService.getUnionVerifyByShopId(getShopId());
		if (unionVerify == null){
			unionVerify = new VoUnionVerify();
			unionVerify.setState("2");
		}
		return BaseResult.okResult(unionVerify);
	}

	/**
	 * 商品图片上传
	 *
	 * @param paramImg
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(
			value = "商品图片上传;",
			notes = "商品图片上传;",
			httpMethod = "POST")
	@PostMapping("/uploadImg")
	public BaseResult uploadImg(@Valid ParamImg paramImg, BindingResult result, HttpServletRequest request) throws Exception {
		servicesUtil.validControllerParam(result);
		StringBuffer dirName = new StringBuffer();
		if (!LocalUtils.isEmptyAndNull(paramImg.getSonType())){
			dirName.append(paramImg.getType()+"/"+paramImg.getSonType()+"/");
		}else {
			dirName.append(paramImg.getType()+"/");
		}
		dirName.append(getShopId());
		dirName.append("/").append(DateUtil.format(new Date(), "YYYYMMdd"));
		dirName.append("/").append(getUserId()).append("/");
		String filePath = OSSUtil.uploadBaseMethod(request, dirName.toString(), 20);
		return BaseResult.okResult(filePath);
	}
}
