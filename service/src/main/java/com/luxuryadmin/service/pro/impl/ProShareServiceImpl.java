package com.luxuryadmin.service.pro.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.entity.shp.ShpShopConfig;
import com.luxuryadmin.mapper.pro.ProClassifyMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.mapper.pro.ProShareMapper;
import com.luxuryadmin.mapper.pro.ProShareSeeUserMapper;
import com.luxuryadmin.mapper.shp.ShpShopConfigMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.pro.VoShareProduct;
import com.luxuryadmin.service.pro.ProShareService;
import com.luxuryadmin.service.shp.ShpShareTotalService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-06-10 13:54:32
 */
@Slf4j
@Service
public class ProShareServiceImpl implements ProShareService {

    @Resource
    private ProShareMapper proShareMapper;

    @Resource
    private ProProductMapper proProductMapper;

    @Autowired
    private ShpShopService shopService;

    @Autowired
    private ShpShareTotalService shareTotalService;
    @Autowired
    private ServicesUtil servicesUtil;
    @Resource
    private ShpShopMapper shpShopMapper;
    @Resource
    private ShpShopConfigMapper shpShopConfigMapper;
    @Resource
    private ProClassifyMapper proClassifyMapper;
    @Resource
    private ProShareSeeUserMapper proShareSeeUserMapper;

    @Override
    public VoShareProduct getProIdsByShareBatch(ParamShareProductQuery shareProduct) {
        return proShareMapper.getProIdsByShareBatch(shareProduct);
    }

    @Override
    public List<VoProductLoad> listShareProductByProId(ParamShareProductQuery productQuery) {
        return proProductMapper.listShareProductByProId(productQuery);
    }

    @Override
    public String saveShareProduct(ProShare proShare) {
        String saveBatch = proShare.getShareBatch();
        String shareName = proShare.getShareName();
        if (LocalUtils.isEmptyAndNull(saveBatch)) {
            saveBatch = System.currentTimeMillis() + "";
            proShare.setShareBatch(saveBatch);
        }
        if (LocalUtils.isEmptyAndNull(shareName)) {
            proShare.setShareName(saveBatch);
        }
        this.setProShareName(proShare);

        proShareMapper.saveObject(proShare);
        this.updateShareShopInfo(proShare);
        return saveBatch;
    }

    /**
     * 分享名称的设置
     * @param proShare
     * @return
     */
    public ProShare setProShareName(ProShare proShare) {
        int tempId = 0;
        if (proShare.getShareName().startsWith("tempPro")) {
            tempId = Integer.parseInt(proShare.getShareName().split("_")[1]);
        }

        if (tempId <= 0) {
            //判断店铺分享还是商品分享 all：店铺分享 商品id逗号分割是商品分享
            if ("all".equals(proShare.getProId())) {
                VoUserShopBase voUserShopBase = shpShopMapper.getVoUserShopBaseByShopId(proShare.getFkShpShopId());
                proShare.setShowName(voUserShopBase.getShopName());
                proShare.setShareImg(voUserShopBase.getShopHeadImgUrl());
            } else {
                String proId = proShare.getProId();
                List idList = Arrays.asList(proId.split(","));
                ProProduct product = (ProProduct) proProductMapper.getObjectById(idList.get(0));
                //判断商品是多件商品分享还是单个商品分享
                if (idList.size() > 1) {
                    proShare.setShowName(product.getName() + "等多件商品");
                    proShare.setShareImg(product.getSmallImg());
                } else {
                    proShare.setShowName(product.getName());
                    proShare.setShareImg(product.getSmallImg());
                }
            }
        } else {
            //如果是临时仓分享
            List<VoShareSearchList> shareSearchLists = proShareMapper.getShareProductForTempId(proShare.getFkShpShopId(), tempId);
            //判断临时仓可分享商品是否为空
            if (LocalUtils.isEmptyAndNull(shareSearchLists)) {
                VoUserShopBase voUserShopBase = shpShopMapper.getVoUserShopBaseByShopId(proShare.getFkShpShopId());
                proShare.setShareImg(voUserShopBase.getShopHeadImgUrl());
                proShare.setShowName("临时仓");
            } else {
                proShare.setShareImg(shareSearchLists.get(0).getSmallImg());
                proShare.setShowName(shareSearchLists.get(0).getTempName() + "临时仓");
            }
        }
        return proShare;
    }

