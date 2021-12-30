package com.luxuryadmin.api.AuthorController;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantWeChat;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxService;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxEntity;
import com.luxuryadmin.common.utils.weixxin.mpsdk.WxUtil;
import com.luxuryadmin.entity.org.OrgAccessUser;
import com.luxuryadmin.entity.org.OrgOrganization;
import com.luxuryadmin.entity.shp.ShpBindCount;
import com.luxuryadmin.param.api.ParamUnionId;
import com.luxuryadmin.param.api.ParamWxData;
import com.luxuryadmin.param.api.ParamWxJsCode;
import com.luxuryadmin.param.org.ParamAccessUserAdd;
import com.luxuryadmin.param.pro.ParamShareSeeUserAdd;
import com.luxuryadmin.service.org.OrgAccessUserService;
import com.luxuryadmin.service.org.OrgOrganizationService;
import com.luxuryadmin.service.pro.ProShareSeeUserService;
import com.luxuryadmin.service.shp.ShpBindCountService;
import com.luxuryadmin.service.shp.ShpShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sanjin145
 * @date 2020-09-22
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/api/wx", method = RequestMethod.GET)
@Api(tags = {"Z005.【微信接口】模块"}, description = "/shop/api/wx | 微信第三方接口")
public class WxServiceController extends BaseController {


    @Autowired
    private WxService wxService;

    @Autowired
    private OrgAccessUserService orgAccessUserService;

    @Autowired
    private OrgOrganizationService orgOrganizationService;

