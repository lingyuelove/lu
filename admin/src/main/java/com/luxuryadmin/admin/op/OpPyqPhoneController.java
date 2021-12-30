package com.luxuryadmin.admin.op;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.op.ParamOpPyqPhone;
import com.luxuryadmin.service.op.OpPyqPhoneService;
import com.luxuryadmin.vo.op.VoOpBizComment;
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
public class OpPyqPhoneController extends BaseController {

    @Autowired
    private OpPyqPhoneService opPyqPhoneService;

    @RequestMapping(value = "/importPhone", method = RequestMethod.GET)
    @ApiOperation(
            value = "导入朋友圈推广号码;",
            notes = "导入朋友圈推广号码;",
            httpMethod = "GET")
    public BaseResult importPhone(@Valid ParamOpPyqPhone param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String excelUrl = param.getExcelHttpUrl();
        String remark = param.getRemark();
        String batch = param.getBatch();
        opPyqPhoneService.readExcel(excelUrl, 0, remark, batch);
        return BaseResult.okResult("执行成功");
    }


}
