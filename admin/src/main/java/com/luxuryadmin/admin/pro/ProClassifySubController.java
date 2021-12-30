package com.luxuryadmin.admin.pro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.PinYinHead;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.param.pro.ParamProClassifySubAdd;
import com.luxuryadmin.param.pro.ParamProClassifySubQuery;
import com.luxuryadmin.param.pro.ParamProClassifySubUpdate;
import com.luxuryadmin.param.shp.ParamUploadShopValid;
import com.luxuryadmin.service.pro.ProClassifySubService;
import com.luxuryadmin.vo.pro.VoProClassifySub;
import com.luxuryadmin.vo.pro.VoProClassifySubPageForAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @Author:     Mong
 * @Date:    2021/5/27 19:03
 * @Description:  商品二级分类控制层
 */
@Slf4j
@RequestMapping(value = "/shop/admin/pro", method = RequestMethod.POST)
@RestController
@Api(tags = {"1.1.【二级分类】品牌模块----2.6.2"}, description = "/shop/user/pro |二级分类品牌模块相关")
public class ProClassifySubController extends BaseController {

    @Autowired
    private ProClassifySubService proClassifySubService;


    @RequestMapping(value = "/listProClassifySubPage", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询二级分类列表;----2.6.2",
            notes = "分页查询二级分类列表;----2.6.2",
            httpMethod = "GET")
    @RequiresPerm(value = "public:classify:sub")
    public BaseResult listProClassifySubPage(@Valid ParamProClassifySubQuery paramQuery, BindingResult result){
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(paramQuery.getPageNum())){
            paramQuery.setPageNum("1");
        }
        //根据一级分类id查询二级分类
        VoProClassifySubPageForAdmin classifySubPageForAdmin=proClassifySubService.getClassifySubPageForAdmin(paramQuery);
        return  BaseResult.okResult(classifySubPageForAdmin);
    }


    @ApiOperation(
            value = "添加商品二级分类;----2.6.2",
            notes = "添加商品二级分类;----2.6.2",
            httpMethod = "POST")
    @RequestMapping(value = "/addProClassifySub", method = RequestMethod.POST)
    public BaseResult addProClassifySub(@Valid ParamProClassifySubAdd paramAdd,BindingResult result){
        servicesUtil.validControllerParam(result);
        //根据名称查询分类是否存在
        VoProClassifySub voProClassifySub=proClassifySubService.getProClassifySubByNameForAdmin(paramAdd.getName());
        if (voProClassifySub!=null){
            return BaseResult.defaultErrorWithMsg("该分类已存在!");
        }

        proClassifySubService.addProClassifySub(paramAdd);
        return BaseResult.okResult();
    }



    @ApiOperation(
            value = "修改商品二级分类;----2.6.2",
            notes = "修改商品二级分类;----2.6.2",
            httpMethod = "POST")
    @RequestMapping(value = "/updateProClassifySub", method = RequestMethod.POST)
    public BaseResult updateProClassifySub(@Valid ParamProClassifySubUpdate paramUpdate, BindingResult result){
        servicesUtil.validControllerParam(result);

        proClassifySubService.updateProClassifySub(paramUpdate);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "修改商品二级分类显示隐藏;----2.6.2",
            notes = "修改商品二级分类显示隐藏;----2.6.2",
            httpMethod = "POST")
    @RequestMapping(value = "/updateProClassifySubForState", method = RequestMethod.POST)
    public BaseResult updateProClassifySubForState(@Valid ParamProClassifySubUpdate paramUpdate, BindingResult result){
        servicesUtil.validControllerParam(result);

        proClassifySubService.updateProClassifySubForState(paramUpdate);
        return BaseResult.okResult();
    }
    @ApiOperation(
            value = "删除商品二级分类;----2.6.2",
            notes = "删除商品二级分类;----2.6.2",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "二级分类id"),
    })
    @RequestMapping(value = "/deleteProClassifySub", method = RequestMethod.POST)
    public BaseResult deleteProClassifySub(@RequestParam String id){
        proClassifySubService.deleteProClassifySub(id);
        return BaseResult.okResult();
    }



    @ApiOperation(
            value = "根据id查询商品二级分类;----2.6.2",
            notes = "根据id查询商品二级分类;----2.6.2",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "二级分类id"),
    })
    @RequestMapping(value = "/getProClassifySubById", method = RequestMethod.POST)
    public BaseResult getProClassifySubById(@RequestParam String id){
        VoProClassifySub voProClassifySub=proClassifySubService.getProClassifySubById(id);
        return BaseResult.okResult(voProClassifySub);
    }
    /**
     * 品牌图片上传
     *
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "品牌图片上传; ----2.6.2",
            notes = "品牌图片上传;----2.6.2",
            httpMethod = "POST")
    @PostMapping("/classifySubImg")
    public BaseResult uploadImg( HttpServletRequest request) throws Exception {

        StringBuffer dirName = new StringBuffer();
        dirName.append("classifySub");
        dirName.append("/").append(DateUtil.format(new Date(), "YYYYMMdd"));
        dirName.append("/");
        String filePath = OSSUtil.uploadBaseMethod(request, dirName.toString(), 10);
        return BaseResult.okResult(filePath);
    }
}
