package com.luxuryadmin.service.sys;

import com.luxuryadmin.entity.sys.SysSalt;
import com.luxuryadmin.enums.sys.EnumSaltType;

/**
 * @author monkey king
 * @date 2019-12-05 14:53:46
 */
public interface SysSaltService {


    /**
     * 根据userId和type查找实体;
     *
     * @param userId   用户id
     * @param saltType 模块类型;枚举类; {@link EnumSaltType}
     * @return
     */
    SysSalt getSysSaltByUserIdAndType(int userId, EnumSaltType saltType);

    /**
     * new一个实体
     *
     * @param userId   用户id
     * @param saltType 模块类型;枚举类; {@link EnumSaltType}
     * @param salt     盐值
     * @return
     */
    SysSalt createSysSalt(int userId, EnumSaltType saltType, String salt);

    /**
     * 添加SysSalt入库
     *
     * @param sysSalt {@link SysSalt}
     */
    void saveSysSalt(SysSalt sysSalt);

    /**
     * 更新SysSalt表
     *
     * @param sysSalt {@link SysSalt}
     * @return
     */
    int updateSysSalt(SysSalt sysSalt);

}
