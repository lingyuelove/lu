package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProPrintTpl;
import com.luxuryadmin.param.pro.ParamProPrintTpl;
import com.luxuryadmin.vo.pro.ParamProPrintTplAdd;
import com.luxuryadmin.vo.pro.VoProPrintTpl;

import java.util.List;

/**
 * 商品打印模板
 */
public interface ProPrintTplService {

    /**
     * 新增【打印模板】
     * @param shopId
     * @param userId
     * @param paramProPrintTplAdd
     * @return
     */
    ProPrintTpl addProPrintTpl(Integer shopId, Integer userId, ParamProPrintTplAdd paramProPrintTplAdd);

    /**
     * 修改【打印模板】
     * @param shopId
     * @param userId
     * @param paramProPrintTplUpdate
     * @return
     */
    Integer updateProPrintTpl(Integer shopId, Integer userId, ParamProPrintTplAdd paramProPrintTplUpdate);

    /**
     * 根据主键ID获取【打印模板】
     * @param shopId
     * @param proPrintTplId
     * @return
     */
    VoProPrintTpl loadProPrintTplById(Integer shopId, Integer proPrintTplId);

    /**
     * 根据ID删除打印模板
     * @param shopId
     * @param proPrintTplId
     * @return
     */
    Integer deleteProPrintTplById(Integer shopId, Integer proPrintTplId);

    /**
     * 根据店铺ID获取【打印模板】列表
     * @param shopId
     * @return
     */
    List<VoProPrintTpl> loadProPrintTplList(ParamProPrintTpl proPrintTpl);
}
