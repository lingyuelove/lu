package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-08-03 17:14:15
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShopValidInfo {
    /**
     * 营业执照路径
     */
    private String licenseImgUrl;

    /**
     * 图片认证地址
     */
    private String validImgUrl;

    /**
     * 视频认证地址
     */
    private String validVideoUrl;


    /**
     * 图片集合
     */
    private List<String> validImgUrlList;

    /**
     * 视频集合
     */
    private List<String> validVideoUrlList;
}
