package com.luxuryadmin.mapper.biz;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.biz.BizLeaguerAdd;
import com.luxuryadmin.vo.biz.VoBizLeaguerAdd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface BizLeaguerAddMapper extends BaseMapper {

    /**
     * 获取好友申请记录
     *
     * @param shopId
     * @return
     */
    List<VoBizLeaguerAdd> listBizLeaguerAddByShopId(int shopId);

    /**
     * 更新申请添加友商记录表
     *
     * @param bizLeaguerAdd
     * @return
     */
    int updateBizLeaguerAdd(BizLeaguerAdd bizLeaguerAdd);

    /**
     * 根据被添加好友【店铺ID】查询所有未被处理的消息
     * 【biz_leaguer_add.state】 状态为 10:已发请求(待确认); 12:已发请求(已过期);
     *
     * @param fkShpShopId
     * @return
     */
    Integer selectUnHandleCountByShopId(Integer fkShpShopId);

    /**
     * 查找添加友商记录
     *
     * @param shopId
     * @param leaguerShopId
     * @return
     */
    BizLeaguerAdd getLeaguerAddByShopIdAndLeaguerShopId(
            @Param("shopId") Integer shopId, @Param("leaguerShopId") Integer leaguerShopId);

    /**
     * 删除店铺友商添加记录
     * @param shopId
     */
    void deleteLeaguerAddForShop(int shopId);
}