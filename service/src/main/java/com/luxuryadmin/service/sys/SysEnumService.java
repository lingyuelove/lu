package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.sys.SysEnum;
import com.luxuryadmin.enums.sys.EnumTableName;
import com.luxuryadmin.vo.sys.VoSysEnum;

import java.util.List;

/**
 * 各种枚举模板;例如: 商品属性;商品状态;商品分类;订单类型;用户类型
 *
 * @author monkey king
 * @date 2019-12-26 16:30:49
 */
public interface SysEnumService {
    /**
     * 检查该表是否有数据;<br/>
     * 检查更新;
     *
     * @param tableName
     * @return true:需要更新 | 不需要更新
     */
    boolean checkUpdateSysEnumByType(EnumTableName tableName);

    /**
     * 根据类型初始化数据;自动生成系统默认的数据;
     *
     * @param tableName
     */
    void initSysEnumByType(EnumTableName tableName);


    /**
     * 恢复系统默认的值;
     *
     * @param tableName
     */
    void resetSysEnumByType(EnumTableName tableName);


    /**
     * 遍历商品分类枚举
     *
     * @return
     */
    List<SysEnum> getEnumProClassify();


    /**
     * 遍历商品属性枚举
     *
     * @return
     */
    List<SysEnum> getEnumProAttribute();

    /**
     * 遍历商品状态枚举
     *
     * @return
     */
    List<SysEnum> getEnumProState();

    /**
     * 遍历订单类型枚举
     *
     * @return
     */
    List<SysEnum> getEnumOrdType();

    /**
     * 遍历用户类型枚举
     *
     * @return
     */
    List<SysEnum> getEnumShpUserType();

    /**
     * 获取系统的模板的用户类型
     *
     * @param name
     * @param type
     * @return
     */
    List<VoSysEnum> listVoSysEnum(String name, String type);

    /**
     * 获取系统的模板的用户类型
     *
     * @param type
     * @return
     */
    List<VoSysEnum> listVoSysEnum(String type);

    /**
     * 获取系统的模板的用户类型
     *
     * @return
     */
    List<VoSysEnum> listTplName();

    /**
     * 删除模板名称及权限
     *
     * @param name
     */
    int deleteTplName(String name);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    int saveBatch(List<SysEnum> list);
}
