package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.biz.BizShopUnion;
import com.luxuryadmin.entity.shp.ShpWechat;
import com.luxuryadmin.mapper.biz.BizLeaguerConfigMapper;
import com.luxuryadmin.mapper.biz.BizShopUnionMapper;
import com.luxuryadmin.mapper.shp.ShpWechatMapper;
import com.luxuryadmin.param.shp.ParamShpWechatUpdate;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.biz.BizUnionVerifyService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpWechatService;
import com.luxuryadmin.vo.biz.VoBizLeaguerShop;
import com.luxuryadmin.vo.biz.VoUnionVerify;
import com.luxuryadmin.vo.shp.VoShpWechat;
import com.luxuryadmin.vo.shp.VoShpWechatByShow;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 店铺微信控制器Service实现类
 *
 * @author sanjin
 * @Date 2020/08/31 16:04
 */
@Service
public class ShpWechatServiceImpl implements ShpWechatService {

    @Resource
    private ShpWechatMapper shpWechatMapper;
    @Autowired
    private ShpShopService shpShopService;
    @Autowired
    private BizUnionVerifyService bizUnionVerifyService;
    @Resource
    private BizShopUnionMapper bizShopUnionMapper;
    @Override
    public Integer addShpWechat(ShpWechat shpWechat) {
        shpWechat.setInsertTime(new Date());
        shpWechat.setUpdateTime(new Date());
        shpWechat.setDel(ConstantCommon.DEL_OFF);
        return shpWechatMapper.insertSelective(shpWechat);
    }

    @Override
    public List<VoShpWechat> listShpWechat(Integer shopId) {
        List<VoShpWechat> shpWechatList =  shpWechatMapper.selectShpWechatByShopId(shopId);
        if (!LocalUtils.isEmptyAndNull(shpWechatList)){
            shpWechatList.forEach(voShpWechat -> {
                if (!LocalUtils.isEmptyAndNull(voShpWechat.getContactResponsible())){
                    voShpWechat.setShowName(voShpWechat.getContactResponsible()+"("+voShpWechat.getContactPersonName()+")");
                }else {
                    voShpWechat.setShowName(voShpWechat.getContactPersonName());
                }
            });
        }
        return shpWechatList;
    }

    /**
     * 【逻辑删除】店铺微信;
     *
     * @param id
     * @param shopId
     */
    @Override
    public Integer deleteShpWechat(String id, Integer shopId) {
        return shpWechatMapper.deleteShpWechat(id, shopId);
    }

    @Override
    public VoShpWechatByShow getWechatByShow(Integer shopId, Integer userId,  String unionUrl) {
        VoShpWechatByShow wechatByShow = new VoShpWechatByShow();
        List<VoShpWechat> wechatList = this.listShpWechat(shopId);
        wechatByShow.setList(wechatList);
        List<String> stringList = new ArrayList<>();
        stringList.add("拍细节图");
        wechatByShow.setStrings(stringList);

        VoShpWechat shpWechat = this.addByShopId(shopId, userId);
        wechatByShow.setVoShpWechat(shpWechat);
        VoUnionVerify unionVerify= bizUnionVerifyService.getUnionVerifyByShopId(shopId);
        //判断该商家是否已加入商家联盟 如果已加入 显示验证通过
        BizShopUnion shopUnionOld = bizShopUnionMapper.getByShopId(shopId);
        if (!LocalUtils.isEmptyAndNull(shopUnionOld) && shopUnionOld.getState()== 10 && "20".equals(shopUnionOld.getType())){

            wechatByShow.setUnionVerifyState("1");
        }else if (unionVerify == null){
            wechatByShow.setUnionVerifyState("3");
        }else {
            wechatByShow.setUnionVerifyState(unionVerify.getState());
        }
        VoUserShopBase userShopBase =  shpShopService.getVoUserShopBaseByShopId(shopId);
        if (userShopBase == null){
            return wechatByShow;
        }
        wechatByShow.setUnionVerifyUrl(unionUrl+"?shopName="+userShopBase.getShopName()+"&phone="+userShopBase.getShopPhone()+"&address="+userShopBase.getShopCity()+userShopBase.getShopAddress());
        return wechatByShow;
    }

    @Override
    public void updateShpWechat(ParamShpWechatUpdate shpWechatUpdate) {
        ShpWechat shpWechat = new ShpWechat();
        shpWechat.setId(shpWechatUpdate.getId());
        shpWechat.setContactPersonWechat(shpWechatUpdate.getContactPersonWechat());
        shpWechat.setContactPersonName(shpWechatUpdate.getContactPersonName());
        shpWechat.setContactResponsible(shpWechatUpdate.getContactResponsible());
        shpWechat.setUpdateAdmin(shpWechatUpdate.getUserId());
        shpWechat.setUpdateTime(new Date());
        shpWechatMapper.updateByPrimaryKeySelective(shpWechat);
    }

    @Override
    public VoShpWechat addByShopId(Integer shopId, Integer userId) {
        VoShpWechat shpWechat = shpWechatMapper.getWechatByShow(shopId);
        if (shpWechat == null) {
            VoUserShopBase voUserShopBase = shpShopService.getVoUserShopBaseByShopId(shopId);
            if (voUserShopBase == null) {
                throw new MyException("店铺为空");
            }
            ShpWechat wechat = new ShpWechat();
            wechat.setType("1");
            wechat.setInsertTime(new Date());
            wechat.setInsertAdmin(userId);
            wechat.setFkShopId(shopId);
            wechat.setUpdateAdmin(userId);
            wechat.setUpdateTime(new Date());
            wechat.setSortNum(1);
            wechat.setDel("0");
            wechat.setContactPersonName(voUserShopBase.getShopkeeper());
            wechat.setContactPersonWechat(DESEncrypt.decodeUsername(voUserShopBase.getShopPhone()));
            wechat.setContactResponsible("店铺联系人");
            shpWechatMapper.insertSelective(wechat);
            VoShpWechat shpWechatNew = new VoShpWechat();
            BeanUtils.copyProperties(wechat, shpWechatNew);
            shpWechatNew.setId(wechat.getId().toString());
            return shpWechatNew;
        }

        return shpWechat;
    }
}
