package com.luxuryadmin.admin.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.ConstantWeChat;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.encrypt.PBKDF2Util;
import com.luxuryadmin.common.utils.*;
import com.luxuryadmin.entity.shp.ShpBindCount;
import com.luxuryadmin.entity.shp.ShpUserToken;
import com.luxuryadmin.entity.sys.SysUser;
import com.luxuryadmin.enums.login.EnumOtherLoginType;
import com.luxuryadmin.enums.login.EnumSendSmsType;
import com.luxuryadmin.param.login.*;
import com.luxuryadmin.service.shp.ShpBindCountService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.shp.ShpUserTokenService;
import com.luxuryadmin.service.shp.impl.ShpUserServiceImpl;
import com.luxuryadmin.service.sys.SysUserService;
import com.luxuryadmin.vo.shp.VoShpUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 登录注册Controller
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@Api(tags = "A001.【登录注册】模块", description = "/login |登录注册发短信验证码接口 ")
public class LoginController extends BaseController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    RedisUtil redisUtil;

    @RequestRequire
    @ApiOperation(
            value = "1.用户帐号密码登录接口",
            notes = "用户登录接口--帐号密码登录;<br/>登录成功返回用户信息;",
            httpMethod = "POST")
    @RequestMapping("/login")
    public BaseResult login(@Valid ParamLoginPwd paramLoginPwd)  {
        //更新用户表上次登录时间、更新人、更新时间等字段
        SysUser sysUser = sysUserService.getUserByName(paramLoginPwd.getUsername());
        if (LocalUtils.isEmptyAndNull(sysUser)){
            return BaseResult.defaultErrorWithMsg("用户不存在");
        }
        sysUserService.updateSysUser(sysUser);
        //判断授权信息
        sysUserService.checkUserIsEffective(sysUser, paramLoginPwd);
        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展
        String uuid = LocalUtils.getUUID();
        String key = ConstantRedisKey.getAdminTokenKey(uuid);
        //设置key值
        redisUtil.set(key, sysUser.getId().toString());
        //设置过期时间
        redisUtil.expire(key,8, TimeUnit.HOURS);

        HashMap hashMap = new HashMap();
        hashMap.put("uuid",uuid);
        hashMap.put("username", DESEncrypt.decodeUsername(sysUser.getUsername()));
        hashMap.put("id",sysUser.getId());
        hashMap.put("nickname",sysUser.getNickname());
        hashMap.put("token",uuid);
        //返回json数据
        BaseResult result = BaseResult.okResult(hashMap);
        return result;
    }
    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @RequestRequire
    @ApiOperation(
            value = "退出登录接口",
            notes = "退出登录接口;",
            httpMethod = "POST")
    @RequestMapping(value = "/logout")
    public BaseResult logout(HttpServletRequest request,HttpServletResponse response) {
        //用户退出逻辑
        String token = request.getHeader("Authorization");
        if(LocalUtils.isEmptyAndNull(token)) {
            return BaseResult.defaultErrorWithMsg("退出登录失败！");
        }
        String key = ConstantRedisKey.getAdminTokenKey(token);
        String userId = redisUtil.get(key);
        SysUser sysUser = sysUserService.selectById(Integer.parseInt(userId));
        if(sysUser!=null) {
            //登出日志暂无记录人员记录
            //update-end--Author:wangshuai  Date:20200714  for：登出日志没有记录人员
            log.info(" 用户名:  "+sysUser.getNickname()+",退出成功！ ");
            //清空用户登录Token缓存
            redisUtil.delete(key);

            return BaseResult.okResult("退出登录成功！");
        }else {
            return BaseResult.defaultErrorWithMsg("Token无效!");
        }
    }
}
