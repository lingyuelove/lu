package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProDownloadImg;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 产品图片下载记录
 *
 * @author monkey king
 * @date 2020-03-03 17:39:17
 */
public interface ProDownloadImgService {

    /**
     * 统计下载人数<br/>
     * 统计该店铺该商品图片被多少人下载;
     *
     * @param shopId 店铺id
     * @param bizId  商品业务逻辑id
     * @return 下载人数
     */
    int countDownload(int shopId, String bizId);

    /**
     * 自己是否下载过
     *
     * @param shopId 店铺id
     * @param userId 用户id
     * @param bizId  业务逻辑id
     * @return
     */
    int existsSelfDownload(int shopId, int userId, String bizId);

    /**
     * 新增或者更新实体
     *
     * @param shopId 店铺id
     * @param userId 用户id
     * @param bizId  商品业务逻辑id
     */
    void saveProDownloadImg(int shopId, int userId, String bizId, HttpServletRequest request);


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
     */
    void deleteProDownloadImgByShopId(int shopId);

}
