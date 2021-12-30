package com.luxuryadmin.api.pro;

import com.alibaba.fastjson.JSON;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.weixxin.mpsdk.*;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.param.shp.ParamShopConfigUpdate;
import com.luxuryadmin.service.pro.ProShareService;
import com.luxuryadmin.service.shp.ShpShopConfigService;
import com.luxuryadmin.vo.pro.VoSharePage;
import com.luxuryadmin.vo.pro.VoShareUserPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @PackgeName: com.luxuryadmin.api.pro
 * @ClassName: ProShareController
 * @Author: ZhangSai
 * Date: 2021/6/30 15:21
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/share")
@Api(tags = {"Z003.分享店铺商品 --2.6.2"}, description = "/shop/user/share |分享店铺商品")
public class ProShareController extends BaseController {
    @Autowired
    private ProShareService shareService;

    @Autowired
    private ShpShopConfigService shopConfigService;

    @ApiOperation(
            value = "小程序访客",
            notes = "小程序访客;",
            httpMethod = "POST")
    @RequestMapping("/getShareByList")
    @RequiresPermissions("share:see:appletAccessUser")
    public BaseResult<VoSharePage> getShareByList(@Valid ParamShare paramShare, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(paramShare.getPageNum())) {
            paramShare.setPageNum("1");
        }
        paramShare.setShopId(getShopId());
        VoSharePage sharePage = shareService.getShareByList(paramShare);
        return BaseResult.okResult(sharePage);
    }


    @ApiOperation(
            value = "删除访客记录",
            notes = "删除访客记录;",
            httpMethod = "POST")

    @RequestMapping("/deleteById")
    public BaseResult deleteById(@Valid ParamShareDelete paramShareDelete, BindingResult result) {
        servicesUtil.validControllerParam(result);
        shareService.deleteById(Integer.parseInt(paramShareDelete.getId()));
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "小程序访客记录",
            notes = "小程序访客记录;",
            httpMethod = "POST")

    @RequestMapping("/getShareUserList")
    public BaseResult<VoShareUserPage> getShareUserList(@Valid ParamShareUser paramShareUser, BindingResult result) {
        servicesUtil.validControllerParam(result);
        if (LocalUtils.isEmptyAndNull(paramShareUser.getPageNum())) {
            paramShareUser.setPageNum("1");
        }
        VoShareUserPage shareUserPage = shareService.listUnionShareUser(paramShareUser);
        return BaseResult.okResult(shareUserPage);
    }

    @ApiOperation(
            value = "是否开启小程序访客功能",
            notes = "是否开启小程序访客功能;",
            httpMethod = "POST")

    @RequestMapping("/updateShareOpenState")
    public BaseResult shareOpenState(@Valid ParamShareOpenState shareOpenState, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ParamShopConfigUpdate shopConfigUpdate = new ParamShopConfigUpdate();
        shopConfigUpdate.setOpenShareUser(shareOpenState.getOpenShareUser());
        shopConfigUpdate.setShopId(getShopId());
        shopConfigService.updateShopConfig(shopConfigUpdate);
        return BaseResult.okResult();
    }

    /**
     * 接口生成的小程序码
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "接口生成的小程序码 --2.6.2",
            notes = "接口生成的小程序码 --2.6.2;",
            httpMethod = "POST")
    @PostMapping("/createQRCode")
    public BaseResult getWxQrCode(
            @Valid ParamShareProductSave save, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String codeImg = null;
        Map<String,Object> stringObjectMap = new HashMap<>();
        try {
            save.setShopId(getShopId());
            save.setUserId(getUserId());
            save.setShopNumber(getShopNumber());
            save.setUserNumber(getUserNumber());
            String shareBatch = shareService.saveShareProductForQRCode(save);
            String page = "pages/redirect/index";
            String scene = null;
            if ("2".equals(save.getType())) {
                scene = "shareBatch=" + shareBatch+"&type=1";
            } else {
                scene = "shareBatch=" + shareBatch+"&type=0";

            }
            Object url =TestImageBinary.getQrCodeImgUrl(scene,page,true,true);
            codeImg= url.toString();
            stringObjectMap.put("codeImg",codeImg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MyException("接口生成的小程序码失败: " + e.getMessage());
        }
        return  BaseResult.okResult(stringObjectMap);
    }

    /**
     * 获取小程序分享二维码 获取为二进制内容 暂时弃用 可用作测试
     * @param save
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "接口生成的小程序码 --2.5.5",
            notes = "接口生成的小程序码 --2.5.5;",
            httpMethod = "POST")
    @PostMapping("/createQRCode2")
    public String createQRCode( @Valid ParamShareProductSave save) {

        //接口地址拼接参数
        String getTokenApi = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + WxService.getAccessTokenApplets();

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        Map<String, String> querys = new HashMap<>();
        String page = "pages/redirect/index";
        String scene = null;
        if ("0".equals(save.getType())) {
            scene = "shareBatch=" + 1+"&type=0";

        } else {
            scene = "shareBatch=" + 1+"&type=1";
        }
        querys.put("page", page);
        querys.put("width", "430");
        querys.put("scene", scene);
        String data = JSON.toJSONString(querys);
        String contentType = null;
        try {
            contentType = HttpUtils.httpsRequest(getTokenApi, "POST", data);
        } catch (Exception e) {
//            throw new MyException("not JSON format! ");
            throw new MyException("获取二维码失败;[" + "]");
        }
        return contentType;
    }
}
