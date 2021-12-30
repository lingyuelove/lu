package com.luxuryadmin.service.stat.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.stat.StatProdSaleClassify;
import com.luxuryadmin.entity.stat.StatShop;
import com.luxuryadmin.entity.stat.StatTotal;
import com.luxuryadmin.mapper.biz.BizLeaguerMapper;
import com.luxuryadmin.mapper.ord.OrdOrderMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.mapper.shp.ShpUserMapper;
import com.luxuryadmin.mapper.stat.StatProdSaleClassifyMapper;
import com.luxuryadmin.mapper.stat.StatShopMapper;
import com.luxuryadmin.mapper.stat.StatTotalMapper;
import com.luxuryadmin.param.stat.ParamRegDataQuery;
import com.luxuryadmin.service.stat.StatDailyCountService;
import com.luxuryadmin.vo.stat.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 运营数据统计Service实现类
 */
@Service
@Slf4j
public class StatDailyCountServiceImpl implements StatDailyCountService {

    @Resource
    private StatTotalMapper statTotalMapper;

    @Resource
    private StatShopMapper statShopMapper;

    @Resource
    private StatProdSaleClassifyMapper statProdSaleClassifyMapper;

    @Resource
    private ShpUserMapper shpUserMapper;

    @Resource
    private ShpShopMapper shpShopMapper;

    @Resource
    private OrdOrderMapper ordOrderMapper;

    @Resource
    private ProProductMapper proProductMapper;

    @Resource
    private BizLeaguerMapper bizLeaguerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer dailyCountStat(Date countDate) {

        //统计大盘数据
        Integer result1 = dailyCountTotal(countDate);

        //统计店铺汇总数据
        Integer result2 = countShopData();

        //统计销售商品数据
        Integer result3 = countSaleProdData();
        return result1+result2+result3;
    }


    /**
     * 统计大盘数据
     * @param countDate
     * @return
     */
    private Integer dailyCountTotal(Date countDate) {
        StatTotal statTotal = new StatTotal();

        //step1.获取时间
        String countDateStr = DateUtil.formatShort(countDate);
        String countMonth = DateUtil.format(countDate,"yyyy-MM");
        String countYear = DateUtil.format(countDate,"yyyy");
        statTotal.setCountDate(countDateStr);
        statTotal.setCountMonth(countMonth);
        statTotal.setCountYear(countYear);

        //step2-1.获取注册人数
        Integer regPersonNum = shpUserMapper.countRegPersonNumByCountDate(countDate);
        regPersonNum = null == regPersonNum ? 0 : regPersonNum;
        statTotal.setRegPersonNum(regPersonNum);

        //step2-2.获取注册店铺数
        Integer regShopNum = shpShopMapper.countRegShopNumByCountDate(countDate);
        regShopNum = null == regShopNum ? 0 : regShopNum;
        statTotal.setRegShopNum(regShopNum);

        //step2-3.获取订单数量(后面取消的订单的数据不会更新)
        Integer orderNum = ordOrderMapper.countOrderNumByCountDate(countDate);
        orderNum = null == orderNum ? 0 : orderNum;
        statTotal.setOrderNum(orderNum);

        //step2-4.获取订单销售额(后面取消的订单的数据不会更新)
        BigDecimal orderSellAmount = ordOrderMapper.countOrderAmountByCountDate(countDate);
        orderSellAmount = null == orderSellAmount ? new BigDecimal("0.00") : orderSellAmount;
        statTotal.setOrderSellAmount(orderSellAmount);

        //step3.设置时间
        statTotal.setUpdateTime(new Date());

        //查询数据库中指定【统计日期】的全盘统计数据是否存在
        //存在则更新，否则插入
        StatTotal statTotalOld = statTotalMapper.selectStatTotalByCountDate(countDate);
        if(null == statTotalOld){
            //新增
            statTotal.setInsertTime(new Date());
            return statTotalMapper.saveObject(statTotal);
        }else{//更新
            statTotal.setId(statTotalOld.getId());
            return statTotalMapper.updateObject(statTotal);
        }
    }

