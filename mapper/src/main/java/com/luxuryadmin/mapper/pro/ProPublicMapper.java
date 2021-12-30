package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProPublic;
import com.luxuryadmin.param.pro.ParamPublicForAdmin;
import com.luxuryadmin.param.pro.ParamProPublic;
import com.luxuryadmin.vo.pro.VoProPublic;
import com.luxuryadmin.vo.pro.VoProPublicForAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公价商品表 Mapper
 *
 * @author Administrator
 */
@Mapper
public interface ProPublicMapper extends BaseMapper {

    /**
     * 查询公价商品
     *
     * @param queryParam        查询参数
     * @return
     */
    List<VoProPublic> listProPublic(ParamProPublic queryParam);


    /**
     * 获取公价商品,外层需要分页获取
     *
     * @return
     */
    List<ProPublic> listAllProPublic();

    /**
     * 批量更新公价图图片
     *
     * @param list
     */
    void updateBatchSmallImg(List<ProPublic> list);

    /**
     * 根据品牌查询系列
     *
     * @param name
     * @return
     */
    List<VoProPublic> querySerialNo(String name);


    /**
     * 根据品牌查询系列
     *
     * @param name
     * @param serialNo
     * @return
     */
    List<VoProPublic> queryTypeNo(@Param("name") String name, @Param("serialNo") String serialNo);


    /**
     * 后台集合显示
     * @param publicForAdmin
     * @return
     */
    List<VoProPublicForAdmin> getPublicByListForAdmin(ParamPublicForAdmin publicForAdmin);

    /**
     * 公价商品搜索
     * @param proPublic
     * @return
     */
    ProPublic getByPublic(ProPublic proPublic);
}