package com.luxuryadmin.service.sys.impl;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.sys.SysUser;
import com.luxuryadmin.entity.sys.SysUserRoleRef;
import com.luxuryadmin.mapper.sys.SysUserMapper;
import com.luxuryadmin.mapper.sys.SysUserRoleRefMapper;
import com.luxuryadmin.param.login.ParamLoginPwd;
import com.luxuryadmin.param.sys.ParamSysPermDelete;
import com.luxuryadmin.param.sys.ParamSysUser;
import com.luxuryadmin.param.sys.ParamSysUserAdd;
import com.luxuryadmin.service.sys.SysRoleService;
import com.luxuryadmin.service.sys.SysUserService;
import com.luxuryadmin.vo.sys.VoSysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author monkey king
 * @date 2019-12-05 15:19:13
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserRoleRefMapper sysUserRoleRefMapper;

    @Autowired
    private SysRoleService sysRoleService;


    @Override
    public List<VoSysUser> querySysUserList(ParamSysUser paramSysUser) {
        paramSysUser.setUsername(DESEncrypt.encodeUsername(paramSysUser.getUsername()));
        List<VoSysUser> voSysRoles = sysUserMapper.listSysUser(paramSysUser);
        if (LocalUtils.isEmptyAndNull(voSysRoles)){
            return voSysRoles;
        }
//        List<VoSysUser> voSysUsers = new ArrayList<>();
        for (VoSysUser voSysUser : voSysRoles) {

            voSysUser.setUsername(DESEncrypt.decodeUsername(voSysUser.getUsername()));
            voSysUser.setPhone(DESEncrypt.decodeUsername(voSysUser.getPhone()));
            voSysUser.setPassword(null);
            SysUserRoleRef sysUserRoleRef = new SysUserRoleRef();
            sysUserRoleRef.setFkSysUserId(voSysUser.getId());
            List<SysUserRoleRef> list = sysRoleService.queryUserRole(sysUserRoleRef);
            if(!LocalUtils.isEmptyAndNull(list)){
                List<String> perms = list.stream().map(s -> s.getFkSysRoleId().toString()).collect(Collectors.toList());
                voSysUser.setPerms(perms);
            }

        }
        return voSysRoles;
    }

    public static void main(String[] args) {
        System.out.println(DESEncrypt.encodeUsername("15868133544"));
        String aaa = "B93771A61C408776328F992BB23BE19C";
        System.out.println(DESEncrypt.decodeUsername(aaa));
    }

    @Override
    public int addSysUser(SysUser sysUser) {
        String username =DESEncrypt.encodeUsername(sysUser.getUsername());
        sysUser.setPhone(username);
        sysUser.setUsername(username);
        SysUser oldUser= sysUserMapper.getByName(username);
        if (oldUser != null ){
            if ("1".equals(oldUser.getDel())){
                sysUser.setId(oldUser.getId());
                sysUser.setDel("0");

                int row=  sysUserMapper.updateObject(sysUser);
                return oldUser.getId();
            }else {
                throw new MyException("该账号已添加");
            }

        }
        int row = sysUserMapper.saveObject(sysUser);
        return sysUser.getId();
    }

    @Override
    public int updateSysUser(SysUser sysUser) {
        sysUser.setUpdateTime(new Date());
        int row = sysUserMapper.updateObject(sysUser);
        return row;
    }

    @Override
    public int deleteSysUser(ParamSysPermDelete sysPermDelete) {
        List<String> idList = Arrays.asList(sysPermDelete.getId().split(","));
        idList.forEach(s -> {
            SysUser sysUser =new SysUser();
            sysUser.setDel(ConstantCommon.DEL_ON);
            sysUser.setUpdateTime(new Date());
            sysUser.setId(Integer.parseInt(s));
            sysUser.setInsertAdmin(sysPermDelete.getUserId());
            int row = sysUserMapper.updateObject(sysUser);
            this.deleteSysUserPrem(sysUser);
        });

        return 0;
    }

    @Override
    public int addSysUserRole(ParamSysUserAdd paramSysUser) {
//        List<String> roles = paramSysUser.getRoles();
        int row = 0;
//        for (String role : roles) {
//            SysUserRoleRef sysUserRoleRef = new SysUserRoleRef();
//            sysUserRoleRef.setFkSysUserId(paramSysUser.getId());
//            sysUserRoleRef.setFkSysRoleId(LocalUtils.strParseInt(role));
//            sysUserRoleRef.setInsertAdmin(paramSysUser.getInsertAdmin());
//            sysUserRoleRef.setVersions(0);
//            sysUserRoleRefMapper.saveObject(sysUserRoleRef);
//            row++;
//        }

        SysUserRoleRef sysUserRoleRef = new SysUserRoleRef();
        sysUserRoleRef.setFkSysUserId(paramSysUser.getId());
        sysUserRoleRef.setFkSysRoleId(Integer.parseInt(paramSysUser.getRoleId()));
        sysUserRoleRef.setInsertAdmin(paramSysUser.getInsertAdmin());
        sysUserRoleRef.setVersions(0);
        sysUserRoleRefMapper.saveObject(sysUserRoleRef);
        return row;
    }

    @Override
    public int deleteSysUserPrem(SysUser sysUser) {
        return sysUserRoleRefMapper.deleteByUserId(sysUser.getId());
    }

    @Override
    public SysUser selectByName(String username) {
        return sysUserMapper.getByName(username);
    }

    @Override
    public SysUser getUserByName(String username) {
         username = DESEncrypt.encodeUsername(username);
        SysUser sysUser = this.selectByName( username);
        return sysUser;
    }

    @Override
    public void checkUserIsEffective(SysUser sysUser, ParamLoginPwd paramLoginPwd) {
        if (sysUser == null) {
            //用户不存在
            throw new MyException(EnumCode.ERROR_NOT_EXIST_USER);
        }
        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        boolean flag = encode.matches(paramLoginPwd.getPassword(),sysUser.getPassword());
        if (!flag) {
            //密码错误
            throw new MyException(EnumCode.ERROR_PASSWORD);
        }
        if ("0".equals(sysUser.getState())) {
            //账号过期
            throw new MyException(EnumCode.USER_ACCOUNT_EXPIRED);
        }

//        if (sysUser == null) {
//            //密码过期
//            throw new MyException(EnumCode.USER_CREDENTIALS_EXPIRED);
//        }
//        if (sysUser == null) {
//            //账号不可用
//            throw new MyException(EnumCode.USER_ACCOUNT_EXPIRED);
//        }
//        if (sysUser == null) {
//            //账号锁定
//            throw new MyException(EnumCode.USER_ACCOUNT_LOCKED);
//        }

    }

    @Override
    public SysUser selectById(Integer id) {
        return (SysUser) sysUserMapper.getObjectById(id);
    }

    @Override
    public List<VoSysUser> loadOpUser() {
        return sysUserMapper.loadOpUser();
    }
}
