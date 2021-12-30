package com.luxuryadmin.api.util;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

/**
 * @PackgeName: com.luxuryadmin.api.util
 * @ClassName: PublicProductUtil
 * @Author: ZhangSai
 * Date: 2021/9/10 11:28
 */
@Contended
@Component
@Data
@ApiModel(value="公价查询连接", description="公价查询连接")
public class PublicProductUtil {
    /**
     * 公价查询连接 选取公价图的时候添加参数
     */
    @Value("${public.url}")
    private String publicUrl;
    @Value("${union.url}")
    private String unionUrl;
}