    /**
     * 统计店铺汇总数据
     * @return
     */
    private Integer countShopData() {
        List<Integer> shopIdList = shpShopMapper.queryShpShopIdList();
        if(!CollectionUtils.isEmpty(shopIdList)){
            for (Integer shopId : shopIdList) {
                //根据shopId统计
                //上传商品数量
                Integer prodNumUpload = proProductMapper.countAllUploadProductNumByShopId(shopId);
                prodNumUpload = null == prodNumUpload ? 0 : prodNumUpload;
                
                //在售商品数量
                Integer prodNumOnsale = proProductMapper.countOnSaleProductNumByShopIdByAdmin(shopId);
                prodNumOnsale = null == prodNumOnsale ? 0 : prodNumOnsale;
                
                //总销售额
                Integer totalSaleNum= ordOrderMapper.getTotalOrderNumByShopId(shopId);
                totalSaleNum = null == totalSaleNum ? 0 : totalSaleNum;

                //总销售量
                BigDecimal totalSaleAmount = ordOrderMapper.countOrderAmountByShopId(shopId);
                totalSaleAmount = null == totalSaleAmount ? new BigDecimal("0.00") : totalSaleAmount;

                //店铺员工数量
                Integer shopStaffNum = shpUserMapper.countRegPersonNumByShopId(shopId);
                shopStaffNum = null == shopStaffNum ? 0 : shopStaffNum;

                //店铺友商数量
                Integer shopLeaguerNum = bizLeaguerMapper.countLeaguerNumByShopId(shopId);
                shopLeaguerNum = null == shopLeaguerNum ? 0 : shopLeaguerNum;

                StatShop statShop = statShopMapper.selectStatShopByShopId(shopId);
                if(null == statShop){//新增
                    statShop = new StatShop();
                    statShop.setFkShpShopId(shopId);
                    statShop.setProdNumUpload(prodNumUpload);
                    statShop.setProdNumOnsale(prodNumOnsale);
                    statShop.setTotalSaleNum(totalSaleNum);
                    statShop.setTotalSaleAmount(totalSaleAmount);
                    statShop.setShopStaffNum(shopStaffNum);
                    statShop.setShopLeaguerNum(shopLeaguerNum);

                    statShop.setInsertTime(new Date());
                    statShop.setUpdateTime(new Date());
                    statShopMapper.saveObject(statShop);
                }else{//更新
                    statShop.setProdNumUpload(prodNumUpload);
                    statShop.setProdNumOnsale(prodNumOnsale);
                    statShop.setTotalSaleNum(totalSaleNum);
                    statShop.setTotalSaleAmount(totalSaleAmount);
                    statShop.setShopStaffNum(shopStaffNum);
                    statShop.setShopLeaguerNum(shopLeaguerNum);

                    statShop.setUpdateTime(new Date());
                    statShopMapper.updateObject(statShop);
                }
            }
        }
        return 1;
    }

    /**
     * 统计销售商品数据
     * @return
     */
    private Integer countSaleProdData() {
        List<VoStatSaleProdData> list = ordOrderMapper.listStatSaleProdClassifyData();
        if(!CollectionUtils.isEmpty(list)){
            for (VoStatSaleProdData voStatSaleProdData : list) {
                String classifyCode = voStatSaleProdData.getProClassifyCode();
                StatProdSaleClassify statProdSaleClassify = statProdSaleClassifyMapper.selectStatProdSaleByClassifyCode(classifyCode);
                if(null == statProdSaleClassify){//新增
                    statProdSaleClassify = new StatProdSaleClassify();
                    BeanUtils.copyProperties(voStatSaleProdData,statProdSaleClassify);

                    statProdSaleClassify.setInsertTime(new Date());
                    statProdSaleClassify.setUpdateTime(new Date());
                    statProdSaleClassifyMapper.saveObject(statProdSaleClassify);
                }else {//更新
                    BeanUtils.copyProperties(voStatSaleProdData,statProdSaleClassify);
                    statProdSaleClassify.setUpdateTime(new Date());
                    statProdSaleClassifyMapper.updateObject(statProdSaleClassify);
                }
            }
        }
        return 1;
    }

