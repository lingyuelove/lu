package com.luxuryadmin.service.impl;

import com.luxuryadmin.entity.MpUser;
import com.luxuryadmin.enums.EnumIsMember;
import com.luxuryadmin.enums.EnumVipType;
import com.luxuryadmin.mapper.MpAddVipTimeMapper;
import com.luxuryadmin.mapper.MpUserMapper;
import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.MpAddVipTimeService;
import com.luxuryadmin.vo.award.VOAwardDay;
import com.luxuryadmin.vo.award.VOAwardRecordList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 追加时长表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:00
 */
@Service
@Transactional
public class MpAddVipTimeServiceImpl implements MpAddVipTimeService {


    /**
     * 注入dao
     */
    @Resource
    private MpAddVipTimeMapper mpAddVipTimeMapper;

    @Resource
    private BasicsService basicsService;

    @Resource
    private MpUserMapper mpUserMapper;

    /**
     * 获取奖励记录o
     *
     * @param param
     * @return
     */
    @Override
    public VOAwardDay getAwardRecord(ParamBasic param) {
        Integer userId = basicsService.getUserId();
        MpUser mpUser = mpUserMapper.getObjectById(userId);
        VOAwardDay voAwardDay = new VOAwardDay();
        if (EnumVipType.MP_VIP.getCode().equals(mpUser.getVipType()) && EnumIsMember.YES.getCode().equals(mpUser.getIsMember())) {
            List<VOAwardRecordList> voAwardRecordLists = mpAddVipTimeMapper.listAddVipTime(userId);
            if (voAwardRecordLists != null && voAwardRecordLists.size() > 0) {
                voAwardRecordLists.forEach(vo -> {
                    vo.setRemark(vo.getRemark() + "成为VIP");
                });
                voAwardDay.setVoAwardRecordLists(voAwardRecordLists);
            }
            //获取奖励总天数
            Integer totalAddDay = mpAddVipTimeMapper.getTotalAddDay(userId);
            voAwardDay.setTotalAddDay(totalAddDay);
            return voAwardDay;
        }
        return voAwardDay;
    }
}
