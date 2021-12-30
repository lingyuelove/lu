package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpWechat;
import com.luxuryadmin.param.shp.ParamShpWechatUpdate;
import com.luxuryadmin.vo.shp.VoShpWechat;
import com.luxuryadmin.vo.shp.VoShpWechatByShow;

import java.util.List;

/**
 * 店铺微信控制器Service
 * @author sanjin
 * @Date 2020/08/31 16:02
 */
public interface ShpWechatService {

    /**
     * 添加店铺微信
     * @param shpWechat
     */
    Integer addShpWechat(ShpWechat shpWechat);

    /**
     * 根据店铺ID查询店铺微信列表
     * @param shopId
     * @return
     */
    List<VoShpWechat> listShpWechat(Integer shopId);

    /**
     *【逻辑删除】店铺微信;
     * @param id
     * @param shopId
     */
    Integer deleteShpWechat(String id, Integer shopId);

    /**
     * 查询店铺微信列表
     * @param shopId
     * @return
     */
    VoShpWechatByShow getWechatByShow(Integer shopId,Integer userId,  String unionUrl);

    /**
     * 更新店铺微信
     * @param shpWechatUpdate
     */
    void updateShpWechat(ParamShpWechatUpdate shpWechatUpdate);

    VoShpWechat addByShopId(Integer shopId,Integer userId);
}
