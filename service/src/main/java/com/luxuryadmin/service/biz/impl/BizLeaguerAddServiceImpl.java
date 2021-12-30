package com.luxuryadmin.service.biz.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.BoolUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.entity.biz.BizLeaguerAdd;
import com.luxuryadmin.entity.biz.BizLeaguerConfig;
import com.luxuryadmin.mapper.biz.BizLeaguerAddMapper;
import com.luxuryadmin.mapper.biz.BizLeaguerConfigMapper;
import com.luxuryadmin.param.biz.ParamAddLeaguer;
import com.luxuryadmin.service.biz.BizLeaguerAddService;
import com.luxuryadmin.service.biz.BizLeaguerConfigService;
import com.luxuryadmin.service.biz.BizLeaguerService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.biz.VoBizLeaguerAdd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-01-12 18:43:47
 */
@Slf4j
@Service
public class BizLeaguerAddServiceImpl implements BizLeaguerAddService {

    @Resource
    private BizLeaguerAddMapper bizLeaguerAddMapper;

    @Autowired
    private BizLeaguerService bizLeaguerService;

    @Autowired
    private BizLeaguerConfigMapper bizLeaguerConfigMapper;

    @Autowired
    private BizLeaguerConfigService bizLeaguerConfigService;

    @Autowired
    private ShpShopService shpShopService;

    @Override
    public List<VoBizLeaguerAdd> listBizLeaguerAddByShopId(int shopId) {
        List<VoBizLeaguerAdd> voBizLeaguerAdds = bizLeaguerAddMapper.listBizLeaguerAddByShopId(shopId);
        if (LocalUtils.isEmptyAndNull(voBizLeaguerAdds)){
            return null;
        }
        voBizLeaguerAdds.forEach(voBizLeaguerAdd -> {
            if ("1".equals(voBizLeaguerAdd.getSource())){
                voBizLeaguerAdd.setSource("对方通过商家联盟添加");
            }else {
                voBizLeaguerAdd.setSource("对方通过友商搜索添加");
            }

        });
        return voBizLeaguerAdds;
    }

