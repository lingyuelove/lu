package com.luxuryadmin.service.op;

import com.luxuryadmin.entity.op.OpBanner;
import com.luxuryadmin.param.op.ParamAppBannerQuery;
import com.luxuryadmin.param.op.ParamOpBannerQuery;
import com.luxuryadmin.vo.op.VoOpBanner;
import com.luxuryadmin.vo.op.VoOpBannerNativePage;
import com.luxuryadmin.vo.op.VoOpBannerPos;

import java.util.List;

/**
 * Banner Service
 */
public interface OpBannerService {

    /**
     * 根据枚举类获取所有的Banner位置
     * @return
     */
    List<VoOpBannerPos> listAllOpBannerPos();

    /**
     * 添加OpBanner对象
     * @param opBanner
     * @return
     */
    int addOpBanner(OpBanner opBanner)  throws Exception;

    /**
     * 根据条件查询banner列表
     * @param paramOpProblemQuery
     * @return
     */
    List<VoOpBanner> listOpBanner(ParamOpBannerQuery paramOpProblemQuery);

    /**
     * 根据ID获取Banner
     * @param bannerId
     * @return
     */
    OpBanner getBannerById(Integer bannerId);

    /**
     * 根据ID更新Banner
     * @param opBanner
     * @return
     */
    int updateOpBanner(OpBanner opBanner) throws Exception;

    /**
     * 根据ID和用户ID,逻辑删除Banner
     * @param valueOf
     * @param uid
     * @return
     */
    int delOpBanner(Integer valueOf, Integer uid);

    /**
     * 根据条件查询对应位置的Banner
     * @param paramAppBannerQuery
     * @return
     */
    List<VoOpBanner> listOpBannerByPath(ParamAppBannerQuery paramAppBannerQuery);

    /**
     * 根据枚举类获取所有的Banner位置
     * @return
     */
    List<VoOpBannerNativePage> listAllOpBannerNativePage();
}
