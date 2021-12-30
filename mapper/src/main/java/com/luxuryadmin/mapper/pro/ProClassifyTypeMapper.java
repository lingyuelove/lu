package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProClassifyType;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.vo.pro.VoClassify;
import com.luxuryadmin.vo.pro.VoClassifyTypeGrandSonList;
import com.luxuryadmin.vo.pro.VoClassifyTypeList;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *商品补充信息分类表 dao
 *@author zhangsai
 *@Date 2021-08-03 10:45:15
 */
@Mapper
public interface ProClassifyTypeMapper  extends BaseMapper<ProClassifyType> {

    /**
     * 获取商品补充信息列表
     * @param classifyTypeSearch
     * @return
     */
    List<VoClassifyTypeList> getClassifyTypeList(ParamClassifyTypeSearch classifyTypeSearch);

    /**
     * 获取商品二级分类
     * @param classifyTypeId
     * @param shopId
     * @return
     */
    List<VoClassifyTypeSonList> getClassifyTypeSonLists(ParamClassifyTypeSearch classifyTypeSearch);

    /**
     * 获取三级分类
     * @param classifyTypeId
     * @param shopId
     * @return
     */
    List<VoClassifyTypeGrandSonList> getClassifyTypeGrandSonList( @Param("classifyTypeId")Integer classifyTypeId, @Param("shopId")Integer shopId);


    /**
     * 根据类型和名称判断是否有相同的分类
     * @param type
     * @param name
     * @param classifyCode
     * @return
     */
    ProClassifyType getClassifyTypeByNameAndType(@Param("type") String type, @Param("name")String name,@Param("classifyCode")String classifyCode);

    /**
     * 把第三级分类设置为del=1的状态
     * @param classifyTypeId
     */
    void updateClassifyTypeForDel(Integer classifyTypeId);
}
