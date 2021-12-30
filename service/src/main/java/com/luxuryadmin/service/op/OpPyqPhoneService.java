package com.luxuryadmin.service.op;

import com.luxuryadmin.entity.op.OpPyqPhone;

import java.util.List;

/**
 * @author monkey king
 * @date 2021-07-21 2:31:22
 */
public interface OpPyqPhoneService {

    /**
     * 批量保存
     *
     * @param list
     */
    void saveBatch(List<OpPyqPhone> list);

    /**
     * 解析excel表格
     *
     * @param excelUrl
     * @param userId
     * @param remark
     * @param batch
     * @return
     */
    List<OpPyqPhone> readExcel(String excelUrl, int userId, String remark, String batch);
}
