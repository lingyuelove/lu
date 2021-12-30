package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.op.OpBanner;
import com.luxuryadmin.enums.op.EnumOpBannerNativePage;
import com.luxuryadmin.enums.op.EnumOpBannerPosType;
import com.luxuryadmin.mapper.op.OpBannerMapper;
import com.luxuryadmin.param.op.ParamAppBannerQuery;
import com.luxuryadmin.param.op.ParamOpBannerQuery;
import com.luxuryadmin.service.op.OpBannerService;
import com.luxuryadmin.vo.op.VoOpBanner;
import com.luxuryadmin.vo.op.VoOpBannerNativePage;
import com.luxuryadmin.vo.op.VoOpBannerPos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
/**
 * @author sanjin145
 */
public class OpBannerServiceImpl implements OpBannerService {

    @Resource
    private OpBannerMapper opBannerMapper;

    @Autowired
    protected RedisUtil redisUtil;

    @Override
    public List<VoOpBannerPos> listAllOpBannerPos() {
        List<VoOpBannerPos> list = new ArrayList();
        for (EnumOpBannerPosType enumPosType : EnumOpBannerPosType.values()) {
            VoOpBannerPos voOpBannerPos = new VoOpBannerPos();
            voOpBannerPos.setPosCode(enumPosType.getCode());
            voOpBannerPos.setPosShowName(enumPosType.getName());
            list.add(voOpBannerPos);
        }
        return list;
    }

    /**
     * 添加OpBanner对象
     *
     * @param opBanner
     * @return
     */
    @Override
    public int addOpBanner(OpBanner opBanner) throws Exception {
        //如果位置是【首页-弹窗】，查询是否已存在记录
        //存在的话，提示已存在【首页-弹窗】Banner，不允许添加。
        String pos = opBanner.getPos();
        if (EnumOpBannerPosType.INDEX_POPUP_WINDOW.getCode().equals(pos)) {
            checkUniqueIndexPopWindow();
        }

        opBanner.setInsertTime(new Date());
        opBanner.setUpdateTime(new Date());
        return opBannerMapper.insertSelective(opBanner);
    }

    private void checkUniqueIndexPopWindow() throws Exception {
        Integer indexPopWindowCount = opBannerMapper.selectIndexPopWindowCount();
        indexPopWindowCount = null == indexPopWindowCount ? 0 : indexPopWindowCount;
        Boolean isExistIndexPopWindow = indexPopWindowCount > 0;
        if (isExistIndexPopWindow) {
            throw new Exception("已存在【首页-弹窗】Banner，不允许添加多条");
        }
    }

    @Override
    public List<VoOpBanner> listOpBanner(ParamOpBannerQuery paramOpProblemQuery) {
        return opBannerMapper.selectOpBannerListByParam(paramOpProblemQuery);
    }

    @Override
    public OpBanner getBannerById(Integer bannerId) {
        return opBannerMapper.selectByPrimaryKey(bannerId);
    }

    @Override
    public int updateOpBanner(OpBanner opBanner) throws Exception {
        /**
         * 如果更新的是【首页-弹窗】,更新timestamp值
         */
        String pos = opBanner.getPos();

        if (EnumOpBannerPosType.INDEX_POPUP_WINDOW.getCode().equals(pos)) {
            //查询老的数据，如果位置也是【首页-弹窗】,则不校验
            OpBanner oldUpdateOpBanner = opBannerMapper.selectByPrimaryKey(opBanner.getId());
            if (!EnumOpBannerPosType.INDEX_POPUP_WINDOW.getCode().equals(oldUpdateOpBanner.getPos())) {
                checkUniqueIndexPopWindow();
            }

            String bannerIndexPopWindowTimestampRedisKey = ConstantRedisKey.BANNER_INDEX_POP_WINDOW_TIMESTAMP;
            String bannerIndexPopWindowTimestamp = "" + System.currentTimeMillis();
            redisUtil.set(bannerIndexPopWindowTimestampRedisKey, bannerIndexPopWindowTimestamp);
        }

        opBanner.setUpdateTime(new Date());
        return opBannerMapper.updateByPrimaryKeySelective(opBanner);
    }

    @Override
    public int delOpBanner(Integer bannerId, Integer uid) {
        OpBanner opBanner = new OpBanner();
        opBanner.setId(bannerId);
        opBanner.setDel(ConstantCommon.DEL_ON);
        opBanner.setUpdateAdmin(uid);
        opBanner.setUpdateTime(new Date());
        return opBannerMapper.updateByPrimaryKeySelective(opBanner);
    }

    @Override
    public List<VoOpBanner> listOpBannerByPath(ParamAppBannerQuery paramAppBannerQuery) {
        List<VoOpBanner> list = opBannerMapper.selectOpBannerByPos(paramAppBannerQuery);
        /**
         * 移动端根据时间戳判断弹窗是否更新
         * 如果需要跟新弹窗，需要删除redis中Banner弹窗对应时间戳的值
         * ConstantRedisKey.BANNER_INDEX_POP_WINDOW_TIMESTAMP
         */
        String bannerIndexPopWindowTimestampRedisKey = ConstantRedisKey.BANNER_INDEX_POP_WINDOW_TIMESTAMP;
        String bannerIndexPopWindowTimestamp = redisUtil.get(bannerIndexPopWindowTimestampRedisKey);
        if (LocalUtils.isEmptyAndNull(bannerIndexPopWindowTimestamp)) {
            bannerIndexPopWindowTimestamp = "" + System.currentTimeMillis();
            redisUtil.set(bannerIndexPopWindowTimestampRedisKey, bannerIndexPopWindowTimestamp);
        }
        for (VoOpBanner voOpBanner : list) {
            voOpBanner.setTimestamp(bannerIndexPopWindowTimestamp);
            voOpBanner.setJumpH5Url(voOpBanner.getJumpH5Url() + "?timestamp=" + bannerIndexPopWindowTimestamp);
        }
        return list;
    }

    @Override
    public List<VoOpBannerNativePage> listAllOpBannerNativePage() {
        List<VoOpBannerNativePage> list = new ArrayList();
        for (EnumOpBannerNativePage enumPosType : EnumOpBannerNativePage.values()) {
            VoOpBannerNativePage voOpBannerNativePage = new VoOpBannerNativePage();
            voOpBannerNativePage.setNativePageCode(enumPosType.getCode());
            voOpBannerNativePage.setNativePageShowName(enumPosType.getName());
            list.add(voOpBannerNativePage);
        }
        return list;
    }
}
