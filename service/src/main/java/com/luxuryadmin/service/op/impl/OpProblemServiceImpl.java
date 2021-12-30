package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.op.OpProblem;
import com.luxuryadmin.mapper.op.OpProblemMapper;
import com.luxuryadmin.param.op.ParamOpProblemQuery;
import com.luxuryadmin.service.op.OpProblemService;
import com.luxuryadmin.vo.op.OpProblemType;
import com.luxuryadmin.vo.op.VoOpProblem;
import com.luxuryadmin.vo.op.VoOpProblemCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sanjin
 * @date 2020-07-09 11:49:06
 */
@Slf4j
@Service
public class OpProblemServiceImpl implements OpProblemService {

    @Resource
    private OpProblemMapper opProblemMapper;
    @Autowired
    protected ServicesUtil servicesUtil;
    @Override
    public List<OpProblemType> listAllOpProblemCategoryName() {
        List<OpProblemType> nameList = new ArrayList<>();
        OpProblemType opProblemType1 = new OpProblemType();
        opProblemType1.setName("同行模块");
        opProblemType1.setIconImg(servicesUtil.formatImgUrl("/opProblem/peer.png", false));
        nameList.add(opProblemType1);
        OpProblemType opProblemType2 = new OpProblemType();
        opProblemType2.setName("销售模块");
        opProblemType2.setIconImg(servicesUtil.formatImgUrl("/opProblem/sale.png", false));
        nameList.add(opProblemType2);
        OpProblemType opProblemType5 = new OpProblemType();
        opProblemType5.setName("店铺模块");
        opProblemType5.setIconImg(servicesUtil.formatImgUrl("/opProblem/shop.png", false));
        nameList.add(opProblemType5);
        OpProblemType opProblemType4 = new OpProblemType();
        opProblemType4.setName("分享模块");
        opProblemType4.setIconImg(servicesUtil.formatImgUrl("/opProblem/share.png", false));
        nameList.add(opProblemType4);
        OpProblemType opProblemType3 = new OpProblemType();
        opProblemType3.setName("仓库模块");
        opProblemType3.setIconImg(servicesUtil.formatImgUrl("/opProblem/storehouse.png", false));
        nameList.add(opProblemType3);
        OpProblemType opProblemType6 = new OpProblemType();
        opProblemType6.setName("其他模块");
        opProblemType6.setIconImg(servicesUtil.formatImgUrl("/opProblem/other.png", false));
        nameList.add(opProblemType6);
        return nameList;
    }

    @Override
    public List<VoOpProblem> listOpProblem(ParamOpProblemQuery paramOpProblemQuery) {
        List<VoOpProblem> list = opProblemMapper.selectOpVoProblem(paramOpProblemQuery);
        return list;
    }

    @Override
    public OpProblem getProblemById(Integer id) {
        OpProblem opProblem = opProblemMapper.selectByPrimaryKey(id);

        return opProblem;
    }

    @Override
    public int addOpProblem(OpProblem opProblem) {
        opProblem.setInsertTime(new Date());
        opProblem.setUpdateTime(new Date());
        opProblem.setDel(ConstantCommon.DEL_OFF);
        return opProblemMapper.insertSelective(opProblem);
    }

    @Override
    public int updateOpProblem(OpProblem opProblem) {
//        opProblem.setUpdateTime(new Date());
        return opProblemMapper.updateByPrimaryKeySelective(opProblem);
    }

    @Override
    public int delOpProblem(Integer id,Integer uid) {
        OpProblem delOpProblem = opProblemMapper.selectByPrimaryKey(id);
        delOpProblem.setDel(ConstantCommon.DEL_ON);
        delOpProblem.setUpdateAdmin(uid);
        delOpProblem.setUpdateTime(new Date());
        return opProblemMapper.updateByPrimaryKeySelective(delOpProblem);
    }

    @Override
    public List<VoOpProblemCategory> listEnableOpProblem() {
        List<VoOpProblemCategory> opProblemCategories =listOpProblemCategory();
        if (LocalUtils.isEmptyAndNull(opProblemCategories)){
            return null;
        }
        opProblemCategories.forEach(voOpProblemCategory -> {
            List<VoOpProblem> list = opProblemMapper.selectEnableOpProblem(null);
            if (!LocalUtils.isEmptyAndNull(list)){
                voOpProblemCategory.setList(list);
            }
        });

        return opProblemCategories;
    }

    @Override
    public List<VoOpProblem> selectEnableOpProblem(ParamOpProblemQuery paramOpProblemQuery) {
        List<VoOpProblem> list = opProblemMapper.selectEnableOpProblem(paramOpProblemQuery);
        if (!LocalUtils.isEmptyAndNull(list)){
            return list;
        }
        return null;
    }

    public List<VoOpProblemCategory> listOpProblemCategory(){
        List<VoOpProblemCategory> opProblemCategories = new ArrayList<>();
        VoOpProblemCategory voOpProblemCategory1 = new VoOpProblemCategory();
        voOpProblemCategory1.setProblemCategoryName("同行模块");
        opProblemCategories.add(voOpProblemCategory1);
        VoOpProblemCategory voOpProblemCategory2 = new VoOpProblemCategory();
        voOpProblemCategory2.setProblemCategoryName("销售模块");
        opProblemCategories.add(voOpProblemCategory2);
        VoOpProblemCategory voOpProblemCategory3 = new VoOpProblemCategory();
        voOpProblemCategory3.setProblemCategoryName("仓库模块");
        opProblemCategories.add(voOpProblemCategory3);
        VoOpProblemCategory voOpProblemCategory4 = new VoOpProblemCategory();
        voOpProblemCategory4.setProblemCategoryName("分享模块");
        opProblemCategories.add(voOpProblemCategory4);
        VoOpProblemCategory voOpProblemCategory5 = new VoOpProblemCategory();
        voOpProblemCategory5.setProblemCategoryName("店铺模块");
        opProblemCategories.add(voOpProblemCategory5);
        VoOpProblemCategory voOpProblemCategory6 = new VoOpProblemCategory();
        voOpProblemCategory6.setProblemCategoryName("其他模块");
        opProblemCategories.add(voOpProblemCategory6);
        return opProblemCategories;
    }
}
