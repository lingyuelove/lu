package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProClassifySub;
import com.luxuryadmin.param.pro.ParamProClassifySubAdd;
import com.luxuryadmin.param.pro.ParamProClassifySubQuery;
import com.luxuryadmin.vo.pro.VoProClassifySub;
import com.luxuryadmin.vo.pro.VoProClassifySubForAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 商品二级分类
 *
 * @author Mong
 * @Date 2021-05-27 11:37:31
 */
@Mapper
public interface ProClassifySubMapper extends BaseMapper {


    /**
     * 根据一级分类id和商铺id查询二级分类
     *
     * @param classifyCode
     * @param shopId
     * @return
     */
    List<VoProClassifySub> listProClassifySub(@Param("classifyCode") String classifyCode, @Param("shopId") Integer shopId);

    /**
     * 分页查询二级分类
     *
     * @param paramQuery
     * @return
     */
    List<VoProClassifySub> listProClassifySubPage(ParamProClassifySubQuery paramQuery);


    /**
     * 根据名称查询二级分类
     *
     * @param name
     * @param shopId
     * @return
     */
    VoProClassifySub getProClassifySubByName(@Param("name") String name, @Param("shopId") Integer shopId);

    /**
     * 查询用户自增的所有品牌
     * @param name
     * @return
     */
    List<ProClassifySub> getClassifySubListByName(@Param("name") String name);
    /**
     * 根据id查询二级分类
     *
     * @param id
     * @return
     */
    VoProClassifySub getProClassifySubById(String id);


    /**
     * 查询品牌分类<br/>用于公价查询系统
     *
     * @param classifyCode 一级分类code
     * @return
     */
    List<VoProClassifySub> listAllProClassifySub(@Param("classifyCode") String classifyCode);

    /**
     * 后台品牌集合显示
     * @param paramQuery
     * @return
     */
    List<VoProClassifySubForAdmin> getClassifySubPageForAdmin(ParamProClassifySubQuery paramQuery);
}
