package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProPublic;
import com.luxuryadmin.param.pro.ParamPublicAddForAdmin;
import com.luxuryadmin.param.pro.ParamPublicForAdmin;
import com.luxuryadmin.param.pro.ParamPublicUpdateForAdmin;
import com.luxuryadmin.param.pro.ParamProPublic;
import com.luxuryadmin.vo.pro.VoProPublic;
import com.luxuryadmin.vo.pro.VoProPublicByPageForAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公价商品表 业务逻辑层;
 *
 * @author monkey king
 * @date 2021-06-22 23:24:26
 */
public interface ProPublicService {


    /**
     * 批量插入公价商品
     *
     * @param list
     */
    void saveBatch(List<ProPublic> list);

    /**
     * 批量插入公价商品
     *
     * @param list
     */
    void updateBatchSmallImg(List<ProPublic> list);


    /**
     * 查询公价商品
     *
     * @return
     */
    List<VoProPublic> listProPublic(ParamProPublic queryParam);


    /**
     * 获取公价商品,外层需要分页获取
     *
     * @return
     */
    List<ProPublic> listAllProPublic();

    /**
     * 根据品牌查询系列
     *
     * @param name
     * @return
     */
    List<VoProPublic> querySerialNo(String name);


    /**
     * 根据品牌查询系列
     *
     * @param name
     * @param serialNo
     * @return
     */
    List<VoProPublic> queryTypeNo(String name, String serialNo);


    /**
     * 后台公价商品集合显示
     * @param publicForAdmin
     * @return
     */
    VoProPublicByPageForAdmin getPublicByPageForAdmin(ParamPublicForAdmin publicForAdmin);

    /**
     * 后台新增公价商品
     * @param publicAddForAdmin
     */
    void addPublicForAdmin(ParamPublicAddForAdmin publicAddForAdmin);

    /**
     * 后台编辑公价商品
     * @param publicUpdateForAdmin
     */
    void updatePublicForAdmin(ParamPublicUpdateForAdmin publicUpdateForAdmin);

    /**
     * 后台删除公价商品
     * @param id
     */
    void deletePublicForAdmin(Integer id);
}
