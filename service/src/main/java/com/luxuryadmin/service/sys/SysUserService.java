package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.sys.SysUser;
import com.luxuryadmin.param.login.ParamLoginPwd;
import com.luxuryadmin.param.sys.ParamSysPermDelete;
import com.luxuryadmin.param.sys.ParamSysUser;
import com.luxuryadmin.param.sys.ParamSysUserAdd;
import com.luxuryadmin.vo.sys.VoSysUser;

import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-05 14:53:46
 */
public interface SysUserService {

    /**
     * 添加SysUser入库
     *
     * @param sysUser {@link SysUser}
     */
    //void saveSysUser(SysUser sysUser);

    /**
     * 更新SysUser表
     *
     * @param sysUser {@link SysUser}
     * @return
     */
    //int updateSysUser(SysUser sysUser);

    /**
     * 分页查询账号
     * @param paramSysUser
     * @return
     */
    List<VoSysUser> querySysUserList(ParamSysUser paramSysUser);

    /**
     * 新增账号
     * @param sysUser
     * @return
     */
    int addSysUser(SysUser sysUser);

    /**
     * 根据id修改账号
     * @param sysUser
     * @return
     */
    int updateSysUser(SysUser sysUser);

    /**
     * 根据id删除账号
     * @param sysPermDelete
     * @return
     */
    int deleteSysUser(ParamSysPermDelete sysPermDelete);

    /**
     * 添加账号角色
     * @param paramSysUser
     * @return
     */
    int addSysUserRole(ParamSysUserAdd paramSysUser);

    /**
     * 根据账号删除账号角色
     * @param sysUser
     * @return
     */
    int deleteSysUserPrem(SysUser sysUser);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    SysUser selectByName(String username);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    SysUser getUserByName(String username);

    void checkUserIsEffective(SysUser sysUser, ParamLoginPwd paramLoginPwd);

    /**
     * 根据用户内部id查询用户信息
     * @param id
     * @return
     */
    SysUser selectById(Integer id);

    /**
     * 获取运营帐号
     * @return
     */
    List<VoSysUser> loadOpUser();

}
