package com.luxuryadmin.api.fin;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.api.shp.ShpUserBaseController;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.fin.ParamAlreadyCreate;
import com.luxuryadmin.param.fin.ParamCreateSalaryQuery;
import com.luxuryadmin.param.fin.ParamDeleteSalaryDetail;
import com.luxuryadmin.param.fin.ParamSalaryDetailQuery;
import com.luxuryadmin.param.shp.ParamEmployee;
import com.luxuryadmin.service.fin.FinSalaryDetailService;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.fin.VoCreateSalaryDetail;
import com.luxuryadmin.vo.shp.VoEmployee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 薪资提成明细--管理控制层
 *
 * @author monkey king
 * @date 2020-09-24 00:16:54
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/fin", method = RequestMethod.POST)
@Api(tags = "F001.【财务】模块", description = "/shop/user/fin | 财务模块")
public class FinSalaryDetailController extends ShpUserBaseController {


    @Autowired
    private FinSalaryService finSalaryService;

    @Autowired
    private FinSalaryDetailService finSalaryDetailService;


    /**
     * 获取员工薪资详情<br/>
     * 1.先获取查询月份是否有已生成的薪资;
     * 2.如果当前月份还未创建薪资,则获取该用户最新一条薪资明细的方案;
     * 3.如果该用户从未创建过薪资,则获取该店铺最新创建一条薪资明细的方案;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取员工薪资详情",
            notes = "获取员工薪资详情",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/getEmployeeSalaryDetail")
    public BaseResult getEmployeeSalaryDetail(@RequestParam Map<String, String> params,
                                              @Valid ParamSalaryDetailQuery salaryDetailQuery, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);
        //查看的
        int shopId = getShopId();
        int userId = getUserId();
        int queryUserId = Integer.parseInt(salaryDetailQuery.getUserId());
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_SALARY);
        if (queryUserId != userId && !hasPerm) {
            //没有查看全部薪资的权限,只能查看个人薪资
            return BaseResult.defaultErrorWithMsg("无查看他人薪资权限!");
        }
        Map<String, Object> hashMap = new HashMap<>(16);

        String st = salaryDetailQuery.getStartDate();
        String et = salaryDetailQuery.getEndDate();
        hashMap.put("proAttr", salaryDetailQuery.getProAttr());
        hashMap.put("createState", "0");
        hashMap.put("salaryState", "0");
        salaryDetailQuery.setLocalUserId(getUserId());
        salaryDetailQuery.setShopId(getShopId());
        VoCreateSalaryDetail fd = finSalaryDetailService.getCreateSalaryDetail(shopId, queryUserId, st);
        String salaryState = "0";
        if (fd != null) {
            salaryState = fd.getSalaryState();
            hashMap.put("createState", "1");
            hashMap.put("salaryState", salaryState);
        } else {
            fd = finSalaryDetailService.getOwnLastCreateSalaryDetail(shopId, queryUserId);
            if (fd == null) {
                fd = finSalaryDetailService.getShopLastCreateSalaryDetail(shopId);
            }
        }
        //薪资还没发放,需要实时更新
        if (ConstantCommon.ZERO.equals(salaryState)) {
            //分开获取该接口信息;先获取所有的提成统计,再获取该员工的提成方案明细
            hashMap.putAll(finSalaryDetailService.getSalaryDetail(salaryDetailQuery, result));
        } else {
            hashMap.putAll(JSONObject.parseObject(fd.getSaleResultJson()));
        }

        if (fd != null) {
            //把用户选择的属性返回给用户
            fd.setProAttr(salaryDetailQuery.getProAttr());
            Map<String, Object> map = LocalUtils.convertBeanToMap(fd);
            map.remove("salaryState");
            hashMap.putAll(map);
        }
        List<Integer> list = finSalaryService.listAlreadyCreateSalaryUserId(shopId, st, et);
        hashMap.put("alreadyCreateSalaryUser", list);
        return BaseResult.okResult(hashMap);
    }

    /**
     * 获取已经生成薪资的用户<br/>
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取已经生成薪资的用户",
            notes = "获取已经生成薪资的用户",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/loadAlreadyCreateSalaryUser")
    public BaseResult loadAlreadyCreateSalaryUser(@RequestParam Map<String, String> params,
                                                  @Valid ParamAlreadyCreate param, BindingResult result) {
        //分开获取该接口信息;先获取所有的提成统计,再获取该员工的提成方案明细
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        String st = param.getStartDate();
        String et = param.getEndDate();
        List<Integer> list = finSalaryService.listAlreadyCreateSalaryUserId(shopId, st, et);
        return LocalUtils.getBaseResult(list);
    }

    /**
     * 创建薪资<br/>
     * 1.必须为本店铺员工;
     * 2.每个月只能有一条薪资记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "创建薪资",
            notes = "创建薪资",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestMapping("/createSalary")
    @RequiresPermissions("fin:check:AllSalary")
    public BaseResult createSalary(@RequestParam Map<String, String> params,
                                   @Valid ParamCreateSalaryQuery param, BindingResult result, HttpServletRequest request) throws Exception {
        servicesUtil.validControllerParam(result);
        param.setShopId(getShopId());
        param.setLocalUserId(getUserId());
        BigDecimal finalMoney = finSalaryDetailService.createSalary(param, result, request);

        return BaseResult.okResult(finalMoney);
    }


    /**
     * 删除薪资明细
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除薪资明细",
            notes = "删除薪资明细",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/deleteSalaryDetail")
    @RequiresPermissions("fin:check:AllSalary")
    public BaseResult deleteSalaryDetail(@RequestParam Map<String, String> params,
                                         @Valid ParamDeleteSalaryDetail param, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        int userId = Integer.parseInt(param.getUserId());
        String st = param.getStartDate() + " 00:00:00";
        finSalaryDetailService.deleteSalaryDetail(getShopId(), userId, st, request);
        return BaseResult.okResult();
    }


    public static void main(String[] args) throws ParseException {
        Date startDate = DateUtil.parseShort("2020-12-27");
        Calendar stCalendar = Calendar.getInstance();
        stCalendar.setTime((startDate));
        stCalendar.set(Calendar.DAY_OF_MONTH, 1);

        System.out.println(stCalendar.getTime());

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(now.getTime());

        System.out.println(stCalendar.after(now));
    }

}
