package com.luxuryadmin.service.biz.impl;

import java.util.Date;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.biz.BizShopUnion;
import com.luxuryadmin.entity.biz.BizUnionVerify;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.mapper.biz.BizShopUnionMapper;
import com.luxuryadmin.mapper.biz.BizUnionVerifyMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.param.biz.*;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.biz.BizUnionVerifyService;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.biz.VoBizUnionVerifyList;
import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminPage;
import com.luxuryadmin.vo.biz.VoUnionVerify;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 商家联盟审核表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-03 17:44:45
 */
@Service
@Transactional
public class BizUnionVerifyServiceImpl implements BizUnionVerifyService {


    /**
     * 注入dao
     */
    @Resource
    private BizUnionVerifyMapper bizUnionVerifyMapper;

    @Resource
    private BizShopUnionMapper bizShopUnionMapper;

    @Autowired
    private OpPushService opPushService;

    @Autowired
    protected ServicesUtil servicesUtil;

    @Autowired
    private BizShopUnionService bizShopUnionService;

    @Autowired
    private ShpShopService shopService;

    @Autowired
    private ShpShopMapper shpShopMapper;

    @Override
    public void addUnionVerify(ParamUnionVerifyAdd unionVerifyAdd) {
        //是否为会员判断
        ShpShop shpShop = shpShopMapper.getShopInfo(unionVerifyAdd.getShopId());
        if (shpShop == null) {
            throw new MyException("请先开通店铺会员");
        }
        VoUnionVerify voUnionVerify = bizUnionVerifyMapper.getUnionVerifyByShopId(unionVerifyAdd.getShopId());
        if (voUnionVerify != null && "0".equals(voUnionVerify.getState())) {
            throw new MyException("商家联盟认证信息在审核中");
        }
        //判断该商家是否已加入商家联盟
        BizShopUnion shopUnionOld = bizShopUnionMapper.getByShopId(unionVerifyAdd.getShopId());
        if (!LocalUtils.isEmptyAndNull(shopUnionOld)) {
            if (shopUnionOld.getState() == 10 && "20".equals(shopUnionOld.getType())) {
                throw new MyException("该商家在商家联盟已认证");
            }
        }
        BizUnionVerify bizUnionVerify = new BizUnionVerify();
        BeanUtils.copyProperties(unionVerifyAdd, bizUnionVerify);
        bizUnionVerify.setState("0");
        bizUnionVerify.setFkProClassifyCode(unionVerifyAdd.getClassifyCode());
        bizUnionVerify.setInsertAdmin(unionVerifyAdd.getUserId());
        bizUnionVerify.setFkShpShopId(unionVerifyAdd.getShopId());
        bizUnionVerify.setInsertTime(new Date());
        bizUnionVerifyMapper.saveObject(bizUnionVerify);
    }

    @Override
    public VoUnionVerify getUnionVerifyByShopId(Integer shopId) {

        VoUnionVerify voUnionVerify = bizUnionVerifyMapper.getUnionVerifyByShopId(shopId);
        return voUnionVerify;
    }

