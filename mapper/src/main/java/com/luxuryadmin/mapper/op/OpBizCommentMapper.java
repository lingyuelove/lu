package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.param.op.ParamBizCommentQuery;
import com.luxuryadmin.vo.op.VoOpBizComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OpBizCommentMapper  extends BaseMapper {
    /**
     * 条件查询 意见反馈
     * @param paramBizCommentQuery
     * @return
     */
    List<VoOpBizComment> listOpBizComment(ParamBizCommentQuery paramBizCommentQuery);
}