package com.luxuryadmin.service.biz;

import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.entity.biz.BizLeaguerAdd;
import com.luxuryadmin.param.biz.ParamAddLeaguer;
import com.luxuryadmin.vo.biz.VoBizLeaguerAdd;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 * @date 2020-01-12 18:40:10
 */
public interface BizLeaguerAddService {

    /**
     * 获取好友申请记录
     *
     * @param shopId
     * @return
     */
    List<VoBizLeaguerAdd> listBizLeaguerAddByShopId(int shopId);


    /**
     * 发起添加友商好友申请
     * @param paramAddLeaguer
     */
    void saveBizLeaguerAdd(ParamAddLeaguer paramAddLeaguer);

    /**
     * 更新申请添加友商记录表
     *
     * @param bizLeaguerAdd
     * @param state
     * @param note
     * @param visible
     * @return
     */
    void updateBizLeaguerAdd(BizLeaguerAdd bizLeaguerAdd, String state, String note, String visible);


    /**
     * 查找添加友商记录
     *
     * @param shopId
     * @param leaguerShopId
     * @return
     */
    BizLeaguerAdd getLeaguerAddByShopIdAndLeaguerShopId(Integer shopId, Integer leaguerShopId);

    /**
     * 删除店铺友商添加记录
     * @param shopId
     */
    void deleteLeaguerAddForShop(int shopId);
}
