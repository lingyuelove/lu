package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.entity.shp.ShpAlterHistory;
import com.luxuryadmin.entity.shp.ShpShareTotal;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.mapper.shp.ShpAlterHistoryMapper;
import com.luxuryadmin.mapper.shp.ShpShareTotalMapper;
import com.luxuryadmin.mapper.shp.ShpShareTypeMapper;
import com.luxuryadmin.service.shp.ShpShareTotalService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.shp.VoShopMemberAddHour;
import com.luxuryadmin.vo.shp.VoShpShareTotal;
import com.luxuryadmin.vo.shp.VoShpShareType;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 商铺分享进度时长累计表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-06-08 14:15:43
 */
@Service
public class ShpShareTotalServiceImpl implements ShpShareTotalService {


    /**
     * 注入dao
     */
    @Resource
    private ShpShareTotalMapper shpShareTotalMapper;

    @Resource
    private ShpShareTypeMapper shpShareTypeMapper;
    @Resource
    private ShpAlterHistoryMapper shpAlterHistoryMapper;
    @Autowired
    private ShpShopService shopService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateShareTotal(ProShare proShare) {
        try {
            //查询可添加时长的活动类型
            VoShpShareType voShpShareType = shpShareTypeMapper.getShareTypeByCode("1");
            if (voShpShareType == null) {
                throw new MyException("暂无此类型，暂不添加会员天数");
            }
            //查询浸提是否已添加时长
            VoShpShareTotal voShpShareTotal = shpShareTotalMapper.getShareTotalByToDay(voShpShareType.getCode(), proShare.getFkShpShopId());
            if (voShpShareTotal == null) {
                //新增
                this.addShareTotal(proShare, voShpShareType);
                //添加记录
                this.addAlterHistory(proShare, voShpShareType);
            } else {
                //判断活动类型的添加时长的次数和今日次数
                if (voShpShareTotal.getTotalCount() < voShpShareType.getAddNum()) {
                    ShpShareTotal shpShareTotal = new ShpShareTotal();
                    shpShareTotal.setTotalCount(voShpShareTotal.getTotalCount() + 1);
                    shpShareTotal.setId(voShpShareTotal.getId());
                    shpShareTotal.setTotalHours(voShpShareTotal.getTotalHours().add(voShpShareType.getHours()));
                    shpShareTotalMapper.updateObject(shpShareTotal);
                    //添加记录
                    this.addAlterHistory(proShare, voShpShareType);
                }
            }
        } catch (Exception e) {
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException("时长添加失败: " + e.getMessage());
        }


    }

    @Override
    public void addShareTotal(ProShare proShare, VoShpShareType voShpShareType) {
        ShpShareTotal shpShareTotal = new ShpShareTotal();
        shpShareTotal.setFkShpShopId(proShare.getFkShpShopId());
        shpShareTotal.setFkShpShareTypeCode(1);
        shpShareTotal.setFkShpUserId(proShare.getFkShpUserId());
        shpShareTotal.setTotalCount(1);
        shpShareTotal.setTotalHours(voShpShareType.getHours());
        shpShareTotal.setInsertAdmin(proShare.getFkShpUserId());
        shpShareTotal.setInsertTime(new Date());
        shpShareTotalMapper.saveObject(shpShareTotal);
    }

