package com.luxuryadmin.service.shp;

import com.luxuryadmin.entity.shp.ShpUserInvite;
import com.luxuryadmin.vo.usr.VoInviteDetail;

import java.util.List;

/**
 * 店铺注册用户邀请记录表
 *
 * @author monkey king
 * @date 2019-12-09 15:31:19
 */
public interface ShpUserInviteService {


    /**
     * 添加ShpUserInvite入库
     *
     * @param shpUserInvite {@link ShpUserInvite}
     */
    void saveShpUserInvite(ShpUserInvite shpUserInvite);

    /**
     * new一个实体
     *
     * @param inviteUserId   邀请者userId
     * @param beInviteUserId 被邀请者userId
     * @return {@link ShpUserInvite}
     */
    ShpUserInvite createShpUserInvite(int inviteUserId, int beInviteUserId);

    /**
     * 根据用户ID查询邀请人数量
     *
     * @param userId
     * @return
     */
    Integer getInvitePersonCountByUserId(int userId);


    /**
     * 根据用户ID分页查询邀请人详情
     *
     * @param userId
     * @return
     */
    List<VoInviteDetail> getInviteDetailListByUserId(int userId);

    /**
     * 获取邀请该用户的userId(即TA的邀请人的邀请码)<br/>
     *
     * @param userId
     * @return
     */
    Integer getParentInviteCode(int userId);

    /**
     * 邀请好友(后台线程)
     *
     * @param inviteUserId   邀请人
     * @param beInviteUserId 被邀请人
     */
    void inviteUser(int inviteUserId, int beInviteUserId);

    /**
     * 获取当前用户id的邀请用户
     *
     * @param userId
     * @return
     */
    List<Integer> listBeInviteUserIdByUserId(Integer userId);

    /**
     * 更新定时任务方法
     */
    void addOrUpdateCensus();

}
