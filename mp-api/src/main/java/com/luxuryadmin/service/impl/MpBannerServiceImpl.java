package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.MpBanner;
import com.luxuryadmin.mapper.MpBannerMapper;
import com.luxuryadmin.param.op.ParamAppBannerQuery;
import com.luxuryadmin.service.MpBannerService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.op.VoOpBannerForUnion;
import com.luxuryadmin.vo.pro.VoProClassify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * banner表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Service
@Transactional
public class MpBannerServiceImpl implements MpBannerService {


    /**
     * 注入dao
     */
    @Resource
    private MpBannerMapper mpBannerMapper;

    @Resource
    private ProClassifyService proClassifyService;

    @Resource
    private ServicesUtil servicesUtil;

    /**
     * 获取banner信息
     *
     * @return
     */
    @Override
    public VoShopUnionByAppShow listBannerInfo() {
        VoShopUnionByAppShow shopUnionByAppShow = new VoShopUnionByAppShow();
        List<VoProClassify> voProClassifyList = proClassifyService.listSysProClassifyByState(null);
        shopUnionByAppShow.setClassifyList(voProClassifyList);
        ParamAppBannerQuery paramAppBannerQuery = new ParamAppBannerQuery();
        paramAppBannerQuery.setPos("shopUnion");
        List<MpBanner> bannerInfoLists = mpBannerMapper.listBannerInfo();
        if (bannerInfoLists != null && bannerInfoLists.size() > 0) {
            List<VoOpBannerForUnion> voBannerInfoLists = new ArrayList<>();
            for (MpBanner banner : bannerInfoLists) {
                VoOpBannerForUnion voOpBannerForUnion = new VoOpBannerForUnion();
                voOpBannerForUnion.setTitle(StringUtil.isNotBlank(banner.getBannerName()) ? banner.getBannerName() : null);
                voOpBannerForUnion.setImgUrl(StringUtil.isNotBlank(banner.getUrl()) ? servicesUtil.formatImgUrl(banner.getUrl()) : null);
                //voOpBannerForUnion.setJumpType(StringUtil.isNotBlank(banner.getSkipType()) ? banner.getSkipType() : null);
                voOpBannerForUnion.setJumpH5Url(StringUtil.isNotBlank(banner.getSkipAddress()) ? banner.getSkipAddress() : null);
                voOpBannerForUnion.setJumpNativePage(StringUtil.isNotBlank(banner.getSkipAddress()) ? banner.getSkipAddress() : null);
                voOpBannerForUnion.setAppId(StringUtil.isNotBlank(banner.getAppId()) ? banner.getAppId() : null);
                voBannerInfoLists.add(voOpBannerForUnion);
            }
            shopUnionByAppShow.setBannerList(voBannerInfoLists);
        }
        return shopUnionByAppShow;
    }
}
