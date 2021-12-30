package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.op.OpProblem;
import com.luxuryadmin.param.op.ParamOpProblemQuery;
import com.luxuryadmin.vo.op.VoOpProblem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OpProblemMapper extends BaseMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OpProblem record);

    int insertSelective(OpProblem record);

    OpProblem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OpProblem record);

    int updateByPrimaryKey(OpProblem record);

    List<VoOpProblem> selectOpVoProblem(ParamOpProblemQuery paramOpProblemQuery);

    List<VoOpProblem> selectEnableOpProblem(ParamOpProblemQuery paramOpProblemQuery);
}