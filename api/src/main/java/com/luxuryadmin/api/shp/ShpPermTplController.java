package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpPermTpl;
import com.luxuryadmin.entity.shp.ShpUserType;
import com.luxuryadmin.param.shp.ParamShpUserPermAdd;
import com.luxuryadmin.param.shp.ParamShpUserPermUpdate;
import com.luxuryadmin.param.shp.ParamShpUserTypeSort;
import com.luxuryadmin.service.shp.ShpPermTplService;
import com.luxuryadmin.service.shp.ShpUserTypeService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户权限模板--控制层
 *
 * @author monkey king
 * @date 2019-12-30 15:09:24
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"E003.【员工】模块"}, description = "/shop/user |【角色权限】模块的接口 ")
public class ShpPermTplController extends BaseController {

    @Autowired
    private ShpPermTplService shpPermTplService;

    @Autowired
    private ShpUserTypeService shpUserTypeService;

    /**
     * 新增身份模板
     *
     * @return Result
     */
    @ApiOperation(
            value = "新增身份模板",
            notes = "新增身份模板",
            httpMethod = "POST")
    @RequestRequire
    @RequestMapping("/addUserTypeNew")
    public BaseResult addUserTypeNew(@Valid ParamShpUserPermAdd userPerm, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String tplName = userPerm.getTplName();
        boolean exists = shpUserTypeService.existsUserType(getShopId(), tplName);
        if (exists) {
            return BaseResult.defaultErrorWithMsg("该身份名称已存在!");
        }
        ShpUserType userType = shpUserTypeService.packShpUserTypeToSave(getShopId(), getUserId(), tplName, tplName, 999, "");
        Integer userTypeId = shpUserTypeService.saveOrUpdateShpUserTypeAndReturnId(userType);
        return BaseResult.okResult(userTypeId);
    }


    /**
     * 对身份进行排序
     *
     * @return Result
     */
    @ApiOperation(
            value = "对身份进行排序",
            notes = "对身份进行排序",
            httpMethod = "POST")
    @RequestRequire
    @RequestMapping("/sortUserType")
    public BaseResult sortUserType(@Valid ParamShpUserTypeSort userPerm, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String idsStr = userPerm.getIds();
        String[] usedArray = idsStr.split(",");
        List<ShpUserType> list = new ArrayList<>();
        int sort = 1;
        for (String id : usedArray) {
            ShpUserType userType = new ShpUserType();
            userType.setId(Integer.parseInt(id));
            userType.setSort(sort);
            sort++;
            list.add(userType);
        }
        shpUserTypeService.sortUserType(list, getShopId());
        return BaseResult.okResult();
    }

    /**
     * 更新身份权限
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "更新身份权限",
            notes = "更新身份权限",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/updateUserTypePermNew")
    public BaseResult updateUserTypePermNew(@RequestParam Map<String, String> params,
                                            @Valid ParamShpUserPermUpdate userPerm, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int userTypeId = Integer.parseInt(userPerm.getUserTypeId());
        String permIds = userPerm.getPermIds();
        List<ShpPermTpl> permTplList = pickShpPermTpl(userTypeId, permIds);
        shpPermTplService.saveShpPermTpl(getShopId(), userTypeId, permTplList);
        return BaseResult.okResult();
    }

    /**
     * 删除身份模板
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除身份模板",
            notes = "删除身份模板",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/deleteUserTypeNew")
    public BaseResult deleteUserTypeNew(@RequestParam Map<String, String> params,
                                        @Valid ParamShpUserPermUpdate userPerm, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String userTypeIdStr = userPerm.getUserTypeId();
        int userTypeId = Integer.parseInt(userTypeIdStr);
        shpUserTypeService.deleteUserTypeNew(getShopId(), userTypeId);
        return BaseResult.okResult();
    }

    /**
     * 封装ShpUserPermissionRef实体
     *
     * @param userTypeId
     * @param permIds
     * @return
     */
    private List<ShpPermTpl> pickShpPermTpl(int userTypeId, String permIds) {
        if (!LocalUtils.isEmptyAndNull(permIds)) {
            List<ShpPermTpl> list = new ArrayList<>();
            String[] ids = permIds.split(";");
            Date date = new Date();
            for (String permId : ids) {
                ShpPermTpl userPermRef = new ShpPermTpl();
                userPermRef.setFkShpUserTypeId(userTypeId);
                userPermRef.setFkShpPermIndexId(Integer.parseInt(permId));
                userPermRef.setFkShpShopId(getShopId());
                userPermRef.setInsertTime(date);
                userPermRef.setInsertAdmin(getUserId());
                list.add(userPermRef);
            }
            return list;
        }
        return null;
    }

}