    /**
     * 获取商家联盟卖家审核列表
     *
     * @param param
     * @return
     */
    @Override
    public List<VoBizUnionVerifyList> getBizUnionVerify(ParamBizUnionVerifyList param) {
        if (param.getPageSize() == null) {
            param.setPageSize(10);
        }
        if (StringUtil.isBlank(param.getPageNum())) {
            param.setPageNum("1");
        }
        PageHelper.startPage(Integer.parseInt(param.getPageNum()), param.getPageSize());
        List<VoBizUnionVerifyList> bizUnionVerifys = bizUnionVerifyMapper.getBizUnionVerify(param);
        if (LocalUtils.isEmptyAndNull(bizUnionVerifys)) {
            return bizUnionVerifys;
        }
        bizUnionVerifys.forEach(bu -> {
            if (StringUtil.isNotBlank(bu.getClassifyCode())) {
                String[] split = bu.getClassifyCode().split(",");
                StringBuffer stringBuffer = new StringBuffer();
                for (String str : split) {
                    stringBuffer.append(servicesUtil.getClassifyCn(str) + ",");
                }
                bu.setClassifyCodeName(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
            }

            bu.setAddress(bu.getProvince() + bu.getCity());
            bu.setProvince(null);
            bu.setCity(null);
        });
        return bizUnionVerifys;
    }

    /**
     * 审核（通过/未通过）
     *
     * @param param
     */
    @Override
    public void audit(ParamAudit param, int userId) {
        BizUnionVerify bizUnionVerify = bizUnionVerifyMapper.getObjectById(param.getId());
        if ("1".equals(bizUnionVerify.getState()) && !bizUnionVerify.getState().equals(param.getState())) {
            //修改商家审核状态
            throw new MyException("通过之后不可修改");
        }
        StringBuilder remark = new StringBuilder();
        //判断该店铺是否存在
        ShpShop shpShop = shopService.getShpShopById(bizUnionVerify.getFkShpShopId().toString());
        if (LocalUtils.isEmptyAndNull(shpShop)) {
            throw new MyException("暂未获取到该店铺");
        }
        switch (param.getState()) {
            case "1":
                //已通过 走商家联盟新增接口
                ParamShopUnionAdd shopUnionAdd = new ParamShopUnionAdd();
                shopUnionAdd.setShopNumber(shpShop.getNumber());
                shopUnionAdd.setType("20");
                shopUnionAdd.setUserId(userId);
                bizShopUnionService.addShopUnion(shopUnionAdd);
                remark.append("恭喜商成为联盟卖家，打开同步至友商（联盟）开关并保持商品上架，即可同步");
                break;
            case "2":
                //未通过
                remark.append("很遗憾，您提交的成为联盟卖家审核失败");
                if (StringUtil.isNotBlank(param.getFailRemark())) {
                    bizUnionVerify.setFailRemark(param.getFailRemark());
                    remark.append("，失败原因：" + param.getFailRemark());
                }

                break;
            default:
                throw new MyException("审核状态错误");
        }
        ThreadUtils.getInstance().executorService.execute(() -> {
            sendMessage(bizUnionVerify, remark.toString());
        });
        bizUnionVerify.setState(param.getState());
        bizUnionVerifyMapper.updateObject(bizUnionVerify);
    }

    public void sendMessage(BizUnionVerify bizUnionVerify, String remark) {
        opPushService.sendMessageUnionVerify(bizUnionVerify, remark);

    }

    /**
     * 编辑 --商家联盟审核
     *
     * @param param
     * @param userId
     */
    @Override
    public void update(ParamBizUnionVerifyUpdate param, int userId) {
        BizUnionVerify bizUnionVerify = bizUnionVerifyMapper.getObjectById(param.getId());
        if (bizUnionVerify.getState().equals("1") && !bizUnionVerify.getState().equals(param.getState())) {
            //修改商家审核状态
            throw new MyException("通过之后不可修改");
        }
        if (!bizUnionVerify.getState().equals(param.getState())) {
            ParamAudit paramAudit = new ParamAudit();
            paramAudit.setId(bizUnionVerify.getId());
            paramAudit.setState(param.getState());
            if (StringUtil.isNotBlank(param.getFailRemark())) {
                paramAudit.setFailRemark(param.getFailRemark());
            }
            //判断商家联盟审核是否通过
            audit(paramAudit, userId);
        }
        BeanUtils.copyProperties(param, bizUnionVerify);
        if (LocalUtils.isEmptyAndNull(param.getClassifyCode())) {
            bizUnionVerify.setFkProClassifyCode("");
        } else {
            bizUnionVerify.setFkProClassifyCode(param.getClassifyCode());
        }

        bizUnionVerify.setUpdateTime(new Date());
        bizUnionVerify.setUpdateAdmin(userId);
        bizUnionVerifyMapper.updateObject(bizUnionVerify);
    }

    /**
     * 此方法废弃不用
     *
     * @param bizUnionVerify
     * @param userId
     * @return
     */
    public BizShopUnion saveBizShopUnion(BizUnionVerify bizUnionVerify, int userId) {
        BizShopUnion bizShopUnion = new BizShopUnion();
        bizShopUnion.setFkShpShopId(bizUnionVerify.getFkShpShopId());
        bizShopUnion.setState(10);
        bizShopUnion.setType("20");
        bizShopUnion.setInsertTime(new Date());
        bizShopUnion.setUpdateTime(new Date());
        bizShopUnion.setInsertAdmin(userId);
        bizShopUnion.setDel('0');
        return bizShopUnion;
    }

}
