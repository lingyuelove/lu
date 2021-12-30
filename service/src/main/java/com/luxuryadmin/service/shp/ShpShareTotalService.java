package com.luxuryadmin.service.shp;


import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.entity.shp.ShpShareTotal;
import com.luxuryadmin.vo.shp.VoShopMemberAddHour;
import com.luxuryadmin.vo.shp.VoShpShareTotal;
import com.luxuryadmin.vo.shp.VoShpShareType;

/**
 *商铺分享进度时长累计表 service
 *@author zhangsai
 *@Date 2021-06-08 14:15:43
 */
public interface ShpShareTotalService {

    /**
     * 新增/修改分享商品添加会员时长
     * @param proShare
     */
    void addOrUpdateShareTotal(ProShare proShare);

    /**
     * 新增
     * @param proShare
     * @param voShpShareType
     */
    void addShareTotal(ProShare proShare, VoShpShareType voShpShareType);

    /**
     * 获取当前店铺的分享时长
     * @param shopId
     * @return
     */
    VoShopMemberAddHour getByShopId(Integer shopId);

    /**
     * 更新时长至店铺表
     */
    void updateShopMemberAddHour();
}