    @Override
    public void saveBizLeaguerAdd(ParamAddLeaguer paramAddLeaguer) {
        Integer shopId =paramAddLeaguer.getMyShopId();
        Integer leaguerShopId =Integer.parseInt(paramAddLeaguer.getLeaguerShopId());
        //查找是否已经成为友商;
        BizLeaguer bizLeaguer = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(shopId, leaguerShopId);
        if (!LocalUtils.isEmptyAndNull(bizLeaguer)) {
            throw new MyException("该店铺已经是你的友商,不能重复添加!");
        }
        //查找是否已经重复添加过,如果有添加过,则重新更新
        BizLeaguerAdd bizLeaguerAdd = bizLeaguerAddMapper.getLeaguerAddByShopIdAndLeaguerShopId(shopId, leaguerShopId);
        Date date = new Date();
        if (LocalUtils.isEmptyAndNull(bizLeaguerAdd)) {
            bizLeaguerAdd = new BizLeaguerAdd();
            bizLeaguerAdd.setFkInviterShopId(shopId);
            bizLeaguerAdd.setFkBeInviterShopId(leaguerShopId);
            bizLeaguerAdd.setInsertTime(date);
        }
        bizLeaguerAdd.setState("10");
        bizLeaguerAdd.setInsertAdmin(paramAddLeaguer.getUserId());
        bizLeaguerAdd.setUpdateTime(date);
        bizLeaguerAdd.setRemark(paramAddLeaguer.getRequestMsg());
        if (LocalUtils.isEmptyAndNull(paramAddLeaguer.getSource())){
            bizLeaguerAdd.setSource("0");
        }else {
            bizLeaguerAdd.setSource(paramAddLeaguer.getSource());
        }

//        bizLeaguerAdd.setSource();
        if (bizLeaguerAdd.getId() != null) {
            //更新
            bizLeaguerAddMapper.updateObject(bizLeaguerAdd);
        }else{

            bizLeaguerAddMapper.saveObject(bizLeaguerAdd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBizLeaguerAdd(BizLeaguerAdd bizLeaguerAdd,String state, String note, String visible) {
        try {

            Date date = new Date();
            bizLeaguerAdd.setState(state);
            bizLeaguerAdd.setUpdateTime(date);
            if ("20".equals(state)) {
                int shopId = bizLeaguerAdd.getFkBeInviterShopId();
                int leaguerShopId = bizLeaguerAdd.getFkInviterShopId();
                //如果对方也同时请求添加本店铺为友商,则把对方请求的记录变为已添加状态;
                BizLeaguerAdd bizLeaguerAdd2 = bizLeaguerAddMapper.getLeaguerAddByShopIdAndLeaguerShopId(shopId, leaguerShopId);
                if (!LocalUtils.isEmptyAndNull(bizLeaguerAdd2)) {
                    bizLeaguerAdd2.setState(state);
                    bizLeaguerAdd2.setUpdateTime(date);
                    bizLeaguerAdd2.setBecomeFriendTime(date);
                    bizLeaguerAddMapper.updateBizLeaguerAdd(bizLeaguerAdd2);
                }
                //同意成为友商; 添加友商记录
                bizLeaguerAdd.setBecomeFriendTime(date);
                //对方主动添加,对方为邀请方;
                BizLeaguer bizLeaguer1 = new BizLeaguer(leaguerShopId, shopId, "1");
                //我方同意对方的添加申请;给对方友商备注
                BizLeaguer bizLeaguer2 = new BizLeaguer(shopId, leaguerShopId, "2");
                bizLeaguer2.setNote(note);
                bizLeaguer2.setVisible(visible);

                //2020-11-23 16-28
                //根据店铺总的友商设置，为新添加的友商设置价格权限默认值
                //A添加B为友商，会生成两条添加记录【biz_leaguer】
                //(1)A邀请人，B被邀请人，B根据A设置的针对所有店铺的【友商价格权限】，设置B的默认【友商价格权限】
                //(2)B邀请人，A被邀请人，A根据B设置的针对所有店铺的【友商价格权限】，设置A的默认【友商价格权限】
                //所有店铺的【友商价格权限】配置在【biz_leaguer_config】表里
                BizLeaguerConfig configMine = bizLeaguerConfigMapper.selectBizLeaguerConfigByShopId(shopId);
                Integer bossUserIdMine = shpShopService.getBossUserIdByShopId(shopId);
                configMine = null == configMine ? bizLeaguerConfigService.buildBizLeaguerConfig(shopId,bossUserIdMine) : configMine;

                BizLeaguerConfig configLeaguer = bizLeaguerConfigMapper.selectBizLeaguerConfigByShopId(leaguerShopId);
                Integer bossUserIdLeaguer = shpShopService.getBossUserIdByShopId(leaguerShopId);
                configLeaguer = null == configLeaguer ? bizLeaguerConfigService.buildBizLeaguerConfig(leaguerShopId,bossUserIdLeaguer) : configLeaguer;

                bizLeaguer1.setIsCanSeeSalePrice(BoolUtil.convertBooleanToInteger(configLeaguer.getIsCanSeeSalePrice()));
                bizLeaguer1.setIsCanSeeTradePrice(BoolUtil.convertBooleanToInteger(configLeaguer.getIsCanSeeTradePrice()));

                bizLeaguer2.setIsCanSeeSalePrice(BoolUtil.convertBooleanToInteger(configMine.getIsCanSeeSalePrice()));
                bizLeaguer2.setIsCanSeeTradePrice(BoolUtil.convertBooleanToInteger(configMine.getIsCanSeeTradePrice()));

                bizLeaguerService.saveBizLeaguer(bizLeaguer1);
                bizLeaguerService.saveBizLeaguer(bizLeaguer2);
            }
            bizLeaguerAddMapper.updateBizLeaguerAdd(bizLeaguerAdd);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("添加友商失败!");
        }
    }

    @Override
    public BizLeaguerAdd getLeaguerAddByShopIdAndLeaguerShopId(Integer shopId, Integer leaguerShopId) {
        return bizLeaguerAddMapper.getLeaguerAddByShopIdAndLeaguerShopId(shopId, leaguerShopId);
    }

    @Override
    public void deleteLeaguerAddForShop(int shopId) {
        bizLeaguerAddMapper.deleteLeaguerAddForShop(shopId);
    }


}
