package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.shp.*;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.param.mem.ParamMemShop;
import com.luxuryadmin.param.shp.ParamGetShopInfoByNumber;
import com.luxuryadmin.param.shp.ParamShpShop;
import com.luxuryadmin.service.biz.BizLeaguerConfigService;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.service.fin.FinShopRecordTypeService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.mem.VoMemShop;
import com.luxuryadmin.vo.mem.VoMemShopById;
import com.luxuryadmin.vo.shp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @Date 2019/12/14 2:19
 */
@Slf4j
@Service
public class ShpShopServiceImpl implements ShpShopService {
    @Resource
    private ShpShopMapper shpShopMapper;

    @Autowired
    private ShpShopNumberService shpShopNumberService;

    @Autowired
    private ShpDetailService shpDetailService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ProAttributeService proAttributeService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private OrdTypeService ordTypeService;

    @Autowired
    private ProSourceService proSourceService;

    @Autowired
    private ProAccessoryService proAccessoryService;

    @Autowired
    private ProSaleChannelService proSaleChannelService;

    @Autowired
    private ShpAfterSaleGuaranteeService shpAfterSaleGuaranteeService;

    @Autowired
    private ShpUserPermissionTplService shpUserPermissionTplService;

    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    @Autowired
    private ShpServiceService shpServiceService;

    @Autowired
    private FinShopRecordTypeService finShopRecordTypeService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProShareService proShareService;

    @Autowired
    private ProDownloadImgService proDownloadImgService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ProLockRecordService proLockRecordService;

    @Autowired
    private BizLeaguerConfigService bizLeaguerConfigService;

    @Autowired
    private FinSalaryService finSalaryService;

    @Autowired
    private ProDynamicService proDynamicService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ProCheckService proCheckService;

    @Autowired
    private ServicesUtil servicesUtil;


    @Autowired
    private ShpPermTplService shpPermTplService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int becomeShopkeeper(ShpShop shpShop, ShpDetail shpDetail) {
        try {
            String username = shpShop.getContact();
            int userId = shpShop.getFkShpUserId();
            ShpShopNumber shpShopNumber = shpShopNumberService.getShpShopNumberOverId();
            String shopNumber = shpShopNumber.getNumber();
            shpShop.setNumber(shopNumber);
            shpShopMapper.saveObject(shpShop);
            int shopId = shpShop.getId();
            shpDetail.setFkShpShopId(shopId);
            shpDetail.setInsertTime(shpShop.getInsertTime());
            shpDetailService.saveOrUpdateShpDetail(shpDetail);
            shpShopNumberService.usedShpShopNumber(shopId, shpShopNumber);
            ShpUser shpUser = shpUserService.packShpUserForLogin(userId, shopId, "1");
            String nickname = shpUserService.getNicknameByUserId(userId);
            shpUserService.updateShpUser(shpUser);
            //????????????????????????????????????;
            shpUserShopRefService.saveShpUserShopRef(userId, shopId, nickname, -9, userId);
            //?????????????????????
            //?????????????????????
            proClassifyService.initProClassifyByShopIdAndUserId(shopId, userId);
            //?????????????????????
            proAttributeService.initProAttributeByShopIdAndUserId(shopId, userId);
            //?????????????????????
            ordTypeService.initOrdTypeByShopIdAndUserId(shopId, userId);
            //?????????????????????
            proSourceService.initProSourceByShopIdAndUserId(shopId, userId);
            //??????????????????
            proAccessoryService.initProAccessoryByShopIdAndUserId(shopId, userId);
            //???????????????????????????(??????????????????)(???v2.6.7)
            shpPermTplService.initShopSystemPerm(shopId, userId);
            //?????????????????????
            proSaleChannelService.initProSaleChannelByShopIdAndUserId(shopId, userId);
            //???????????????????????????
            shpAfterSaleGuaranteeService.initShpAfterSaleGuarantee(shopId, userId);
            //?????????????????????
            shpServiceService.initSingleShpServiceType(shopId, userId);
            //???????????????????????????
            finShopRecordTypeService.initFinShopRecordTypeByShopIdAndUserId(shopId, userId);
            //?????????????????????
            bizLeaguerConfigService.buildBizLeaguerConfig(shopId, userId);
            //?????????????????????????????????
            finSalaryService.initFinSalary(shopId, userId, new Date());
            //???????????????????????????
            proDynamicService.saveInitializeDynamic(shopId);

            //????????????
            String token = redisUtil.get(ConstantRedisKey.SHP_TOKEN_ + username);
            String shopIdKey = ConstantRedisKey.getShopIdRedisKeyByToken(token);
            int day = 30;
            redisUtil.setEx(shopIdKey, shopId + "", day);
            //??????????????????
            String bossUserIdKey = ConstantRedisKey.getBossUserIdRedisKeyByShopId(shopId);
            String shopNameKey = ConstantRedisKey.getShopNameRedisKeyByShopId(shopId);
            String shopNumberKey = ConstantRedisKey.getShopNumberRedisKeyByShopId(shopId);
            redisUtil.set(bossUserIdKey, userId + "");
            redisUtil.set(shopNameKey, shpShop.getName() + "");
            redisUtil.set(shopNumberKey, shopNumber + "");
            String isMember = ConstantRedisKey.getShopMemberRedisKeyByShopId(shopId);
            redisUtil.set(isMember, shpShop.getIsMember());
            String memberState = ConstantRedisKey.getMemberStateRedisKeyByShopId(shopId);
            redisUtil.set(memberState, shpShop.getMemberState().toString());
            String vipExpireKey = ConstantRedisKey.getVipExpireRedisKeyByShopId(shopId);
            redisUtil.set(vipExpireKey, DateUtil.formatShort(shpShop.getTryEndTime()));
            return shopId;

        } catch (Exception e) {
            log.error("=======??????????????????====: " + e.getMessage(), e);
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("????????????: " + e.getMessage());
        }
    }

