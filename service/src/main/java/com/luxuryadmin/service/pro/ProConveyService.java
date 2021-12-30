package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProConvey;
import com.luxuryadmin.param.pro.ParamConveyAdd;
import com.luxuryadmin.param.pro.ParamConveyDelete;
import com.luxuryadmin.param.pro.ParamConveyQuery;
import com.luxuryadmin.param.pro.ParamConveyUpdate;
import com.luxuryadmin.vo.pro.VoConvey;

import java.util.List;

/**
 *商品传送表 service
 *@author zhangsai
 *@Date 2021-11-22 15:12:44
 */
public interface ProConveyService {

    /**
     * 新增
     * @param conveyAdd
     */
    void addConvey(ParamConveyAdd conveyAdd);

    /**
     * 删除
     * @param conveyDelete
     */
    void deleteConvey(ParamConveyDelete conveyDelete);

    /**
     * 提取交接单
     * @param conveyUpdate
     */
    void receiveConvey(ParamConveyUpdate conveyUpdate);
    /**
     * 集合显示
     * @param paramConveyQuery
     * @return
     */
    List<VoConvey> listConvey(ParamConveyQuery paramConveyQuery);

    /**
     * 获取寄卖交接单详情
     * @param id
     * @return
     */
    ProConvey getConveyDetail(Integer id);
}
