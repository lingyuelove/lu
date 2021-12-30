package com.luxuryadmin.mapper.sys;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.shp.ShpUserType;
import com.luxuryadmin.entity.sys.SysEnum;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.sys.VoSysEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-26 16:27:03
 */
@Mapper
public interface SysEnumMapper extends BaseMapper {

    /**
     * 获取该表记录数量
     *
     * @param type
     * @return
     */
    int getAllCountByType(String type);

    /**
     * 删除该表所有数据;(物理删除)
     *
     * @param type
     * @return
     */
    int deleteAllByType(String type);

    /**
     * 获取系统的模板的用户类型
     *
     * @param name
     * @param type
     * @return
     */
    List<VoSysEnum> listVoSysEnum(@Param("name") String name, @Param("type") String type);

    /**
     * 获取模板名称
     *
     * @return
     */
    List<VoSysEnum> listTplName();

    /**
     * 删除模板名称及权限
     *
     * @param name
     * @return
     */
    int deleteTplName(String name);

    /**
     * 获取系统生成分类;
     *
     * @param state
     * @return
     */
    List<VoProClassify> listSysProClassifyByState(String state);

    /**
     * 根据类型来获取分类
     * @param code
     * @return
     */
    VoProClassify getProClassifyByCode(String code);
}