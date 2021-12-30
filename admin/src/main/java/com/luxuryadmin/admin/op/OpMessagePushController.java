package com.luxuryadmin.admin.op;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.enums.op.EnumOpMessageCreateSource;

import com.luxuryadmin.param.op.ParamOpMessageQuery;
import com.luxuryadmin.param.op.ParamOpMessageSave;
import com.luxuryadmin.service.op.OpMessageService;
import com.luxuryadmin.vo.op.VoOpMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname OpProblemController
 * @Description 帮助中心Controller
 * @Date 2020/7/09 10:00
 * @Created by sanjin
 */
@Slf4j
@RestController
@RequestMapping(value = "/op/messagePush")
@Api(tags = {"1.【消息推送】模块"}, description = "/op/messagePush | 【消息推送】 ")
public class OpMessagePushController extends BaseController {

    @Autowired
    private OpMessageService opMessageService;

    @RequestMapping(value = "/addOpMessage", method = RequestMethod.POST)
    @ApiOperation(
            value = "添加【消息推送】计划模板;",
            notes = "添加【消息推送】计划模板;",
            httpMethod = "POST")
    public BaseResult addOpMessage(ParamOpMessageSave paramOpMessageSave) {
        //获取修改者用户ID

        Integer uid  = getAdminUserId();

        try {
            paramOpMessageSave.setCreateSource(EnumOpMessageCreateSource.CMS.getCode());
            opMessageService.addOpMessage(paramOpMessageSave,uid,null,null,null);
        } catch (Exception e) {
            return BaseResult.errorResult("【添加【消息推送】计划模板】失败。errorMsg=" + e.getMessage());
        }

        return BaseResult.okResult();
    }

    @RequestMapping(value = "/listOpMessage", method = RequestMethod.POST)
    @ApiOperation(
            value = "分页查询【消息推送】记录;",
            notes = "分页查询【消息推送】记录;</BR> 只展示后台添加的消息",
            httpMethod = "POST")
    @RequiresPerm(value = "advert:message:list")
    public BaseResult<List<VoOpMessage>> listOpMessage(@Valid ParamOpMessageQuery paramOpMessageQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramOpMessageQuery.getPageNum(), paramOpMessageQuery.getPageSize());

        List<VoOpMessage> opMessageList = opMessageService.listOpMessageForCms(paramOpMessageQuery);
        PageInfo pageInfo = new PageInfo(opMessageList);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @RequestMapping(value = "/loadOpMessageById", method = RequestMethod.POST)
    @ApiOperation(
            value = "根据ID查询【消息推送】记录;",
            notes = "根据ID查询【消息推送】记录;</BR> 只展示后台添加的消息",
            httpMethod = "POST")
    public BaseResult<VoOpMessage> loadOpMessageById(Long msgId) {

        VoOpMessage opMessage = opMessageService.loadOpMessageByIdForCms(msgId);
        return LocalUtils.getBaseResult(opMessage);
    }

    @RequestMapping(value = "/delOpMessage", method = RequestMethod.POST)
    @ApiOperation(
            value = "删除【消息推送】记录;",
            notes = "删除【消息推送】记录;",
            httpMethod = "POST")
    public BaseResult delOpMessage(@RequestBody HashMap<String, String> map){
        String id = map.get("id");
        //获取修改者用户ID
        Integer uid  =getAdminUserId();
        int result = opMessageService.delOpMessage(Long.valueOf(id),uid);
        return LocalUtils.getBaseResult(result);
    }

    @RequestMapping(value = "/updateOpMessage", method = RequestMethod.POST)
    @ApiOperation(
            value = "修改【消息中心】消息;",
            notes = "修改【消息中心】消息;",
            httpMethod = "POST")
    public BaseResult updateOpMessage(@Valid ParamOpMessageSave paramOpMessageSave, BindingResult result){
        if(null == paramOpMessageSave.getId()){
            return BaseResult.errorResult("编辑消息ID不能为空");
        }

        servicesUtil.validControllerParam(result);
        //获取修改者用户ID
        Integer uid  = getAdminUserId();

        //更新
        int id = 0;
        try {
            id = opMessageService.updateOpMessage(paramOpMessageSave,uid);
        } catch (Exception e) {
            return BaseResult.errorResult("【修改【消息推送】计划模板】失败。errorMsg=" + e.getMessage());
        }
        return LocalUtils.getBaseResult(id);
    }

    @RequestMapping(value = "/rightSend", method = RequestMethod.POST)
    @ApiOperation(
            value = "立即发送消息;",
            notes = "立即发送消息;",
            httpMethod = "POST")
    public BaseResult rightSend(Long msgId){
        if(null == msgId){
            return BaseResult.errorResult("发送消息ID不能为空");
        }

        //获取修改者用户ID
        String token =getAdminToken();
        Integer uid  =getAdminUserId();

        //更新
        int id = 0;
        try {
            id = opMessageService.rightSend(msgId,uid,null,null,null);
        } catch (Exception e) {
            return BaseResult.errorResult("【立即发送消息】失败。errorMsg=" + e.getMessage());
        }
        return LocalUtils.getBaseResult(id);
    }



}
