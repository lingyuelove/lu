package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.pro.VoProQuality;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品成色表 Mapper
 *
 * @author monkey king
 * @date 2020-05-27 15:26:29
 */
@Mapper
public interface ProQualityMapper extends BaseMapper {

    /**
     * 获取商品成色表
     *
     * @return
     */
    List<VoProQuality> listVoProQuality();
}
