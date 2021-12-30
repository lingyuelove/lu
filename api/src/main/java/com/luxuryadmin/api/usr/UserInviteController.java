package com.luxuryadmin.api.usr;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.enums.sys.EnumSysConfigNew;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.sys.SysConfigNewService;
import com.luxuryadmin.vo.usr.VoInviteList;
import com.luxuryadmin.vo.usr.VoUserInviteData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user")
@Api(tags = {"Z002.【邀请】模块"}, description = "/shop/user |邀请")
public class UserInviteController extends BaseController {


    @Autowired
    private SysConfigNewService sysConfigNewService;

    @Autowired
    private ShpUserService shpUserService;


    /**
     * 获取邀请链接信息
     *
     * @param params
     * @return Result
     */
    @ApiOperation(
            value = "1.获取邀请H5链接",
            notes = "获取邀请H5链接;<br/>",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestMapping(value = "/getUserInviteH5Url", method = RequestMethod.GET)
    public BaseResult getUserInviteH5Url(@RequestParam Map<String, String> params) {
        int userId = getUserId();
        Integer inviteNum = shpUserService.getShpUserBaseByUserIdAndShopId(userId).getUserNumber();
        String configValue = sysConfigNewService.getSysConfigByMasterKeyAndSubKey(EnumSysConfigNew.INVITE_PAGE);
        String h5Url = configValue + inviteNum;
        return BaseResult.okResult(h5Url);
    }

    /**
     * 获取邀请链接信息
     *
     * @param params
     * @return Result
     */
    @ApiOperation(
            value = "2.获取邀请H5相关数据",
            notes = "获取邀请H5相关数据;【V2.2.0】<br/>",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestMapping(value = "/getUserInviteData", method = RequestMethod.GET)
    public BaseResult<VoUserInviteData> getUserInviteData(@RequestParam Map<String, String> params) {
        VoUserInviteData voUserInviteData = new VoUserInviteData();
        int userId = getUserId();
        Integer inviteNum = shpUserService.getShpUserBaseByUserIdAndShopId(userId).getUserNumber();
        String configValue = sysConfigNewService.getSysConfigByMasterKeyAndSubKey(EnumSysConfigNew.INVITE_PAGE);
        String h5Url = configValue + inviteNum;
        voUserInviteData.setH5Url(h5Url);
        voUserInviteData.setInviteNum(""+inviteNum);
        return BaseResult.okResult(voUserInviteData);
    }

    /**
     * 获取邀请用户列表
     *
     * @param pageNum
     * @return Result
     */
    @ApiOperation(
            value = "3.获取邀请用户列表",
            notes = "获取邀请用户列表;【V2.2.0】<br/>",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "pageNum", value = "分页页码"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestMapping(value = "/getInviteList", method = RequestMethod.POST)
    public BaseResult<VoInviteList> getInviteList(Integer pageNum) {
        int userId = getUserId();
        VoInviteList voUserInviteData = shpUserService.getInviteList(userId,pageNum);
        return BaseResult.okResult(voUserInviteData);
    }

}
