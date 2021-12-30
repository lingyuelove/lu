package com.luxuryadmin.service.op;


import com.luxuryadmin.param.op.ParamUnionAgentAdd;
import com.luxuryadmin.param.op.ParamUnionAgentUpdate;
import com.luxuryadmin.param.op.ParamUsernameAndUserIdQuery;
import com.luxuryadmin.vo.biz.VoMpUnionAgencyUser;
import com.luxuryadmin.vo.op.VoOpUnionAgentList;

import java.util.List;

/**
 * 工作人员表
 *
 * @author taoqimin
 * @date 2020-09-16
 */
public interface OpUnionAgentService {
    /**
     * 添加代理帐号
     *
     * @param paramUnionAgentAdd
     */
    void addUnionAgent(ParamUnionAgentAdd paramUnionAgentAdd);

    /**
     * 修改代理帐号
     *
     * @param paramUnionAgentUpdate
     */
    void updateUnionAgent(ParamUnionAgentUpdate paramUnionAgentUpdate);

    /**
     * 删除代理帐号
     *
     * @param id
     */
    void deleteUnionAgent(Integer id);

    /**
     * 获取代理人员列表
     *
     * @param params
     * @return
     */
    List<VoOpUnionAgentList> listUnionAgent(ParamUsernameAndUserIdQuery params);

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
     * 刷新商家联盟小程序的分享权限<br/>
     * 只要推广人员列表和代理列表其中一个允许分享,则代表有权限
     */
    void refreshUnionMpSharePerm();

}
