package com.luxuryadmin.service.biz.impl;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.mapper.biz.BizLeaguerMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamSpecificLeaguerProductQuery;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.biz.BizLeaguerConfigService;
import com.luxuryadmin.service.biz.BizLeaguerService;
import com.luxuryadmin.service.biz.BizLogService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpWechatService;
import com.luxuryadmin.vo.biz.VoBizLeaguer;
import com.luxuryadmin.vo.biz.VoCanSeeLeaguerPriceInfo;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.shp.VoShpWechat;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 友商首页--业务逻辑层
 *
 * @author monkey king
 * @date 2020-01-11 20:12:23
 */
@Slf4j
@Service
public class BizLeaguerServiceImpl implements BizLeaguerService {

    private static final String TYPE = "leaguer";

    @Resource
    private BizLeaguerMapper bizLeaguerMapper;

    @Resource
    private BizLeaguerConfigService bizLeaguerConfigService;

    @Autowired
    private BizLogService bizLogService;


    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ShpWechatService shpWechatService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ServicesUtil servicesUtil;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Override
    public List<VoBizLeaguer> searchShop(int shopId, String shopNumber) {
        return bizLeaguerMapper.searchShop(shopId, shopNumber);
    }

    @Override
    public VoBizLeaguer getLeaguerShop(int myShopId, int leaguerShopId) {
        VoBizLeaguer voBizLeaguer = bizLeaguerMapper.getLeaguerShop(myShopId, leaguerShopId);
        if (LocalUtils.isEmptyAndNull(voBizLeaguer)) {
            return voBizLeaguer;
        }
        //查看是否添加过此友商
        String leaguerFriendState = this.getLeaguerFriendState(myShopId, leaguerShopId);
        voBizLeaguer.setLeaguerFriendState(leaguerFriendState);
        List<VoShpWechat> voShpWechatList = shpWechatService.listShpWechat(leaguerShopId);
        voBizLeaguer.setVoShpWechatList(voShpWechatList);
        return voBizLeaguer;
    }

    @Override
    public int saveBizLeaguer(BizLeaguer bizLeaguer) {
        return bizLeaguerMapper.saveObject(bizLeaguer);
    }


