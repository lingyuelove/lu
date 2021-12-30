package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.shp.ShpDetail;
import com.luxuryadmin.mapper.shp.ShpDetailMapper;
import com.luxuryadmin.service.shp.ShpDetailService;
import com.luxuryadmin.vo.shp.VoShopValidInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author monkey king
 * @Date 2019/12/25 1:43
 */
@Slf4j
@Service
public class ShpDetailServiceImpl implements ShpDetailService {

    @Resource
    private ShpDetailMapper shpDetailMapper;
    @Autowired
    private ServicesUtil servicesUtil;

    @Override
    public int saveOrUpdateShpDetail(ShpDetail shpDetail) {
        Integer id = shpDetail.getId();
        if (id != null) {
            return shpDetailMapper.updateObject(shpDetail);
        }
        return shpDetailMapper.saveObject(shpDetail);
    }

    @Override
    public int updateShpDetailByShopId(ShpDetail shpDetail) {
        return shpDetailMapper.updateShpDetailByShopId(shpDetail);
    }

    @Override
    public ShpDetail selectByShopId(Integer id) {
        return shpDetailMapper.getObjectByShopId(id);
    }

    @Override
    public VoShopValidInfo getShopValidInfo(String shopId) {
        VoShopValidInfo shopValidInfo = shpDetailMapper.getShopValidInfo(shopId);
        if (!LocalUtils.isEmptyAndNull(shopValidInfo)) {
            //营业执照信息
            shopValidInfo.setLicenseImgUrl(servicesUtil.formatImgUrl(shopValidInfo.getLicenseImgUrl()));
            //认证图片信息
            String validImgUrl = shopValidInfo.getValidImgUrl();
            if (!LocalUtils.isEmptyAndNull(validImgUrl)) {
                String[] split = validImgUrl.split(";");
                List<String> imgList = new ArrayList<>();
                for (String img : split) {
                    imgList.add(servicesUtil.formatImgUrl(img));
                }
                shopValidInfo.setValidImgUrlList(imgList);
            }
            //认证视频信息
            String videoUrl = shopValidInfo.getValidVideoUrl();
            if (!LocalUtils.isEmptyAndNull(videoUrl)) {
                String[] split = videoUrl.split(";");
                List<String> videoList = new ArrayList<>();
                for (String img : split) {
                    videoList.add(servicesUtil.formatImgUrl(img));
                }
                shopValidInfo.setValidVideoUrlList(videoList);
            }
        }
        return shopValidInfo;
    }
}
