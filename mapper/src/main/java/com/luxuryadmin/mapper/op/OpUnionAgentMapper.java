package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.biz.VoMpUnionAgencyUser;
import com.luxuryadmin.vo.op.VoOpUnionAgentList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作人员列表Mapper
 *
 * @author Administrator
 */
@Mapper
public interface OpUnionAgentMapper extends BaseMapper {

    /**
     * 查询代理是否被添加过;
     *
     * @param shpUserId
     * @param id
     * @return
     */
    int existsObject(@Param("shpUserId") Integer shpUserId, @Param("id") Integer id);

    /**
     * 删除代理
     *
     * @param id
     */
    void deleteUnionAgent(@Param("id") Integer id);

    /**
     * 获取代理人员列表
     *
     * @param username
     * @param employeeId
     * @return
     */
    List<VoOpUnionAgentList> listUnionAgent(@Param("username") String username, @Param("employeeId") Integer employeeId);

    /**
     * 查询某个帐号下关联的代理人员(包含自己)
     *
     * @param userId
     * @return
     */
    List<VoMpUnionAgencyUser> listVoMpUnionAgencyUser(int userId);

    /**
     * 获取代理分享小程序的最长天数
     *
     * @param userId
     * @return
     */
    Integer getUnionAgencyValidDay(int userId);

    /**
     * 获取允许分享的代理的userId
     *
     * @return
     */
    String getAllowedAgencyShareUserId();
}