    @Override
    public VoUserShopBase getVoUserShopBaseByShopId(int shopId) {
        return shpShopMapper.getVoUserShopBaseByShopId(shopId);
    }

    @Override
    public List<VoShopBase> chooseShop(int userId, String token) {
        return shpShopMapper.listShpShopBaseInfo(userId);
    }

    @Override
    public Map<String, Object> getShopNumberAndShopNameByShopId(int shopId) {
        return shpShopMapper.getShopNumberAndShopNameByShopId(shopId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateShopInfo(ShpShop shpShop, ShpDetail shpDetail) {
        try {
            int row1 = shpShopMapper.updateObject(shpShop);
            int row2 = shpDetailService.updateShpDetailByShopId(shpDetail);
            return row1 + row2;
        } catch (Exception e) {
            log.error("=======??????????????????????????????====: " + e.getMessage(), e);
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("????????????????????????: " + e.getMessage());
        }
    }

    @Override
    public VoUserShopBase getShopInfoToOrdReceipt(int shopId) {
        return shpShopMapper.getShopInfoToOrdReceipt(shopId);
    }

    @Override
    public VoUserShopBase getShareShopInfoByShopNumber(String shopNumber) {
        return shpShopMapper.getShareShopInfoByShopNumber(shopNumber);
    }

    @Override
    public Integer getShopIdByShopNumber(String shopNumber) {
        return shpShopMapper.getShopIdByShopNumber(shopNumber);
    }

    @Override
    public Integer getShopIdByPhoneAndShopNumber(String phone, String shopNumber) {
        return shpShopMapper.getShopIdByPhoneAndShopNumber(phone, shopNumber);
    }

    @Override
    public List<VoUserShopBase> listAllShopIdAndUserId() {
        return shpShopMapper.listAllShopIdAndUserId();
    }

    @Override
    public List<VoShpShop> queryShpShopList(ParamShpShop paramShpShop) {
        List<VoShpShop> shpShops = shpShopMapper.listShpShop(paramShpShop);
        formatVoShpShops(shpShops);
        return shpShops;
    }

    /**
     * ?????????????????????
     *
     * @param voShpShops
     */
    private void formatVoShpShops(List<VoShpShop> voShpShops) {
        if (!CollectionUtils.isEmpty(voShpShops)) {
            for (VoShpShop voShpShop : voShpShops) {
                Integer shopId = voShpShop.getId();

                //??????????????????
                Integer onSaleProdNum = proProductService.getOnSaleProductNumByShopIdByAdmin(shopId);
                voShpShop.setOnSaleProdNum(onSaleProdNum);
                //??????????????????
                Integer uploadProdNum = proProductService.getAllUploadProductNumByShopId(shopId);
                voShpShop.setUploadProdNum(uploadProdNum);
                //????????????
                Integer confirmOrderNum = ordOrderService.getTotalOrderNumByShopId(shopId);
                voShpShop.setConfirmOrderNum(confirmOrderNum);
                //??????????????????
                Integer shopUserNum = shpUserService.getValidShpUserNumByShopId(shopId);
                voShpShop.setShopUserNum(shopUserNum);
            }
        }
    }

    @Override
    public HashMap<String, Object> getShpShopInfo(String id) {
        ShpShop shpShop = (ShpShop) shpShopMapper.getObjectById(LocalUtils.strParseInt(id));
        if (null == shpShop) {
            return null;
        }
        ShpUser shpUser = shpUserService.getObjectById(shpShop.getFkShpUserId().toString());
        ShpDetail shpDetail = shpDetailService.selectByShopId(shpShop.getId());
        HashMap<String, Object> info = new HashMap<>();
        info.put("user", shpUser);
        info.put("detail", shpDetail);
        return info;
    }

    @Override
    public ShpShop getShpShopById(String id) {
        return (ShpShop) shpShopMapper.getObjectById(LocalUtils.strParseInt(id));
    }

    @Override
    public void updateShpShop(ShpShop shpShop) {
        shpShopMapper.updateObject(shpShop);
    }

    @Override
    public List<Map<String, String>> getShopNumberByPhone(String phone) {
        return shpShopMapper.getShopNumberByPhone(phone);
    }

    @Override
    public Integer getBossUserIdByShopId(int shopId) {
        return shpShopMapper.getBossUserIdByShopId(shopId);
    }

    @Override
    public List<Integer> queryShpShopIdList() {
        return shpShopMapper.queryShpShopIdList();
    }

    @Override
    public void destroyShop(int shopId) {

        Object obj = shpShopMapper.getObjectById(shopId);

        if (!LocalUtils.isEmptyAndNull(obj)) {
            ShpShop shop = (ShpShop) obj;
            //????????????
            String phone = shop.getContact();
            String shopName = shop.getName();
            //????????????, ?????????????????????"-99"; ???????????????????????????
            shpShopMapper.changeShopState(shopId, "-99");
            //???????????????????????????????????????,??????????????????;
            List<String> usernameList = shpUserShopRefService.listAllUserNameByShopId(shopId, "1");
            if (!LocalUtils.isEmptyAndNull(usernameList)) {
                for (String username : usernameList) {
                    String un = DESEncrypt.decodeUsername(username);
                    String usernameKey = ConstantRedisKey.getShpTokenKey(un);
                    String token = redisUtil.get(usernameKey);
                    if (!LocalUtils.isEmptyAndNull(token)) {
                        String shopIdKey = ConstantRedisKey.getShopIdRedisKeyByToken(token);
                        String shopIdString = redisUtil.get(shopIdKey);
                        if ((shopId + "").equals(shopIdString)) {
                            shpUserService.exitLogin(token);
                        }
                    }
                }
            }
            //??????????????????????????????
            shpUserShopRefService.deleteUserShopRefByShopId(shopId);
            //??????????????????
            proProductService.deleteProProductByShopId(shopId);
            //??????????????????????????????
            proShareService.deleteProShareByShopId(shopId);
            //??????????????????????????????
            proDownloadImgService.deleteProDownloadImgByShopId(shopId);
            //??????????????????????????????
            proLockRecordService.deleteProLockRecordByShopId(shopId);
            //??????????????????
            proClassifyService.deleteProClassifyByShopId(shopId);
            //??????????????????
            proAttributeService.deleteProAttributeByShopId(shopId);
            //????????????????????????
            ordTypeService.deleteOrdTypeByShopId(shopId);
            //??????????????????
            proSourceService.deleteProSourceByShopId(shopId);
            //?????????????????????
            proAccessoryService.deleteProAccessoryByShopId(shopId);
            //????????????????????????
            shpUserPermissionTplService.deleteShpUserPermissionTplByShopId(shopId);
            //????????????????????????(???v2.6.7)
            shpPermTplService.deleteShpPermTplByShopId(shopId);
            //??????????????????
            shpPermUserRefService.deleteShpUserPermissionRefByShopId(shopId);
            //????????????????????????
            proSaleChannelService.deleteProSaleChannelByShopId(shopId);
            //?????????????????????
            shpAfterSaleGuaranteeService.deleteShpAfterSaleGuaranteeByShopId(shopId);
            //????????????????????????
            shpServiceService.deleteShpServiceTypeByShopId(shopId);
            //????????????????????????
            shpServiceService.deleteShpServiceRecordByShopId(shopId);
            //??????????????????????????????
            finShopRecordTypeService.deleteFinShopRecordTypeByShopId(shopId);
            //????????????,??????,??????????????????
            ordOrderService.deleteOrderByShopId(shopId);
            //????????????????????????
            finSalaryService.deleteFinSalaryForDestroyShop(shopId);
            //????????????????????????
            shpOperateLogService.deleteShpOperateLogByShopId(shopId);
            //????????????????????????
            proCheckService.deleteProCheckByShopId(shopId);
            //????????????????????????
            bizLeaguerConfigService.deleteLeaguerConfigByShop(shopId);
        }
    }

    @Override
    public int updateShopMember(int shopId, String yesOrNo) {
        String isMemberKey = ConstantRedisKey.getShopMemberRedisKeyByShopId(shopId);
        redisUtil.set(isMemberKey, yesOrNo);
        String memberStateKey = ConstantRedisKey.getMemberStateRedisKeyByShopId(shopId);
        String memberStateValue = "2";
        if ("no".equalsIgnoreCase(yesOrNo)) {
            memberStateValue = "1";
        }
        redisUtil.set(memberStateKey, memberStateValue);
        return shpShopMapper.updateShopMember(shopId, yesOrNo);
    }

    @Override
    public List<ShpShop> listShpShopForInitVip() {
        return shpShopMapper.listShpShopForInitVip();
    }

    @Override
    public List<ShpShop> listVipExpireShop(String expireTime) {
        return shpShopMapper.listVipExpireShop(expireTime);
    }

    @Override
    public List<VoMemShop> queryMemShopList(ParamMemShop paramMemShop) {
        List<VoMemShop> memShops = shpShopMapper.listMemShop(paramMemShop);
        return memShops;
    }

    @Override
    public VoMemShopById getForAdminVoMemShopById(Integer shopId) {
        VoMemShopById voMemShopById = shpShopMapper.getVoMemShopById(shopId);
        return voMemShopById;
    }

    @Override
    public ShpShop updateShpShopMember(Integer fkShpShopId, Integer payMonth) {
        ShpShop shpShop = this.getShpShopById(fkShpShopId + "");

        if (LocalUtils.isEmptyAndNull(shpShop)) {
            return null;
        }
        Date nowDate = new Date();
        shpShop.setIsMember("yes");
        //0:?????????; 1:????????????;2:????????????;3:????????????
        shpShop.setMemberState(2);
        shpShop.setUpdateTime(nowDate);
        //????????????:  1:??????; 3:??????; 6:?????????; 12:??????; 36:?????????;
        shpShop.setPayMonth(payMonth);
        //??????????????????
        shpShop.setTotalMonth(shpShop.getTotalMonth() + payMonth);
        //??????????????????????????????????????????????????????
        if (shpShop.getPayStartTime() == null) {
            shpShop.setPayStartTime(nowDate);
        }
        if (shpShop.getPayEndTime() == null || shpShop.getPayEndTime().before(nowDate)) {
            shpShop.setPayStartTime(nowDate);
        }
        //??????????????????
        Date endDate = shpShop.getPayEndTime();
        if (shpShop.getPayEndTime() == null || shpShop.getPayEndTime().before(shpShop.getTryEndTime())) {
            endDate = shpShop.getTryEndTime();
        } else {
            if (shpShop.getPayEndTime().after(endDate)) {
                endDate = nowDate;
            }
        }
        Date newPayEndTime = DateUtil.addMonthsFromOldDate(endDate, payMonth).getTime();
        //??????????????????????????????
        shpShop.setPayEndTime(newPayEndTime);
        //???????????????????????????????????????
        this.updateShpShop(shpShop);
        //??????redis???????????????,???:???????????????,??????????????????;
        Integer myShopId = fkShpShopId;

        //????????????; no:????????? | yes:??????
        String isMemberKey = ConstantRedisKey.getShopMemberRedisKeyByShopId(myShopId);
        redisUtil.set(isMemberKey, shpShop.getIsMember());

        //????????????: 0:?????????(???????????????); 1:????????????;2:????????????;3:????????????
        String memberStateKey = ConstantRedisKey.getMemberStateRedisKeyByShopId(myShopId);
        redisUtil.set(memberStateKey, "2");

        //??????????????????
        String vipExpireStr = redisUtil.get(ConstantRedisKey.getVipExpireRedisKeyByShopId(fkShpShopId));
        //??????????????????redis????????????
        String vipExpireKey = ConstantRedisKey.getVipExpireRedisKeyByShopId(fkShpShopId);
        redisUtil.set(vipExpireKey, DateUtil.formatShort(newPayEndTime));
        return shpShop;
    }

    @Override
    public List<VoInviteShop> listInviteShop(Object[] userIdArray, String time) {
        if (LocalUtils.isEmptyAndNull(userIdArray)) {
            return null;
        }
        String userIds = LocalUtils.packString(userIdArray);

        List<VoInviteShop> listInviteShop = shpShopMapper.listInviteShop(userIds, time);
        if (!LocalUtils.isEmptyAndNull(listInviteShop)) {
            for (VoInviteShop inviteShop : listInviteShop) {
                try {
                    //???????????????????????????????????????
                    String shopPayStartTime = inviteShop.getShopPayStartTime();
                    String shopInsertTime = inviteShop.getShopInsertTime();
                    String userInsertTime = inviteShop.getUserInsertTime();
                    String memberState = inviteShop.getMemberState() + "";
                    String memberStateText = "???????????????";
                    switch (memberState) {
                        case "3":
                        case "2":
                            inviteShop.setShowTime("???????????????" + DateUtil.formatShort(DateUtil.parse(shopPayStartTime)));
                            memberStateText = "????????????";
                            break;
                        case "1":
                            inviteShop.setShowTime("???????????????" + DateUtil.formatShort(DateUtil.parse(shopInsertTime)));
                            memberStateText = "????????????";
                            break;
                        case "0":
                            inviteShop.setShowTime("???????????????" + DateUtil.formatShort(DateUtil.parse(shopInsertTime)));
                            memberStateText = "????????????";
                            break;
                        default:
                            inviteShop.setShowTime("???????????????" + DateUtil.formatShort(DateUtil.parse(userInsertTime)));
                            memberState = "-1";
                            break;
                    }
                    //????????????????????????????????????;????????????????????????,????????????????????????
                    String headImgUrl = inviteShop.getUserHeadImgUrl();
                    //??????????????????????????????,????????????????????????;
                    String showName = "???????????????" + inviteShop.getNickname();
                    if (!"-1".equals(memberState)) {
                        String shopHeadImgUrl = inviteShop.getShopHeadImgUrl();
                        //???????????????????????????
                        if (!shopHeadImgUrl.toLowerCase().contains("headimg.png")) {
                            headImgUrl = shopHeadImgUrl;
                        }
                        showName = "???????????????" + inviteShop.getShopName();
                    }
                    String username = DESEncrypt.decodeUsername(inviteShop.getUsername());
                    inviteShop.setUsername(LocalUtils.returnAsteriskPhone(username));
                    inviteShop.setMemberState(memberState);
                    inviteShop.setShowName(showName);
                    inviteShop.setHeadImgUrl(servicesUtil.formatImgUrl(headImgUrl));
                    inviteShop.setUserHeadImgUrl(null);
                    inviteShop.setShopHeadImgUrl(null);
                    inviteShop.setShopPayStartTime(null);
                    inviteShop.setShopInsertTime(null);
                    inviteShop.setUserInsertTime(null);
                    inviteShop.setNickname(null);
                    inviteShop.setShopName(null);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new MyException(e.getMessage());
                }
            }
        }

        return listInviteShop;
    }

    @Override
    public List<Map<String, Object>> countInviteShop(Object[] userIdArray, String time) {
        String userIds = LocalUtils.packString(userIdArray);
        return shpShopMapper.countInviteShop(userIds, time);
    }

    @Override
    public boolean existsShopName(int userId, String shopName) {
        return shpShopMapper.existsShopName(userId, shopName) > 0;
    }


    @Override
    public boolean existsShopNameExceptOwn(int shopId, int userId, String shopName) {
        return shpShopMapper.existsShopNameExceptOwn(shopId, userId, shopName) > 0;
    }

    @Override
    public void extendTryEndTime(int shopId, Date newTryEndTime) {
        ShpShop shpShop = (ShpShop) shpShopMapper.getObjectById(shopId);
        if (!LocalUtils.isEmptyAndNull(shpShop)) {
            // 0:?????????; 1:????????????;2:????????????;3:????????????
            if (shpShop.getMemberState() <= 1) {
                //???????????????????????????
                shpShop.setMemberState(1);
                shpShop.setUpdateTime(new Date());
                shpShop.setTryEndTime(newTryEndTime);
                shpShopMapper.updateObject(shpShop);
                String memberStateKey = ConstantRedisKey.getMemberStateRedisKeyByShopId(shopId);
                String vipExpireKey = ConstantRedisKey.getVipExpireRedisKeyByShopId(shopId);
                redisUtil.set(memberStateKey, "1");
                redisUtil.set(vipExpireKey, DateUtil.formatShort(newTryEndTime));
            } else {
                throw new MyException("?????????????????????!");
            }
        }
    }


    /**
     * 1.?????? ?????????????????????;
     * ????????????:
     * a.????????????;
     * b.????????????;
     * c.????????????????????????-9;
     * d.????????????????????????;
     * e.????????????????????????;
     * (?????????????????????shp_user_permission_ref??????????????????,
     * ???????????????????????????????????????????????????????????????,??????????????????,???????????????????????????????????????)
     * f.???????????????????????????;
     * g.????????????????????????bossUserId;
     *
     * @param shopId
     * @param oldUsername ??????????????????
     * @param newUsername ??????????????????
     */
    @Override
    public void changeShopkeeper(int shopId, String oldUsername, String newUsername) {
        ShpShop shpShop = getShpShopById(shopId + "");
        ShpDetail shpDetail = shpDetailService.selectByShopId(shopId);
        if (LocalUtils.isEmptyAndNull(shpShop) || LocalUtils.isEmptyAndNull(shpDetail)) {
            throw new MyException("???????????????!");
        }

        Integer oldUserId = shpUserService.getShpUserIdByUsername(oldUsername);
        if (!shpShop.getFkShpUserId().equals(oldUserId)) {
            throw new MyException("[" + oldUsername + "]???????????????????????????!");
        }
        Integer newUserId = shpUserService.getShpUserIdByUsername(newUsername);
        //????????????????????????????????????(????????????)
        ShpUserShopRef newUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, newUserId);
        // -9:???????????????; 0?????????(?????????)   1?????????
        if (LocalUtils.isEmptyAndNull(newUserShopRef) || !"1".equals(newUserShopRef.getState())) {
            throw new MyException("[" + newUsername + "]??????????????????????????????!");
        }
        //????????????
        ShpUserShopRef oldUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, oldUserId);
        if (LocalUtils.isEmptyAndNull(oldUserShopRef)) {
            throw new MyException("[" + oldUsername + "]??????????????????????????????!");
        }

        //?????????????????????userTypeId,??????????????????????????????;
        Integer oldUserTypeId = newUserShopRef.getFkShpUserTypeId();
        newUserShopRef.setFkShpUserTypeId(-9);
        oldUserShopRef.setFkShpUserTypeId(oldUserTypeId);
        shpShop.setFkShpUserId(newUserId);
        shpShop.setUpdateTime(new Date());
        shpShop.setContact(newUsername);
        shpDetail.setShopkeeperPhone(newUsername);
        shpShopMapper.updateObject(shpShop);
        shpDetailService.updateShpDetailByShopId(shpDetail);
        shpUserShopRefService.updateObject(oldUserShopRef);
        shpUserShopRefService.updateObject(newUserShopRef);

        //?????????????????????????????????,???????????????????????????;??????????????????,??????????????????????????????,???????????????????????????
        shpUserPermissionRefService.deleteUserPermissionByUserId(shopId, oldUserId + "");
        //???????????????????????????????????????????????????;
        shpUserPermissionRefService.changeUserPermission(shopId, oldUserId, newUserId);
        //??????redis???bossUserId?????????;
        String bossUserIdKey = ConstantRedisKey.getBossUserIdRedisKeyByShopId(shopId);
        redisUtil.set(bossUserIdKey, newUserId + "");
        //??????????????????redis????????????
        servicesUtil.updateRedisUserPerm(shopId, oldUserId);
        servicesUtil.updateRedisUserPerm(shopId, newUserId);
    }

