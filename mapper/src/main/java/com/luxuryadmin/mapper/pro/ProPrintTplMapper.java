package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.entity.pro.ProPrintTpl;
import com.luxuryadmin.vo.pro.VoProPrintTpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProPrintTplMapper {
    int deleteObject(Integer id);

    int insert(ProPrintTpl record);

    int saveObject(ProPrintTpl record);

    ProPrintTpl getObjectById(Integer id);

    int updateObject(ProPrintTpl record);

    int updateByPrimaryKey(ProPrintTpl record);

    /**
     * 根据主键ID和店铺ID获取打印模板
     * @param shopId
     * @param proPrintTplId
     * @return
     */
    VoProPrintTpl selectProPrintTplById(@Param("shopId") Integer shopId, @Param("id") Integer proPrintTplId);

    /**
     * 根据主键ID和店铺ID删除打印模板
     * @param shopId
     * @param proPrintTplId
     * @return
     */
    Integer deleteProPrintTplById(@Param("shopId") Integer shopId, @Param("id") Integer proPrintTplId);

    /**
     * 根据店铺ID获取打印模板列表
     * @param shopId
     * @return
     */
    List<VoProPrintTpl> selectProPrintTplList(Integer shopId);
}