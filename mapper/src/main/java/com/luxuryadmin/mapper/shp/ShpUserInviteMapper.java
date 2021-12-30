package com.luxuryadmin.mapper.shp;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.usr.VoInviteDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author monkey king
 */
@Mapper
public interface ShpUserInviteMapper extends BaseMapper {

    /**
     * 根据用户ID查询邀请人数量
     * @param userId
     * @return
     */
    Integer selectInvitePersonCountByUserId(int userId);

    /**
     * 根据用户ID分页查询邀请人详情
     * @param userId
     * @return
     */
    List<VoInviteDetail> selectInviteDetailListByUserId(int userId);

    /**
     * 获取邀请该用户的userId(即TA的邀请人的邀请码)<br/>
     *
     * @param userId
     * @return
     */
    Integer getParentInviteCode(int userId);

    /**
     * 获取当前用户id的邀请用户
     *
     * @param userId
     * @return
     */
    List<Integer> listBeInviteUserIdByUserId(Integer userId);

    /**
     * 根据用户ID查询邀请用戶注冊數量
     * @param userId
     * @return
     */
    Integer selectRegisterUserCountByUserId(int userId);

    /**
     * 当日新开店数
     * @param userId
     * @return
     */
    Integer selectNewShopCountByUserId(int userId);
}