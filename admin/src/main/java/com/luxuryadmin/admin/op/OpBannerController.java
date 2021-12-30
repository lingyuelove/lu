package com.luxuryadmin.admin.op;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.op.OpBanner;

import com.luxuryadmin.param.op.ParamOpBanner;
import com.luxuryadmin.param.op.ParamOpBannerQuery;
import com.luxuryadmin.service.op.OpBannerService;
import com.luxuryadmin.vo.op.VoOpBanner;
import com.luxuryadmin.vo.op.VoOpBannerNativePage;
import com.luxuryadmin.vo.op.VoOpBannerPos;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Classname OpProblemController
 * @Description 帮助中心Controller
 * @Date 2020/7/09 10:00
 * @Created by sanjin
 */
@Slf4j
@RestController
@RequestMapping(value = "/op/banner")
@Api(tags = {"6.【banner管理】模块"}, description = "/op/banner | 【banner管理】 ")
public class OpBannerController extends BaseController {

    @Autowired
    private OpBannerService opBannerService;

    @RequestMapping(value = "/listAllOpBannerPos", method = RequestMethod.GET)
    @ApiOperation(
            value = "查询所有【banner】位置;",
            notes = "查询所有【banner】位置;",
            httpMethod = "GET")
    public BaseResult<List<VoOpBannerPos>> listAllOpBannerPos() {
        List<VoOpBannerPos> opBannerPosList = opBannerService.listAllOpBannerPos();
        return LocalUtils.getBaseResult(opBannerPosList);
    }

    @RequestMapping(value = "/listAllOpBannerNativePage", method = RequestMethod.GET)
    @ApiOperation(
            value = "查询所有【banner】原生页面;",
            notes = "查询所有【banner】原生页面;",
            httpMethod = "GET")
    public BaseResult<List<VoOpBannerNativePage>> listAllOpBannerNativePage() {
        List<VoOpBannerNativePage> opBannerPosList = opBannerService.listAllOpBannerNativePage();
        return LocalUtils.getBaseResult(opBannerPosList);
    }

