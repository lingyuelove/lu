package com.luxuryadmin.api.sys;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.param.sys.ParamSysAppVersion;
import com.luxuryadmin.service.sys.SysVersionService;
import com.luxuryadmin.vo.sys.VoSysAppVersion;
import com.luxuryadmin.vo.sys.VoSysVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys/version")
@Api(tags = {"A002.【APP版本更新】模块"}, description = "/sys/version |APP版本更新")
public class SysVersionController extends BaseController {


    @Autowired
    private SysVersionService sysVersionService;

    /**
     * 获取App系统配置;
     * 配置加密开关对此接口不影响;该接口返回的数据;是经过AES对称加密;<br/>
     * 接口参数map可分区平台是android还是iOS
     *
     * @return Result
     */
    @ApiOperation(
            value = "1.获取版本更新信息",
            notes = "获取版本更新信息;<br/>",
            httpMethod = "POST")
    @RequestMapping(value = "/getAppVersionInfo", method = RequestMethod.POST)
    public BaseResult<VoSysAppVersion> getAppSysConfig(ParamSysAppVersion paramAppVersion) {

        String platform = paramAppVersion.getPlatform();
        String appVersion = paramAppVersion.getAppVersion();
        VoSysVersion sysVersionDbNewest = sysVersionService.querySysVersionByPlatform(platform);
        if(null == sysVersionDbNewest){
            return BaseResult.errorNeedUpgrade("没有获取到对应的版本更新信息",null);
        }

        Boolean isNeedUpdate = Boolean.FALSE;
        String appVersionNewest;
        try {
            appVersionNewest = sysVersionDbNewest.getAppVersion();
            isNeedUpdate = VersionUtils.compareVersion(appVersion,appVersionNewest)<0;
        } catch (Exception e) {
            return BaseResult.errorNeedUpgrade(e.getMessage(),null);
        }
        VoSysAppVersion voSysAppVersion = new VoSysAppVersion();
        //是否需要更新
        voSysAppVersion.setIsNeedUpdate(isNeedUpdate);
        //是否强制更新
        voSysAppVersion.setIsForce("1".equals(sysVersionDbNewest.getForcedUpdate()));
        if(isNeedUpdate) {
            voSysAppVersion.setFindTitle("发现有新版本");
            voSysAppVersion.setVersionTitle("新版本V" + appVersionNewest);
            voSysAppVersion.setUpdateInfo(sysVersionDbNewest.getContent());
            voSysAppVersion.setUpdateBtnText("立即更新");
            voSysAppVersion.setUpdateUrl(sysVersionDbNewest.getUpdateUrl());
        }
        return BaseResult.errorNeedUpgrade("请求成功",voSysAppVersion);
    }
}
