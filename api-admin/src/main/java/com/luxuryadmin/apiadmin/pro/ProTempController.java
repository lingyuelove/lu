package com.luxuryadmin.apiadmin.pro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProTemp;
import com.luxuryadmin.param.pro.ParamProTempCreate;
import com.luxuryadmin.param.pro.ParamProTempId;
import com.luxuryadmin.param.pro.ParamProTempQuery;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProTempService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.VoProTemp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * 临时仓
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin/pro")
@Api(tags = {"C004.【临时仓】模块-----2.5.2---mong"}, description = "/shop/user/pro |临时仓】模块相关")
public class ProTempController extends ProProductBaseController {

    @Autowired
    private ProTempService proTempService;

    @Autowired
    private OrdOrderService ordOrderService;

    /**
     * 加载【加载临时仓】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载临时仓",
            notes = "加载临时仓",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listProTemp")
    @RequiresPermissions("pro:check:temp")
    public BaseResult listProTemp(@RequestParam Map<String, String> params,
                                  @Valid ParamProTempQuery paramQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(getPageNum(), paramQuery.getPageSize());
        List<VoProTemp> proTempList = proTempService.listVoProTemp(getShopId());
        if (!LocalUtils.isEmptyAndNull((proTempList))) {
            ArrayList<String> tempIdList = new ArrayList<>();
            for (VoProTemp temp : proTempList) {
                tempIdList.add(temp.getTempId());
            }
            int shopId = getShopId();
            Object[] tempIdArray = tempIdList.toArray();
            List<VoProTemp> totalNumList = proTempService.countTempProductTotalNum(shopId, tempIdArray);
            List<VoProTemp> saleOutMap = proTempService.countTempProductSaleOutNum(shopId, tempIdArray);
            for (VoProTemp temp : proTempList) {
                String tempId = temp.getTempId();
                for (VoProTemp totalNum : totalNumList) {
                    if (tempId.equals(totalNum.getTempId())) {
                        temp.setProTotalNum(totalNum.getProTotalNum());
                    }
                }
                //判断版本控制是否在2.5.2或以上版本
                //临时仓售卖商品数量
                Integer saleTempNum = ordOrderService.getOrderNumByTemp(getShopId(), null, Integer.parseInt(tempId));
                if (saleTempNum != null) {
                    temp.setSaleNum(saleTempNum.toString());
                } else {
                    temp.setSaleNum("0");
                }
                //判断redis是否有默认价格类型的key
                if (redisUtil.hasKey(ConstantRedisKey.getProDefaultRequest(temp.getTempId()))) {
                    temp.setDefaultRequest(redisUtil.get(ConstantRedisKey.getProDefaultRequest(temp.getTempId())));
                } else {
                    //设置默认价格类型
                    temp.setDefaultRequest("tradePrice");
                }


            }
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(proTempList);
        String vid = LocalUtils.getUUID();
        String proTempKey = ConstantRedisKey.getCreateProTempKey(getShopId(), getUserId());
        redisUtil.setExMINUTES(proTempKey, vid, 120);
        hashMap.put("vid", vid);
        hashMap.put("totalSize", new PageInfo(proTempList).getTotal());
        return BaseResult.okResult(hashMap);
    }

    /**
     * 创建临时仓库
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "创建临时仓库",
            notes = "创建临时仓库",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/createProTemp")
    @RequiresPermissions("pro:temp:add")
    public BaseResult createProTemp(@RequestParam Map<String, String> params,
                                    @Valid ParamProTempCreate paramCreate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String proTempKey = ConstantRedisKey.getCreateProTempKey(getShopId(), getUserId());
        servicesUtil.validVid(proTempKey, paramCreate.getVid());
        ProTemp proTemp = new ProTemp();
        proTemp.setFkShpShopId(getShopId());
        proTemp.setState("10");
        proTemp.setName(paramCreate.getName());
        proTemp.setInsertTime(new Date());
        proTemp.setInsertAdmin(getUserId());
        proTempService.createProTemp(proTemp);
        redisUtil.delete(proTempKey);
        return BaseResult.okResult();
    }

    /**
     * 删除临时仓
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除临时仓",
            notes = "创建临时仓库",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/deleteProTemp")
    @RequiresPermissions("pro:temp:add")
    public BaseResult deleteProTemp(@RequestParam Map<String, String> params,
                                    @Valid ParamProTempId proTempId, BindingResult result) {
        servicesUtil.validControllerParam(result);
        proTempService.deleteProTemp(getShopId(), Integer.parseInt(proTempId.getProTempId()));
        return BaseResult.okResult();
    }


}