    /**
     * APP??????2.0
     * 1.?????? ?????????????????????;
     * ????????????:
     * a.????????????;
     * b.????????????;
     * c.????????????????????????-9;
     * d.????????????????????????;
     * e.????????????????????????;
     * (?????????????????????shp_user_permission_ref??????????????????,
     * ???????????????????????????????????????????????????????????????,??????????????????,???????????????????????????????????????)
     * f.???????????????????????????;
     * g.????????????????????????bossUserId;
     *
     * @param shopId
     * @param oldUsername ??????????????????
     * @param newUsername ??????????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeShopkeeperNew(int shopId, String oldUsername, String newUsername) throws Exception {
        ShpShop shpShop = getShpShopById(shopId + "");
        ShpDetail shpDetail = shpDetailService.selectByShopId(shopId);
        if (LocalUtils.isEmptyAndNull(shpShop) || LocalUtils.isEmptyAndNull(shpDetail)) {
            throw new MyException("???????????????!");
        }

        Integer oldUserId = shpUserService.getShpUserIdByUsername(oldUsername);
        if (!shpShop.getFkShpUserId().equals(oldUserId)) {
            throw new MyException("[" + oldUsername + "]???????????????????????????!");
        }
        Integer newUserId = shpUserService.getShpUserIdByUsername(newUsername);
        //????????????????????????????????????(????????????)
        ShpUserShopRef newUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, newUserId);
        // -9:???????????????; 0?????????(?????????)   1?????????
        if (LocalUtils.isEmptyAndNull(newUserShopRef) || !"1".equals(newUserShopRef.getState())) {
            throw new MyException("[" + newUsername + "]??????????????????????????????!");
        }
        //????????????
        ShpUserShopRef oldUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, oldUserId);
        if (LocalUtils.isEmptyAndNull(oldUserShopRef)) {
            throw new MyException("[" + oldUsername + "]??????????????????????????????!");
        }
        try {
            //?????????????????????userTypeId,??????????????????????????????;
            Integer oldUserTypeId = newUserShopRef.getFkShpUserTypeId();

            String oldRemark = "#???????????????,newUserId:"+newUserId;
            String newRemark = "#???????????????,oldUserId:"+oldUserId;

            newUserShopRef.setFkShpUserTypeId(-9);
            newUserShopRef.setUpdateTime(new Date());
            newUserShopRef.setRemark(newRemark);
            oldUserShopRef.setFkShpUserTypeId(oldUserTypeId);
            oldUserShopRef.setUpdateTime(new Date());
            oldUserShopRef.setRemark(oldRemark);
            shpShop.setFkShpUserId(newUserId);
            shpShop.setUpdateTime(new Date());
            shpShop.setContact(newUsername);
            shpDetail.setShopkeeperPhone(newUsername);
            shpShopMapper.updateObject(shpShop);
            shpDetailService.updateShpDetailByShopId(shpDetail);
            shpUserShopRefService.updateObject(oldUserShopRef);
            shpUserShopRefService.updateObject(newUserShopRef);

            //?????????????????????????????????,???????????????????????????;??????????????????,??????????????????????????????,???????????????????????????
            shpPermUserRefService.deleteShpPermUserRefByUserId(shopId, oldUserId + "");
            //???????????????????????????????????????????????????;
            shpPermUserRefService.changeShpPermUserRef(shopId, oldUserId, newUserId);
            //??????redis???bossUserId?????????;
            String bossUserIdKey = ConstantRedisKey.getBossUserIdRedisKeyByShopId(shopId);
            redisUtil.set(bossUserIdKey, newUserId + "");
            //??????????????????redis????????????
            servicesUtil.updateRedisUserPerm(shopId, oldUserId);
            servicesUtil.updateRedisUserPerm(shopId, newUserId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("?????????????????????!");
        }
    }

    @Override
    public String removeShop(int shopId, int userId) {
        ShpUserShopRef shpUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, userId);
        if (LocalUtils.isEmptyAndNull(shpUserShopRef)) {
            throw new MyException("????????????????????????");
        }
        shpUserShopRef.setUpdateTime(new Date());
        shpUserShopRef.setState("-9");
        String remark = shpUserShopRef.getRemark();
        remark = LocalUtils.isEmptyAndNull(remark) ? "" : remark + "#";
        shpUserShopRef.setRemark(remark + "??????????????????????????????");
        shpUserShopRefService.updateUserShopRef(shpUserShopRef);
        //?????????????????????????????????????????????
        shpPermUserRefService.deleteShpPermUserRefByUserId(shopId, userId + "");
        //??????????????????; ???????????????????????????shopId
        ShpUser shpUser = shpUserService.packShpUserForLogin(userId, 0, "0");
        shpUserService.updateShpUser(shpUser);
        return shpUserShopRef.getName();
    }

    @Override
    public Integer getShopCount(int userId) {
        return shpShopMapper.getShopCount(userId);
    }

    @Override
    public int countOwnShopCount(int userId) {
        return shpShopMapper.countOwnShopCount(userId);
    }

    /**
     * ????????????????????????????????????
     *
     * @param param
     * @return
     */
    @Override
    public VoShopInfo getShpShopInfoByNumber(ParamGetShopInfoByNumber param) {
        VoShopInfo voShopInfo = shpShopMapper.getShpShopInfoByNumber(param);
        if (voShopInfo != null) {
            voShopInfo.setAddress(voShopInfo.getProvince() + voShopInfo.getCity());
            voShopInfo.setCity(null);
            voShopInfo.setProvince(null);
        }
        return voShopInfo;
    }
}