    /******************************************  后台查询数据 **************************************************/
    @Override
    public VoStateDataTotal loadStatDataTotal(Date countDate) {
        //获取【今日】和【昨日】顶部数据
        StatTotal statTotalToday = statTotalMapper.selectStatTotalByCountDate(countDate);
        Date yesterday = DateUtil.addDaysFromOldDate(countDate,-1).getTime();
        StatTotal statTotalYesterday = statTotalMapper.selectStatTotalByCountDate(yesterday);

        //获取总顶部数据
        VoStateDataTotalAll dataTotalAll = statTotalMapper.selectAllCountDate();

        VoStateDataTotal vo = new VoStateDataTotal();
        vo.setNewestUpdateTime(DateUtil.format(statTotalToday.getUpdateTime()));
        //注册人数
        vo.setRegPersonNumToday(""+statTotalToday.getRegPersonNum());
        vo.setRegPersonNumTotal(dataTotalAll.getRegPersonNumTotal());
        String changeRatio = calChangeRatio(statTotalToday.getRegPersonNum(),statTotalYesterday.getRegPersonNum());
        String sign = getSignByRatio(changeRatio);
        changeRatio = changeRatio.replace("-","");
        vo.setRegPersonNumChangeRatio(changeRatio);
        vo.setRegPersonNumSign(sign);

        //注册店铺数
        vo.setRegShopNumToday("" + statTotalToday.getRegShopNum());
        vo.setRegShopNumTotal(dataTotalAll.getRegShopNumTotal());
        changeRatio = calChangeRatio(statTotalToday.getRegShopNum(),statTotalYesterday.getRegShopNum());
        sign = getSignByRatio(changeRatio);
        changeRatio = changeRatio.replace("-","");
        vo.setRegShopNumChangeRatio(changeRatio);
        vo.setRegShopNumSign(sign);

        //订单量
        vo.setOrderNumToday("" + statTotalToday.getOrderNum());
        vo.setOrderNumTotal(dataTotalAll.getOrderNumTotal());
        changeRatio = calChangeRatio(statTotalToday.getOrderNum(),statTotalYesterday.getOrderNum());
        sign = getSignByRatio(changeRatio);
        changeRatio = changeRatio.replace("-","");
        vo.setOrderNumChangeRatio(changeRatio);
        vo.setOrderNumSign(sign);

        //订单销售额
        vo.setOrderSellAmountToday("" + statTotalToday.getOrderSellAmount().divide(new BigDecimal(100)).setScale(2));
        vo.setOrderSellAmountTotal(dataTotalAll.getOrderSellAmountTotal());
        changeRatio = calChangeRatio(statTotalToday.getOrderSellAmount(),statTotalYesterday.getOrderSellAmount());
        sign = getSignByRatio(changeRatio);
        changeRatio = changeRatio.replace("-","");
        vo.setOrderSellAmountChangeRatio(changeRatio);
        vo.setOrderSellAmountSign(sign);

        return vo;
    }

    @Override
    public List<VoStateShopRank> loadStatDataShopRank(String rankField) {
        String rankFieldDb = convertRankFieldToDb(rankField);
        List<VoStateShopRank> voList = statShopMapper.selectStatDataShopRank(rankFieldDb);
        //如果查询的字段是金额的话，需要除以100转换
        if(!CollectionUtils.isEmpty(voList)){
            Integer rank = 0;
            for (VoStateShopRank voStateShopRank : voList) {
                rank++;
                voStateShopRank.setRank(rank);
                if("totalSaleAmount".equals(rankField)) {
                    String showData = voStateShopRank.getShowData();
                    BigDecimal showDataBd = new BigDecimal(showData).divide(new BigDecimal("100.00")).setScale(2);
                    voStateShopRank.setShowData(showDataBd.toString());
                }
            }
        }
        return voList;
    }

    @Override
    public List<VoStateProdSellClassify> loadProdSellClassify(String showField) {
        String showFieldDb = convertShowFieldToDb(showField);
        List<VoStateProdSellClassify> voList = statProdSaleClassifyMapper.selectProdSellClassifyList(showFieldDb);
        int index = 0;
        int length = voList.size();
        //【其它】所在位置index
        int otherIndex = -1;
        if(!CollectionUtils.isEmpty(voList)){
            for (VoStateProdSellClassify voStateProdSellClassify : voList) {
                if("totalSaleAmount".equals(showField)){
                    String showData = voStateProdSellClassify.getValue();
                    BigDecimal showDataBd = new BigDecimal(showData).divide(new BigDecimal("100.00")).setScale(2);
                    voStateProdSellClassify.setValue(showDataBd.toString());
                }
                //记录【其它】的位置
                String classifyCodeName = voStateProdSellClassify.getName();
                if("其它".equals(classifyCodeName)){
                    otherIndex = index;
                }

                index++;
            }

            //把【其它】放在最后
            voList.add(length-1, voList.remove(otherIndex));
        }
        return voList;
    }

