package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.entity.op.OpBizComment;
import com.luxuryadmin.mapper.op.OpBizCommentMapper;
import com.luxuryadmin.param.op.ParamBizCommentQuery;
import com.luxuryadmin.service.op.OpBizCommentService;
import com.luxuryadmin.vo.op.VoOpBizComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 意见反馈
 *
 * @author monkey king
 * @date 2020-01-14 15:53:46
 */
@Slf4j
@Service
public class OpBizCommentServiceImpl implements OpBizCommentService {

    @Resource
    private OpBizCommentMapper opBizCommentMapper;


    @Override
    public int saveOpBizComment(OpBizComment opBizComment) {
        return opBizCommentMapper.saveObject(opBizComment);
    }

    @Override
    public List<VoOpBizComment> queryOpBizCommentList(ParamBizCommentQuery paramBizCommentQuery) {
        if(!StringUtils.isEmpty(paramBizCommentQuery.getPhone())){
            paramBizCommentQuery.setPhone(DESEncrypt.encodeUsername(paramBizCommentQuery.getPhone()));
        }
        List<VoOpBizComment> voOpBizComments = opBizCommentMapper.listOpBizComment(paramBizCommentQuery);
        for (VoOpBizComment voOpBizComment : voOpBizComments) {
            voOpBizComment.setPhone(DESEncrypt.decodeUsername(voOpBizComment.getPhone()));
        }
        return voOpBizComments;
    }

}
