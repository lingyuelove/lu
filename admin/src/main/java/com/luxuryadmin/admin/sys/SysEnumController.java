package com.luxuryadmin.admin.sys;

import java.util.ArrayList;
import java.util.Date;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.sys.SysEnum;
import com.luxuryadmin.enums.sys.EnumTableName;
import com.luxuryadmin.param.shp.ParamUserPermTpl;
import com.luxuryadmin.service.sys.SysEnumService;
import com.luxuryadmin.vo.sys.VoSysEnum;
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
import java.util.Map;

/**
 * 商品分类控制层
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys", method = RequestMethod.POST)
@Api(tags = {"5.【系统管理】模块", "5.【系统管理】模块"})
public class SysEnumController extends BaseController {

    @Autowired
    private SysEnumService sysEnumService;


    /**
     * 重置系统枚举类
     *
     * @param params 前端参数
     * @return Result
     */


    @ApiOperation(
            value = "重置系统枚举类",
            notes = "重置系统枚举类",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "type", value = "表名;all代表全部"),
    })
    @RequestRequire
    @RequestMapping("/resetSysEnum")
    public BaseResult resetSysEnum(@RequestParam Map<String, String> params) {
        String token = getToken();
        String type = params.get("type");
        //校验该帐号是否是店长角色;
        boolean isShopkeeper = true;
        //此处校验权限----预留代码

        if (isShopkeeper) {
            sysEnumService.resetSysEnumByType(getEnumTableName(type));
            return BaseResult.okResult();
        }
        return BaseResult.okResultNoData();
    }


    /**
     * 保存模板权限
     *
     * @param userPermTpl
     * @param result
     * @return
     */
    @ApiOperation(
            value = "保存模板权限",
            notes = "保存模板权限",
            httpMethod = "POST")
    @RequestMapping("/saveTplPerm")
    public BaseResult saveTplPerm(@Valid ParamUserPermTpl userPermTpl, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String permIds = userPermTpl.getPermIds();
        String sort = userPermTpl.getSort();
        String[] permIdList = LocalUtils.splitString(permIds, ";");
        int row = 0;
        if (!LocalUtils.isEmptyAndNull(permIdList)) {
            List<SysEnum> sysEnumList = new ArrayList<>();
            String tplName = userPermTpl.getTplName();
            //添加模板之前,先删除模板旧数据;
            sysEnumService.deleteTplName(tplName);
            int i = 0;
            for (String permId : permIdList) {
                SysEnum sysEnum = new SysEnum();
                sysEnum.setName(tplName);
                sysEnum.setCode(permId);
                sysEnum.setDescription(userPermTpl.getTplName());
                sysEnum.setType("shp_perm_index");
                sysEnum.setState(1);
                sysEnum.setSort(Integer.parseInt(sort));
                sysEnum.setInsertTime(new Date());
                sysEnum.setInsertAdmin(0);
                sysEnum.setUpdateAdmin(0);
                sysEnum.setVersions(1);
                sysEnum.setDel("0");
                sysEnumList.add(sysEnum);
            }
            row = sysEnumService.saveBatch(sysEnumList);
        }
        return BaseResult.okResult(row);
    }


    /**
     * 显示模板权限
     *
     * @param params
     * @return
     */
    @ApiOperation(
            value = "显示模板权限",
            notes = "显示模板权限",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "tplName", value = "模板名称"),
    })
    @RequestMapping("/showTplPerm")
    public BaseResult showTplPerm(@RequestParam Map<String, String> params) {
        String tplName = params.get("tplName");
        List<VoSysEnum> permIdList = sysEnumService.listVoSysEnum(tplName, "shp_perm_index");
        return LocalUtils.getBaseResult(permIdList);
    }

    /**
     * 获取所有模板名称
     *
     * @param params
     * @return
     */
    @ApiOperation(
            value = "获取所有模板名称",
            notes = "获取所有模板名称",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestMapping("/listTplName")
    public BaseResult listTplName(@RequestParam Map<String, String> params) {
        List<VoSysEnum> tplNameList = sysEnumService.listTplName();
        return LocalUtils.getBaseResult(tplNameList);
    }

    /**
     * 删除模板名称
     *
     * @param params
     * @return
     */
    @ApiOperation(
            value = "删除模板名称",
            notes = "删除模板名称",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "tplName", value = "模板名称"),
    })
    @RequestMapping("/deleteTplName")
    public BaseResult deleteTplName(@RequestParam Map<String, String> params) {
        String tplName = params.get("tplName");
        int rows = sysEnumService.deleteTplName(tplName);
        return BaseResult.okResult(rows);
    }


    /**
     * 根据前端参数获取枚举类
     *
     * @param type
     * @return
     */
    private EnumTableName getEnumTableName(String type) {
        EnumTableName tableName;
        switch ("" + type) {
            case "pro_attribute":
                tableName = EnumTableName.PRO_ATTRIBUTE;
                break;
            case "pro_classify":
                tableName = EnumTableName.PRO_CLASSIFY;
                break;
            case "pro_state":
                tableName = EnumTableName.PRO_STATE;
                break;
            default:
                tableName = null;
                break;
        }
        return tableName;
    }

}
