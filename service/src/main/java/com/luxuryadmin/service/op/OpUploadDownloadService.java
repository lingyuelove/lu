package com.luxuryadmin.service.op;

import com.luxuryadmin.entity.op.OpUploadDownload;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.vo.op.VoOpUploadDownload;

import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-09-06 14:59:37
 */
public interface OpUploadDownloadService {

    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    OpUploadDownload getObjectById(Integer id);

    /**
     * 获取上传下载任务列表
     *
     * @param shopId
     * @return
     */
    List<VoOpUploadDownload> listVoOpUploadDownload(Integer shopId);

    /**
     * 保存实体且返回id
     *
     * @param opUploadDownload
     * @return
     */
    int saveOrUpdateObjectAndReturnId(OpUploadDownload opUploadDownload);


    /**
     * 封装即将要保存的实体
     *
     * @param shopId
     * @param userId
     * @param exportType 任务类型：in：导入，out：导出
     * @param module     模块；订单模块；商品模块；账单模块，其它模块
     * @param taskName
     * @param st
     * @param et
     * @return
     */
    OpUploadDownload packOpUploadDownload(int shopId, int userId, String exportType,
                                          String module, String taskName, Date st, Date et);

    /**
     * 把导出的文件上传至oss
     *
     * @param opUploadDownload 从数据库查询出来的实体;
     * @param list             要导出的excel格式的集合数据(参考ExVo开头的实体类)
     */
    void uploadFileToOss(OpUploadDownload opUploadDownload, List list);
}