    @Override
    public VoStatRegData loadRegData(ParamRegDataQuery paramRegDataQuery) {
        VoStatRegData voStatRegData = new VoStatRegData();
        //日期范围列表
        List<String> dateRangeList = new ArrayList<>();
        voStatRegData.setDateRangeList(dateRangeList);

        //注册人数列表
        List<Integer> regPersonNumList = new ArrayList<>();
        voStatRegData.setRegPersonNumList(regPersonNumList);

        //注册店铺列表
        List<Integer> regShopNumList = new ArrayList<>();
        voStatRegData.setRegShopNumList(regShopNumList);

        try {
            String regStartDateStr = paramRegDataQuery.getRegStartDate();
            String regEndDateStr = paramRegDataQuery.getRegEndDate();
            if(StringUtil.isAnyBlank(regStartDateStr,regEndDateStr)){
                throw new MyException("日期不能为空");
            }

            Date regStartDate = DateUtil.parseShort(regStartDateStr);
            Date regEndDate = DateUtil.parseShort(regEndDateStr);

            Integer intervalDays = DateUtil.calIntervalDays(regEndDate,regStartDate);
            if(intervalDays>31){
                throw new MyException("最多只能查询一个月的数据");
            }

            List<StatTotal> totalList = statTotalMapper.selectStatTotalByRangeDate(paramRegDataQuery);
            int length = totalList.size();
            int index = 0;
            if(!CollectionUtils.isEmpty(totalList)){
                for (StatTotal statTotal : totalList) {
                    String countDate = statTotal.getCountDate();
                    dateRangeList.add(countDate);

                    Integer regPersonNum = statTotal.getRegPersonNum();
                    regPersonNumList.add(regPersonNum);

                    Integer regShopNum = statTotal.getRegShopNum();
                    regShopNumList.add(regShopNum);
                }
            }
            return voStatRegData;
        }catch (Exception e){
            throw new MyException(e.getMessage());
        }

    }

    /**
     * 将排序字段转换为数据库字段
     * @param rankField
     * @return
     */
    private String convertRankFieldToDb(String rankField) {
        switch (rankField) {
            //上传商品数量
            case "prodNumUpload":
                rankField = "prod_num_upload";
                break;
            //在售商品数量
            case "prodNumOnsale":
                rankField = "prod_num_onsale";
                break;
            //总销售量
            case "totalSaleNum":
                rankField = "total_sale_num";
                break;
            //总销售额
            case "totalSaleAmount":
                rankField = "total_sale_amount";
                break;
            //员工人数
            case "shopStaffNum":
                rankField = "shop_staff_num";
                break;
            //友商数量
            case "shopLeaguerNum":
                rankField = "shop_leaguer_num";
                break;
            default:
                rankField = "prod_num_upload";
                break;
        }
        return rankField;
    }

    /**
     * 将排序字段转换为数据库字段
     * @param showField
     * @return
     */
    private String convertShowFieldToDb(String showField) {
        switch (showField) {
            //总销售量
            case "totalSaleNum":
                showField = "total_sale_num";
                break;
            //总销售额
            case "totalSaleAmount":
                showField = "total_sale_amount";
                break;
            default:
                showField = "total_sale_num";
                break;
        }
        return showField;
    }


    /**
     * 计算环比
     * @param dataNew
     * @param dataOld
     * @return
     */
    private String calChangeRatio(Object dataNew, Object dataOld) {
        if(dataNew instanceof Integer){
            if(null == dataOld || dataOld.equals(0)){
                return "100";
            }

            Integer dataNewInt = (Integer) dataNew;
            Integer dataOldInt = (Integer) dataOld;
            Integer changeData =  dataNewInt - dataOldInt;
            String changeRatio = changeData * 100 / dataOldInt + "";
            return changeRatio;
        }else if(dataNew instanceof BigDecimal){
            BigDecimal dataOldBd = (BigDecimal)dataOld;
            if(null == dataOldBd || dataOldBd.compareTo(BigDecimal.ZERO)==0){
                return "100";
            }
            BigDecimal dataNewBig = (BigDecimal) dataNew;
            BigDecimal dataOldBig = (BigDecimal) dataOld;
            BigDecimal changeData =  dataNewBig.subtract(dataOldBig);
            String changeRatio = changeData.divide(dataOldBig,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100.00)).setScale(2) + "";
            return changeRatio;
        }else{
            return "0.00";
        }
    }

    /**
     * 获取环比符号
     * @param changeRatio
     * @return
     */
    private String getSignByRatio(String changeRatio) {
        return changeRatio.indexOf("-")>-1?"-":"+";
    }

}
