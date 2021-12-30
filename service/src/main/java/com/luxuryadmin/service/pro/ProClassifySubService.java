package com.luxuryadmin.service.pro;

import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.pro.VoProClassifySub;
import com.luxuryadmin.vo.pro.VoProClassifySubPageForAdmin;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mong
 * @Date: 2021/5/27 15:21
 * @Description: 商品二级分类逻辑层
 */
public interface ProClassifySubService {
    /**
     * 查询二级分类
     *
     * @param paramProClassifySubQuery
     */
    List<VoProClassifySub> listAllProClassifySub(ParamProClassifySubQuery paramProClassifySubQuery);

    /**
     * 根据一级分类id查询二级分类
     *
     * @param paramQuery
     * @return
     */
    List<VoProClassifySub> listProClassifySubPage(ParamProClassifySubQuery paramQuery);

    /**
     * 添加二级分类
     *
     * @param paramAdd
     */
    void addProClassifySub(ParamProClassifySubAdd paramAdd);


    /**
     * 添加二级分类api端
     *
     * @param classifySubAddForApi
     */
    void addProClassifySubForApi(ParamClassifySubAddForApi classifySubAddForApi);

    /**
     * 修改二级分类
     *
     * @param paramUpdate
     */
    void updateProClassifySub(ParamProClassifySubUpdate paramUpdate);


    /**
     * 修改二级分类
     *
     * @param paramUpdate
     */
    void updateProClassifySubForState(ParamProClassifySubUpdate paramUpdate);

    /**
     * 修改二级分类
     *
     * @param classifySubUpdateForApi
     */
    void updateProClassifySubForApi(ParamClassifySubUpdateForApi classifySubUpdateForApi);


    /**
     * 删除二级分类
     *
     * @param id
     */
    void deleteProClassifySub(String id);

    /**
     * 根据名称查询二级分类
     *
     * @param name
     * @param shopId
     * @return
     */
    VoProClassifySub getProClassifySubByName(String name, Integer shopId);

    /**
     * 根据名称查询二级分类 后端
     *
     * @param name
     * @return
     */
    VoProClassifySub getProClassifySubByNameForAdmin(String name);

    /**
     * 根据id查询商品二级分类
     *
     * @param id
     */
    VoProClassifySub getProClassifySubById(String id);


    /**
     * 查询二级分类
     *
     * @param classifyName    一级分类code
     * @return
     */
    List<VoProClassifySub> listAllProClassifySub(String classifyName);

    /**
     * 后台品牌集合显示
     * @param paramQuery
     * @return
     */
    VoProClassifySubPageForAdmin getClassifySubPageForAdmin(ParamProClassifySubQuery paramQuery);

    /**
     * 根据code获取名字
     * @param classifyCode
     * @return
     */
    String getClassifyCodeName(String classifyCode);
}
