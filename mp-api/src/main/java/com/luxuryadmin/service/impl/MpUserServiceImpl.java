package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.context.Cont;
import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.mapper.MpUserMapper;
import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.user.ParamUserInfoSave;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.MpUserService;
import com.luxuryadmin.service.TokenService;
import com.luxuryadmin.util.KKBeanUtil;
import com.luxuryadmin.vo.user.VOAddVipUserList;
import com.luxuryadmin.vo.user.VOUserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 小程序用户表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Service
@Transactional
public class MpUserServiceImpl implements MpUserService {


    /**
     * 注入dao
     */
    @Resource
    private MpUserMapper mpUserMapper;

    @Resource
    private BasicsService basicsService;

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 同步用户信息
     *
     * @param param
     */
    @Override
    public void saveUserInfo(ParamUserInfoSave param) {
        Integer userId = basicsService.getUserId();
        if (userId == null) {
            throw new MyException("用户未登录");
        }
        if (!userId.equals(param.getUserId())) {
            throw new MyException("不是同一用户不可修改用户信息");
        }
        MpUser objectById = mpUserMapper.getObjectById(userId);
        if (objectById == null || "1".equals(objectById.getDel())) {
            throw new MyException("用户不存在");
        }
        BeanUtils.copyProperties(param, objectById);
        objectById.setUsername(DESEncrypt.encodeUsername(param.getUsername()));
        objectById.setUpdateTime(new Date());
        objectById.setNickname(param.getWxNickname() == null ? "" : param.getWxNickname());
        mpUserMapper.updateObject(objectById);
    }

    /**
     * 获取当前登录用户信息
     */
    @Override
    public VOUserInfo getUserInfo() {
        Integer userId = basicsService.getUserId();
        if (userId == null) {
            return null;
        }
        MpUser mpUser = mpUserMapper.getUserInfo(userId);
        VOUserInfo voUserInfo = null;
        //查询白名单
        String adminUser = redisUtil.get(Cont.MP_BAOYANG_CONFIG_WHITELIST);
        if (mpUser != null) {
            voUserInfo = KKBeanUtil.copy(VOUserInfo.class, mpUser);
            voUserInfo.setUsername(DESEncrypt.decodeUsername(mpUser.getUsername()));
            if (StringUtil.isNotBlank(adminUser) && adminUser.contains(DESEncrypt.decodeUsername(mpUser.getUsername()))) {
                voUserInfo.setShowConfig(1);
            } else {
                voUserInfo.setShowConfig(0);
            }
        }
        voUserInfo.setCloudWarehouseNum(redisUtil.get(Cont.MP_BAOYANG_CONFIG_CLOUDWAREHOUSE));
        voUserInfo.setShopNum(redisUtil.get(Cont.MP_BAOYANG_CONFIG_SHOPNUM));
        voUserInfo.setHomeUrl(redisUtil.get(Cont.MP_BAOYANG_CONFIG_HOMEURL));
        voUserInfo.setAppId(redisUtil.get(Cont.MP_BAOYANG_CONFIG_APPID));
        return voUserInfo;
    }

    /**
     * 修改体验期会员
     *
     * @return
     */
    @Override
    public void updatePastUserInfo(Date date) {
        mpUserMapper.updatePastUserInfo(date);
    }

    /**
     * 获取正式会员信息
     *
     * @param date
     * @return
     */
    @Override
    public void updateVipUserInfo(Date date) {
        mpUserMapper.updateVipUserInfo(date);
    }


    /**
     * 用户退出登录
     */
    @Override
    public void exitLogin() {
        Integer userId = basicsService.getUserId();
        if (userId == null) {
            throw new MyException("用户未登录");
        }
        MpUser mpUser = mpUserMapper.getObjectById(userId);
        if (mpUser != null) {
            String username = DESEncrypt.decodeUsername(mpUser.getUsername());
            tokenService.clearToken(username);
        }
    }

    @Override
    public List<String> getUserName(Date date) {
        return mpUserMapper.getUserName(date);
    }

    @Override
    public List<MpUser> getVipUserName() {
        return mpUserMapper.getVipUserName();
    }

    /**
     * 将小程序会员转为奢当家会员
     *
     * @param yesSdjVipPhones
     */
    @Override
    public void updateSdjVipByMpVipUsername(List<String> yesSdjVipPhones) {
        mpUserMapper.updateSdjVipByMpVipUsername(yesSdjVipPhones);
    }

    /**
     * 将奢当家会员过期
     *
     * @param noSdjVipPhones
     */
    @Override
    public void updateSdjVipPast(List<String> noSdjVipPhones) {
        mpUserMapper.updateSdjVipPast(noSdjVipPhones);
    }

    /**
     * 获取添加用户信息
     *
     * @param param
     * @return
     */
    @Override
    public List<VOAddVipUserList> listAddVipUser(ParamBasic param) {
        List<VOAddVipUserList> vo = mpUserMapper.listAddVipUser();
        vo.stream().forEach(v -> {
            String username = DESEncrypt.decodeUsername(  v.getUsername());
            v.setUsername(username);
        });
        return vo;
    }

}
