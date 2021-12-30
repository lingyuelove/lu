package com.luxuryadmin.service.op;


import com.luxuryadmin.param.op.ParamEmployeeAdd;
import com.luxuryadmin.param.op.ParamUnionAgentAdd;
import com.luxuryadmin.param.op.ParamUnionAgentQuery;
import com.luxuryadmin.param.sys.ParamJobWxCensuses;
import com.luxuryadmin.vo.op.VoOpEmployeeCensus;
import com.luxuryadmin.vo.op.VoOpEmployeeList;

import java.util.List;

/**
 * 工作人员表
 *
 * @author taoqimin
 * @date 2020-09-16
 */
public interface OpEmployeeService {


    /**
     * 添加员工帐号
     *
     * @param paramEmployeeAdd
     * @return
     */
    void addEmployeeAccount(ParamEmployeeAdd paramEmployeeAdd);

    /**
     * 修改商家联盟分享开关
     *
     * @param params
     */
    void opEmployeeService(ParamUnionAgentQuery params);

    /**
     * 刪除商家联盟分享开关
     * @param id
     */
    void deleteUnionSwitch(Integer id);

    /**
     * 获取工作人员列表
     * @return
     */
    List<VoOpEmployeeList> listEmployee();

    List<VoOpEmployeeCensus> listEmployeeCensus(ParamJobWxCensuses param);


}
