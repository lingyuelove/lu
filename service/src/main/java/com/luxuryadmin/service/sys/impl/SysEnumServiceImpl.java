package com.luxuryadmin.service.sys.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.entity.sys.SysEnum;
import com.luxuryadmin.enums.ord.EnumOrdType;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.pro.EnumProClassify;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.enums.shp.EnumShpUserType;
import com.luxuryadmin.enums.sys.EnumTableName;
import com.luxuryadmin.mapper.sys.SysEnumMapper;
import com.luxuryadmin.service.sys.SysEnumService;
import com.luxuryadmin.vo.sys.VoSysEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-26 16:32:10
 */
@Slf4j
@Service
public class SysEnumServiceImpl implements SysEnumService {

    @Resource
    private SysEnumMapper sysEnumMapper;


    /**
     * 遍历枚举类的值入库;
     *
     * @return
     */
    private List<SysEnum> generatorSysEnum(EnumTableName tableName) {
        List<SysEnum> sysEnumList = new ArrayList<>();
        String type = getType(tableName) + "";
        switch (type) {
            case "pro_attribute":
                sysEnumList.addAll(getEnumProAttribute());
                break;
            case "pro_classify":
                sysEnumList.addAll(getEnumProClassify());
                break;
            case "pro_state":
                sysEnumList.addAll(getEnumProState());
                break;
            case "ord_type":
                sysEnumList.addAll(getEnumOrdType());
            case "shp_user_type":
                sysEnumList.addAll(getEnumShpUserType());
                break;
            default:
                sysEnumList.addAll(getEnumProAttribute());
                sysEnumList.addAll(getEnumProClassify());
                sysEnumList.addAll(getEnumProState());
                sysEnumList.addAll(getEnumOrdType());
                sysEnumList.addAll(getEnumShpUserType());
                break;
        }
        return sysEnumList;
    }

    @Override
    public boolean checkUpdateSysEnumByType(EnumTableName tableName) {
        String type = getType(tableName);
        int dbCount = sysEnumMapper.getAllCountByType(type);
        return dbCount <= 0;
    }

    @Override
    public void initSysEnumByType(EnumTableName tableName) {
        String type = getType(tableName);
        List<SysEnum> sysEnumList = generatorSysEnum(tableName);
        boolean isExists = checkUpdateSysEnumByType(tableName);
        if (isExists) {
            int rows = sysEnumMapper.saveBatch(sysEnumList);
            log.info("=======SysEnum: " + type + ";初始化成功; 受影响行数: " + rows);
        }

    }


    @Override
    public void resetSysEnumByType(EnumTableName tableName) {
        String type = getType(tableName);
        log.info("=======【重置】SysEnum======" + type);
        sysEnumMapper.deleteAllByType(getType(tableName));
        initSysEnumByType(tableName);
    }

    /**
     * @param tableName 表名; 如果为null, 则代表所有表;反之,则为具体某一张表;
     * @return
     */
    private String getType(EnumTableName tableName) {
        return (null == tableName) ? null : tableName.getCode();
    }


    @Override
    public List<SysEnum> getEnumProClassify() {
        int i = 1;
        Date insertTime = new Date();
        List<SysEnum> sysEnumList = new ArrayList<>();
        for (EnumProClassify myEnum : EnumProClassify.values()) {
            int state = 1;
            //全部启用
            /*if (i > 3) {
                state = 0;
            }*/
            String code = myEnum.getCode();
            String name = myEnum.getName();
            String description = myEnum.getDescription();
            SysEnum sysEnum = new SysEnum();
            sysEnum.setCode(code);
            sysEnum.setName(name);
            sysEnum.setDescription(description);
            sysEnum.setType(EnumTableName.PRO_CLASSIFY.getCode());
            sysEnum.setState(state);
            sysEnum.setSort(i);
            sysEnum.setInsertTime(insertTime);
            sysEnum.setInsertAdmin(0);
            sysEnum.setVersions(1);
            sysEnum.setDel("0");
            sysEnum.setRemark(ConstantCommon.AUTO_REMARK);
            sysEnumList.add(sysEnum);
            i++;
        }
        return sysEnumList;
    }

