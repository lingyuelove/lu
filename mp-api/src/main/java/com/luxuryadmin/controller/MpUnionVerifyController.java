package com.luxuryadmin.controller;

import com.luxuryadmin.entity.MpUnionVerify;
import com.luxuryadmin.service.MpUnionVerifyService;

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
 *商家联盟审核表 controller
 *@author zhangsai
 *@Date 2021-11-24 13:56:01
 */
@Api(description="商家联盟审核表")
@Controller
public class MpUnionVerifyController  extends Object {


	/**
	 * 测试生成字段
	 */
	@SuppressWarnings("")
	private String test="初始化字段";
	@Autowired
	private MpUnionVerifyService mpUnionVerifyService;

}
