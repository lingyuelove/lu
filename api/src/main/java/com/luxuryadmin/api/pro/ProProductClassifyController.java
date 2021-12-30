package com.luxuryadmin.api.pro;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.entity.pro.ProProductClassify;
import com.luxuryadmin.service.pro.ProProductClassifyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *商品补充信息关联表 controller
 *@author zhangsai
 *@Date 2021-08-03 10:45:15
 */
@Api(description="商品补充信息关联表")
@Controller
public class ProProductClassifyController  extends BaseController {

	@Autowired
	private ProProductClassifyService proProductClassifyService;


}
