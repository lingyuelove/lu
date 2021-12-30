package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.entity.pro.ProShareSeeUser;
import com.luxuryadmin.entity.pro.ProShareSeeUserAgency;
import com.luxuryadmin.entity.shp.ShpShopConfig;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.mapper.pro.ProShareMapper;
import com.luxuryadmin.mapper.pro.ProShareSeeUserMapper;
import com.luxuryadmin.mapper.shp.ShpShopConfigMapper;
import com.luxuryadmin.param.pro.ParamShareSeeUserAdd;
import com.luxuryadmin.service.pro.ProShareSeeUserAgencyService;
import com.luxuryadmin.service.pro.ProShareSeeUserService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * 小程序访客记录表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-07-06 14:11:33
 */
@Service
@Slf4j
public class ProShareSeeUserServiceImpl implements ProShareSeeUserService {


    /**
     * 注入dao
     */
    @Resource
    private ProShareSeeUserMapper proShareSeeUserMapper;
    @Resource
    private ProShareMapper proShareMapper;

    @Resource
    private ShpShopConfigMapper shpShopConfigMapper;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ProShareSeeUserAgencyService proShareSeeUserAgencyService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addShareSeeUser(ParamShareSeeUserAdd shareSeeUserAdd) {
        //判断是否有此分享记录
        ProShare share = proShareMapper.getShareForShareBatch(shareSeeUserAdd.getShareBatch());
        if (share == null) {
            return;
        }
        //查看店铺设置 此店铺是否开启分享
        ShpShopConfig shopConfig = shpShopConfigMapper.getShopConfigByShopId(share.getFkShpShopId());
        if (shopConfig == null) {
            return;
        }
        if ("0".equals(shopConfig.getOpenShareUser())) {
            return;
        }
        String openId = shareSeeUserAdd.getOpenId();
        //查看分享用户列表
        ProShareSeeUser proShareSeeUser = proShareSeeUserMapper.getShareSeeUserByOpenIdAndShareId(openId, share.getId());
        if (proShareSeeUser != null) {
            return;
        }
        String userId = shareSeeUserAdd.getUserId();
        userId = LocalUtils.isEmptyAndNull(userId) ? "0" : userId;
        ProShareSeeUser shareSeeUser = new ProShareSeeUser();
        shareSeeUser.setFkProShareId(share.getId());
        shareSeeUser.setFkShpUserId(Integer.parseInt(userId));
        shareSeeUser.setFkProShareBatch(shareSeeUserAdd.getShareBatch());
        shareSeeUser.setAvatarUrl(shareSeeUserAdd.getAvatarUrl());
        shareSeeUser.setNickName(shareSeeUserAdd.getNickName());
        shareSeeUser.setGender(shareSeeUserAdd.getGender());
        shareSeeUser.setFkShpShopId(share.getFkShpShopId());
        shareSeeUser.setOpenId(openId);
        shareSeeUser.setType(LocalUtils.isEmptyAndNull(shareSeeUserAdd.getType()) ? "0" : shareSeeUserAdd.getType());
        shareSeeUser.setInsertTime(new Date());
        proShareSeeUserMapper.saveObject(shareSeeUser);

    }

    @Override
    public void addShareSeeUserForUnion(ParamShareSeeUserAdd param) {
        String userId = param.getUserId();
        String userPhone = param.getUserPhone();
        String openId = param.getOpenId();
        userId = LocalUtils.isEmptyAndNull(userId) ? "0" : userId;
        //排除掉非首次访问此小程序;
        boolean isExistsWx = existsWxUserByOpenId("1", openId);
        ProShareSeeUser shareSeeUser = new ProShareSeeUser();
        shareSeeUser.setFkProShareId(0);
        shareSeeUser.setFkShpUserId(Integer.parseInt(userId));
        shareSeeUser.setFkProShareBatch(param.getShareBatch());
        shareSeeUser.setAvatarUrl(param.getAvatarUrl());
        shareSeeUser.setNickName(param.getNickName());
        shareSeeUser.setGender(param.getGender());
        shareSeeUser.setFkShpShopId(0);
        shareSeeUser.setOpenId(param.getOpenId());
        shareSeeUser.setType("1");
        shareSeeUser.setIp(param.getIp());
        shareSeeUser.setInsertTime(new Date());
        shareSeeUser.setPhone(userPhone);
        proShareSeeUserMapper.saveObject(shareSeeUser);

        //后台线程进行判断: 访问的小程序用户 判断是否符合代理拉新;
        ThreadUtils.getInstance().executorService.execute(() -> {
            //排除掉:从推广起已经是奢当家的注册用户;
            String allUsername = ConstantRedisKey.ALL_USERNAME;
            String value = redisUtil.get(allUsername);
            boolean isExists = true;
            //先从缓存判断该用户是否已经存在
            if (!value.contains(userPhone)) {
                isExists = shpUserService.existsShpUserByUsername(userPhone);
            }
            if (!isExistsWx && !isExists) {
                //保存该记录,
                boolean exists = proShareSeeUserAgencyService.existsWxUserByOpenId("1", openId);
                if (!exists) {
                    ProShareSeeUserAgency agency = new ProShareSeeUserAgency();
                    BeanUtils.copyProperties(shareSeeUser, agency);
                    agency.setState("0");
                    agency.setAlreadySendMoney(0);
                    proShareSeeUserAgencyService.saveObject(agency);
                }
            }
        });
    }

    @Override
    public boolean existsWxUserByOpenId(String type, String openId) {
        return proShareSeeUserMapper.existsWxUserByOpenId(type, openId) > 0;
    }

    @Override
    public Integer existsWxUserReturnUserId(String type, String phone) {
        return proShareSeeUserMapper.existsWxUserReturnUserId(type, phone);
    }
}
