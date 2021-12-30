package com.luxuryadmin.api.org;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.entity.org.OrgTempSeat;
import com.luxuryadmin.param.org.ParamTempSeatAddOrUpdate;
import com.luxuryadmin.service.org.OrgTempSeatService;
import com.luxuryadmin.vo.org.VoTempSeatList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 店铺机构仓排序位置分组表
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/org/temp", method = RequestMethod.POST)
@Api(tags = "C0041.【机构临时仓】模块", description = "/shop/user/org/temp | 机构临时仓管理")
public class OrgTempSeatController extends BaseController {

    @Autowired
    private OrgTempSeatService tempSeatService;

    /**
     * 添加机构仓排序位置
     *
     * @param paramTempSeatAddOrUpdate 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加【展会位置分组】;",
            notes = "添加【展会位置分组】",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/addTempSeat")
    public BaseResult addTempSeat(@Valid ParamTempSeatAddOrUpdate paramTempSeatAddOrUpdate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        OrgTempSeat tempSeat = tempSeatService.getByName(paramTempSeatAddOrUpdate.getName(),paramTempSeatAddOrUpdate.getShopId());
        if (tempSeat != null){
            throw new MyException("此分组已存在");
        }
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        paramTempSeatAddOrUpdate.setInsertAdmin(userId);
        paramTempSeatAddOrUpdate.setShopId(shopId);
        Integer resultNew =tempSeatService.addTempSeat(paramTempSeatAddOrUpdate);
        return BaseResult.okResult(resultNew);
    }

    /**
     * 删除
     *
     * @param id 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除【展会位置分组】;",
            notes = "删除【展会位置分组】",
            httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "主键id"),
    })
    @RequestRequire
    @PostMapping("/deleteTempSeat")
    public BaseResult deleteTempSeat(@RequestParam(value="id",required=true) String id) {

        Integer result =tempSeatService.deleteTempSeat(Integer.parseInt(id));
        return BaseResult.okResult(result);
    }
    /**
     * 机构临时仓排序位置集合显示
     *
     * @return Result
     */
    @ApiOperation(
            value = "展会位置分组集合显示;",
            notes = "展会位置分组置集合显示",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token"),
    })
    @GetMapping("/getShopTempSeat")
    public BaseResult<List<VoTempSeatList>> getShopTempSeat() {
        Integer shopId =  getShopId();
        List<VoTempSeatList> tempSeatLists = tempSeatService.getShopTempSeat(shopId);
        return BaseResult.okResult(tempSeatLists);
    }

}