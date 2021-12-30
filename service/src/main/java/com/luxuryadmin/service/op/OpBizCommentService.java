package com.luxuryadmin.service.op;

import com.luxuryadmin.entity.op.OpBizComment;
import com.luxuryadmin.param.op.ParamBizCommentQuery;
import com.luxuryadmin.vo.op.VoOpBizComment;

import java.util.List;

/**
 * 意见反馈--业务逻辑层
 *
 * @author monkey king
 * @date 2020-01-14 15:53:21
 */
public interface OpBizCommentService {

    /**
     * 添加反馈记录
     *
     * @param opBizComment
     * @return
     */
    int saveOpBizComment(OpBizComment opBizComment);

    /**
     * 分页查询意见反馈
     * @param paramBizCommentQuery
     * @return
     */
    List<VoOpBizComment> queryOpBizCommentList(ParamBizCommentQuery paramBizCommentQuery);
}
