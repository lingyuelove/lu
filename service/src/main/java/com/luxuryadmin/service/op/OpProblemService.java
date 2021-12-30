package com.luxuryadmin.service.op;

import com.luxuryadmin.entity.op.OpProblem;
import com.luxuryadmin.param.op.ParamOpProblemQuery;
import com.luxuryadmin.vo.op.OpProblemType;
import com.luxuryadmin.vo.op.VoOpProblem;
import com.luxuryadmin.vo.op.VoOpProblemCategory;

import java.util.List;

/**
 * 用户管理--帮助中心问题
 *
 * @author sanjin
 * @date 2020-07-09 11:48:06
 */
public interface OpProblemService {


    /**
     * 加载帮助中心问题
     *
     * @return
     */
    List<OpProblemType> listAllOpProblemCategoryName();

    /**
     * 根据条件查询所有帮助中心问题
     *
     * @param
     * @return
     */
    List<VoOpProblem> listOpProblem(ParamOpProblemQuery paramOpProblemQuery);

    /**
     * 根据ID查询对应的帮助中心问题
     * @param id
     * @return
     */
    OpProblem getProblemById(Integer id);

    /**
     * 新增帮助中心问题
     * @param opProblem
     * @return
     */
    int addOpProblem(OpProblem opProblem);

    /**
     * 修改帮助中心问题
     * @param opProblem
     * @return
     */
    int updateOpProblem(OpProblem opProblem);

    /**
     * 逻辑删除帮助中心问题
     * @param id
     * @return
     */
    int delOpProblem(Integer id,Integer uid);

    /**
     * 查询所有【帮助中心】启用的问题
     * @return
     */
    List<VoOpProblemCategory> listEnableOpProblem();

    /**
     * api端查询
     * @param paramOpProblemQuery
     * @return
     */
    List<VoOpProblem> selectEnableOpProblem(ParamOpProblemQuery paramOpProblemQuery);
}
