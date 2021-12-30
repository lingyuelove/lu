package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.sys.VoSysJobWx;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author monkey king
 * @date 2021-08-03 20:34:15
 */
@Mapper
public interface SysJobWxMapper extends BaseMapper {

    /**
     * 获取工作微信列表
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

}
