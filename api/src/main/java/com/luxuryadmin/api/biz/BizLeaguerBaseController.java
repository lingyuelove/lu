package com.luxuryadmin.api.biz;

import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.vo.biz.VoBizLeaguer;

import java.util.List;

/**
 * 友商基础模块
 *
 * @author monkey king
 * @date 2020-08-04 15:56:00
 */
public class BizLeaguerBaseController extends ProProductBaseController {

    /**
     * 格式化图片
     *
     * @param voBizLeaguer
     */
    protected void formatVoBizLeaguer(VoBizLeaguer voBizLeaguer) {
        if (!LocalUtils.isEmptyAndNull(voBizLeaguer)) {
            String headImgUrl = voBizLeaguer.getHeadImgUrl();
            if(!LocalUtils.isEmptyAndNull(headImgUrl) && !headImgUrl.contains("http")){
                voBizLeaguer.setHeadImgUrl(servicesUtil.formatImgUrl(headImgUrl));
            }

            String coverImgUrl = voBizLeaguer.getCoverImgUrl();
            if(!LocalUtils.isEmptyAndNull(coverImgUrl) && !coverImgUrl.contains("http")){
                voBizLeaguer.setCoverImgUrl(servicesUtil.formatImgUrl(coverImgUrl));
            }
        }
    }

    protected void formatVoBizLeaguer(List<VoBizLeaguer> voBizLeaguerList) {
        if (!LocalUtils.isEmptyAndNull(voBizLeaguerList)) {
            for (VoBizLeaguer voBizLeaguer : voBizLeaguerList) {
                formatVoBizLeaguer(voBizLeaguer);
            }
        }
    }
    /**
     * 格式化前端商品列表请求的查询参数;
     *
     * @param queryParam
     */
    protected void formatLeaguerQueryParam(ParamLeaguerProductQuery queryParam) {
        queryParam.setShopId(getShopId());


        //如果有分类参数
        String classifyCode = queryParam.getClassifyCode();
        if (!LocalUtils.isEmptyAndNull(classifyCode) && classifyCode.length() >= 2) {
            classifyCode = classifyCode.substring(0, 2) + "%";
            queryParam.setClassifyCode(classifyCode);
        }
        //如果有查询名称参数
        String name = queryParam.getProName();
        if (!LocalUtils.isEmptyAndNull(name)) {
            name = name.trim();
            name = name.replaceAll("\\s+", ".*");
            queryParam.setProName(name);
        }

        //对多选的参数(适用人群)进行逗号分开;
        String targetUser = queryParam.getTargetUser();
        targetUser = (LocalUtils.isEmptyAndNull(targetUser) || "通用".equals(targetUser)) ? null : LocalUtils.formatParamForSqlInQuery(targetUser);
        queryParam.setTargetUser(targetUser);

        String newClassifyCode = queryParam.getClassifyCode();
        String newName = queryParam.getProName();
        String newTargetUser = queryParam.getTargetUser();

        //如果有查询参数,则不管当前是否显示未星标商品,都需要全部搜索
        if (!LocalUtils.isEmptyAndNull(newClassifyCode) || !LocalUtils.isEmptyAndNull(newName) ||
                !LocalUtils.isEmptyAndNull(newTargetUser) || !LocalUtils.isEmptyAndNull(queryParam.getPriceMin())
                || !LocalUtils.isEmptyAndNull(queryParam.getPriceMax())) {
            queryParam.setOnlyShowTopLeaguer("0");
        }
        //对多选的参数(品牌分类)进行逗号分开;
        queryParam.setClassifySub(LocalUtils.formatParamForSqlInQuery(queryParam.getClassifySub(), ","));

        String sortValue = queryParam.getSortValue();
        final String desc = "DESC";
        //sortKey包含: normal,price, time,notDown
        String sortKey = queryParam.getSortKey();
        String sortKeyDb;
        switch (sortKey + "") {
            //按照价格排序
            case "price":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`trade_price` DESC,pro.`init_price` DESC";
                } else {
                    sortKeyDb = "pro.`trade_price` ASC,pro.`init_price` ASC";
                }
                break;
            //按照时间排序或者按照默认排序;
            case "time":
            default:
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`update_time` DESC, pro.`id` DESC";
                } else {
                    sortKeyDb = "pro.`update_time` ASC, pro.`id` ASC";
                }
                break;
        }
        queryParam.setSortKeyDb(sortKeyDb);

    }
}
