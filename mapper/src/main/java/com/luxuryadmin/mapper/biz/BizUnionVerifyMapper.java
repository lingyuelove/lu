package com.luxuryadmin.mapper.biz;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.biz.BizUnionVerify;
import com.luxuryadmin.param.biz.ParamBizUnionVerifyList;
import com.luxuryadmin.vo.biz.VoBizUnionVerifyList;
import com.luxuryadmin.vo.biz.VoUnionVerify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 *商家联盟审核表 dao
 *@author zhangsai
 *@Date 2021-11-03 17:44:45
 */
@Mapper
public interface BizUnionVerifyMapper extends BaseMapper<BizUnionVerify> {

    /**
     * 获取商家联盟卖家审核列表
     *
     * @param param
     * @return
     */
    List<VoBizUnionVerifyList> getBizUnionVerify(ParamBizUnionVerifyList param);

    /**
     * 获取详情
     * @param shopId
     * @return
     */
    VoUnionVerify getUnionVerifyByShopId(Integer shopId);
}
