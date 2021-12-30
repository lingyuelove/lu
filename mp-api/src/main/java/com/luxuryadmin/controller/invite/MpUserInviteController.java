package com.luxuryadmin.controller.invite;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.invite.ParamUserInviteList;
import com.luxuryadmin.service.MpUserInviteService;
import com.luxuryadmin.vo.invite.VOUserInviteList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * 邀请记录 controller
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Slf4j
@RestController
@RequestMapping(value = "/invite")
@Api(tags = "MP.邀请记录", description = "邀请记录")
public class MpUserInviteController extends BaseController {


    @Autowired
    private MpUserInviteService mpUserInviteService;

    @GetMapping("/listUserInvite")
    @ApiOperation(value = "获取邀请用户列表", httpMethod = "GET")
    public BaseResult listUserInvite(@Valid ParamUserInviteList param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(getPageNum(), getPageSize());
        List<VOUserInviteList> vo = mpUserInviteService.listUserInvite(param);
        return LocalUtils.getBaseResult(vo);
    }
}