    @RequestMapping(value = "/listOpBanner", method = RequestMethod.POST)
    @ApiOperation(
            value = "分页查询【banner】列表;",
            notes = "分页查询【banner】列表;",
            httpMethod = "POST")
    @RequiresPerm(value = "advert:banner:list")
    public BaseResult<PageInfo<List<VoOpBanner>>> listOpProblem(@Valid ParamOpBannerQuery paramOpProblemQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramOpProblemQuery.getPageNum(), paramOpProblemQuery.getPageSize());
        List<VoOpBanner> opProblemList = opBannerService.listOpBanner(paramOpProblemQuery);
        PageInfo pageInfo = new PageInfo(opProblemList);
        return BaseResult.okResult(pageInfo);
    }

    @RequestMapping(value = "/addOpBanner", method = RequestMethod.POST)
    @ApiOperation(
            value = "增加【banner】",
            notes = "增加【banner】",
            httpMethod = "POST")
    public BaseResult addOpBanner(@Valid ParamOpBanner paramOpBanner, BindingResult result){
        //前台参数校验
        servicesUtil.validControllerParam(result);
        try {
            checkParamOpBanner(paramOpBanner);
        }catch (Exception e){
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }

        //数据对象属性复制
        OpBanner opBanner = new OpBanner();
        BeanUtils.copyProperties(paramOpBanner,opBanner);

        //设置操作者用户ID
        Integer uid  = getAdminUserId();
        opBanner.setInsertAdmin(uid);
        opBanner.setUpdateAdmin(uid);

        //数据库执行插入Banner记录操作
        int id = 0;
        try {
            id = opBannerService.addOpBanner(opBanner);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return LocalUtils.getBaseResult(id);
    }



    @RequestMapping(value = "/getOpBanner", method = RequestMethod.GET)
    @ApiOperation(
            value = "根据ID查询【banner】;",
            notes = "根据ID查询【banner】;",
            httpMethod = "GET")
    public BaseResult<VoOpBanner> getOpBanner(@RequestParam String id){
        OpBanner banner = opBannerService.getBannerById(Integer.valueOf(id));
        VoOpBanner voOpBanner = new VoOpBanner();
        BeanUtils.copyProperties(banner,voOpBanner);
        return LocalUtils.getBaseResult(voOpBanner);
    }

    @RequestMapping(value = "/updateOpBanner", method = RequestMethod.POST)
    @ApiOperation(
            value = "修改【banner】;",
            notes = "修改【banner】;",
            httpMethod = "POST")
    public BaseResult updateOpBanner(@Valid ParamOpBanner paramOpBanner, BindingResult result){
        //校验参数
        if(null == paramOpBanner.getId()){
            return BaseResult.errorResult("编辑Banner,ID不能为空");
        }
        servicesUtil.validControllerParam(result);
        try {
            checkParamOpBanner(paramOpBanner);
        }catch (Exception e){
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }

        OpBanner opBanner = new OpBanner();
        BeanUtils.copyProperties(paramOpBanner,opBanner);

        //获取修改者用户ID

        Integer uid  =getAdminUserId();

        opBanner.setUpdateAdmin(uid);

        //更新
        int id = 0;
        try {
            id = opBannerService.updateOpBanner(opBanner);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return LocalUtils.getBaseResult(id);
    }

    @RequestMapping(value = "/delOpBanner", method = RequestMethod.POST)
    @ApiOperation(
            value = "删除【Banner】;",
            notes = "删除【Banner】;",
            httpMethod = "POST")
    public BaseResult delOpBanner(@RequestParam String id){
        //获取修改者用户ID
        String token =getAdminToken();
        Integer uid  =getAdminUserId();

        int result = opBannerService.delOpBanner(Integer.valueOf(id),uid);
        return LocalUtils.getBaseResult(result);
    }

    /***********************************  私有方法 *************************************/
    /**
     * 校验Banner前台参数
     * @param paramOpBanner
     */
    private Boolean checkParamOpBanner(ParamOpBanner paramOpBanner) throws Exception{

        checkAppVersionNotNull(paramOpBanner);

        //校验AppVersion大小
        compareAppVersion(paramOpBanner);

        String jumpType = paramOpBanner.getJumpType();
        if("noJump".equals(jumpType)){
            //不跳转，不做校验
            //不能直接返回，防止以后后面还会添加新的校验逻辑，直接返回会跳过
        }else if("h5".equals(jumpType)){
            if(LocalUtils.isEmptyAndNull(paramOpBanner.getJumpH5Url())){
                throw new Exception("跳转类型为【H5】，【跳转H5页面URL】不能为空");
            }
        }else if("native".equals(jumpType)){
            if(LocalUtils.isEmptyAndNull(paramOpBanner.getJumpNativePage())){
                throw new Exception("跳转类型为【native】原生，【跳转原生地址】不能为空");
            }
        }else if("externalPage".equals(jumpType)){

        }else{
            throw new Exception("无效的跳转类型");
        }
        return Boolean.TRUE;
    }

    /**
     * 如果【最小版本号】和【最大版本号】都填写了
     * 【最大版本号】应该大于等于【最小版本号】
     * @param paramOpBanner
     */
    private void compareAppVersion(ParamOpBanner paramOpBanner) throws Exception {
        String publishPlatform = paramOpBanner.getPublishPlatform();
        String androidMinVersion = paramOpBanner.getAndroidMinVersion();
        String androidMaxVersion = paramOpBanner.getAndroidMaxVersion();
        String iosMinVersion = paramOpBanner.getIosMinVersion();
        String iosMaxVersion = paramOpBanner.getIosMaxVersion();

        Boolean isAndroidMinVersionNull = LocalUtils.isEmptyAndNull(androidMinVersion);
        Boolean isAndroidMaxVersionNull = LocalUtils.isEmptyAndNull(androidMaxVersion);
        Boolean isIosMinVersionNull = LocalUtils.isEmptyAndNull(iosMinVersion);
        Boolean isIosMaxVersionNull = LocalUtils.isEmptyAndNull(iosMaxVersion);
        if("all".equals(publishPlatform)){
            if(!isAndroidMinVersionNull && !isAndroidMaxVersionNull && VersionUtils.compareVersion(androidMinVersion,androidMaxVersion)>0){
                throw new Exception("发布全部平台，【安卓最小版本号】大于【安卓最大版本号】");
            }
            if(!isIosMinVersionNull && !isIosMaxVersionNull && VersionUtils.compareVersion(iosMinVersion,iosMaxVersion)>0){
                throw new Exception("发布全部平台，【ios最小版本号】大于【ios最大版本号】");
            }
        }else if("ios".equals(publishPlatform)){
            if(!isIosMinVersionNull && !isIosMaxVersionNull && VersionUtils.compareVersion(iosMinVersion,iosMaxVersion)>0){
                throw new Exception("发布ios平台，【ios最小版本号】大于【ios最大版本号】");
            }
        }else if("android".equals(publishPlatform)){
            if(!isAndroidMinVersionNull && !isAndroidMaxVersionNull && VersionUtils.compareVersion(androidMinVersion,androidMaxVersion)>0){
                throw new Exception("发布android平台，【安卓最小版本号】大于【安卓最大版本号】");
            }
        }else{
            throw new Exception("无效的发布平台类型");
        }
    }

    /**
     * 字段关联的参数校验
     * 1.【发布平台publishPlatform】
     *    (1)为all,校验【androidMinVersion】【androidMaxVersion】【iosMinVersion】【iosMaxVersion】不能为空
     *    (2)为ios,校验【iosMinVersion】【iosMaxVersion】不能为空
     *    (3)为android,校验【androidMinVersion】【androidMaxVersion】不能为空
     * 2.【跳转类型 noJump】
     *    (1)noJump,不用校验【jumpH5Url】【jumpNativePage】是否为空
     *    (2)h5,校验【jumpH5Url】是否为空
     *    (3)native,校验【jumpNativePage】是否为空
     */
    private void checkAppVersionNotNull(ParamOpBanner paramOpBanner) throws Exception {
        String publishPlatform = paramOpBanner.getPublishPlatform();
        Boolean isAndroidMinVersionNull = LocalUtils.isEmptyAndNull(paramOpBanner.getAndroidMinVersion());
        Boolean isAndroidMaxVersionNull = LocalUtils.isEmptyAndNull(paramOpBanner.getAndroidMaxVersion());
        Boolean isIosMinVersionNull = LocalUtils.isEmptyAndNull(paramOpBanner.getIosMinVersion());
        Boolean isIosMaxVersionNull = LocalUtils.isEmptyAndNull(paramOpBanner.getIosMaxVersion());
        if("all".equals(publishPlatform)){
            if(isAndroidMinVersionNull){
                throw new Exception("发布全部平台，【安卓最小版本号】不能为空");
            }
            if(isAndroidMaxVersionNull){
                throw new Exception("发布全部平台，【安卓最大版本号】不能为空");
            }
            if(isIosMinVersionNull){
                throw new Exception("发布全部平台，【ios最小版本号】不能为空");
            }
            if(isIosMaxVersionNull){
                throw new Exception("发布全部平台，【ios最大版本号】不能为空");
            }
        }else if("ios".equals(publishPlatform)){
            if(isIosMinVersionNull){
                throw new Exception("发布ios平台，【ios最小版本号】不能为空");
            }
            if(isIosMaxVersionNull){
                throw new Exception("发布ios平台，【ios最大版本号】不能为空");
            }
        }else if("android".equals(publishPlatform)){
            if(isAndroidMinVersionNull){
                throw new Exception("发布android平台，【安卓最小版本号】不能为空");
            }
            if(isAndroidMaxVersionNull){
                throw new Exception("发布android平台，【安卓最大版本号】不能为空");
            }
        }else{
            throw new Exception("无效的发布平台类型");
        }
    }


}
