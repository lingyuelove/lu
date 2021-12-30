package com.luxuryadmin.apiadmin.pro;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.param.pro.ParamClassifySubAddForApi;
import com.luxuryadmin.param.pro.ParamProClassifySubQuery;
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
@RequestMapping(value = "/shop/admin/classify/sub", method = RequestMethod.POST)
@RestController
@Api(tags = {"C004.1【二级分类】品牌模块----2.5.4---mong"}, description = "/shop/user/classify/sub |二级分类模块相关")
public class ProClassifySubController extends BaseController {

    @Autowired
    private ProClassifySubService proClassifySubService;
    @ApiOperation(value = "加载二级分类---2.5.4---mong",
            notes = "加载二级分类；根据商铺及一级分类加载",
            httpMethod = "POST"
    )
    @RequestMapping("listProClassifySub")
    public BaseResult listProClassifySub( @Valid ParamProClassifySubQuery paramProClassifySubQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        paramProClassifySubQuery.setShopId(getShopId());
        //查询店铺和一级分类下所有二级分类，按首字母进行匹配
        List<VoProClassifySub> classifySubs = proClassifySubService.listAllProClassifySub(paramProClassifySubQuery);
        return BaseResult.okResult(classifySubs);
    }


}
