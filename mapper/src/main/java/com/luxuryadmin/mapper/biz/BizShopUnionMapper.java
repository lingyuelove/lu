package com.luxuryadmin.mapper.biz;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.biz.BizShopUnion;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamShopUnionForAdminBySearch;
import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminList;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.shp.VoShpUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * biz_shop_union dao
 *
 * @author zhangsai
 * @Date 2021-07-16 17:58:54
 */
@Mapper
public interface BizShopUnionMapper extends BaseMapper<BizShopUnion> {

    /**
     * 获取列表集合显示
     *
     * @param shopUnionForAdminBySearch
     * @return
     */
    List<VoShopUnionForAdminList> getUnionForAdminList(ParamShopUnionForAdminBySearch shopUnionForAdminBySearch);


    /**
     * 根据店铺id获取是否已添加至联盟
     *
     * @param shopId
     * @return
     */
    BizShopUnion getByShopId(Integer shopId);

    /**
     * app端详情
     *
     * @param shopId
     * @return
     */
    VoShopUnionByAppShow getShopUnionForAppByShopId(Integer shopId);


    /**
     * 获取商家联盟;根据状态;
     *
     * @param state -10 已退出 10已加入
     * @return
     */
    List<BizShopUnion> listBizShopUnionByState(String state);

    /**
     * 获取商家联盟小程序分享用户组
     *
     * @return
     */
    List<VoShpUser> listUnionMpShareUser();

}
