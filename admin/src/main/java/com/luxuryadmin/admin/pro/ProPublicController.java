package com.luxuryadmin.admin.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProClassifySubService;
import com.luxuryadmin.service.pro.ProPublicService;
import com.luxuryadmin.vo.pro.VoProClassifySub;
import com.luxuryadmin.vo.pro.VoProPublic;
import com.luxuryadmin.vo.pro.VoProPublicByPageForAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 公价查询控制层
 *
 * @author monkey king
 * @Date 2021-06-22 23:32:05
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin/public")
@Api(tags = {"1.5.商品模板接口----2.6.2"}, description = "/shop/admin/public |商品模板接口")
public class ProPublicController extends BaseController {


    @Autowired
    private ProPublicService proPublicService;


    @Autowired
    private ProClassifySubService proClassifySubService;

    /**
     * 初始化公价查询系统参数
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化公价查询系统参数",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/initPublicProduct")
    public BaseResult<List<VoProClassifySub>> initPublicProduct(@RequestParam Map<String, String> params,
                                        @Valid ParamProClassifySubQuery paramProClassifySubQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //查询店铺和一级分类下所有二级分类，按首字母进行匹配
        List<VoProClassifySub> classifySubs = proClassifySubService.listProClassifySubPage(paramProClassifySubQuery);
        return BaseResult.okResult(classifySubs);
    }

    /**
     * 获取公价系统商品
     *
     * @param publicForAdmin 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取公价系统商品",
            httpMethod = "POST")
    @PostMapping("/getPublicByPageForAdmin")
    @RequiresPerm(value = "public:product:list")
    public BaseResult<VoProPublicByPageForAdmin> getPublicByPageForAdmin(@Valid ParamPublicForAdmin publicForAdmin, BindingResult result) {
        servicesUtil.validControllerParam(result);

        VoProPublicByPageForAdmin proPublicList = proPublicService.getPublicByPageForAdmin(publicForAdmin);

        return BaseResult.okResult(proPublicList);
    }


    @ApiOperation(
            value = "添加公价系统商品;----2.6.2",
            notes = "添加公价系统商品;----2.6.2",
            httpMethod = "POST")
    @RequestMapping(value = "/addPublicForAdmin", method = RequestMethod.POST)
    public BaseResult addPublicForAdmin(@Valid ParamPublicAddForAdmin publicAddForAdmin, BindingResult result){
        servicesUtil.validControllerParam(result);
        //根据名称查询分类是否存在
//        ParamPublicForAdmin publicForAdmin =new ParamPublicForAdmin();
//        VoProClassifySub voProClassifySub=proPublicService.getPublicByPageForAdmin(publicAddForAdmin.getName());
//        if (voProClassifySub!=null){
//            return BaseResult.defaultErrorWithMsg("该分类已存在!");
//        }

        proPublicService.addPublicForAdmin(publicAddForAdmin);
        return BaseResult.okResult();
    }



    @ApiOperation(
            value = "修改公价系统商品;----2.6.2",
            notes = "修改公价系统商品;----2.6.2",
            httpMethod = "POST")
    @RequestMapping(value = "/updatePublicForAdmin", method = RequestMethod.POST)
    public BaseResult updatePublicForAdmin(@Valid ParamPublicUpdateForAdmin publicUpdateForAdmin, BindingResult result){
        servicesUtil.validControllerParam(result);

        proPublicService.updatePublicForAdmin(publicUpdateForAdmin);
        return BaseResult.okResult();
    }
    @ApiOperation(
            value = "删除公价系统商品;----2.6.2",
            notes = "删除公价系统商品;----2.6.2",
            httpMethod = "POST")
    @RequestMapping(value = "/deletePublicForAdmin", method = RequestMethod.POST)
    public BaseResult deletePublicForAdmin(@Valid ParamPublicByDeleteForAdmin publicByDeleteForAdmin, BindingResult result){
        servicesUtil.validControllerParam(result);

        proPublicService.deletePublicForAdmin(publicByDeleteForAdmin.getId());
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "商品图片上传; ----2.6.2",
            notes = "商品图片上传;----2.6.2",
            httpMethod = "POST")
    @PostMapping("/publicImg")
    public BaseResult uploadImg( HttpServletRequest request) throws Exception {

        StringBuffer dirName = new StringBuffer();
        dirName.append("publicImg");
        dirName.append("/").append(DateUtil.format(new Date(), "YYYYMMdd"));
        dirName.append("/");
        String filePath = OSSUtil.uploadBaseMethod(request, dirName.toString(), 10);
        return BaseResult.okResult(filePath);
    }
}
