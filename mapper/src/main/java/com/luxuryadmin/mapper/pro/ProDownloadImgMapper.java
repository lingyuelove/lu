package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProDownloadImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**产品图片下载记录
 * @author monkey king
 * @date 2020-03-03 17:31:54
 */
@Mapper
public interface ProDownloadImgMapper extends BaseMapper {
    /**
     * 统计下载人数<br/>
     * 统计该店铺该商品图片被多少人下载;
     *
     * @param shopId 店铺id
     * @param bizId 商品业务逻辑id
     * @return
     */
    int countDownload(
            @Param("shopId") int shopId, @Param("bizId") String bizId);

    /**
     * 自己是否下载过
     * @param shopId 店铺id
     * @param userId 用户id
     * @param bizId 业务逻辑id
     * @return
     */
    int existsSelfDownload(@Param("shopId") int shopId,
                           @Param("userId") int userId, @Param("bizId") String bizId);


    /**
     * 获取所有下载记录
     *
     * @return
     */
    List<ProDownloadImg> listProDownloadImg();


    /**
     * 删除店铺商品下载记录
     *
     * @param shopId
     * @return
     */
    int deleteProDownloadImgByShopId(int shopId);

    /**
     * 查询下载图片记录
     * @param shopId
     * @param userId
     * @param bizId
     * @return
     */
    ProDownloadImg selectDownload(@Param("shopId") int shopId, @Param("userId") int userId, @Param("bizId") String bizId);
}
