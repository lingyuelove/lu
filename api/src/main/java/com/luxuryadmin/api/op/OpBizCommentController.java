package com.luxuryadmin.api.op;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpBizComment;
import com.luxuryadmin.param.op.ParamBizComment;
import com.luxuryadmin.service.op.OpBizCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * @author monkey king
 * @date 2020-01-14 15:48:47
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"D002.【我的】模块"}, description = "/shop/user |意见反馈")
public class OpBizCommentController extends BaseController {

    @Autowired
    private OpBizCommentService opBizCommentService;

    /**
     * 意见反馈
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "意见反馈",
            notes = "意见反馈",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),

    })
    @RequestRequire
    @RequestMapping("/feedback")
    public BaseResult feedback(@RequestParam Map<String, String> params,
                               @Valid ParamBizComment paramComment, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String key = "init_feedback_" + getShopId() + "_" + getUserId();
        String value = redisUtil.get(key);
        if (!LocalUtils.isEmptyAndNull(value)) {
            return BaseResult.defaultErrorWithMsg("你才刚刚反馈过意见~休息一会再来吧！");
        }
        redisUtil.setExMINUTES(key, getIpAddr(), 10);
        //deviceId,apiVersion,appVersion,timestamp,platform,phoneType,phoneSystem,netType,channel
        OpBizComment comment = new OpBizComment();
        comment.setPlatform(paramComment.getPlatform().toLowerCase());
        comment.setFkShpUserId(getUserId());
        comment.setContent(paramComment.getContent());
        comment.setIp(getIpAddr());
        comment.setReplyStatus("0");
        comment.setInsertTime(new Date());
        int row = opBizCommentService.saveOpBizComment(comment);
        return BaseResult.okResult(row);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            System.out.println(System.currentTimeMillis());
        }
    }
}