    @Override
    public String saveShareProductForQRCode(ParamShareProductSave save) {
        ProShare share = new ProShare();
        share.setFkShpShopId(save.getShopId());
        share.setFkShpUserId(save.getUserId());
        share.setShopNumber(save.getShopNumber());
        share.setUserNumber(save.getUserNumber());
        share.setShowPrice(save.getShowPrice());
        share.setProId(save.getProIds());
        share.setFkProClassifyCode(save.getClassifyCode());
        if (!LocalUtils.isEmptyAndNull(save.getTempPro())) {
            String tempId = save.getTempId();
            //从临时仓分享商品时,必须加上临时仓id
            if (LocalUtils.isEmptyAndNull(tempId)) {
                throw new MyException("tempId不允许为空!");
            }
            share.setShareName("tempPro_" + tempId);
        }
        String saveBatch = this.saveShareProduct(share);
        this.setProShareName(share);

        return saveBatch;
    }


    /**
     * 分享商品添加会员时长
     *
     * @param proShare
     */
    private void updateShareShopInfo(ProShare proShare) {
//        Integer shareCount=proShareMapper.listProShareByShopId(proShare.getFkShpShopId());
//        if (shareCount<24){
//            VoShpShareType voShpShareType=shpShareTypeMapper.getShareTypeByCode("1");
//            VoShpShareTotal voShpShareTotal=shpShareTotalMapper.getShareTotalByToDay(voShpShareType.getCode(),proShare.getFkShpUserId(),proShare.getFkShpShopId());
//
//            ShpShareTotal shpShareTotal=new ShpShareTotal();
//        }
        //查询店铺
        ShpShop shop = shopService.getShpShopById(proShare.getFkShpShopId().toString());
        if (shop == null) {
            return;
        }
        //判断是否为会员 是会员并且分享同行价添加时长"yes".equals(shop.getIsMember()) &&
        String tradePrice = "tradePrice";
        String showPrice = proShare.getShowPrice();
        boolean isOk = !LocalUtils.isEmptyAndNull(showPrice) && showPrice.contains(tradePrice);
        if (isOk || tradePrice.equals(proShare.getShowPrice())) {
            shareTotalService.addOrUpdateShareTotal(proShare);
        }
    }

    @Override
    public void deleteProShareByShopId(int shopId) {
        proShareMapper.deleteProShareByShopId(shopId);
    }

    @Override
    public VoSharePage getShareByList(ParamShare paramShare) {
        PageHelper.startPage(Integer.parseInt(paramShare.getPageNum()), paramShare.getPageSize());
        List<VoShareList> shareLists = proShareMapper.getShareByList(paramShare.getShopId());
        if (shareLists != null && shareLists.size() > 0) {
            shareLists.forEach(voShareList -> {

                String smallImg = voShareList.getShareImg();
                smallImg = servicesUtil.formatImgUrl(smallImg, true);
                voShareList.setShareImg(smallImg);
            });
        }

        PageInfo<VoShareList> pageInfo = new PageInfo(shareLists);
        VoSharePage voSharePage = new VoSharePage();
        voSharePage.setPageNum(pageInfo.getPageNum());
        voSharePage.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            voSharePage.setHasNextPage(true);
        } else {
            voSharePage.setHasNextPage(false);
        }
        voSharePage.setTotal(pageInfo.getTotal());
        voSharePage.setList(shareLists);
        ShpShopConfig shopConfig = shpShopConfigMapper.getShopConfigByShopId(paramShare.getShopId());
        if (shopConfig != null) {
            voSharePage.setOpenShareUser(shopConfig.getOpenShareUser());
        } else {
            voSharePage.setOpenShareUser("0");
        }