    @Override
    public List<VoLeaguerProduct> listBizLeaguerProductByShopIds(ParamLeaguerProductQuery paramQuery) {
        Integer shopId = paramQuery.getShopId();
        //查找该店铺的友商成员; BizLeaguer.state='20';
        List<Integer> leaguerShopId = bizLeaguerMapper.listBizLeaguerShopIdByShopId(shopId, paramQuery.getOnlyShowTopLeaguer());
        //获取对其不可见的友商id;只返回shopId
        List<Integer> noVisibleIds = bizLeaguerMapper.listBizLeaguerNoVisible(shopId);
        List<Integer> noWantSeeShopIds = bizLeaguerMapper.listBizLeaguerNoWantSee(shopId);
        noVisibleIds.addAll(noWantSeeShopIds);
        List<VoLeaguerProduct> leaguerProduct = null;
        if (!LocalUtils.isEmptyAndNull(leaguerShopId)) {
            leaguerShopId.removeAll(noVisibleIds);
            if (LocalUtils.isEmptyAndNull(leaguerShopId)) {
                return null;
            }
            String leaShopIds = LocalUtils.packString(leaguerShopId.toArray());
            PageHelper.startPage(Integer.parseInt(paramQuery.getPageNum()), 10);
            leaguerProduct = proProductService.listBizLeaguerProduct(leaShopIds, paramQuery);
            for (VoLeaguerProduct leaPro : leaguerProduct) {
                String smallImg = leaPro.getSmallImg();
                if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                    leaPro.setSmallImg(servicesUtil.formatImgUrl(leaPro.getSmallImg(), true));
                }

                //是否可以查看友商价
                //我的店铺ID 10052 友商店铺ID 10087
                Boolean isCanSeeTradePrice = isCanSeeTradePrice(leaPro.getShopId(), shopId);
                //如果不可以查看友商价，则隐藏【友商价】字段
                isCanSeeTradePrice = null == isCanSeeTradePrice ? Boolean.FALSE : isCanSeeTradePrice;
                if (!isCanSeeTradePrice) {
                    leaPro.setTradePrice(null);
                }

                //是否可以查看销售价
                Boolean isCanSeeSalePrice = isCanSeeSalePrice(leaPro.getShopId(), shopId);
                //如果不可以查看销售价，则隐藏【销售价】字段
                if (!isCanSeeSalePrice) {
                    leaPro.setSalePrice(null);
                }
                //更新时间文本写死
                if (!LocalUtils.isEmptyAndNull(leaPro.getReleaseTime())) {
                    leaPro.setShowTime("更新时间:" + leaPro.getReleaseTime());
                }
            }
        }
        return leaguerProduct;
    }


    @Override
    public List<VoLeaguerProduct> listSpecificBizLeaguerProduct(ParamSpecificLeaguerProductQuery paramQuery, Integer shopId) {
        List<VoLeaguerProduct> leaguerProduct;
        PageHelper.startPage(Integer.parseInt(paramQuery.getPageNum()), 10);
        leaguerProduct = proProductService.listSpecificBizLeaguerProduct(paramQuery);
        for (VoLeaguerProduct leaPro : leaguerProduct) {
            String smallImg = leaPro.getSmallImg();
            if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                leaPro.setSmallImg(servicesUtil.formatImgUrl(leaPro.getSmallImg(), true));

                //是否可以查看友商价
                Boolean isCanSeeTradePrice = isCanSeeTradePrice(leaPro.getShopId(), shopId);
                //如果不可以查看友商价，则隐藏【友商价】字段
                if (!isCanSeeTradePrice) {
                    leaPro.setTradePrice(null);
                }

                //是否可以查看销售价
                Boolean isCanSeeSalePrice = isCanSeeSalePrice(leaPro.getShopId(), shopId);
                //如果不可以查看销售价，则隐藏【销售价】字段
                if (!isCanSeeSalePrice) {
                    leaPro.setSalePrice(null);
                }
                //更新时间文本写死
                if (!LocalUtils.isEmptyAndNull(leaPro.getReleaseTime())) {
                    leaPro.setShowTime("更新时间:" + leaPro.getReleaseTime());
                }
            }
        }
        return leaguerProduct;
    }

    @Override
    public List<VoLeaguerProduct> listSpecificBizUnionProduct(ParamSpecificLeaguerProductQuery paramQuery) {
        PageHelper.startPage(Integer.parseInt(paramQuery.getPageNum()), 10);
        List<VoLeaguerProduct> leaguerProduct = proProductService.listSpecificBizLeaguerProduct(paramQuery);
        for (VoLeaguerProduct leaPro : leaguerProduct) {
            String smallImg = leaPro.getSmallImg();
            if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                leaPro.setSmallImg(servicesUtil.formatImgUrl(leaPro.getSmallImg(), true));

                VoCanSeeLeaguerPriceInfo canSeeLeaguerPriceInfo = isCanSeePriceForUnion(paramQuery.getShopId(), paramQuery.getUserId());
                //是否可以查看友商价; 从商家联盟进来查看店铺的, 都可以查看同行价;修改于2021-09-18 22:26:00
                //Boolean isCanSeeTradePrice = canSeeLeaguerPriceInfo.getIsCanSeeTradePrice();
                ////如果不可以查看友商价，则隐藏【友商价】字段
                //if (!isCanSeeTradePrice) {
                //    leaPro.setTradePrice(null);
                //}
                //是否可以查看销售价
                Boolean isCanSeeSalePrice = canSeeLeaguerPriceInfo.getIsCanSeeSalePrice();
                //如果不可以查看销售价，则隐藏【销售价】字段
                if (!isCanSeeSalePrice) {
                    leaPro.setSalePrice(null);
                }
                //更新时间文本写死
                if (!LocalUtils.isEmptyAndNull(leaPro.getReleaseTime())) {
                    leaPro.setShowTime("更新时间:" + leaPro.getReleaseTime());
                }

            }
        }
        return leaguerProduct;
    }

    /**
     * 是否可以查看【销售价】
     *
     * @param leaguerShopIdA 查看友商的店铺,调用接口
     * @param leaguerShopIdB 友商的店铺ID
     * @return
     */
    @Override
    public Boolean isCanSeeSalePrice(Integer leaguerShopIdA, Integer leaguerShopIdB) {
        /**
         * Version1 2020-10
         * leaguerShopIdA 是否有查看 leaguerShopIdB的商品【销售价】的资格
         * （1）leaguerShopIdB 的 【biz_leaguer_config.is_can_see_sale_price】
         *          is_can_see_sale_price=0，直接结束返回
         *          is_can_see_sale_price=1，继续
         *  (2)【biz_leaguer.fk_inviter_shop_id】=leaguerShopIdA
         *     【biz_leaguer.fk_be_inviter_shop_id】=leaguerShopIdB
         *          is_can_see_sale_price=0，返回false
         *          is_can_see_sale_price=1，返回true
         */

        /**
         * Version2 2020-11-23
         * leaguerShopIdA 是否有查看 leaguerShopIdB的商品【友商价】的资格
         *  (1)【biz_leaguer.fk_inviter_shop_id】=leaguerShopIdA
         *     【biz_leaguer.fk_be_inviter_shop_id】=leaguerShopIdB
         *      is_can_see_sale_price=0，返回false
         *      is_can_see_sale_price=1，返回true
         */
        Boolean isCanSeeSalePrice = Boolean.FALSE;
//        try {
//            VoCanSeeLeaguerPriceInfo voCanSeeLeaguerPriceInfo =  bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(leaguerShopIdA,null);
//            if(null != voCanSeeLeaguerPriceInfo) {
//                isCanSeeSalePrice = voCanSeeLeaguerPriceInfo.getIsCanSeeSalePrice();
//            }
//        } catch (Exception e) {
//            log.error("" + e);
//        }
//        if(!isCanSeeSalePrice){
//            return Boolean.FALSE;
//        }

        BizLeaguer bizLeaguer = bizLeaguerMapper.getBizLeaguerByShopIdAndLeaguerShopId(leaguerShopIdA, leaguerShopIdB);
        Integer isCanSeeSalePriceWithLeaguer = null;
        if (null != bizLeaguer) {
            isCanSeeSalePriceWithLeaguer = bizLeaguer.getIsCanSeeSalePrice();
        }
        if (null == isCanSeeSalePrice) {
            return false;
        }

        return isCanSeeSalePriceWithLeaguer.equals(1);
    }

    /**
     * 是否可以查看【友商价】
     *
     * @param leaguerShopIdA 查看友商的店铺,调用接口
     * @param leaguerShopIdB 友商的店铺ID
     * @return
     */
    @Override
    public Boolean isCanSeeTradePrice(Integer leaguerShopIdA, Integer leaguerShopIdB) {
        /**
         * Version1 2020-10
         * leaguerShopIdA 是否有查看 leaguerShopIdB的商品【友商价】的资格
         * （1）leaguerShopIdB 的 【biz_leaguer_config.is_can_see_trade_price】
         *          is_can_see_trade_price=0，直接结束返回
         *          is_can_see_trade_price=1，继续
         *  (2)【biz_leaguer.fk_inviter_shop_id】=leaguerShopIdA
         *     【biz_leaguer.fk_be_inviter_shop_id】=leaguerShopIdB
         *          is_can_see_trade_price=0，返回false
         *          is_can_see_trade_price=1，返回true
         */

        /**
         * Version2 2020-11-23
         * leaguerShopIdA 是否有查看 leaguerShopIdB的商品【友商价】的资格
         *  (1)【biz_leaguer.fk_inviter_shop_id】=leaguerShopIdA
         *     【biz_leaguer.fk_be_inviter_shop_id】=leaguerShopIdB
         *          is_can_see_trade_price=0，返回false
         *          is_can_see_trade_price=1，返回true
         */
//        Boolean isCanSeeTradePrice = null;
//        try {
//            VoCanSeeLeaguerPriceInfo voCanSeeLeaguerPriceInfo =  bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(leaguerShopIdA,null);
//            if(null != voCanSeeLeaguerPriceInfo) {
//                isCanSeeTradePrice = voCanSeeLeaguerPriceInfo.getIsCanSeeTradePrice();
//            }
//        } catch (Exception e) {
//            log.error("获取是否可以查看友商价异常：" + e);
//            return Boolean.FALSE;
//        }
//        if(!isCanSeeTradePrice){
//            return Boolean.FALSE;
//        }

        BizLeaguer bizLeaguer = bizLeaguerMapper.getBizLeaguerByShopIdAndLeaguerShopId(leaguerShopIdA, leaguerShopIdB);
        if (null == bizLeaguer) {
            log.error("获取是否可以查看友商价异常：bizLeaguer为空。leaguerShopIdA={},leaguerShopIdB={}", leaguerShopIdA, leaguerShopIdB);
            return Boolean.FALSE;
        }
        Integer isCanSeeTradePriceWithLeaguer = bizLeaguer.getIsCanSeeTradePrice();

        return isCanSeeTradePriceWithLeaguer.equals(1);
    }

    /**
     * 友商联盟是否可以查看【友商价】
     *
     * @param leaguerShopId 查看友商的店铺,调用接口
     * @param userId        友商的店铺ID
     * @return
     */
    @Override
    public VoCanSeeLeaguerPriceInfo isCanSeePriceForUnion(Integer leaguerShopId, Integer userId) {

        VoCanSeeLeaguerPriceInfo bizLeaguer = bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(leaguerShopId, userId);
        if (null == bizLeaguer) {
            log.error("获取是否可以查看友商价异常：bizLeaguer为空。leaguerShopIdA={},leaguerShopIdB={}", leaguerShopId, userId);
            throw new MyException("获取是否可以查看友商价异常：bizLeaguer为空。leaguerShopIdA={" + leaguerShopId + "},leaguerShopIdB={}" + userId);
        }
        return bizLeaguer;
    }

    @Override
    public List<VoBizLeaguer> listBizLeaguerByShopId(int shopId) {
        return bizLeaguerMapper.listBizLeaguerByShopId(shopId);
    }

    @Override
    public int modifyVisible(BizLeaguer bizLeaguer) {
        return bizLeaguerMapper.modifyVisible(bizLeaguer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBizLeaguer(int userId, int shopId, int leaguerShopId, HttpServletRequest request) {
        try {
            bizLeaguerMapper.deleteBizLeaguer(shopId, leaguerShopId);
            bizLeaguerMapper.deleteBizLeaguer(leaguerShopId, shopId);
            bizLogService.saveBizLog(shopId, userId, TYPE, "删除友商[" + leaguerShopId + "]");

            //添加【店铺操作日志】-【删除友商】
            VoUserShopBase voUserShopBaseLeaguer = shpShopService.getVoUserShopBaseByShopId(leaguerShopId);
            if (null != voUserShopBaseLeaguer) {
                String leaguerShopName = voUserShopBaseLeaguer.getShopName();
                ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
                paramAddShpOperateLog.setShopId(shopId);
                paramAddShpOperateLog.setOperateUserId(userId);
                paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.LEAGUER_ALBUM.getName());
                paramAddShpOperateLog.setOperateName("删除友商");
                paramAddShpOperateLog.setOperateContent("【" + leaguerShopName + "-" + leaguerShopId + "】");
                paramAddShpOperateLog.setProdId(null);
                paramAddShpOperateLog.setOrderId(null);
                paramAddShpOperateLog.setRequest(request);

                shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("删除友商失败!");
        }
    }

    @Override
    public int updateLeaguerNote(int leaguerId, int shopId, int leaguerShopId, String note) {
        return bizLeaguerMapper.updateLeaguerNote(leaguerId, shopId, leaguerShopId, note);
    }

    @Override
    public BizLeaguer getBizLeaguerByShopIdAndLeaguerShopId(int shopId, int leaguerShopId) {
        return bizLeaguerMapper.getBizLeaguerByShopIdAndLeaguerShopId(shopId, leaguerShopId);
    }


    @Override
    public VoBizLeaguer getSpecificLeaguerDetail(int myShopId, int leaguerShopId) {
        VoBizLeaguer voBizLeaguer = bizLeaguerMapper.getSpecificLeaguerDetail(myShopId, leaguerShopId);
        if (!LocalUtils.isEmptyAndNull(voBizLeaguer)) {
            voBizLeaguer.setNotSeeGoods(voBizLeaguer.getIsWantSeeLeaguerProd());
        }
        return voBizLeaguer;
    }

    @Override
    public void deleteLeaguerForShop(int shopId) {
        bizLeaguerMapper.deleteLeaguerForShop(shopId);
    }

    @Override
    public String getLeaguerFriendState(int myShopId, int leaguerShopId) {
        if (myShopId == leaguerShopId) {
            return "2";
        }
        String leaguerFriendState = bizLeaguerMapper.getLeaguerFriendState(myShopId, leaguerShopId);
        if (LocalUtils.isEmptyAndNull(leaguerFriendState)) {
            return "0";
        } else {
            return "1";
        }
    }


}
