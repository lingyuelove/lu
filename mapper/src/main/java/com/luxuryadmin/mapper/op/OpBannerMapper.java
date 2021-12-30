package com.luxuryadmin.mapper.op;

import com.luxuryadmin.entity.op.OpBanner;
import com.luxuryadmin.param.op.ParamAppBannerQuery;
import com.luxuryadmin.param.op.ParamOpBannerQuery;
import com.luxuryadmin.vo.op.VoOpBanner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OpBannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OpBanner record);

    int insertSelective(OpBanner record);

    OpBanner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpBanner record);

    int updateByPrimaryKey(OpBanner record);

    List<VoOpBanner> selectOpBannerListByParam(ParamOpBannerQuery paramOpProblemQuery);

    /**
     * APP查询对应位置的Banner
     * @param paramAppBannerQuery
     * @return
     */
    List<VoOpBanner> selectOpBannerByPos(ParamAppBannerQuery paramAppBannerQuery);

    /**
     * 查询【首页-弹窗】位置Banner数量
     * @return
     */
    Integer selectIndexPopWindowCount();
}