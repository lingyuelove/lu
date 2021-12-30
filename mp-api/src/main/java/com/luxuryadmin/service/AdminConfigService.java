package com.luxuryadmin.service;


import com.luxuryadmin.param.adminconfig.ParamAddVipUser;
import com.luxuryadmin.param.adminconfig.ParamAdminConfig;
import com.luxuryadmin.vo.adminconfig.VOAdminConfig;

public interface AdminConfigService {


    /**
     * 修改配置信息
     *
     * @param param
     */
    void updateConfig(ParamAdminConfig param);

    /**
     * 获取配置时间
     */
    VOAdminConfig getConfig();

    /**
     * 添加会员用户
     *
     * @param param
     */
    void addVipUser(ParamAddVipUser param);
}
