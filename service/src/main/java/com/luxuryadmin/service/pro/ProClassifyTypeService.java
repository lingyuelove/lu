package com.luxuryadmin.service.pro;


import com.luxuryadmin.param.pro.ParamClassifyTypeAdd;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamClassifyTypeSunAdd;
import com.luxuryadmin.param.pro.ParamClassifyTypeUpdate;
import com.luxuryadmin.vo.pro.VoClassify;
import com.luxuryadmin.vo.pro.VoClassifyTypeList;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonList;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonPage;

import java.util.List;
import java.util.Map;

/**
 *商品补充信息分类表 service
 *@author zhangsai
 *@Date 2021-08-03 10:45:15
 */
public interface ProClassifyTypeService{

    /**
     * 新增补充信息 方法废弃
     * @param classifyTypeAdd
     */
//    void addClassifyType(ParamClassifyTypeAdd classifyTypeAdd);

    /**
     * 新增子模块补充信息
     * @param classifyTypeAdd
     */
    void addClassifyTypeSun(ParamClassifyTypeAdd classifyTypeAdd);

    /**
     * 编辑扩展信息分类
     * @param classifyTypeUpdate
     */
    void updateClassifyType(ParamClassifyTypeUpdate classifyTypeUpdate);

    /**
     * 删除补充信息
     * @param id
     */
    void deleteClassifyType(Integer id);

    /**
     * 补充信息集合显示
     * @param classifyTypeSearch
     * @return
     */
    List<VoClassifyTypeSonList> getClassifyTypeList(ParamClassifyTypeSearch classifyTypeSearch);


    /**
     * 获取分类
     * @param classifyTypeSearch
     * @return
     */
    List<VoClassify> getClassifyTypeForClassify(ParamClassifyTypeSearch classifyTypeSearch);

    /**
     * 补充信息集合显示
     * @param classifyTypeSearch
     * @return
     */
    List<VoClassify> getClassifyListForApp(ParamClassifyTypeSearch classifyTypeSearch);

    /**
     * 补充信息后台集合显示
     * @param classifyTypeSearch
     * @return
     */
    VoClassifyTypeSonPage getClassifyListForAdmin(ParamClassifyTypeSearch classifyTypeSearch);
}
