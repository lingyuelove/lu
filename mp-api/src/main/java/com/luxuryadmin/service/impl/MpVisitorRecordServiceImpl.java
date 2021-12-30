package com.luxuryadmin.service.impl;

import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.mapper.MpVisitorRecordMapper;
import com.luxuryadmin.param.visitor.ParamVisitorRecordList;
import com.luxuryadmin.service.MpVisitorRecordService;
import com.luxuryadmin.vo.visitor.VOVisitorRecord;
import com.luxuryadmin.vo.visitor.VOVisitorRecordList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 访客记录 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Service
@Transactional
public class MpVisitorRecordServiceImpl implements MpVisitorRecordService {


    /**
     * 注入dao
     */
    @Resource
    private MpVisitorRecordMapper mpVisitorRecordMapper;


    /**
     * 后台查询访客记录
     *
     * @param param
     * @return
     */
    @Override
    public VOVisitorRecord getVisitorRecord(ParamVisitorRecordList param) {
        VOVisitorRecord voVisitorRecord = new VOVisitorRecord();
        if (StringUtil.isNotBlank(param.getUsername())) {
            param.setUsername(DESEncrypt.encodeUsername(param.getUsername()));
        }
        //获取列表数据
        List<VOVisitorRecordList> voVisitorRecords = mpVisitorRecordMapper.getVisitorRecord(param);
        if (voVisitorRecords != null && voVisitorRecords.size() > 0) {
            voVisitorRecords.stream().forEach(vv -> {
                vv.setUsername(DESEncrypt.decodeUsername(vv.getUsername()));
                if (vv.getPayEndTime() != null) {
                    vv.setPastTime(DateUtil.format(vv.getPayEndTime()));
                } else {
                    if (vv.getTryEndTime() != null) {
                        vv.setPastTime(DateUtil.format(vv.getTryEndTime()));
                    } else {
                        vv.setPastTime(null);
                    }
                }
            });
            PageInfo pageInfo = new PageInfo(voVisitorRecords);
            voVisitorRecord.setObjList(pageInfo);
        }
        //获取访问人次
        List<Integer> visitorPersonNums = mpVisitorRecordMapper.getVisitorPersonNum(param);
        if (visitorPersonNums != null && visitorPersonNums.size() > 0) {
            voVisitorRecord.setVisitorPersonNum(visitorPersonNums.size());
            voVisitorRecord.setVisitorNum(visitorPersonNums.stream().mapToInt(Integer::intValue).sum());
        } else {
            voVisitorRecord.setVisitorPersonNum(0);
            voVisitorRecord.setVisitorNum(0);
        }

        return voVisitorRecord;
    }
}
