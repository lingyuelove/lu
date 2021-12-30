package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.common.utils.sf.SFUtil;
import com.luxuryadmin.entity.pro.ProDeliver;
import com.luxuryadmin.enums.pro.EnumProDeliverSource;
import com.luxuryadmin.enums.pro.EnumProDeliverType;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.op.ParamIdQuery;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProDeliverLogisticsService;
import com.luxuryadmin.service.pro.ProLockRecordService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.luxuryadmin.service.pro.ProDeliverService;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro/deliver")
@Api(tags = {"发货"}, description = "/shop/user/pro/deliver |发货】模块相关")
public class ProDeliverController extends ProProductBaseController {

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ProLockRecordService proLockRecordService;

    @Autowired
    private ProDeliverService proDeliverService;
    @Autowired
    private ProDeliverLogisticsService proDeliverLogisticsService;

    @ApiOperation(
            value = "分页查询【发货】列表;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @PostMapping("/listProDeliver")
    public BaseResult listProDeliver(
            @Valid ParamProPageDeliver param, BindingResult result) throws UnsupportedEncodingException {
        servicesUtil.validControllerParam(result);
        Date date = new Date();
        System.out.println(date);
////        发货更新物流信息接口
//        proDeliverLogisticsService.addOrUpdateList(getShopId());
        System.out.println(date);
        param.setShopId(getShopId());
        param.setCurrentUserId(getUserId());
        PageHelper.startPage(getPageNum(), getPageSize());
        List<VoProDeliverByPage> voProDeliverByPages = new ArrayList<>();

        Integer dropSum = 0;
        try {
            if (VersionUtils.compareVersion(param.getAppVersion(), "2.6.6") >= 0) {
                List<ProDeliver> proDelivers = proDeliverService.listDeliver(param);
                if (param.getState().equals("0")) {
                    List<ProDeliver> proDeliversAll = proDeliverService.listDeliverAll(param.getShopId(), param.getStartTime(), param.getEndTime(), param.getShpUserId(),
                            param.getDeliverStartTime(), param.getDeliverEndTime(), param.getProName());
                    if (proDeliversAll != null && proDeliversAll.size() > 0) {
                        Map<String, List<ProDeliver>> proDeliversBySourceAllMap = proDeliversAll.stream().collect(Collectors.groupingBy(ProDeliver::getDeliverSource));
                        List<ProDeliver> proDeliverAllByOrder = proDeliversBySourceAllMap.get(EnumProDeliverSource.ORDER.name());
                        if (proDeliverAllByOrder != null && proDeliverAllByOrder.size() > 0) {
                            List<Integer> ids = proDeliverAllByOrder.stream().map(ProDeliver::getId).collect(Collectors.toList());
                            Integer orderSum = ordOrderService.getOrderSum(ids);
                            dropSum = dropSum + orderSum;
                        }
                        List<ProDeliver> proDeliverAllByLock = proDeliversBySourceAllMap.get(EnumProDeliverSource.LOCK_RECORD.name());
                        if (proDeliverAllByLock != null && proDeliverAllByLock.size() > 0) {
                            List<Integer> ids = proDeliverAllByLock.stream().map(ProDeliver::getId).collect(Collectors.toList());
                            Integer lockSum = proLockRecordService.getLockSum(ids);
                            dropSum = dropSum + lockSum;
                        }
                    }
                }
                if (proDelivers != null && proDelivers.size() > 0) {
                    Map<String, List<ProDeliver>> proDeliversBySourceMap = proDelivers.stream().collect(Collectors.groupingBy(ProDeliver::getDeliverSource));
                    List<ProDeliver> proDeliverByOrder = proDeliversBySourceMap.get(EnumProDeliverSource.ORDER.name());
                    List<ProDeliver> proDeliverByLock = proDeliversBySourceMap.get(EnumProDeliverSource.LOCK_RECORD.name());
                    List<VoProDeliverByPage> voProDeliverInterim = new ArrayList<>();
                    //订单
                    if (proDeliverByOrder != null && proDeliverByOrder.size() > 0) {
                        List<Integer> ids = proDeliverByOrder.stream().map(ProDeliver::getId).collect(Collectors.toList());
                        voProDeliverInterim = ordOrderService.listProDeliverInfo(ids, param.getState());
                    }
                    //锁单
                    if (proDeliverByLock != null && proDeliverByLock.size() > 0) {
                        List<Integer> ids = proDeliverByLock.stream().map(ProDeliver::getId).collect(Collectors.toList());
                        List<VoProDeliverByPage> voProDeliverByPages1 = proLockRecordService.listProDeliverInfo(ids, param.getState());
                        for (VoProDeliverByPage vp :
                                voProDeliverByPages1) {
                            voProDeliverInterim.add(vp);
                        }
                    }
                    Map<Integer, VoProDeliverByPage> collect = voProDeliverInterim.stream().collect(Collectors.toMap(VoProDeliverByPage::getDeliverId, Function.identity()));
                    for (int i = 0; i < proDelivers.size(); i++) {
                        voProDeliverByPages.add(collect.get(proDelivers.get(i).getId()));
                    }
                }
            } else {
                switch (param.getDeliverSource()) {
                    case "ORDER":
                        voProDeliverByPages = ordOrderService.getProDeliverInfo(param);
                        break;
                    case "LOCK_RECORD":
                        voProDeliverByPages = proLockRecordService.getProDeliverInfo(param);
                        break;
                    default:
                        throw new MyException("发货来源信息不正确");
                }
            }
        } catch (Exception e) {
            throw new MyException("发货管理列表异常" + e);
        }
        List<VoProductLoad> voProductLoads = new ArrayList<>();
        Map<Integer, String> cacheMap = new HashMap<>();
        voProDeliverByPages.stream().forEach(vp -> {
            VoProductLoad voProductLoad = new VoProductLoad();
            BeanUtils.copyProperties(vp, voProductLoad);
            voProductLoads.add(voProductLoad);
            cacheMap.put(vp.getDeliverId(), vp.getShowTime());
        });
        List<String> deliveryWays = new ArrayList<>();
        EnumProDeliverType[] values = EnumProDeliverType.values();
        for (EnumProDeliverType e :
                values) {
            deliveryWays.add(e.getMsg());
        }
        formatVoProductLoad(null, voProductLoads, null);
        Map<Integer, VoProDeliverByPage> voProDeliverByPageMapById = voProDeliverByPages.stream().collect(Collectors.toMap(VoProDeliverByPage::getDeliverId, Function.identity()));
        List<VoProDeliverByPage> voProDeliver = new ArrayList<>();
        voProductLoads.stream().forEach(vp -> {
            VoProDeliverByPage voProDeliverByPage = new VoProDeliverByPage();
            BeanUtils.copyProperties(vp, voProDeliverByPage);
            VoProDeliverByPage vpd = voProDeliverByPageMapById.get(vp.getDeliverId());
            voProDeliverByPage.setAddress(vpd.getAddress() == null ? null : vpd.getAddress());

            try {
                if (VersionUtils.compareVersion(param.getAppVersion(), "2.6.6") >= 0) {
                    if (vpd.getState() == 0) {
                        voProDeliverByPage.setShowTime("销售时间: " + cacheMap.get(vp.getDeliverId()));
                    } else {
                        voProDeliverByPage.setShowTime("发货时间: " + cacheMap.get(vp.getDeliverId()));
                        voProDeliverByPage.setDeliverType(vpd.getDeliverType());
                    }
                    voProDeliverByPage.setDeliverSource(vpd.getDeliverSource());
                } else {
                    if (vpd.getDeliverSource().equals(EnumProDeliverSource.ORDER.name())
                            && vpd.getState() == 0) {
                        voProDeliverByPage.setShowTime("开单时间: " + cacheMap.get(vp.getDeliverId()));
                    } else if (vpd.getDeliverSource().equals(EnumProDeliverSource.LOCK_RECORD.name())
                            && vpd.getState() == 0) {
                        voProDeliverByPage.setShowTime("锁单时间: " + cacheMap.get(vp.getDeliverId()));
                    } else {
                        voProDeliverByPage.setShowTime("发货时间: " + cacheMap.get(vp.getDeliverId()));
                    }
                    voProDeliverByPage.setDeliverSource(null);
                }
            } catch (Exception e) {
                throw new MyException("发货管理列表异常" + e);
            }
            if (vpd.getOrderBizId() != null) {
                voProDeliverByPage.setOrderBizId(vpd.getOrderBizId());
            }
            if (vpd.getLockId() != null) {
                voProDeliverByPage.setLockId(vpd.getLockId());
            }
            voProDeliverByPage.setDeliverNickname(vpd.getDeliverNickname());
            voProDeliverByPage.setRemark(vpd.getRemark() == null ? null : vpd.getRemark());
            voProDeliverByPage.setState(vpd.getState());
            voProDeliverByPage.setDeliveryWays(deliveryWays);
            voProDeliver.add(voProDeliverByPage);
        });
        try {
            if (VersionUtils.compareVersion(param.getAppVersion(), "2.6.6") >= 0) {
                VoDeliverList voDeliverList = new VoDeliverList();
                voDeliverList.setDropSum(dropSum.toString());
                voDeliverList.setObjList(voProDeliver);
                return BaseResult.okResult(voDeliverList);
            } else {
                return LocalUtils.getBaseResult(voProDeliver);
            }
        } catch (Exception e) {
            throw new MyException("发货管理列表异常" + e);
        }
    }


    @ApiOperation(
            value = "发货",
            httpMethod = "POST")
    @PostMapping("/sendProDeliver")
    public BaseResult sendProDeliver(@Valid ParamSendProDeliver param, BindingResult result) throws UnsupportedEncodingException {
        servicesUtil.validControllerParam(result);
        param.setCurrentUserId(getUserId());
        if (!LocalUtils.isEmptyAndNull(param.getLogisticsNumber())) {
            VoDeliver voProDeliverDetail = proDeliverLogisticsService.getDeliverByLogisticsNumber(param.getLogisticsNumber());
            if (voProDeliverDetail == null) {
                String msgData = SFUtil.inspectOrderId(param.getLogisticsNumber());
                if ("false".equals(msgData)) {
                    return BaseResult.defaultErrorWithMsg("单号有误，请重新输入");
                }
            }

            if (LocalUtils.isEmptyAndNull(param.getCheckPhoneNo())) {
                return BaseResult.defaultErrorWithMsg("物流单号所需手机号后四位不能为空");
            }
        }
        proDeliverService.sendProDeliver(param);
        return BaseResult.okResult("发货成功");
    }

    @ApiOperation(
            value = "物流查询接口 --2.6.5",
            httpMethod = "POST")
    @PostMapping("/getDeliverByLogNumAndPhone")
    public BaseResult<VoDeliver> getDeliverByLogisticsNumAndPhone(@Valid ParamDeliverLogisticsDetail params, BindingResult result) throws UnsupportedEncodingException {
        servicesUtil.validControllerParam(result);
        VoDeliver voProDeliverDetail = proDeliverLogisticsService.getDeliverByLogisticsNumAndPhone(params);
        return BaseResult.okResult(voProDeliverDetail);
    }

    @ApiOperation(
            value = "发货详情",
            httpMethod = "POST")
    @PostMapping("/getProDeliverDetail")
    public BaseResult getProDeliverDetail(@Valid ParamIdQuery params, BindingResult result) throws UnsupportedEncodingException {
        servicesUtil.validControllerParam(result);
        VoProDeliverDetail voProDeliverDetail = proDeliverService.getProDeliverDetail(params);
        return BaseResult.okResult(voProDeliverDetail);
    }

//    @ApiOperation(
//            value = "物流详情 --2.6.5",
//            httpMethod = "POST")
//    @PostMapping("/getDeliverByNumber")
//    public BaseResult<VoDeliver> getDeliverByLogisticsNumber(@Valid ParamIdQuery params, BindingResult result) {
//        servicesUtil.validControllerParam(result);
//        VoDeliver voProDeliverDetail = proDeliverLogisticsService.getDeliverByLogisticsNumber(params.getId());
//        return BaseResult.okResult(voProDeliverDetail);
//    }

    @ApiOperation(
            value = "发货方式",
            httpMethod = "POST")
    @PostMapping("/getProDeliverType")
    public BaseResult getProDeliverType(@Valid ParamToken token, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<String> deliveryWays = new ArrayList<>();
        EnumProDeliverType[] values = EnumProDeliverType.values();
        for (EnumProDeliverType e :
                values) {
            deliveryWays.add(e.getMsg());
        }
        return BaseResult.okResult(deliveryWays);
    }

    @ApiOperation(
            value = "删除多个发货管理",
            httpMethod = "POST")
    @RequestMapping("/deleteList")
    public BaseResult deleteList(@Valid ParamShareDelete token, BindingResult result) {
        servicesUtil.validControllerParam(result);
        proDeliverService.deleteList(token.getId());
        return BaseResult.okResult();
    }


    @ApiOperation(
            value = "筛选条件列表",
            httpMethod = "POST")
    @PostMapping("/getFiltrateinfo")
    public BaseResult getFiltrateinfo(@Valid ParamToken token, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        VoFiltrateInfo voFiltrateInfo = proDeliverService.getFiltrateinfo(shopId);
        return BaseResult.okResult(voFiltrateInfo);
    }
}
