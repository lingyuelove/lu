package com.luxuryadmin.service.biz;

import com.luxuryadmin.entity.biz.BizBlacklist;

/**
 * @author monkey king
 * @date 2020-01-12 17:39:39
 */
public interface BizBlacklistService {

    /**
     * 添加黑名单记录
     *
     * @param bizBlacklist
     * @return 受影响行数
     */
    int saveBizBlacklist(BizBlacklist bizBlacklist);
}
