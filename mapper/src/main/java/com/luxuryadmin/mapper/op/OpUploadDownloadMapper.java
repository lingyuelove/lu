package com.luxuryadmin.mapper.op;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.op.VoOpUploadDownload;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author monkey king
 * @date 2021-09-06 14:56:53
 */
@Mapper
public interface OpUploadDownloadMapper extends BaseMapper {

    /**
     * 获取上传下载任务列表
     *
     * @param shopId
     * @return
     */
    List<VoOpUploadDownload> listVoOpUploadDownload(Integer shopId);
}
