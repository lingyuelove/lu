package com.luxuryadmin.service.fin.impl;

import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.fin.FinShopRecord;
import com.luxuryadmin.enums.fin.EnumFinShopRecordInoutType;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.mapper.fin.FinShopRecordMapper;
import com.luxuryadmin.mapper.shp.ShpUserShopRefMapper;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFinShopRecordQuery;
import com.luxuryadmin.param.fin.ParamFinShopRecordUpdate;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.fin.FinFundRecordService;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.vo.fin.VoFinShopRecord;
import com.luxuryadmin.vo.fin.VoFinShopRecordDetail;
import com.luxuryadmin.vo.fin.VoFinShopRecordHomePageTop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 *
 */
@Service
@Slf4j
public class FinShopRecordServiceImpl implements FinShopRecordService {

    @Resource
    private FinShopRecordMapper finShopRecordMapper;

    @Resource
    private ShpUserShopRefMapper shpUserShopRefMapper;

    @Autowired
    private ServicesUtil servicesUtil;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private FinFundRecordService fundRecordService;

    @Override
    public FinShopRecord addFinShopRecord(Integer shopId, Integer userId, ParamFinShopRecordAdd paramFinShopRecordAdd,
                                          EnumFinShopRecordType enumType, String imgUrl, String fkOrderId) {

        /**
         * V2.2.0 2020-11-16 19???10?????????????????????????????????
         */
        FinShopRecord finShopRecord = new FinShopRecord();
        String recordType = enumType.getCode();
        if (EnumFinShopRecordType.MANUAL.getCode().equals(recordType)) {
            finShopRecord.setFkShpShopId(shopId);
            finShopRecord.setInoutType(paramFinShopRecordAdd.getInoutType());
            finShopRecord.setInoutSubType(paramFinShopRecordAdd.getType());
            finShopRecord.setRecordType(recordType);
            String defaultImgUrl = "/default/finRecordImg.png";
            imgUrl = StringUtil.isBlank(imgUrl) ? defaultImgUrl : imgUrl;
            finShopRecord.setImgUrl(imgUrl);
            finShopRecord.setImgUrlDetail(paramFinShopRecordAdd.getImgUrlDetail());
            finShopRecord.setStreamNo(shopId + "" + System.currentTimeMillis());
            BigDecimal changeAmount = new BigDecimal(paramFinShopRecordAdd.getChangeAmount()).multiply(new BigDecimal(100.00));
            finShopRecord.setChangeAmount(changeAmount.abs());
            finShopRecord.setFkOrderId(null);
            finShopRecord.setFkOrderId(fkOrderId);

            //????????????
            String happenTimeStr = paramFinShopRecordAdd.getHappenTime();
            Date happenTime = null;
            try {
                happenTime = DateUtil.parse(happenTimeStr, "yyyy-MM-dd");
            } catch (ParseException e) {
                log.error("" + e);
            }
            Date now = new Date();
            if (now.before(happenTime)) {
                throw new MyException("????????????????????????????????????");
            }
            String happenDate = DateUtil.format(happenTime, "yyyy-MM-dd");
            String happenMonth = DateUtil.format(happenTime, "yyyy-MM");
            String happenYear = DateUtil.format(happenTime, "yyyy");
            finShopRecord.setHappenTime(happenTime);
            finShopRecord.setHappenDate(happenDate);
            finShopRecord.setHappenMonth(happenMonth);
            finShopRecord.setHappenYear(happenYear);
            finShopRecord.setInsertTime(new Date());
            finShopRecord.setUpdateTime(new Date());

            finShopRecord.setInsertAdmin(userId);
            finShopRecord.setNote(paramFinShopRecordAdd.getNote());
            finShopRecord.setDel(ConstantCommon.DEL_OFF);

            finShopRecordMapper.saveObject(finShopRecord);
            //?????????????????????
            ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
            paramFundRecordAdd.setFkShpShopId(shopId);
            paramFundRecordAdd.setUserId(userId);
            paramFundRecordAdd.setMoney(changeAmount.toString());
            paramFundRecordAdd.setInitPrice("0");
            paramFundRecordAdd.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAdd.setFundType("50");
            paramFundRecordAdd.setCount("1");
            paramFundRecordAdd.setFinClassifyName("????????????");
            fundRecordService.addOtherFundRecord(paramFundRecordAdd);
        }
        return finShopRecord;
    }

