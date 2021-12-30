package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.vo.shp.VoEmployee;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author monkey king
 * @date 2020-09-28 15:45:00
 */

@Slf4j
public class ShpUserBaseController extends BaseController {

    /**
     * 格式化员工的头像地址和解密用户名
     *
     * @param voEmployee 员工列表VO层
     */
    protected void formatVoEmployee(VoEmployee voEmployee) {
        if (!LocalUtils.isEmptyAndNull(voEmployee)) {
            String headImageUrl = voEmployee.getHeadImageUrl();
            if (!LocalUtils.isEmptyAndNull(headImageUrl)) {
                voEmployee.setHeadImageUrl(servicesUtil.formatImgUrl(headImageUrl));
            }
            String phone = DESEncrypt.decodeUsername(voEmployee.getUsername());
            voEmployee.setUsername(phone);
        }
    }

    protected void formatVoEmployee(List<VoEmployee> employeeList) {
        if (!LocalUtils.isEmptyAndNull(employeeList)) {
            for (VoEmployee voEmployee : employeeList) {
                formatVoEmployee(voEmployee);
            }
        }
    }
}
