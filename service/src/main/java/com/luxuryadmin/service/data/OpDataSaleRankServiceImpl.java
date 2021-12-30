package com.luxuryadmin.service.data;

import com.github.pagehelper.Page;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.mapper.ord.OrdOrderMapper;
import com.luxuryadmin.mapper.shp.ShpUserMapper;
import com.luxuryadmin.mapper.shp.ShpUserShopRefMapper;
import com.luxuryadmin.param.data.ParamDataSaleRankQuery;
import com.luxuryadmin.vo.data.VoSaleRank;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @Description: TODO
 * @author: walkingPotato
 * @date: 2020-07-29 18:30
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Service
@Slf4j
public class OpDataSaleRankServiceImpl implements OpDataSaleRankService{

    @Resource
    private OrdOrderMapper ordOrderMapper;

    @Resource
    private ShpUserMapper shpUserMapper;

    @Resource
    private ShpUserShopRefMapper shpUserShopRefMapper;

    @Autowired
    private ServicesUtil servicesUtil;


    /**
     * 根据参数查询销售排行榜首页数据
     *
     * @param paramDataSaleRankQuery
     * @return
     */
    @Override
    public VoSaleRankHomePageData loadSaleRankHomePageData(ParamDataSaleRankQuery paramDataSaleRankQuery, Page page) {
        VoSaleRankHomePageData voSaleRankHomePageData = new VoSaleRankHomePageData();
        formatQueryParam(paramDataSaleRankQuery);
        List<VoSaleRank> list = ordOrderMapper.selectSaleRankListByShopId(paramDataSaleRankQuery);
        Integer listSize = 0;

        String sortType = paramDataSaleRankQuery.getSortType();

        Integer totalSaleCount = 0;
        Double  totalSaleAmount = 0.00;
        Double  totalGrossProfit = 0.00;
        DecimalFormat  df   = new DecimalFormat("######0.00");
        Integer index = 0;
        if(!CollectionUtils.isEmpty(list)){
            listSize = list.size();
            for (VoSaleRank voSaleRank : list) {
                index++;
                if(index<=3) {
                    //设置排名
                    if (StringUtil.isBlank(sortType) || sortType.equals("desc")) {
                        voSaleRank.setRanking(index);
                    } else if (StringUtil.isNotBlank(sortType) && sortType.equals("asc")) {
                        voSaleRank.setRanking(listSize - index + 1);
                    }
                }

                totalSaleCount += voSaleRank.getSaleCount();
                totalSaleAmount += voSaleRank.getSaleAmount();
                totalGrossProfit += voSaleRank.getGrossProfit();

                constructSaleRankUserInfo(paramDataSaleRankQuery.getShopId(),voSaleRank);
            }
        }

        //数据汇总接口不需要返回前3名
        //listSize = listSize > 3 ? 3 : listSize;
        //List<VoSaleRank> topThreeList = list.subList(0,listSize);
        //voSaleRankHomePageData.setSaleRankList(topThreeList);

        voSaleRankHomePageData.setTotalSaleCount("" + totalSaleCount);
        voSaleRankHomePageData.setTotalSaleAmount(df.format(new BigDecimal(totalSaleAmount).stripTrailingZeros()));
        voSaleRankHomePageData.setTotalGrossProfit(df.format(new BigDecimal(totalGrossProfit).stripTrailingZeros()));
        String grossProfitRate = "0.00";
        if(!totalSaleAmount.equals(0.00)){
            try {
                grossProfitRate = df.format(LocalUtils.calcNumber(totalGrossProfit*100, "/", totalSaleAmount));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        voSaleRankHomePageData.setGrossProfitRate(grossProfitRate);

        return voSaleRankHomePageData;
    }

    /**
     * 格式化销售分析参数
     * @param queryParam
     */
    private void formatQueryParam(ParamDataSaleRankQuery queryParam) {
        //对多选的参数(销售人员)进行逗号分开;
        queryParam.setSaleUserId(LocalUtils.formatParamForSqlInQuery(queryParam.getSaleUserId()));

        //对多选的参数(订单类型)进行逗号分开;
        queryParam.setOrderType(LocalUtils.formatParamForSqlInQuery(queryParam.getOrderType()));

        //对多选的参数(商品属性)进行逗号分开;
        queryParam.setAttributeCode(LocalUtils.formatParamForSqlInQuery(queryParam.getAttributeCode()));

        //如果有分类参数
        String classifyCode = queryParam.getClassifyCode();
        if (!LocalUtils.isEmptyAndNull(classifyCode)) {
            String[] classifyCodeArray = LocalUtils.splitString(classifyCode, ";");
            StringBuffer newCode = new StringBuffer();
            for (String code : classifyCodeArray) {
                if (code.length() >= 2) {
                    code = code.substring(0, 2);
                }
                newCode.append(code).append("|");
            }
            if (newCode.toString().endsWith("|")) {
                newCode = new StringBuffer(newCode.substring(0, newCode.length() - 1));
            }
            queryParam.setClassifyCode(newCode.toString());
        }
    }

    /**
     * 分页查询【销售排行榜】记录
     * @param paramDataSaleRankQuery
     * @return
     */
    @Override
    public List<VoSaleRank> listSaleRank(ParamDataSaleRankQuery paramDataSaleRankQuery, Page page) {
        formatQueryParam(paramDataSaleRankQuery);
        List<VoSaleRank> list = ordOrderMapper.selectSaleRankListByShopId(paramDataSaleRankQuery);
        String sortType = paramDataSaleRankQuery.getSortType();
        Integer index = 0;
        for (VoSaleRank voSaleRank : list) {
            index = setRanking(page, sortType, index, voSaleRank);
            constructSaleRankUserInfo(paramDataSaleRankQuery.getShopId(),voSaleRank);
        }
        return list;
    }

    private Integer setRanking(Page page, String sortType, Integer index, VoSaleRank voSaleRank) {
        //设置排名
        index++;
        if (StringUtil.isBlank(sortType) || sortType.equals("desc")) {
            voSaleRank.setRanking(page.getStartRow() + index);
        }
        else if (StringUtil.isNotBlank(sortType) && sortType.equals("asc")) {
            Integer ranking = (new Long(page.getTotal() - (page.getPageNum() * page.getPageNum() + index - 2))).intValue();
            voSaleRank.setRanking(ranking);
        }
        return index;
    }

    /**
     * 构造【销售排行榜】用户相关信息
     * @param voSaleRank
     */
    private void constructSaleRankUserInfo(Integer shopId,VoSaleRank voSaleRank) {
        Integer userId = voSaleRank.getUserId();
        VoUserShopBase voUserShopBase = shpUserMapper.getShpUserBaseInfoByUserId(userId);
        if (!LocalUtils.isEmptyAndNull(voUserShopBase)) {
            voSaleRank.setSaleCount(voSaleRank.getSaleCount());
            voSaleRank.setSaleAmount(new BigDecimal(voSaleRank.getSaleAmount()).stripTrailingZeros().doubleValue());
            voSaleRank.setGrossProfit(new BigDecimal(voSaleRank.getGrossProfit()).stripTrailingZeros().doubleValue());
            voSaleRank.setHeadImgUrl(servicesUtil.formatImgUrl(voUserShopBase.getUserHeadImgUrl()));
            String roleName = shpUserShopRefMapper.getUserTypeRoleNameByShopIdAndUserId(shopId,userId);
            voSaleRank.setRoleName(roleName);
        }
    }
}
