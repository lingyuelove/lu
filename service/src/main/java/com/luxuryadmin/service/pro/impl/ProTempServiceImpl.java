package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProTemp;
import com.luxuryadmin.mapper.org.OrgOrganizationTempMapper;
import com.luxuryadmin.mapper.pro.ProTempMapper;
import com.luxuryadmin.service.org.OrgTempSeatService;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.service.pro.ProTempService;
import com.luxuryadmin.vo.org.VoTempSeatList;
import com.luxuryadmin.vo.pro.VoProTemp;
import com.luxuryadmin.vo.pro.VoTempForOrg;
import com.luxuryadmin.vo.pro.VoTempForPro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-01-17 14:06:46
 */
@Slf4j
@Service
public class ProTempServiceImpl implements ProTempService {

    @Resource
    private ProTempMapper proTempMapper;

    @Resource
    private OrgOrganizationTempMapper organizationTempMapper;

    @Autowired
    private ProTempProductService proTempProductService;

    @Autowired
    private OrgTempSeatService tempSeatService;

    @Override
    public List<VoProTemp> listVoProTemp(int shopId) {
        return proTempMapper.listVoProTemp(shopId);
    }

    @Override
    public void createProTemp(ProTemp proTemp) {
        proTempMapper.saveObject(proTemp);
    }


    @Override
    public void updateProTemp(ProTemp proTemp) {
        proTempMapper.updateObject(proTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProTemp(int shopId, int proTempId) {
        try {
            proTempMapper.deleteProTempByShopId(shopId, proTempId);
            proTempProductService.deleteAllProTempProductByTempId(shopId, proTempId);
        } catch (Exception e) {
            log.info("=====删除临时仓异常: shopId:{};proTempId{}", shopId, proTempId);
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public List<VoProTemp> countTempProductTotalNum(int shopId, Object[] tempIdArray) {
        if (LocalUtils.isEmptyAndNull(tempIdArray)) {
            return null;
        }
        String tempIds = LocalUtils.packString(tempIdArray);
        return proTempMapper.countTempProductTotalNum(shopId, tempIds);
    }

    @Override
    public List<VoProTemp> countTempProductSaleOutNum(int shopId, Object[] tempIdArray) {
        if (LocalUtils.isEmptyAndNull(tempIdArray)) {
            return null;
        }
        String tempIds = LocalUtils.packString(tempIdArray);
        return proTempMapper.countTempProductSaleOutNum(shopId, tempIds);
    }

    @Override
    public VoTempForOrg getVoTempForOrg(Integer id,Integer shopId) {
        VoTempForOrg voTempForOrg = proTempMapper.getVoTempForOrg(id);
        if (voTempForOrg == null){
            return  null;
        }
        List<VoTempSeatList> tempSeatLists = tempSeatService.getShopTempSeat(shopId);
        Integer productCount = organizationTempMapper.getProductCount(null,Integer.parseInt(voTempForOrg.getId()));
        voTempForOrg.setProductCount(productCount);
        voTempForOrg.setTempSeatLists(tempSeatLists);
        return voTempForOrg;
    }

    @Override
    public String getTempName(int shopId, int proId) {
        return proTempMapper.getTempName(shopId, proId);
    }

    @Override
    public List<VoTempForPro> getTempForPro(Integer shopId, Integer proId) {
        return proTempMapper.getTempForPro(shopId,proId);
    }

    @Override
    public ProTemp getTempById(Integer id) {
        return (ProTemp)proTempMapper.getObjectById(id);
    }

}
