package com.luxuryadmin.service.shp;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserDetail;

/**
 * 店铺管理员详情表-业务逻辑层(接口)
 *
 * @author monkey king
 * @date 2019-12-05 13:51:27
 */
public interface ShpUserDetailService {


    /**
     * 添加ShpUserDetail入库
     * @param shpUserId {@link ShpUser}.id
     */
    void saveShpUserDetail(int shpUserId);


    /**
     * 根据用户id获取用户身份信息
     * @param id
     * @return
     */
    ShpUserDetail selectByUserId(String id);
}