    @Override
    public VoShopMemberAddHour getByShopId(Integer shopId) {
        //查询店铺
        ShpShop shop = shopService.getShpShopById(shopId.toString());
        if (shop == null) {
            throw new MyException("暂未发现此店铺");
        }
        VoShopMemberAddHour shopMemberAddHour = shpShareTotalMapper.getByShopId("1", shopId);
        if (shopMemberAddHour == null) {
            shopMemberAddHour =new VoShopMemberAddHour();
            shopMemberAddHour.setShareCount(0);
        }
        if (shop.getPayEndTimeOld() != null){
            shopMemberAddHour.setPayEndTime(DateUtil.formatShort(shop.getPayEndTimeOld()));
        }
        if (shop.getPayEndTimeOld() == null && shop.getPayEndTime() != null){
            shopMemberAddHour.setPayEndTime(DateUtil.formatShort(shop.getPayEndTime()));
        }
        //查询可添加时长的活动类型
        VoShpShareType voShpShareType = shpShareTypeMapper.getShareTypeByCode("1");
        shopMemberAddHour.setShareTotalCount(voShpShareType.getAddNum());
        //查询可添加上午天数
        if (shop.getTotalHours() == null){
            shopMemberAddHour.setDayCount(0);
        }else {
            shopMemberAddHour.setTotalHours(shop.getTotalHours().stripTrailingZeros().toPlainString());
            // 向下取整
            BigDecimal day = new BigDecimal(shopMemberAddHour.getTotalHours()).divide(new BigDecimal(24), 0, BigDecimal.ROUND_DOWN);
            // 向下取整
            long dayCount = day.longValue();
            shopMemberAddHour.setDayCount((int)dayCount);
        }

        shopMemberAddHour.setMemberTitle("做任务赚会员时长");
        shopMemberAddHour.setDescription("每分享一次则获得一小时的会员时长");
        shopMemberAddHour.setShowUrl("https://file.luxuryadmin.com/h5/activity/share/share-get-timelong-rules.html");
        shopMemberAddHour.setTipTitle("开通会员后 您将获得任务奖励时长");

        return shopMemberAddHour;
    }

    @Override
    public void updateShopMemberAddHour() {
        List<ShpShareTotal> shpShareTotals = shpShareTotalMapper.getShareTotalByYest();
        if (shpShareTotals != null && shpShareTotals.size() > 0) {
            shpShareTotals.forEach(shpShareTotal -> {
                //查询店铺
                //店铺添加时长
                ShpShop shop = shopService.getShpShopById(shpShareTotal.getFkShpShopId().toString());
                //判断会员是否过期 未过期添加时长
                if ("yes".equals(shop.getIsMember()) && shop.getPayEndTime().after(new Date())) {
                    //添加添加的总时长
                    BigDecimal totalHours = shop.getTotalHours();
                    if (totalHours == null) {
                        totalHours = new BigDecimal(0);
                    }
                    shop.setTotalHours(totalHours.add(shpShareTotal.getTotalHours()));
                    //设置添加的时间
                    Date payEndTime =shop.getPayEndTime();
                    if(shop.getPayEndTimeOld() == null){
                        shop.setPayEndTimeOld(payEndTime);
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(payEndTime);
                    //获取添加的小时
                    BigDecimal bd = shpShareTotal.getTotalHours();
                    String textBD = bd.toPlainString();
                    int radixLoc = textBD.indexOf('.');
                    //获取添加的小时数
                    Integer hours = 0;
                    if (radixLoc <= 0) {
                        hours = Integer.parseInt(shpShareTotal.getTotalHours().toString());
                    } else {
                        //获取添加的小时数
                        hours = Integer.parseInt(textBD.substring(0, radixLoc));
                    }
                    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hours);
                    //获取添加的分钟数
                    if (radixLoc != 2) {
                        double minute = bd.doubleValue();
                        double minuteNow = minute % 1;
                        minuteNow = minuteNow * 60;
                        calendar.add(Calendar.MINUTE, (int) minuteNow);
                    }
                    shop.setPayEndTime(calendar.getTime());
                    shopService.updateShpShop(shop);
                    String vipExpireKey = ConstantRedisKey.getVipExpireRedisKeyByShopId(shpShareTotal.getFkShpShopId());
                    redisUtil.set(vipExpireKey, DateUtil.formatShort(calendar.getTime()));
                }
                if (shop.getPayEndTime() !=null && shop.getPayEndTime().before(new Date())) {
                    shop.setTotalHours(new BigDecimal(0));
                    shop.setPayEndTimeOld(null);
                    shopService.updateShpShop(shop);
                }
            });
        }
    }


    public void addAlterHistory(ProShare proShare, VoShpShareType voShpShareType) {
        ShpAlterHistory alterHistory = new ShpAlterHistory();
        alterHistory.setFkShpShopId(proShare.getFkShpShopId());
        alterHistory.setInsertAdmin(proShare.getFkShpUserId());
        alterHistory.setInsertTime(new Date());
        alterHistory.setCode(voShpShareType.getCode());
        alterHistory.setHours(voShpShareType.getHours());
        shpAlterHistoryMapper.saveObject(alterHistory);
    }
}
