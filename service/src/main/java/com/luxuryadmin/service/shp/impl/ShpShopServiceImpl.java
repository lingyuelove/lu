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
            //添加店铺与用户的关联关系;
            shpUserShopRefService.saveShpUserShopRef(userId, shopId, nickname, -9, userId);
            //初始化模板数据
            //初始化店铺分类
            proClassifyService.initProClassifyByShopIdAndUserId(shopId, userId);
            //初始化店铺属性
            proAttributeService.initProAttributeByShopIdAndUserId(shopId, userId);
            //初始化订单类型
            ordTypeService.initOrdTypeByShopIdAndUserId(shopId, userId);
            //初始化商品来源
            proSourceService.initProSourceByShopIdAndUserId(shopId, userId);
            //初始化附件表
            proAccessoryService.initProAccessoryByShopIdAndUserId(shopId, userId);
            //初始化店铺模板权限(系统自动创建)(新v2.6.7)
            shpPermTplService.initShopSystemPerm(shopId, userId);
            //初始化销售渠道
            proSaleChannelService.initProSaleChannelByShopIdAndUserId(shopId, userId);
            //初始化商品默认标签
            shpAfterSaleGuaranteeService.initShpAfterSaleGuarantee(shopId, userId);
            //初始化店铺服务
            shpServiceService.initSingleShpServiceType(shopId, userId);
            //初始化财务流水类型
            finShopRecordTypeService.initFinShopRecordTypeByShopIdAndUserId(shopId, userId);
            //初始化友商配置
            bizLeaguerConfigService.buildBizLeaguerConfig(shopId, userId);
            //为新员工创建本月的薪资
            finSalaryService.initFinSalary(shopId, userId, new Date());
            //初始化商品位置列表
            proDynamicService.saveInitializeDynamic(shopId);

            //更新缓存
            String token = redisUtil.get(ConstantRedisKey.SHP_TOKEN_ + username);
            String shopIdKey = ConstantRedisKey.getShopIdRedisKeyByToken(token);
            int day = 30;
            redisUtil.setEx(shopIdKey, shopId + "", day);
            //更新店铺缓存
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
            log.error("=======【开店失败】====: " + e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("开店失败: " + e.getMessage());
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
            log.error("=======【更新店铺信息失败】====: " + e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("更新店铺信息失败: " + e.getMessage());
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
     * 格式化店铺信息
     *
     * @param voShpShops
     */
    private void formatVoShpShops(List<VoShpShop> voShpShops) {
        if (!CollectionUtils.isEmpty(voShpShops)) {
            for (VoShpShop voShpShop : voShpShops) {
                Integer shopId = voShpShop.getId();

                //在售商品数量
                Integer onSaleProdNum = proProductService.getOnSaleProductNumByShopIdByAdmin(shopId);
                voShpShop.setOnSaleProdNum(onSaleProdNum);
                //上传商品数量
                Integer uploadProdNum = proProductService.getAllUploadProductNumByShopId(shopId);
                voShpShop.setUploadProdNum(uploadProdNum);
                //开单数量
                Integer confirmOrderNum = ordOrderService.getTotalOrderNumByShopId(shopId);
                voShpShop.setConfirmOrderNum(confirmOrderNum);
                //在职员工数量
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
            //短信通知
            String phone = shop.getContact();
            String shopName = shop.getName();
            //注销店铺, 把店铺状态改成"-99"; 其它表进行物理删除
            shpShopMapper.changeShopState(shopId, "-99");
            //把所有默认登录该店铺的员工,全部踢出登录;
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
            //删除员工与店铺的关系
            shpUserShopRefService.deleteUserShopRefByShopId(shopId);
            //删除店铺商品
            proProductService.deleteProProductByShopId(shopId);
            //删除店铺分享商品记录
            proShareService.deleteProShareByShopId(shopId);
            //删除店铺商品下载记录
            proDownloadImgService.deleteProDownloadImgByShopId(shopId);
            //删除店铺商品锁单记录
            proLockRecordService.deleteProLockRecordByShopId(shopId);
            //删除店铺分类
            proClassifyService.deleteProClassifyByShopId(shopId);
            //删除店铺属性
            proAttributeService.deleteProAttributeByShopId(shopId);
            //删除店铺订单类型
            ordTypeService.deleteOrdTypeByShopId(shopId);
            //删除商品来源
            proSourceService.deleteProSourceByShopId(shopId);
            //删除商品附件表
            proAccessoryService.deleteProAccessoryByShopId(shopId);
            //删除店铺权限模板
            shpUserPermissionTplService.deleteShpUserPermissionTplByShopId(shopId);
            //删除店铺权限模板(新v2.6.7)
            shpPermTplService.deleteShpPermTplByShopId(shopId);
            //删除店铺权限
            shpPermUserRefService.deleteShpUserPermissionRefByShopId(shopId);
            //删除店铺销售渠道
            proSaleChannelService.deleteProSaleChannelByShopId(shopId);
            //删除店铺默标签
            shpAfterSaleGuaranteeService.deleteShpAfterSaleGuaranteeByShopId(shopId);
            //删除店铺服务类型
            shpServiceService.deleteShpServiceTypeByShopId(shopId);
            //删除店铺服务记录
            shpServiceService.deleteShpServiceRecordByShopId(shopId);
            //删除店铺财务流水类型
            finShopRecordTypeService.deleteFinShopRecordTypeByShopId(shopId);
            //删除订单,凭证,凭证分享记录
            ordOrderService.deleteOrderByShopId(shopId);
            //删除店铺薪资记录
            finSalaryService.deleteFinSalaryForDestroyShop(shopId);
            //删除店铺操作记录
            shpOperateLogService.deleteShpOperateLogByShopId(shopId);
            //删除店铺盘点数据
            proCheckService.deleteProCheckByShopId(shopId);
            //删除店铺友商信息
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
        //0:非会员; 1:体验会员;2:正式会员;3:靓号会员
        shpShop.setMemberState(2);
        shpShop.setUpdateTime(nowDate);
        //付费月数:  1:月付; 3:季付; 6:半年付; 12:年付; 36:三年付;
        shpShop.setPayMonth(payMonth);
        //累计付费月数
        shpShop.setTotalMonth(shpShop.getTotalMonth() + payMonth);
        //付费使用开始时间设置新的会员开始时间
        if (shpShop.getPayStartTime() == null) {
            shpShop.setPayStartTime(nowDate);
        }
        if (shpShop.getPayEndTime() == null || shpShop.getPayEndTime().before(nowDate)) {
            shpShop.setPayStartTime(nowDate);
        }
        //会员结束时间
        Date endDate = shpShop.getPayEndTime();
        if (shpShop.getPayEndTime() == null || shpShop.getPayEndTime().before(shpShop.getTryEndTime())) {
            endDate = shpShop.getTryEndTime();
        } else {
            if (shpShop.getPayEndTime().after(endDate)) {
                endDate = nowDate;
            }
        }
        Date newPayEndTime = DateUtil.addMonthsFromOldDate(endDate, payMonth).getTime();
        //设置新的会员结束时间
        shpShop.setPayEndTime(newPayEndTime);
        //数据库封信店铺会员过期时间
        this.updateShpShop(shpShop);
        //更新redis的店铺信息,如:是否为会员,会员到期时间;
        Integer myShopId = fkShpShopId;

        //是否会员; no:非会员 | yes:会员
        String isMemberKey = ConstantRedisKey.getShopMemberRedisKeyByShopId(myShopId);
        redisUtil.set(isMemberKey, shpShop.getIsMember());

        //会员状态: 0:非会员(会员已过期); 1:体验会员;2:正式会员;3:靓号会员
        String memberStateKey = ConstantRedisKey.getMemberStateRedisKeyByShopId(myShopId);
        redisUtil.set(memberStateKey, "2");

        //先取缓存数据
        String vipExpireStr = redisUtil.get(ConstantRedisKey.getVipExpireRedisKeyByShopId(fkShpShopId));
        //会员过期时间redis重新设置
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
                    //选择一个合适的时间进行显示
                    String shopPayStartTime = inviteShop.getShopPayStartTime();
                    String shopInsertTime = inviteShop.getShopInsertTime();
                    String userInsertTime = inviteShop.getUserInsertTime();
                    String memberState = inviteShop.getMemberState() + "";
                    String memberStateText = "未注册店铺";
                    switch (memberState) {
                        case "3":
                        case "2":
                            inviteShop.setShowTime("店铺付费：" + DateUtil.formatShort(DateUtil.parse(shopPayStartTime)));
                            memberStateText = "会员店铺";
                            break;
                        case "1":
                            inviteShop.setShowTime("店铺注册：" + DateUtil.formatShort(DateUtil.parse(shopInsertTime)));
                            memberStateText = "体验店铺";
                            break;
                        case "0":
                            inviteShop.setShowTime("店铺注册：" + DateUtil.formatShort(DateUtil.parse(shopInsertTime)));
                            memberStateText = "过期店铺";
                            break;
                        default:
                            inviteShop.setShowTime("用户注册：" + DateUtil.formatShort(DateUtil.parse(userInsertTime)));
                            memberState = "-1";
                            break;
                    }
                    //选择一个合适的头像来显示;优先显示店铺头像,默认显示用户头像
                    String headImgUrl = inviteShop.getUserHeadImgUrl();
                    //选择合适的名称来显示,优先显示用户昵称;
                    String showName = "用户昵称：" + inviteShop.getNickname();
                    if (!"-1".equals(memberState)) {
                        String shopHeadImgUrl = inviteShop.getShopHeadImgUrl();
                        //店铺头像是否为默认
                        if (!shopHeadImgUrl.toLowerCase().contains("headimg.png")) {
                            headImgUrl = shopHeadImgUrl;
                        }
                        showName = "店铺名称：" + inviteShop.getShopName();
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
            // 0:非会员; 1:体验会员;2:正式会员;3:靓号会员
            if (shpShop.getMemberState() <= 1) {
                //重新设置为体验会员
                shpShop.setMemberState(1);
                shpShop.setUpdateTime(new Date());
                shpShop.setTryEndTime(newTryEndTime);
                shpShopMapper.updateObject(shpShop);
                String memberStateKey = ConstantRedisKey.getMemberStateRedisKeyByShopId(shopId);
                String vipExpireKey = ConstantRedisKey.getVipExpireRedisKeyByShopId(shopId);
                redisUtil.set(memberStateKey, "1");
                redisUtil.set(vipExpireKey, DateUtil.formatShort(newTryEndTime));
            } else {
                throw new MyException("已经是会员级别!");
            }
        }
    }


    /**
     * 1.添加 变更经营者接口;
     * 业务逻辑:
     * a.调换身份;
     * b.互换权限;
     * c.经营者状态设置成-9;
     * d.必须是同一个店铺;
     * e.需要重新更新权限;
     * (经营者不需要在shp_user_permission_ref表里面有权限,
     * 直接把新经营者之前的权限更新给旧经营者即可,为了兼容数据,更新之前删除在改店铺的权限)
     * f.更新经营者联系方式;
     * g.更新缓存经营者的bossUserId;
     *
     * @param shopId
     * @param oldUsername 旧经营者帐号
     * @param newUsername 新经营者帐号
     */
    @Override
    public void changeShopkeeper(int shopId, String oldUsername, String newUsername) {
        ShpShop shpShop = getShpShopById(shopId + "");
        ShpDetail shpDetail = shpDetailService.selectByShopId(shopId);
        if (LocalUtils.isEmptyAndNull(shpShop) || LocalUtils.isEmptyAndNull(shpDetail)) {
            throw new MyException("店铺不存在!");
        }

        Integer oldUserId = shpUserService.getShpUserIdByUsername(oldUsername);
        if (!shpShop.getFkShpUserId().equals(oldUserId)) {
            throw new MyException("[" + oldUsername + "]不是该店铺的经营者!");
        }
        Integer newUserId = shpUserService.getShpUserIdByUsername(newUsername);
        //查找新帐号是否加入该店铺(新经营者)
        ShpUserShopRef newUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, newUserId);
        // -9:已逻辑删除; 0：禁用(或辞职)   1：正常
        if (LocalUtils.isEmptyAndNull(newUserShopRef) || !"1".equals(newUserShopRef.getState())) {
            throw new MyException("[" + newUsername + "]此帐号并未加入该店铺!");
        }
        //原经营者
        ShpUserShopRef oldUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, oldUserId);
        if (LocalUtils.isEmptyAndNull(oldUserShopRef)) {
            throw new MyException("[" + oldUsername + "]此帐号并未加入该店铺!");
        }

        //新经营者之前的userTypeId,将此值赋予给旧经营者;
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

        //找到新经营者之前的权限,从而赋予给旧经营者;为了兼容数据,旧经营者更新权限之前,删除在改店铺的权限
        shpUserPermissionRefService.deleteUserPermissionByUserId(shopId, oldUserId + "");
        //把新经营者之前的权限更新给旧经营者;
        shpUserPermissionRefService.changeUserPermission(shopId, oldUserId, newUserId);
        //更新redis中bossUserId的缓存;
        String bossUserIdKey = ConstantRedisKey.getBossUserIdRedisKeyByShopId(shopId);
        redisUtil.set(bossUserIdKey, newUserId + "");
        //更新他们两人redis中的权限
        servicesUtil.updateRedisUserPerm(shopId, oldUserId);
        servicesUtil.updateRedisUserPerm(shopId, newUserId);
    }

    /**
     * APP权限2.0
     * 1.添加 变更经营者接口;
     * 业务逻辑:
     * a.调换身份;
     * b.互换权限;
     * c.经营者状态设置成-9;
     * d.必须是同一个店铺;
     * e.需要重新更新权限;
     * (经营者不需要在shp_user_permission_ref表里面有权限,
     * 直接把新经营者之前的权限更新给旧经营者即可,为了兼容数据,更新之前删除在改店铺的权限)
     * f.更新经营者联系方式;
     * g.更新缓存经营者的bossUserId;
     *
     * @param shopId
     * @param oldUsername 旧经营者帐号
     * @param newUsername 新经营者帐号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeShopkeeperNew(int shopId, String oldUsername, String newUsername) throws Exception {
        ShpShop shpShop = getShpShopById(shopId + "");
        ShpDetail shpDetail = shpDetailService.selectByShopId(shopId);
        if (LocalUtils.isEmptyAndNull(shpShop) || LocalUtils.isEmptyAndNull(shpDetail)) {
            throw new MyException("店铺不存在!");
        }

        Integer oldUserId = shpUserService.getShpUserIdByUsername(oldUsername);
        if (!shpShop.getFkShpUserId().equals(oldUserId)) {
            throw new MyException("[" + oldUsername + "]不是该店铺的经营者!");
        }
        Integer newUserId = shpUserService.getShpUserIdByUsername(newUsername);
        //查找新帐号是否加入该店铺(新经营者)
        ShpUserShopRef newUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, newUserId);
        // -9:已逻辑删除; 0：禁用(或辞职)   1：正常
        if (LocalUtils.isEmptyAndNull(newUserShopRef) || !"1".equals(newUserShopRef.getState())) {
            throw new MyException("[" + newUsername + "]此帐号并未加入该店铺!");
        }
        //原经营者
        ShpUserShopRef oldUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, oldUserId);
        if (LocalUtils.isEmptyAndNull(oldUserShopRef)) {
            throw new MyException("[" + oldUsername + "]此帐号并未加入该店铺!");
        }
        try {
            //新经营者之前的userTypeId,将此值赋予给旧经营者;
            Integer oldUserTypeId = newUserShopRef.getFkShpUserTypeId();

            String oldRemark = "#变更经营者,newUserId:"+newUserId;
            String newRemark = "#变更经营者,oldUserId:"+oldUserId;

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

            //找到新经营者之前的权限,从而赋予给旧经营者;为了兼容数据,旧经营者更新权限之前,删除在改店铺的权限
            shpPermUserRefService.deleteShpPermUserRefByUserId(shopId, oldUserId + "");
            //把新经营者之前的权限更新给旧经营者;
            shpPermUserRefService.changeShpPermUserRef(shopId, oldUserId, newUserId);
            //更新redis中bossUserId的缓存;
            String bossUserIdKey = ConstantRedisKey.getBossUserIdRedisKeyByShopId(shopId);
            redisUtil.set(bossUserIdKey, newUserId + "");
            //更新他们两人redis中的权限
            servicesUtil.updateRedisUserPerm(shopId, oldUserId);
            servicesUtil.updateRedisUserPerm(shopId, newUserId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("经营者变更失败!");
        }
    }

    @Override
    public String removeShop(int shopId, int userId) {
        ShpUserShopRef shpUserShopRef = shpUserShopRefService.getShpUserShopRefOnJobByUserId(shopId, userId);
        if (LocalUtils.isEmptyAndNull(shpUserShopRef)) {
            throw new MyException("该员工不在该店铺");
        }
        shpUserShopRef.setUpdateTime(new Date());
        shpUserShopRef.setState("-9");
        String remark = shpUserShopRef.getRemark();
        remark = LocalUtils.isEmptyAndNull(remark) ? "" : remark + "#";
        shpUserShopRef.setRemark(remark + "该员工主动退出该店铺");
        shpUserShopRefService.updateUserShopRef(shpUserShopRef);
        //删除该用户在该店铺所拥有的权限
        shpPermUserRefService.deleteShpPermUserRefByUserId(shopId, userId + "");
        //取消默认登录; 取消关联当前店铺的shopId
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
     * 根据店铺编号查询店铺信息
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
