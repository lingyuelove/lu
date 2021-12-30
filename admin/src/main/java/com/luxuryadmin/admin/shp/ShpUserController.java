package com.luxuryadmin.admin.shp;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserDetail;
import com.luxuryadmin.mapper.op.OpChannelMapper;
import com.luxuryadmin.param.admin.shp.ParamLoginRecord;
import com.luxuryadmin.param.shp.ParamShpUser;
import com.luxuryadmin.service.shp.ShpUserInviteService;
import com.luxuryadmin.service.shp.ShpUserService;

import com.luxuryadmin.vo.op.VoOpChannel;
import com.luxuryadmin.service.shp.ShpUserTokenService;
import com.luxuryadmin.vo.admin.shp.VoShpUserToken;
import com.luxuryadmin.vo.shp.VoShpUser;
import com.luxuryadmin.vo.shp.VoShpUserExcel;
import com.luxuryadmin.vo.shp.VoShpUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ShpUserController
 * @Description TODO
 * @Date 2020/6/22 15:28
 * @Created by Administrator
 */
@Slf4j
@RestController
@RequestMapping(value = "/shp")
@Api(tags = {"1.【用户管理】模块"}, description = "/shp | 用户列表 ")
public class ShpUserController  extends BaseController {

    @Autowired
    private ShpUserService shpUserService;

    @Resource
    private OpChannelMapper opChannelMapper;

    @Autowired
    private ShpUserTokenService shpUserTokenService;
    @Autowired
    private ShpUserInviteService shpUserInviteService;
    @RequestMapping(value = "/listShpUser", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询用户列表;",
            notes = "分页查询用户列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "user:list")
    public BaseResult listShpUser(@Valid ParamShpUser paramShpUser, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramShpUser.getPageNum(), paramShpUser.getPageSize());
        List<VoShpUser> voShpUsers = shpUserService.queryShpUserList(paramShpUser);
        PageInfo pageInfo = new PageInfo(voShpUsers);
        return LocalUtils.getBaseResult(pageInfo);
    }


    @ApiOperation(
            value = "分页查询用户列表;",
            notes = "分页查询用户列表;",
            httpMethod = "GET")
    @RequestMapping(value = "/listLoginRecord", method = RequestMethod.GET)
    public BaseResult listLoginRecord(@Valid ParamLoginRecord paramShpUser, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramShpUser.getPageNum(), PAGE_SIZE_10);
        List<VoShpUserToken> listToken = shpUserTokenService.listShpUserToken(Integer.parseInt(paramShpUser.getUserId()));
        PageInfo pageInfo = new PageInfo(listToken);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @RequestMapping(value = "/exportShpUser", method = RequestMethod.GET)
    @ApiOperation(
            value = "导出查询用户列表;",
            notes = "导出查询用户列表",
            httpMethod = "GET")
    @RequiresPerm(value = "user:export")
    public void exportShpUser(@Valid ParamShpUser paramShpUser, BindingResult result,
                                    HttpServletResponse response) {
        servicesUtil.validControllerParam(result);
        //最多导出5万条记录
        PageHelper.startPage(1,50000);
        List<VoShpUser> voShpUsers = shpUserService.queryShpUserList(paramShpUser);
        List<VoShpUserExcel> voShpUserExcelList = convertVoShpUserToExcel(voShpUsers);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("用户列表-" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), VoShpUserExcel.class).sheet("用户列表").doWrite(voShpUserExcelList);
        } catch (Exception e) {
            throw new MyException(""+e.getMessage());
        }
    }

    /**
     * 将用户列表对象转换为Excel导出对象
     * @param voShpUsers
     * @return
     */
    private List<VoShpUserExcel> convertVoShpUserToExcel(List<VoShpUser> voShpUsers) {
        List<VoShpUserExcel> excelList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(voShpUsers)){
            for (VoShpUser voShpUser : voShpUsers) {
                VoShpUserExcel userExcel = new VoShpUserExcel();
                BeanUtils.copyProperties(voShpUser,userExcel);
                excelList.add(userExcel);
            }
        }

        return excelList;
    }


    @RequestMapping(value = "/offShpUser", method = RequestMethod.GET)
    @ApiOperation(
            value = "禁用/启用用户;",
            notes = "禁用/启用用户;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "禁用用户的id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "state", value = "禁用-0,启用-1"),
    })
    @RequiresPerm(value = "user:update")
    public BaseResult offShpUser(@RequestParam String id,String state) {
        ShpUser shpUser = shpUserService.getObjectById(id);
        if(null==shpUser){
            return BaseResult.defaultErrorWithMsg("【" + id + "】用户不存在存在!");
        }
        shpUser.setState(state);
        shpUserService.updateShpUser(shpUser);
        return BaseResult.okResult();
    }

    @RequestMapping(value = "/getShpUserDetail", method = RequestMethod.GET)
    @ApiOperation(
            value = "获取用户身份信息;",
            notes = "获取用户身份信息;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "获取用户身份信息的id"),
    })
    public BaseResult getShpUserDetail(@RequestParam String id) {
        ShpUserDetail shpUserDetail = shpUserService.selectDetailById(id);
        return LocalUtils.getBaseResult(shpUserDetail);
    }

    @RequestMapping(value = "/getShpUserInfo", method = RequestMethod.GET)
    @ApiOperation(
            value = "获取用户详情信息;",
            notes = "获取用户详情信息;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "获取用户详情信息的id"),
    })
    @RequiresPerm(value = "user:detail")
    public BaseResult getShpUserInfo(@RequestParam String id) {
        int shopId = getShopId();
        VoShpUserInfo shpUserInfo = shpUserService.selectInfoById(shopId,id);
        return LocalUtils.getBaseResult(shpUserInfo);
    }

    @RequestMapping(value = "/listAllOpChannel", method = RequestMethod.GET)
    @ApiOperation(
            value = "获取所有渠道;",
            notes = "获取所有渠道;",
            httpMethod = "GET")
    public BaseResult<List<VoOpChannel>> listAllOpChannel() {
        List<VoOpChannel> list = opChannelMapper.selectAllOpChannel();
        return LocalUtils.getBaseResult(list);
    }

    @RequestMapping(value = "/addOrUpdateCensus", method = RequestMethod.GET)
    @ApiOperation(
            value = "获取所有渠道;",
            notes = "获取所有渠道;",
            httpMethod = "GET")
    public BaseResult addOrUpdateCensus() {
        shpUserInviteService.addOrUpdateCensus();
        return BaseResult.okResult();
    }

}
