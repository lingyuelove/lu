package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpUserNumber;

import java.util.List;

/**
 * 用户编号随机号码池;<br/>
 * 用户编号在此业务中获取;
 *
 * @author monkey king
 * @date 2019-12-19 16:37:38
 */
public interface ShpUserNumberService {

    /**
     * 批量插入新生成未使用的随机用户编号;<br/>
     * 增加号码池容量;
     *
     * @param list
     * @return
     */
    int saveBatchNewUserNumber(List<ShpUserNumber> list);

    /**
     * 生成此号段的所有号码并随机分布;<br/>
     * [startNumber,endNumber]<br/>
     * 例如[1000,9999], 生成的号码包括1000和9999; 总共10000个;<br/>
     *
     * @param startNumber 开始号码;
     * @param endNumber   结束号码;
     * @return
     */
    int generateUserRandomNumber(int startNumber, int endNumber);

    /**
     * 获取最后一个编号;
     *
     * @return
     */
    Integer getLastUserNumber();

    /**
     * 获取比当前id大的ShpUserNumber
     *
     * @return
     */
    ShpUserNumber getShpUserNumberOverId();

    /**
     * 更新用户实体;
     *
     * @param shpUserNumber
     * @return
     */
    int updateShpUserNumber(ShpUserNumber shpUserNumber);

    /**
     * 用户已使用该用户编号;更新该记录;
     *
     * @param userId
     * @param shpUserNumber
     * @return
     */
    int usedShpUserNumber(int userId, ShpUserNumber shpUserNumber);

    /**
     * 初始化用户编号;2000个号码
     * @return
     */
    int initShpUserNumber();

}
