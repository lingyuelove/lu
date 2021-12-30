package com.luxuryadmin.api.fin;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.api.shp.ShpUserBaseController;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.fin.ParamSalaryQuery;
import com.luxuryadmin.param.shp.ParamEmployee;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.fin.VoFinSalary;
import com.luxuryadmin.vo.shp.VoEmployee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.*;

/**
 * 薪资管理控制层
 *
 * @author monkey king
 * @date 2020-09-24 00:16:54
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/fin", method = RequestMethod.POST)
@Api(tags = "F001.【财务】模块", description = "/shop/user/fin | 薪资管理")
@RequiresPermissions(value = {"fin:check:salary", "fin:check:AllSalary"}, logical = Logical.OR)
public class FinSalaryController extends ShpUserBaseController {


    @Autowired
    private FinSalaryService finSalaryService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    /**
     * 初始化薪资明细表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化薪资明细表",
            notes = "初始化薪资明细表",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @RequestMapping("/initSalaryDetail")
    public BaseResult initSalaryDetail(@RequestParam Map<String, String> params) {
        ParamEmployee paramEmployee = new ParamEmployee();
        paramEmployee.setShopId(getShopId());
        paramEmployee.setState("1");
        List<VoEmployee> voEmployeeList = shpUserShopRefService.listUserShopRefByShopId(paramEmployee);
        formatVoEmployee(voEmployeeList);
        return LocalUtils.getBaseResult(voEmployeeList);
    }

    /**
     * 加载薪资管理列表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载薪资管理列表",
            notes = "加载薪资管理列表",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/loadSalary")
    public BaseResult loadSalary(@RequestParam Map<String, String> params,
                                 @Valid ParamSalaryQuery salaryQuery, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);
        //是否有查看全部薪资的权限
        boolean hasPerm = hasPermWithCurrentUser(ConstantPermission.CHK_ALL_SALARY);
        //if (!hasPerm) {
        //    //没有查看全部薪资的权限,只能查看个人薪资
        //    salaryQuery.setUserId(getUserId() + "");
        //    //没有查看全部薪资的权限,只能查看个人薪资
        //    return BaseResult.defaultErrorWithMsg("无查看他人薪资权限!");
        //}

        String queryUserId = salaryQuery.getUserId();
        //没有查看全部薪资的权限,只能查看个人薪资
        if (LocalUtils.isEmptyAndNull(queryUserId) && !hasPerm) {
            salaryQuery.setUserId(getUserId() + "");
        }
        if (!(getUserId() + "").equals(salaryQuery.getUserId()) && !hasPerm) {
            return BaseResult.defaultErrorWithMsg("无查看他人薪资权限!");
        }

        int shopId = getShopId();
        //动态刷新薪资
        finSalaryService.realTimeFinSalary(shopId, salaryQuery.getStartDate(), salaryQuery.getEndDate());

        salaryQuery.setShopId(shopId);
        //查询条件的薪资总额
        PageHelper.startPage(getPageNum(), PAGE_SIZE_1000);
        List<VoFinSalary> salaryList = finSalaryService.listFinSalaryByDate(salaryQuery);

        if (LocalUtils.isEmptyAndNull(salaryList)) {
            return BaseResult.okResultNoData();
        }
        Long salaryMoney = finSalaryService.countSalaryMoney(salaryQuery);
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<Integer, Integer> userIdMap = new HashMap<>(16);
        for (VoFinSalary salary1 : salaryList) {

            int userId1 = salary1.getUserId();
            //去重
            Integer id = userIdMap.get(userId1);
            if (id != null) {
                continue;
            }
            userIdMap.put(userId1, userId1);
            List<VoFinSalary> subFinSalary = new ArrayList<>();
            String headImgUrl = "";
            Integer userId = null;
            String userTypeName = "";
            String nickname = "";
            long userSalaryMoney = 0;
            for (VoFinSalary salary2 : salaryList) {
                int userId2 = salary2.getUserId();
                if (userId1 == userId2) {
                    userSalaryMoney = LocalUtils.calcNumber(userSalaryMoney, "+", salary2.getSalaryMoney()).longValue();
                    headImgUrl = salary2.getHeadImgUrl();
                    userId = salary2.getUserId();
                    userTypeName = salary2.getUserTypeName();
                    nickname = salary2.getNickname();
                    VoFinSalary salary = new VoFinSalary();
                    salary.setSalaryId(salary2.getSalaryId());
                    salary.setSalaryState(salary2.getSalaryState());
                    salary.setSalaryMoney(salary2.getSalaryMoney());
                    salary.setSalaryName(salary2.getSalaryName());
                    salary.setInsertTime(salary2.getInsertTime());
                    subFinSalary.add(salary);
                }
            }
            HashMap<String, Object> myMap = new HashMap<>(16);
            myMap.put("headImgUrl", servicesUtil.formatImgUrl(headImgUrl));
            myMap.put("nickname", nickname);
            myMap.put("userSalaryMoney", userSalaryMoney);
            myMap.put("userId", userId);
            myMap.put("userTypeName", userTypeName);
            myMap.put("list", subFinSalary);
            list.add(myMap);
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("salaryMoney", salaryMoney);
        hashMap.put("salaryList", list);
        return BaseResult.okResult(hashMap);
    }


}