    @Override
    public List<SysEnum> getEnumProAttribute() {
        int i = 1;
        Date insertTime = new Date();
        List<SysEnum> sysEnumList = new ArrayList<>();
        for (EnumProAttribute myEnum : EnumProAttribute.values()) {
            int code = myEnum.getCode();
            String name = myEnum.getName();
            String description = myEnum.getDescription();
            SysEnum sysEnum = new SysEnum();
            sysEnum.setCode(code + "");
            sysEnum.setName(name);
            sysEnum.setDescription(description);
            sysEnum.setType(EnumTableName.PRO_ATTRIBUTE.getCode());
            sysEnum.setState(1);
            sysEnum.setSort(i);
            sysEnum.setInsertTime(insertTime);
            sysEnum.setInsertAdmin(0);
            sysEnum.setVersions(1);
            sysEnum.setDel("0");
            sysEnum.setRemark(ConstantCommon.AUTO_REMARK);
            sysEnumList.add(sysEnum);
            i++;
        }
        return sysEnumList;
    }


    @Override
    public List<SysEnum> getEnumProState() {
        int i = 1;
        Date insertTime = new Date();
        List<SysEnum> sysEnumList = new ArrayList<>();
        for (EnumProState myEnum : EnumProState.values()) {
            int code = myEnum.getCode();
            String name = myEnum.getName();
            String description = myEnum.getDescription();
            SysEnum sysEnum = new SysEnum();
            sysEnum.setCode(code + "");
            sysEnum.setName(name);
            sysEnum.setDescription(description);
            sysEnum.setType(EnumTableName.PRO_STATE.getCode());
            sysEnum.setState(1);
            sysEnum.setSort(i);
            sysEnum.setInsertTime(insertTime);
            sysEnum.setInsertAdmin(0);
            sysEnum.setVersions(1);
            sysEnum.setDel("0");
            sysEnum.setRemark(ConstantCommon.AUTO_REMARK);
            sysEnumList.add(sysEnum);
            i++;
        }
        return sysEnumList;
    }


    @Override
    public List<SysEnum> getEnumOrdType() {
        int i = 1;
        Date insertTime = new Date();
        List<SysEnum> sysEnumList = new ArrayList<>();
        for (EnumOrdType myEnum : EnumOrdType.values()) {
            String code = myEnum.getCode();
            String name = myEnum.getName();
            String description = myEnum.getDescription();
            SysEnum sysEnum = new SysEnum();
            sysEnum.setCode(code);
            sysEnum.setName(name);
            sysEnum.setDescription(description);
            sysEnum.setType(EnumTableName.ORD_TYPE.getCode());
            sysEnum.setState(1);
            sysEnum.setSort(i);
            sysEnum.setInsertTime(insertTime);
            sysEnum.setInsertAdmin(0);
            sysEnum.setVersions(1);
            sysEnum.setDel("0");
            sysEnum.setRemark(ConstantCommon.AUTO_REMARK);
            sysEnumList.add(sysEnum);
            i++;
        }
        return sysEnumList;
    }


    @Override
    public List<SysEnum> getEnumShpUserType() {
        int i = 1;
        Date insertTime = new Date();
        List<SysEnum> sysEnumList = new ArrayList<>();
        for (EnumShpUserType myEnum : EnumShpUserType.values()) {
            String code = myEnum.getCode() + "";
            String name = myEnum.getName();
            String description = myEnum.getDescription();
            SysEnum sysEnum = new SysEnum();
            sysEnum.setCode(code);
            sysEnum.setName(name);
            sysEnum.setDescription(description);
            sysEnum.setType(EnumTableName.SHP_USER_TYPE.getCode());
            sysEnum.setState(1);
            sysEnum.setSort(i);
            sysEnum.setInsertTime(insertTime);
            sysEnum.setInsertAdmin(0);
            sysEnum.setVersions(1);
            sysEnum.setDel("0");
            sysEnum.setRemark(ConstantCommon.AUTO_REMARK);
            sysEnumList.add(sysEnum);
            i++;
        }
        return sysEnumList;
    }

    @Override
    public List<VoSysEnum> listVoSysEnum(String name, String type) {
        return sysEnumMapper.listVoSysEnum(name, type);
    }

    @Override
    public List<VoSysEnum> listVoSysEnum(String type) {
        return sysEnumMapper.listVoSysEnum(null, type);
    }

    @Override
    public List<VoSysEnum> listTplName() {
        return sysEnumMapper.listTplName();
    }

    @Override
    public int deleteTplName(String name) {
        return sysEnumMapper.deleteTplName(name);
    }

    @Override
    public int saveBatch(List<SysEnum> list) {
        return sysEnumMapper.saveBatch(list);
    }


}
