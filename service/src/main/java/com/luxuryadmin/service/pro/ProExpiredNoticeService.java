package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.entity.pro.ProExpiredNotice;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.pro.VoCheckListForApi;
import com.luxuryadmin.vo.pro.VoExpiredNoticeByPage;
import com.luxuryadmin.vo.pro.VoExpiredProductByPage;
import com.luxuryadmin.vo.pro.VoProClassify;

import java.util.List;
import java.util.Map;

/**
 * 商品过期提醒表
 *
 * @author ZhangSai
 * @date   2021/05/11 10:45:48
 */
public interface ProExpiredNoticeService {

    /**
     * 新增
     * @param expiredNoticeForAdd
     * @return
     */
    Integer addExpiredNotice(ParamExpiredNoticeForAdd expiredNoticeForAdd);

    /**
     * 删除
     * @param expiredNoticeId
     * @return
     */
    Integer deleteExpiredNotice( Integer expiredNoticeId);

    /**
     * 获取详情
     * @param expiredNoticeId
     * @return
     */
    ProExpiredNotice getExpiredNoticeById(Integer expiredNoticeId);

    /**
     * 获取店铺商品到期提醒列表
     * @param shopId
     * @return
     */
    VoExpiredNoticeByPage getExpiredNoticeByPage(Integer shopId);

    /**
     * 商品列表分类集合显示
     * @param expiredNoticeId
     * @param shopId
     * @return
     */
    List<VoProClassify> getProClassifyList(Integer expiredNoticeId,Integer shopId);

    /**
     * 获取店铺商品到期提醒列表
     * @param expiredProductSearch
     * @return
     */
    VoExpiredProductByPage getExpiredProductByPage(ParamExpiredProductSearch expiredProductSearch);

    /**
     *定时发送商品到期提醒
     */
    void sendMessageForExpiredProduct();

    /**
     * 新增定时所需参数
     * @param shopId
     * @return
     */
    Map<String,Object> getExpiredAddNeed(Integer shopId);
}
