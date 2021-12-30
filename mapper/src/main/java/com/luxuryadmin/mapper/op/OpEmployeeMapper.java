package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.op.VoOpEmployeeList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作人员列表Mapper
 *
 * @author Administrator
 */
@Mapper
public interface OpEmployeeMapper extends BaseMapper {

    /**
     * 根據工作人员id查询数量
     *
     * @param shpUserId
     * @return
     */
    int count(@Param("shpUserId") Integer shpUserId);

    /**
     * 刪除商家联盟分享开关
     *
     * @param id
     */
    void deleteUnionSwitch(@Param("id") Integer id);

    /**
     * 获取工作人员列表
     *
     * @return
     */
    List<VoOpEmployeeList> listEmployee();


    /**
     * 获取允许分享的推广人员的userId
     *
     * @return
     */
    String getAllowedEmployeeShareUserId();
}