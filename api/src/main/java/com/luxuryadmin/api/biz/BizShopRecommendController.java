package com.luxuryadmin.api.biz;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Collections;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.luxuryadmin.entity.biz.BizShopRecommend;
import com.luxuryadmin.service.biz.BizShopRecommendService;
import org.springframework.web.bind.annotation.RestController;


/**
 *添加友商推荐 controller
 *@author zhangsai
 *@Date 2021-05-27 11:42:43
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/biz/recommend", method = RequestMethod.POST)
@Api(tags = {"G002.【友商】模块"}, description = "/shop/user/biz/recommend |【友商】模块相关")
public class BizShopRecommendController  extends BaseController {

	@Autowired
	private BizShopRecommendService bizShopRecommendService;


}
