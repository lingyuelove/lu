package com.luxuryadmin.service.biz.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.BoolUtil;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.biz.BizLeaguerConfig;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.mapper.biz.BizLeaguerConfigMapper;
import com.luxuryadmin.mapper.biz.BizLeaguerMapper;
import com.luxuryadmin.mapper.biz.BizShopRecommendMapper;
import com.luxuryadmin.mapper.biz.BizShopSeeMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.param.biz.ParamCanSeeLeaguerPriceInfo;
import com.luxuryadmin.param.biz.ParamShopSeeSearchByCount;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.biz.BizLeaguerAddService;
import com.luxuryadmin.service.biz.BizLeaguerConfigService;
import com.luxuryadmin.service.biz.BizLeaguerService;
import com.luxuryadmin.service.biz.BizLogService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.biz.VoBizLeaguerShop;
import com.luxuryadmin.vo.biz.VoCanSeeLeaguerPriceInfo;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class BizLeaguerConfigServiceImpl implements BizLeaguerConfigService {

    @Resource
    private BizLeaguerConfigMapper bizLeaguerConfigMapper;

    @Resource
    private ShpShopMapper shopMapper;

    @Resource
    private BizLeaguerMapper bizLeaguerMapper;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ProClassifyService proClassifyService;

    @Resource
    private BizShopSeeMapper bizShopSeeMapper;

    @Autowired
    private BizLeaguerService bizLeaguerService;
    @Autowired
    private BizLeaguerAddService bizLeaguerAddService;
    @Autowired
    private BizLogService bizLogService;
    @Resource
    private BizShopRecommendMapper bizShopRecommendMapper;
    @Autowired
    protected ServicesUtil servicesUtil;
    @Override
    public VoCanSeeLeaguerPriceInfo getCanSeeLeaguerPriceInfo(Integer shopId, Integer userId){
        BizLeaguerConfig bizLeaguerConfig = bizLeaguerConfigMapper.selectBizLeaguerConfigByShopId(shopId);
        if(null == bizLeaguerConfig){
            //??????????????????????????????
            bizLeaguerConfig = buildBizLeaguerConfig(shopId, userId);
        }

        VoCanSeeLeaguerPriceInfo canSeeLeaguerPriceInfo = new VoCanSeeLeaguerPriceInfo();
        BeanUtils.copyProperties(bizLeaguerConfig,canSeeLeaguerPriceInfo);
        return canSeeLeaguerPriceInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCanSeeLeaguerPriceInfo(Integer shopId, Integer userId, ParamCanSeeLeaguerPriceInfo paramCanSeeLeaguerPriceInfo, HttpServletRequest request) {
        BizLeaguerConfig bizLeaguerConfigOld = bizLeaguerConfigMapper.selectBizLeaguerConfigByShopId(shopId);

        BizLeaguerConfig bizLeaguerConfigUpdate = new BizLeaguerConfig();
        BeanUtils.copyProperties(paramCanSeeLeaguerPriceInfo,bizLeaguerConfigUpdate);

        bizLeaguerConfigUpdate.setId(bizLeaguerConfigOld.getId());
        bizLeaguerConfigUpdate.setUpdateAdmin(userId);
        bizLeaguerConfigUpdate.setUpdateTime(new Date());
        bizLeaguerConfigMapper.updateObject(bizLeaguerConfigUpdate);

        //2020-11-23 16:22
        //A???????????????A?????????????????????????????????
        //?????????????????????????????????????????????????????????
        Boolean isCanSeeSalePriceUpdate = paramCanSeeLeaguerPriceInfo.getIsCanSeeSalePrice();
        Boolean isCanSeeTradePriceUpdate = paramCanSeeLeaguerPriceInfo.getIsCanSeeTradePrice();
        Integer isCanSeeSalePrice = BoolUtil.convertBooleanToInteger(isCanSeeSalePriceUpdate);
        //??????????????????????????????
        Integer isUpdateSalePrice = BoolUtil.convertBooleanToInteger(!bizLeaguerConfigOld.getIsCanSeeSalePrice().equals(isCanSeeSalePriceUpdate));
        //?????????????????????????????????????????????????????????
        Integer isCanSeeTradePrice = BoolUtil.convertBooleanToInteger(isCanSeeTradePriceUpdate);
        //??????????????????????????????
        Integer isUpdateTradePrice = BoolUtil.convertBooleanToInteger(!bizLeaguerConfigOld.getIsCanSeeTradePrice().equals(isCanSeeTradePriceUpdate));
        bizLeaguerMapper.updateAllBizLeaguerRecord(shopId,isUpdateSalePrice,isCanSeeSalePrice,isUpdateTradePrice,isCanSeeTradePrice);

        //??????????????????????????????-???????????????????????????
        VoUserShopBase voUserShopBase = shpShopService.getVoUserShopBaseByShopId(shopId);
        if(null!=voUserShopBase){
            String leaguerShopName = voUserShopBase.getShopName();
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(userId);
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.LEAGUER_ALBUM.getName());
            paramAddShpOperateLog.setOperateName("?????????????????????");
            paramAddShpOperateLog.setProdId(null);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);

            String operateContent = "";
            Integer updateFieldCount = 0;

            //??????????????????
            Boolean isShowSalePriceOld = bizLeaguerConfigOld.getIsCanSeeSalePrice();
            Boolean isShowSalePriceNew = paramCanSeeLeaguerPriceInfo.getIsCanSeeSalePrice();
            Boolean isUpdateShowSalePrice = !isShowSalePriceOld.equals(isShowSalePriceNew);
            if(isUpdateShowSalePrice){
                updateFieldCount++;
                if(updateFieldCount!=1){
                    operateContent += ";";
                }
                if(isShowSalePriceNew) {
                    operateContent += "????????????????????????";
                }else{
                    operateContent += "???????????????????????????";
                }
            }

            //??????????????????/?????????
            Boolean isShowTradeOld = bizLeaguerConfigOld.getIsCanSeeTradePrice();
            Boolean isShowTradePriceNew = paramCanSeeLeaguerPriceInfo.getIsCanSeeTradePrice();
            Boolean isUpdateShowTradePrice = !isShowTradeOld.equals(isShowTradePriceNew);
            if(isUpdateShowTradePrice){
                updateFieldCount++;
                if(updateFieldCount!=1){
                    operateContent += ";";
                }
                if(isShowTradePriceNew) {
                    operateContent += "????????????????????????";
                }else{
                    operateContent += "???????????????????????????";
                }
            }

            paramAddShpOperateLog.setOperateContent(operateContent);
            if(updateFieldCount>0) {
                shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
            }
        }
    }

    @Override
    public BizLeaguerConfig selectBizLeaguerConfigByShopId(Integer shopId, Integer userId) throws Exception {
        BizLeaguerConfig bizLeaguerConfig = bizLeaguerConfigMapper.selectBizLeaguerConfigByShopId(shopId);
        if(null == bizLeaguerConfig){
            //??????????????????????????????
            bizLeaguerConfig = buildBizLeaguerConfig(shopId, userId);
        }

        return bizLeaguerConfig;
    }

    @Override
    public BizLeaguerConfig buildBizLeaguerConfig(Integer shopId, Integer userId) {
        BizLeaguerConfig bizLeaguerConfig;
        bizLeaguerConfig = new BizLeaguerConfig();
        bizLeaguerConfig.setFkShpShopId(shopId);
        ShpShop shpShop = (ShpShop)shopMapper.getObjectById(shopId);
        if(null == shpShop){
            throw new MyException("??????ID????????????????????????");
        }
        bizLeaguerConfig.setShopName(shpShop.getName());

        bizLeaguerConfig.setHaveLeaguerNum(5);
        bizLeaguerConfig.setLimitLeaguerNum(0);
        bizLeaguerConfig.setDel(ConstantCommon.DEL_OFF);
        bizLeaguerConfig.setIsCanSeeSalePrice(true);
        bizLeaguerConfig.setIsCanSeeTradePrice(true);
        bizLeaguerConfig.setOnlyShowTopLeaguer("0");
        bizLeaguerConfig.setRemark("");
        bizLeaguerConfig.setInsertAdmin(userId);
        bizLeaguerConfig.setInsertTime(new Date());
        bizLeaguerConfig.setUpdateAdmin(userId);
        bizLeaguerConfig.setUpdateTime(new Date());
        bizLeaguerConfig.setRecommend(ConstantCommon.ONE);
        bizLeaguerConfigMapper.saveObject(bizLeaguerConfig);
        return bizLeaguerConfig;
    }

    @Override
    public VoBizLeaguerShop getLeaguerShop(Integer shopId) {
        VoBizLeaguerShop leaguerShop =  bizLeaguerConfigMapper.getLeaguerShop(shopId);
        if (leaguerShop == null){
            return null;
        }
        //?????????
        String coverImgUrl = leaguerShop.getCoverImgUrl();
        if (!LocalUtils.isEmptyAndNull(coverImgUrl) && !coverImgUrl.contains("http")) {
            leaguerShop.setCoverImgUrl(servicesUtil.formatImgUrl(coverImgUrl, false));
        }
        //?????????
        String headImgUrl = leaguerShop.getHeadImgUrl();
        if (!LocalUtils.isEmptyAndNull(headImgUrl) && !headImgUrl.contains("http")) {
            leaguerShop.setHeadImgUrl(servicesUtil.formatImgUrl(headImgUrl, false));
        }

        Integer leaguerShopProductCount = bizLeaguerConfigMapper.getRecommendShopProductCount(shopId);
        if (leaguerShopProductCount == null){
            leaguerShopProductCount = 0;
        }
        Integer leaguerShopCount = bizLeaguerConfigMapper.getRecommendShopCount(shopId);
        if (leaguerShopCount == null){
            leaguerShopCount = 0;
        }
        ParamShopSeeSearchByCount shopSeeSearchByCount = new ParamShopSeeSearchByCount();
        shopSeeSearchByCount.setShopId(shopId);
        Integer seeAllCount = bizShopSeeMapper.getShopSeeCount(shopSeeSearchByCount);
        if (seeAllCount == null){
            seeAllCount = 0;
        }
        shopSeeSearchByCount.setInsertTime(new Date());
        Integer seeWeekCount = bizShopSeeMapper.getShopSeeCount(shopSeeSearchByCount);
        if (seeWeekCount == null){
            seeWeekCount = 0;
        }
        leaguerShop.setLeaguerShopCount(leaguerShopCount);
        leaguerShop.setSeeAllCount(seeAllCount);
        leaguerShop.setSeeWeekCount(seeWeekCount);
        leaguerShop.setLeaguerShopProductCount(leaguerShopProductCount);
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        leaguerShop.setClassifyList(voProClassifyList);
        return leaguerShop;
    }

    @Override
    public void deleteLeaguerConfigByShop(Integer shopId) {
        bizLeaguerConfigMapper.deleteLeaguerConfigByShop(shopId);
        //??????????????????
        bizLeaguerService.deleteLeaguerForShop(shopId);
        //??????????????????????????????
        bizLeaguerAddService.deleteLeaguerAddForShop(shopId);
        //????????????????????????????????????
        bizLogService.deleteBizLogByShop(shopId);
        //????????????????????????????????????
        bizShopSeeMapper.deleteByShopId(shopId);
        //????????????????????????
        bizShopRecommendMapper.deleteShopRecommendForShopId(shopId);
    }
}