    @Autowired
    private ShpShopService shpShopService;
    @Autowired
    private ShpBindCountService shpBindCountService;
    @Autowired
    private ProShareSeeUserService proShareSeeUserService;
    /**
     * 根据ID获取店铺服务详情
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据ID获取店铺服务详情",
            notes = "根据ID获取店铺服务详情;",
            httpMethod = "GET")
    @RequestMapping("/getSessionKey")
    public BaseResult getSessionKey(
            @Valid ParamWxJsCode param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        WxEntity wxEntity;
        try {
            wxEntity = wxService.code2Session(param.getJsCode());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return BaseResult.okResult(wxEntity);
    }


    /**
     * 保存微信用户信息
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "保存微信用户信息",
            notes = "保存微信用户信息;",
            httpMethod = "POST")
    @PostMapping("/saveUserPhone")
    public BaseResult saveUserPhone(
            @Valid ParamWxData param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String msg = "该手机号已授权该展会!";
        try {
            OrgInfo orgInfo = getUserPhone(param);
            String userPhone = orgInfo.getUserPhone();
            OrgAccessUser accessUser = orgAccessUserService.getAccessUser(userPhone, Integer.parseInt(param.getOrgId()),null);
            //如果不存在或者已删除
            if (LocalUtils.isEmptyAndNull(accessUser)) {
                String unionId = param.getUnionId();
                String keyUid = "_online:access_user:unionId:" + unionId;
                redisUtil.setEx(keyUid, userPhone, 1);
                String keyPhone = "_online:access_user:phone:" + userPhone;
                redisUtil.setEx(keyPhone, unionId, 1);

                ParamAccessUserAdd add = new ParamAccessUserAdd();
                add.setShopId(orgInfo.getShopId());
                add.setUserId(-1);
                add.setOrganizationId(orgInfo.getOrgId());
                add.setPhone(userPhone);
                add.setAccessType("10");
                orgAccessUserService.addAccessUser(add);
                return BaseResult.defaultOkResultWithMsg("授权成功,已加入该展会的白名单!");
            }
            //如果是已删除记录,则重新使用,变更为白名单用户
            if ("-90".equals(accessUser.getAccessType())) {
                accessUser.setAccessType("10");
                accessUser.setUpdateTime(new Date());
                accessUser.setUpdateAdmin(-1);
                orgAccessUserService.updateAccessUser(accessUser);
            } else if ("20".equals(accessUser.getAccessType())) {
                msg = "无权限查看该展会,请联系管理人员!";
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg("获取手机号失败: " + e.getMessage());
        }
        return BaseResult.defaultErrorWithMsg(msg);
    }


    /**
     * 判断是否为有效的机构临时仓访问用户
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "判断是否为有效的机构临时仓访问用户",
            notes = "判断是否为有效的机构临时仓访问用户;",
            httpMethod = "POST")
    @PostMapping("/validOrgAccessUser")
    public BaseResult validOrgAccessUser(
            @Valid ParamUnionId param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        try {
            int orgId = Integer.parseInt(param.getOrgId());
            OrgOrganization org = orgOrganizationService.getOrganization(orgId);
            if (LocalUtils.isEmptyAndNull(org)) {
                return BaseResult.defaultErrorWithMsg("展会不存在!");
            }

            Date startTime = org.getStartTime();
            Date now = new Date();
            if (startTime != null && now.before(startTime)) {
                return BaseResult.defaultErrorWithMsg("线上展会还未开放,敬请关注!");
            }
            Date endTime = org.getEndTime();
            if (endTime != null && now.after(endTime)) {
                return BaseResult.defaultErrorWithMsg("线上展会已结束,谢谢关注!");
            }
            //展会状态 10 不开启限制 | 20 开启限制
            if ("20".equals(org.getState())) {
                String unionId = param.getUnionId();
                String key = "_online:access_user:unionId:" + unionId;
                String phone = redisUtil.get(key);
                if (LocalUtils.isEmptyAndNull(phone)) {
                    return BaseResult.defaultErrorWithMsg("请先扫另外一个二维码授权!");
                }
                OrgAccessUser accessUser = orgAccessUserService.getAccessUser(phone, orgId,null);
                //不是白名单用户,则不允许访问
                if (LocalUtils.isEmptyAndNull(accessUser) || !"10".equals(accessUser.getAccessType())) {
                    return BaseResult.defaultErrorWithMsg("请先加入展会白名单!");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg("获取手机号失败: " + e.getMessage());
        }
        return BaseResult.okResult();
    }

    /**
     * 校验机构信息是否正常,且获取用户手机号
     *
     * @param param
     * @return
     * @throws Exception
     */
    private OrgInfo getUserPhone(ParamWxData param) throws Exception {
        Integer shopId = shpShopService.getShopIdByShopNumber(param.getShopNumber());
        if (LocalUtils.isEmptyAndNull(shopId)) {
            throw new MyException("无法获取举办方信息! code: " + param.getShopNumber());
        }
        int ordId = Integer.parseInt(param.getOrgId());
        OrgOrganization org = orgOrganizationService.getOrganization(ordId);
        if (LocalUtils.isEmptyAndNull(org)) {
            throw new MyException("该展会已失效! orgId: " + param.getOrgId());
        }
        String iv = param.getIv();
        String encryptedData = param.getEncryptedData();
        String sessionKey = param.getSessionKey();
        String userPhone = wxService.getUserPhone(iv, encryptedData, sessionKey);
        return new OrgInfo(shopId, ordId, userPhone);
    }


    @Data
    static
    class OrgInfo {
        OrgInfo(Integer shopId, Integer ordId, String userPhone) {
            this.shopId = shopId;
            this.orgId = ordId;
            this.userPhone = userPhone;
        }

        private Integer shopId;
        /**
         * 机构仓id
         */
        private Integer orgId;
        private String userPhone;
    }

    /**
     * 添加用户观看信息保存
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "添加用户观看分享内容保存 --2.5.5",
            notes = "添加用户观看分享内容保存 --2.5.5;",
            httpMethod = "POST")
    @PostMapping("/addShareSeeUser")
    public BaseResult addShareSeeUser(
            @Valid ParamShareSeeUserAdd shareSeeUserAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String msg = "获取openId成功!";
        WxEntity wxEntity = null;
        try {

            //1.code先调用微信接口查该用户的openId
             wxEntity =  wxService.code2Session(shareSeeUserAdd.getCode());
//
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg("获取openId失败: " + e.getMessage());
        }
        log.info("参数是"+wxEntity);
        if (null != wxEntity) {
            String openId = wxEntity.getOpenId();
            shareSeeUserAdd.setOpenId(openId);
            shareSeeUserAdd.setType("0");
            proShareSeeUserService.addShareSeeUser(shareSeeUserAdd);
        }
        return BaseResult.okResult(msg);
    }


}