    @Override
    public FinShopRecord updateFinShopRecord(ParamFinShopRecordUpdate paramFinShopRecordUpdate, HttpServletRequest request) {
        FinShopRecord finShopRecordOld = finShopRecordMapper.getObjectById(paramFinShopRecordUpdate.getId());
        if (finShopRecordOld == null) {
            return null;
        }
        FinShopRecord finShopRecord = new FinShopRecord();
        finShopRecord.setInoutSubType(paramFinShopRecordUpdate.getType());
        finShopRecord.setFkShpShopId(paramFinShopRecordUpdate.getShopId());
        finShopRecord.setInsertAdmin(paramFinShopRecordUpdate.getUserId());
        BigDecimal changeAmount = new BigDecimal(paramFinShopRecordUpdate.getChangeAmount()).multiply(new BigDecimal(100.00));
        finShopRecord.setChangeAmount(changeAmount.abs());
        BeanUtils.copyProperties(paramFinShopRecordUpdate, finShopRecord);
        if (paramFinShopRecordUpdate.getHappenTime() != null) {
            //????????????
            String happenTimeStr = paramFinShopRecordUpdate.getHappenTime();
            Date happenTime = null;
            try {
                happenTime = DateUtil.parse(happenTimeStr, "yyyy-MM-dd");
            } catch (ParseException e) {
                log.error("" + e);
            }
            Date now = new Date();
            if (now.before(happenTime)) {
                throw new MyException("????????????????????????????????????");
            }
            String happenDate = DateUtil.format(happenTime, "yyyy-MM-dd");
            finShopRecord.setHappenTime(happenTime);
            finShopRecord.setHappenDate(happenDate);
        }
        finShopRecordMapper.updateObject(finShopRecord);
        //??????????????????????????????-??????????????????
        String inoutTypeLog = EnumFinShopRecordInoutType.getNameByCode(finShopRecordOld.getInoutType());
        String recordType = finShopRecordOld.getInoutSubType();
        String content = inoutTypeLog + "-" + recordType + "-" + finShopRecordOld.getId();
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(paramFinShopRecordUpdate.getShopId());
        paramAddShpOperateLog.setOperateUserId(paramFinShopRecordUpdate.getUserId());
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.BILL.getName());
        paramAddShpOperateLog.setOperateName("????????????");
        paramAddShpOperateLog.setOperateContent(content);
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);

        //????????????????????????????????????
        int compareResult = 0;
        if (null != paramFinShopRecordUpdate.getChangeAmount()) {
            compareResult = changeAmount.compareTo(finShopRecordOld.getChangeAmount());
        }
        if (compareResult != 0) {
            String inoutType = EnumFinShopRecordInoutType.IN.getCode();
            //?????????????????????????????????????????????
            BigDecimal updateMoney = new BigDecimal(0);
            if (compareResult == 1) {
//                inoutType =EnumFinShopRecordInoutType.IN.getCode();
                updateMoney = finShopRecordOld.getChangeAmount().subtract(changeAmount);
            }
            //?????????????????????????????????????????????
            if (compareResult == -1) {
//                 inoutType =EnumFinShopRecordInoutType.OUT.getCode();
                updateMoney = changeAmount.subtract(finShopRecordOld.getChangeAmount());
            }
            if (finShopRecordOld.getInoutType().equals(EnumFinShopRecordInoutType.IN.getCode()) && compareResult == -1) {
                inoutType = EnumFinShopRecordInoutType.OUT.getCode();
            }
            if (finShopRecordOld.getInoutType().equals(EnumFinShopRecordInoutType.OUT.getCode()) && compareResult == 1) {
                inoutType = EnumFinShopRecordInoutType.OUT.getCode();
            }
            ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
            paramFundRecordAdd.setFkShpShopId(paramFinShopRecordUpdate.getShopId());
            paramFundRecordAdd.setUserId(paramFinShopRecordUpdate.getUserId());
            paramFundRecordAdd.setMoney(updateMoney.toString());
            paramFundRecordAdd.setInitPrice("0");
            paramFundRecordAdd.setState(inoutType);
            paramFundRecordAdd.setFundType("50");
            paramFundRecordAdd.setCount("1");
            paramFundRecordAdd.setFinClassifyName("??????????????????");
            fundRecordService.addOtherFundRecord(paramFundRecordAdd);
        }

        return finShopRecord;
    }

    @Override
    public Integer deleteFinShopRecord(Integer shopId, Integer userId, int recordId, HttpServletRequest request) {
        FinShopRecord finShopRecordUpdate = finShopRecordMapper.selectFinShopRecordById(shopId, recordId);
        if (null == finShopRecordUpdate) {
            throw new MyException("??????????????????????????????");
        }
        finShopRecordUpdate.setUpdateAdmin(userId);
        finShopRecordUpdate.setUpdateTime(new Date());
        finShopRecordUpdate.setDel(ConstantCommon.DEL_ON);

        String inoutType = EnumFinShopRecordInoutType.getNameByCode(finShopRecordUpdate.getInoutType());
        String recordType = finShopRecordUpdate.getInoutSubType();
        String content = inoutType + "-" + recordType + "-" + finShopRecordUpdate.getId();
        //??????????????????????????????-??????????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.BILL.getName());
        paramAddShpOperateLog.setOperateName("????????????");
        paramAddShpOperateLog.setOperateContent(content);
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        //????????????????????????????????????
        ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(userId);
        paramFundRecordAdd.setMoney(finShopRecordUpdate.getChangeAmount().toString());
        paramFundRecordAdd.setInitPrice("0");
        if (finShopRecordUpdate.getInoutType().equals(EnumFinShopRecordInoutType.IN.getCode())) {
            paramFundRecordAdd.setState(EnumFinShopRecordInoutType.OUT.getCode());
        }
        if (finShopRecordUpdate.getInoutType().equals(EnumFinShopRecordInoutType.OUT.getCode())) {
            paramFundRecordAdd.setState(EnumFinShopRecordInoutType.IN.getCode());
        }
        paramFundRecordAdd.setFundType("50");
        paramFundRecordAdd.setCount("1");
        paramFundRecordAdd.setFinClassifyName("??????????????????");
        fundRecordService.addOtherFundRecord(paramFundRecordAdd);
        return finShopRecordMapper.updateByPrimaryKey(finShopRecordUpdate);
    }

    @Override
    public List<List<VoFinShopRecord>> listFinShopRecordByShopId(Integer shopId, ParamFinShopRecordQuery paramFinShopRecordQuery) throws Exception {
        List<List<VoFinShopRecord>> voFinShopRecordList;
        paramFinShopRecordQuery.setShopId(shopId);
        checkQueryDateRange(paramFinShopRecordQuery);

        List<FinShopRecord> recordList = finShopRecordMapper.listFinShopRecordByShopId(paramFinShopRecordQuery);
        if (recordList != null && recordList.size() > 0) {
            recordList.forEach(finShopRecord -> {
                finShopRecord.setInsertTime(finShopRecord.getHappenTime());
            });
        }

        voFinShopRecordList = formatRecordList(recordList);
        return voFinShopRecordList;
    }

    @Override
    public PageInfo listFinShopRecordByShopIdForAdmin(Integer shopId, ParamFinShopRecordQuery paramFinShopRecordQuery) throws Exception {

        paramFinShopRecordQuery.setShopId(shopId);
        checkQueryDateRange(paramFinShopRecordQuery);
        List<VoFinShopRecord> list = new ArrayList<>();
        List<FinShopRecord> recordList = finShopRecordMapper.listFinShopRecordByShopId(paramFinShopRecordQuery);
        PageInfo pageInfo = new PageInfo(recordList);
        if (!LocalUtils.isEmptyAndNull(recordList)) {
            recordList.forEach(fsr -> {
                VoFinShopRecord voFinShopRecord = new VoFinShopRecord();
                BeanUtils.copyProperties(fsr, voFinShopRecord);
                voFinShopRecord.setInsertTime(DateUtil.format(fsr.getInsertTime()));
                voFinShopRecord.setImgUrlDetail(null);
                //????????????????????????
                voFinShopRecord.setFinRecordTypeName(fsr.getInoutSubType());
                //????????????
                voFinShopRecord.setHappenTime(DateUtil.format(fsr.getHappenTime(), "yyyy-MM-dd"));
                //????????????
                String changeAmount = getChangeAmountWithSign(fsr);
                voFinShopRecord.setChangeAmount(changeAmount);
                //????????????
                String imgUrl = fsr.getImgUrl();

                String imgUrlDetail = fsr.getImgUrlDetail();
                if (!LocalUtils.isEmptyAndNull(imgUrlDetail)) {
                    String[] split = imgUrlDetail.split(";");
                    imgUrl = split[0];
                }
                if (StringUtil.isNotBlank(imgUrl)) {
                    boolean isSmall = true;
                    if (imgUrl.startsWith("/default")) {
                        //?????????????????????????????????
                        isSmall = false;
                    }
                    voFinShopRecord.setImgUrl(servicesUtil.formatImgUrl(imgUrl, isSmall));
                }
                String nameFromShop = shpUserShopRefMapper.getNameFromShop(fsr.getFkShpShopId(), fsr.getInsertAdmin());
                voFinShopRecord.setUserShopNickname(nameFromShop);
                list.add(voFinShopRecord);

            });
        }
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public List<VoFinShopRecord> listFinShopRecordByShopIdForExp(ParamFinShopRecordQuery paramFinShopRecordQuery) {
        List<VoFinShopRecord> list = new ArrayList<>();
        List<FinShopRecord> recordList = finShopRecordMapper.listFinShopRecordByShopId(paramFinShopRecordQuery);
        if (!LocalUtils.isEmptyAndNull(recordList)) {
            recordList.forEach(fsr -> {
                VoFinShopRecord voFinShopRecord = new VoFinShopRecord();
                BeanUtils.copyProperties(fsr, voFinShopRecord);
                voFinShopRecord.setInsertTime(DateUtil.format(fsr.getInsertTime()));
                voFinShopRecord.setImgUrlDetail(null);
                //????????????????????????
                voFinShopRecord.setFinRecordTypeName(fsr.getInoutSubType());
                //????????????
                voFinShopRecord.setHappenTime(DateUtil.format(fsr.getHappenTime(), "yyyy-MM-dd"));
                //????????????
                String changeAmount = getChangeAmountWithSign(fsr);
                voFinShopRecord.setChangeAmount(changeAmount);
                //????????????
                String imgUrl = fsr.getImgUrl();

                String imgUrlDetail = fsr.getImgUrlDetail();
                if (!LocalUtils.isEmptyAndNull(imgUrlDetail)) {
                    String[] split = imgUrlDetail.split(";");
                    imgUrl = split[0];
                }
                if (StringUtil.isNotBlank(imgUrl)) {
                    boolean isSmall = true;
                    if (imgUrl.startsWith("/default")) {
                        //?????????????????????????????????
                        isSmall = false;
                    }
                    try {
                        voFinShopRecord.setImgUrlShow(new URL(servicesUtil.formatImgUrl(imgUrl, isSmall)));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                String nameFromShop = shpUserShopRefMapper.getNameFromShop(fsr.getFkShpShopId(), fsr.getInsertAdmin());
                voFinShopRecord.setUserShopNickname(nameFromShop);
                list.add(voFinShopRecord);

            });
        }
        return list;
    }

    /**
     * ??????????????????????????????
     *
     * @param paramFinShopRecordQuery
     * @throws ParseException
     */
    private void checkQueryDateRange(ParamFinShopRecordQuery paramFinShopRecordQuery) throws Exception {
        //??????????????????
        Boolean isStartNull = Boolean.TRUE;
        Boolean isEndNull = Boolean.TRUE;
        String timeStart = paramFinShopRecordQuery.getHappenTimeStart();
        if (StringUtil.isNotBlank(timeStart)) {
            isStartNull = false;
            String timeStartStr = timeStart + " 00:00:00";
            paramFinShopRecordQuery.setHappenTimeStart(timeStartStr);
        }
        String timeEnd = paramFinShopRecordQuery.getHappenTimeEnd();
        if (StringUtil.isNotBlank(timeEnd)) {
            isEndNull = false;
            String timeEndStr = timeEnd + " 23:59:59";
            paramFinShopRecordQuery.setHappenTimeEnd(timeEndStr);
        }

        //????????????????????????????????????????????????????????????????????????
        //??????????????????
        if ((isStartNull && !isEndNull) || (!isStartNull && isEndNull)) {
            throw new MyException("?????????????????????????????????6??????");
        }

        //????????????????????????
        if (!isStartNull && !isEndNull) {
            Date startDate = DateUtil.parse(timeStart, "yyyy-MM-dd");
            Date endDate = DateUtil.parse(timeEnd, "yyyy-MM-dd");
            int intervalDays = DateUtil.calIntervalDays(endDate, startDate);
            if (intervalDays > 180) {
                throw new MyException("?????????????????????????????????6??????");
            }
            if (endDate.before(startDate)) {
                throw new MyException("????????????????????????????????????");
            }
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param recordList
     * @return
     */
    private List<List<VoFinShopRecord>> formatRecordList(List<FinShopRecord> recordList) {
        Map<String, String> countMap = new HashMap<>(16);
        List<List<VoFinShopRecord>> voFinShopRecordList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(recordList)) {
            List<VoFinShopRecord> tmpVoList = null;
            for (FinShopRecord fsr : recordList) {
                VoFinShopRecord voFinShopRecord = new VoFinShopRecord();
                BeanUtils.copyProperties(fsr, voFinShopRecord);
                //????????????????????????
                voFinShopRecord.setFinRecordTypeName(fsr.getInoutSubType());
                //????????????
                voFinShopRecord.setHappenTime(DateUtil.format(fsr.getHappenTime(), "yyyy-MM-dd"));
                //????????????
                String changeAmount = getChangeAmountWithSign(fsr);
                voFinShopRecord.setChangeAmount(changeAmount);
                //????????????
                String imgUrl = fsr.getImgUrl();

                String imgUrlDetail = fsr.getImgUrlDetail();
                if (!LocalUtils.isEmptyAndNull(imgUrlDetail)) {
                    String[] split = imgUrlDetail.split(";");
                    imgUrl = split[0];
                }
                if (StringUtil.isNotBlank(imgUrl)) {
                    boolean isSmall = true;
                    if (imgUrl.startsWith("/default")) {
                        //?????????????????????????????????
                        isSmall = false;
                    }
                    voFinShopRecord.setImgUrl(servicesUtil.formatImgUrl(imgUrl, isSmall));
                }
                String nameFromShop = shpUserShopRefMapper.getNameFromShop(fsr.getFkShpShopId(), fsr.getInsertAdmin());
                voFinShopRecord.setUserShopNickname("????????????" + nameFromShop);
                String happenDate = fsr.getHappenDate();
                String isExist = countMap.get(happenDate);
                if (StringUtils.isBlank(isExist)) {
                    tmpVoList = new ArrayList<>();
                    voFinShopRecordList.add(tmpVoList);
                    countMap.put(happenDate, "exist");
                }
                tmpVoList.add(voFinShopRecord);
            }
        }
        return voFinShopRecordList;
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     *
     * @param finShopRecord
     * @return
     */
    private String getChangeAmountWithSign(FinShopRecord finShopRecord) {
        //DecimalFormat df = new DecimalFormat("###,###");
        BigDecimal changeAmountDb = finShopRecord.getChangeAmount().divide(new BigDecimal(100.00));
        String changeAmount = StringUtil.removeEnd(changeAmountDb.toString(), ".00");
        String inoutType = finShopRecord.getInoutType();
        //?????????
        String sign = EnumFinShopRecordInoutType.IN.getCode().equals(inoutType) ? "+" : "-";
        changeAmount = sign + changeAmount;
        return changeAmount;
    }


    @Override
    public VoFinShopRecordHomePageTop getFinShopRecordHomePageTop(Integer shopId, ParamFinShopRecordQuery paramFinShopRecordQuery) throws Exception {
        paramFinShopRecordQuery.setShopId(shopId);

        checkQueryDateRange(paramFinShopRecordQuery);
        VoFinShopRecordHomePageTop voFinShopRecordHomePageTop = finShopRecordMapper.selectFinShopRecordHomePageTop(paramFinShopRecordQuery);

        //?????????????????????0
        String totalInAmount = voFinShopRecordHomePageTop.getTotalInAmount();
        String totalOutAmount = voFinShopRecordHomePageTop.getTotalOutAmount();
        totalInAmount = StringUtil.removeEnd(totalInAmount, ".00");
        totalOutAmount = StringUtil.removeEnd(totalOutAmount, ".00");
        voFinShopRecordHomePageTop.setTotalInAmount(totalInAmount);
        voFinShopRecordHomePageTop.setTotalOutAmount(totalOutAmount);
        return voFinShopRecordHomePageTop;
    }

    @Override
    public VoFinShopRecordDetail getFinShopRecordDetailById(Integer shopId, int id) {
        VoFinShopRecordDetail voDetail = new VoFinShopRecordDetail();
        FinShopRecord finShopRecord = finShopRecordMapper.selectFinShopRecordById(shopId, id);
        BeanUtils.copyProperties(finShopRecord, voDetail);
        //????????????
        voDetail.setChangeAmount(getChangeAmountWithSign(finShopRecord));
        //??????????????????
        voDetail.setHappenTime(DateUtil.format(finShopRecord.getHappenTime(), "yyyy-MM-dd"));
        //??????????????????
        voDetail.setInsertTime(DateUtil.format(finShopRecord.getInsertTime()));
        //?????????
        Integer userId = finShopRecord.getInsertAdmin();
        //????????????
        voDetail.setRecordType(EnumFinShopRecordType.geNameByCode(finShopRecord.getRecordType()));
        String userName = shpUserShopRefMapper.getNameFromShop(shopId, userId);
        voDetail.setInsertAdmin(userName);
        //??????????????????
        String imgUrlDetail = finShopRecord.getImgUrlDetail();
        List<String> imgUrlDetailList = new ArrayList<>();
        voDetail.setImgUrlDetailList(imgUrlDetailList);
        if (!StringUtil.isBlank(imgUrlDetail)) {
            String[] imgArr = imgUrlDetail.split(";");
            if (null != imgArr && imgArr.length != 0) {
                for (String imgUrl : imgArr) {
                    imgUrlDetailList.add(servicesUtil.formatImgUrl(imgUrl));
                }
            }
        }
        return voDetail;
    }

    @Override
    public void deleteFinShopRecordByDateRange(Integer shopId, String startTime, String endTime) {
        if (null == shopId) {
            throw new MyException("??????ID????????????????????????");
        }
        //??????????????????????????????????????????
        finShopRecordMapper.delFinShopRecordByDateRange(shopId, startTime, endTime);
    }
}
