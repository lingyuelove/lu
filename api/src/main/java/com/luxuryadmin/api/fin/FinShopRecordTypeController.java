package com.luxuryadmin.api.fin;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.entity.fin.FinShopRecordType;
import com.luxuryadmin.service.fin.FinShopRecordTypeService;
import com.luxuryadmin.vo.fin.VoFinShopRecordType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺财务流水类型模块Controller
 * sanjin145
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/fin/record/type", method = RequestMethod.POST)
@Api(tags = {"F002.【财务】模块"}, description = "/shop/user/fin/record/type |店铺财务流水类型")
public class FinShopRecordTypeController extends BaseController {

    @Autowired
    private FinShopRecordTypeService finShopRecordTypeService;

    /**
     * 根据店铺ID查询【财务流水类型】列表
     *
     * @return Result
     */
    @ApiOperation(
            value = "查询【财务流水类型】列表;",
            notes = "查询【财务流水类型】列表",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "inoutType", value = "流水进出类型 in|收入 out|支出",allowableValues = "in,out")
    })
    @PostMapping("/listFinShopRecordType")
    public BaseResult<List<VoFinShopRecordType>> listFinShopRecordType(@RequestParam(value="inoutType",required=true) String inoutType) {
        Integer shopId =  getShopId();
        List<VoFinShopRecordType> voOrdTypeList = finShopRecordTypeService.listFinShopRecordTypeByShopId(shopId,inoutType);
        return BaseResult.okResult(voOrdTypeList);
    }

    /**
     * 添加财务流水类型
     *
     * @param finShopRecordTypeName 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加【财务流水类型】;",
            notes = "添加【财务流水类型】",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "finShopRecordTypeName", value = "类型名称"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "inoutType", value = "流水进出类型 in|收入 out|支出",allowableValues = "in,out")
    })
    @RequestRequire
    @PostMapping("/addFinShopRecordType")
    public BaseResult addFinShopRecordType(@RequestParam(value="finShopRecordTypeName",required=true) String finShopRecordTypeName,
                                           @RequestParam(value="inoutType",required=true) String inoutType) {
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        if("全部分类".equals(finShopRecordTypeName)){
            throw new MyException("不能添加名称为【全部分类】的分类类型");
        }
        FinShopRecordType shopRecordType = finShopRecordTypeService.addFinShopRecordType(shopId,userId,finShopRecordTypeName,inoutType);
        return BaseResult.okResult(shopRecordType.getId());
    }

    /**
     * 删除财务流水类型
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "删除【财务流水类型】",
            notes = "删除【财务流水类型】;",
            httpMethod = "POST")
    @RequestMapping("/deleteFinShopRecordType")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) })
    public BaseResult deleteFinShopRecordType(@RequestParam(value="finShopRecordId",required=true) String  finShopRecordId) {
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        Integer result = finShopRecordTypeService.deleteFinShopRecordType(shopId,userId,Integer.parseInt(finShopRecordId));
        return BaseResult.okResult(result);
    }
}
