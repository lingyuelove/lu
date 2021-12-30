package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpShareTotal;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.service.shp.ShpShareTotalService;
import com.luxuryadmin.vo.shp.VoShopMemberAddHour;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 *商铺分享进度时长累计表 controller
 *@author zhangsai
 *@Date 2021-06-08 14:15:43
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/share", method = RequestMethod.POST)
@Api(tags = {"E009.【店铺】模块"}, description = "/shop/user/share | 商铺分享进度时长2.5.4")
public class ShpShareTotalController extends BaseController {


	@Autowired
	private ShpShareTotalService shpShareTotalService;


	/**
	 * 分页查询 (所有的实体属性都是条件,如果为空则忽略该字段) (详见Page类.所以的实体都继承该类 默认 page=1 pageSize=10)->商铺分享进度时长累计表
	 */
	@ApiOperation(
			value = "获取当前店铺的分享时长",
			notes = "获取当前店铺的分享时长;",
			httpMethod = "POST")
	@ApiImplicitParams({
	})
	@RequestMapping("/getByShopId")
	public BaseResult<VoShopMemberAddHour> getByShopId(@Valid ParamToken paramToken, BindingResult result){
		servicesUtil.validControllerParam(result);
		VoShopMemberAddHour memberAddHour =shpShareTotalService.getByShopId(getShopId());
		//会员到期时间
		String vipExpireTip = getVipExpire();
		memberAddHour.setVipExpireTip(LocalUtils.isEmptyAndNull(vipExpireTip) ? "" : vipExpireTip + " 到期");
		return BaseResult.okResult(memberAddHour);
	}

	@ApiOperation(
			value = "店铺的分享接口",
			notes = "店铺的分享接口;",
			httpMethod = "POST")
	@ApiImplicitParams({
	})
	@RequestMapping("/updateShopMemberAddHour")
	public BaseResult updateShopMemberAddHour(@Valid ParamToken paramToken, BindingResult result){
		servicesUtil.validControllerParam(result);
		shpShareTotalService.updateShopMemberAddHour();
		return BaseResult.okResult();
	}
}
