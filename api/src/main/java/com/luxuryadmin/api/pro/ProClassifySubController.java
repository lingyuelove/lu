package com.luxuryadmin.api.pro;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProClassifySubService;
import com.luxuryadmin.vo.pro.VoProClassifySub;

import com.vdurmont.emoji.EmojiParser;
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
import java.util.List;

/**
 * @Author:     Mong
 * @Date:    2021/5/27 19:03
 * @Description:  商品二级分类控制层
 */
@Slf4j
@RequestMapping(value = "/shop/user/classify/sub", method = RequestMethod.POST)
@RestController
@Api(tags = {"E002.1【二级分类】模块----2.5.4---mong"}, description = "/shop/user/classify/sub |二级分类模块相关")
public class ProClassifySubController extends BaseController {

    @Autowired
    private ProClassifySubService proClassifySubService;
    @ApiOperation(value = "加载二级分类---2.5.4---mong",
            notes = "加载二级分类；根据商铺及一级分类加载",
            httpMethod = "POST"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestMapping("listProClassifySub")
    public BaseResult listProClassifySub( @Valid ParamProClassifySubQuery paramProClassifySubQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        paramProClassifySubQuery.setShopId(getShopId());
        //查询店铺和一级分类下所有二级分类，按首字母进行匹配
        List<VoProClassifySub> classifySubs = proClassifySubService.listAllProClassifySub(paramProClassifySubQuery);
        return BaseResult.okResult(classifySubs);
    }

    @ApiOperation(
            value = "添加商品二级分类;----2.5.4---mong",
            notes = "添加商品二级分类;----2.5.4---mong",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestMapping(value = "/addProClassifySub", method = RequestMethod.POST)
    public BaseResult addProClassifySub(@Valid ParamClassifySubAddForApi paramAdd, BindingResult result){
        servicesUtil.validControllerParam(result);
        //对emoji表情的处理
        paramAdd.setName(EmojiParser.parseToAliases(paramAdd.getName()));
        //根据名称查询分类是否存在
        VoProClassifySub voProClassifySub=proClassifySubService.getProClassifySubByName(paramAdd.getName(),getShopId());
        if (voProClassifySub!=null){
            return BaseResult.defaultErrorWithMsg("该分类已存在!");
        }
//        EmojiParser.parseToAliases(paramAdd.getName());

        paramAdd.setShopId(getShopId());
        paramAdd.setUserId(getUserId());
        proClassifySubService.addProClassifySubForApi(paramAdd);
        return BaseResult.okResult();
    }



//    @ApiOperation(
//            value = "修改商品二级分类;----2.5.4---mong",
//            notes = "修改商品二级分类;----2.5.4---mong",
//            httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    paramType = "query", required = true, dataType = "String",
//                    name = "token", value = "登录token"),
//    })
//    @RequestMapping(value = "/updateProClassifySub", method = RequestMethod.POST)
//    public BaseResult updateProClassifySub(@Valid ParamClassifySubUpdateForApi classifySubUpdateForApi, BindingResult result){
//        servicesUtil.validControllerParam(result);
//        //根据名称查询分类是否存在
//        VoProClassifySub voProClassifySub=proClassifySubService.getProClassifySubByName(classifySubUpdateForApi.getName(),getShopId());
//        if (voProClassifySub!=null){
//            return BaseResult.defaultErrorWithMsg("该分类已存在!");
//        }
//        classifySubUpdateForApi.setShopId(getShopId());
//        classifySubUpdateForApi.setUserId(getUserId());
//        proClassifySubService.updateProClassifySubForApi(classifySubUpdateForApi);
//        return BaseResult.okResult();
//    }


    @ApiOperation(
            value = "删除商品二级分类;----2.5.4---mong",
            notes = "删除商品二级分类;----2.5.4---mong",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "二级分类id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestMapping(value = "/deleteProClassifySub", method = RequestMethod.POST)
    public BaseResult deleteProClassifySub(@RequestParam String id){
        VoProClassifySub classifySub = proClassifySubService.getProClassifySubById(id);
        if (classifySub == null){
            return BaseResult.errorResult("暂无此二级分类");
        }
        if (classifySub != null && "0".equals(classifySub.getType())){
            return BaseResult.errorResult("此二级分类为系统设置，所以不可删");
        }
        proClassifySubService.deleteProClassifySub(id);
        return BaseResult.okResult();
    }

}