        voSharePage.setOpenShareUserUrl("https://file.luxuryadmin.com/h5/assets/mp-user-access/index.html");
        return voSharePage;
    }

    @Override
    public void deleteById(Integer id) {
        ProShare proShare = new ProShare();
        proShare.setId(id);
        proShare.setDel("1");
        proShareMapper.updateObject(proShare);
    }

    @Override
    public VoShareUserPage listUnionShareUser(ParamShareUser paramShareUser) {
        PageHelper.startPage(Integer.parseInt(paramShareUser.getPageNum()), paramShareUser.getPageSize());
        List<VoShareUserList> list = proShareSeeUserMapper.getShareUserList(paramShareUser);
        if (list != null && list.size() > 0) {
            list.forEach(shareUserList -> {
                if ("1".equals(shareUserList.getSex())) {
                    shareUserList.setSex("男");
                } else if ("2".equals(shareUserList.getSex())) {
                    shareUserList.setSex("女");
                }
            });
        }
        PageInfo<VoShareList> pageInfo = new PageInfo(list);
        VoShareUserPage shareUserPage = new VoShareUserPage();
        shareUserPage.setPageNum(pageInfo.getPageNum());
        shareUserPage.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            shareUserPage.setHasNextPage(true);
        } else {
            shareUserPage.setHasNextPage(false);
        }
        shareUserPage.setTotal(pageInfo.getTotal());
        shareUserPage.setList(list);
        return shareUserPage;
    }

    @Override
    public VoShareProductForApplets getByShareBatch(ParamShareProductForApplets shareProductForApplets) {
        ProShare share = proShareMapper.getShareForShareBatch(shareProductForApplets.getShareBatch());
        if (share == null) {
            throw new MyException("此分享不存在");
        }
        VoShareProductForApplets shareProduct = new VoShareProductForApplets();
        shareProduct.setShareBatch(shareProductForApplets.getShareBatch());
        shareProduct.setShopNumber(share.getShopNumber());
        shareProduct.setUserNumber(share.getUserNumber().toString());
        String proId = null;
        if (!"all".equals(share.getProId())) {
            List<String> idList = Arrays.asList(share.getProId().split(","));
            if (idList != null && idList.size() > 0) {
                proId = idList.get(0);
            }
        }
        if ("1".equals(shareProductForApplets.getType()) && proId != null) {
            ProProduct proProduct = (ProProduct) proProductMapper.getObjectById(proId);
            shareProduct.setBizId(proProduct.getBizId());
            shareProduct.setShopId(share.getFkShpShopId());
        }
        return shareProduct;
    }

    @Override
    public List<VoProClassify> listProClassifyByShareBatch(String shareBatch) {
        ProShare share = proShareMapper.getShareForShareBatch(shareBatch);
        if (share == null){
            return null;
        }
        String classifyCode = null;
        List<String> codeList = null;
        if (!LocalUtils.isEmptyAndNull(share.getFkProClassifyCode())){
             classifyCode = share.getFkProClassifyCode();
            //根据逗号分隔转化为list
            codeList = Arrays.asList(classifyCode.split(","));
        }
        List<VoProClassify> classifies = new ArrayList<>();
        if (!LocalUtils.isEmptyAndNull(codeList)){
            codeList.forEach(code ->{
                VoProClassify classify =proClassifyMapper.getProClassifyByType(share.getFkShpShopId(), null,code);
                if (!LocalUtils.isEmptyAndNull(classify)){
                    classifies.add(classify);
                }

            });
            return classifies;
        }
        return null;
    }

    @Override
    public List<VoUnionAccess> listUnionShareUser(ParamUnionAccess paramShareUser) {
        List<VoUnionAccess> shareLists = proShareMapper.listUnionShareUser(paramShareUser);

        if (!LocalUtils.isEmptyAndNull(shareLists)) {
            for (VoUnionAccess vo : shareLists) {
                //性别  0：未知、1：男、2：女
                String sex = "未知";
                if ("1".equals(vo.getSex())) {
                    sex = "男";
                } else if ("2".equals(vo.getSex())) {
                    sex = "女";
                }
                vo.setSex(sex);
            }
        }
        return shareLists;
    }

    @Override
    public int countAccessUserCount(ParamUnionAccess paramShareUser) {
        return proShareMapper.countAccessUserCount(paramShareUser);
    }

    @Override
    public int countAccessCount(ParamUnionAccess paramShareUser) {
        return proShareMapper.countAccessCount(paramShareUser);
    }

    @Override
    public int saveOrUpdateObject(ProShare proShare) {
        Integer id = proShare.getId();
        if (LocalUtils.isEmptyAndNull(id)) {
            //保存
            proShareMapper.saveObject(proShare);
            id = proShare.getId();

        }else{
            //更新
            proShareMapper.updateObject(proShare);
        }
        return id;
    }
}
