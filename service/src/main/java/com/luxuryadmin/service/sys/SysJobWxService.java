package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.sys.SysJobWx;
import com.luxuryadmin.param.sys.ParamJobWxCensuses;
import com.luxuryadmin.vo.sys.VoSysJobWx;
import com.luxuryadmin.vo.sys.VoSysJobWxCensus;

import java.util.List;

/**
 * @author monkey king
 * @date 2021-08-03 20:44:19
 */
public interface SysJobWxService {

    /**
     * 更新或保存实体
     *
     * @param sysJobWx
     */
    void saveOrUpdateSysJobWx(SysJobWx sysJobWx);

    /**
     * 根据绑定的手机号获取信息
     *
     * @param id
     * @return
     */
    SysJobWx getSysJobWxById(String id);

    /**
     * 获取微信客服号列表
     *
     * @return
     */
    List<VoSysJobWx> listVoSysJobWx();

    /**
     * 获取绑定的客服
     *
     * @return
     */
    List<VoSysJobWx> listBindJobWx();

    /**
     * 获取工作微信统计列表
     * @param param
     * @return
     */
    List<VoSysJobWxCensus> listJobWxCensuses(ParamJobWxCensuses param);

    /**
     * 删除信息
     * @param userId
     */
    void deleteJobWxCensuses( Integer userId);

}
