package com.luxuryadmin.admin.op;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.op.ParamBizCommentQuery;
import com.luxuryadmin.param.shp.ParamShpUser;
import com.luxuryadmin.service.op.OpBizCommentService;
import com.luxuryadmin.vo.op.VoOpBizComment;
import com.luxuryadmin.vo.shp.VoShpUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Classname OpBizCommentController
 * @Description TODO
 * @Date 2020/6/24 10:00
 * @Created by Administrator
 */
@Slf4j
@RestController
@RequestMapping(value = "/op")
@Api(tags = {"1.【用户管理】模块"}, description = "/op/biz | 意见反馈 ")
public class OpBizCommentController  extends BaseController {

    @Autowired
    private OpBizCommentService opBizCommentService;

    @RequestMapping(value = "/listOpBizComent", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询意见反馈;",
            notes = "分页查询意见反馈;",
            httpMethod = "GET")
    @RequiresPerm(value = "user:opBiz")
    public BaseResult listOpBizComent(@Valid ParamBizCommentQuery paramBizCommentQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramBizCommentQuery.getPageNum(), paramBizCommentQuery.getPageSize());
        List<VoOpBizComment> voOpBizComments = opBizCommentService.queryOpBizCommentList(paramBizCommentQuery);
        PageInfo pageInfo = new PageInfo(voOpBizComments);
        return LocalUtils.getBaseResult(pageInfo);
    }


}
