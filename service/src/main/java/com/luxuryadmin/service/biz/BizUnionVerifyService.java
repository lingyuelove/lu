package com.luxuryadmin.service.biz;


import com.luxuryadmin.param.biz.ParamAudit;
import com.luxuryadmin.param.biz.ParamBizUnionVerifyList;
import com.luxuryadmin.param.biz.ParamBizUnionVerifyUpdate;
import com.luxuryadmin.param.biz.ParamUnionVerifyAdd;
import com.luxuryadmin.vo.biz.VoBizUnionVerifyList;
import com.luxuryadmin.vo.biz.VoUnionVerify;

import java.util.List;

/**
 * 商家联盟审核表 service
 *
 * @author zhangsai
 * @Date 2021-11-03 17:44:45
 */
public interface BizUnionVerifyService {

    /**
     * 前端新增商家联盟审核
     *
     * @param unionVerifyAdd
     */
    void addUnionVerify(ParamUnionVerifyAdd unionVerifyAdd);

    /**
     * 根据店铺id获取商家联盟审核
     *
     * @param shopId
     * @return
     */
    VoUnionVerify getUnionVerifyByShopId(Integer shopId);

    /**
     * 获取商家联盟卖家审核列表
     *
     * @param param
     * @return
     */
    List<VoBizUnionVerifyList> getBizUnionVerify(ParamBizUnionVerifyList param);

    /**
     * 审核（通过/未通过）
     *
     * @param param
     */
    void audit(ParamAudit param, int userId);

    /**
     * 编辑
     *
     * @param param
     * @param userId
     */
    void update(ParamBizUnionVerifyUpdate param, int userId);
}
