package com.luxuryadmin.service.pro.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.pro.ProModifyRecord;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.mapper.pro.ProModifyRecordMapper;
import com.luxuryadmin.param.pro.ParamModifyRecordSearch;
import com.luxuryadmin.service.pro.ProModifyRecordService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.pro.VoModifyRecordByList;
import com.luxuryadmin.vo.pro.VoModifyRecordByPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 商品修改记录表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-06-03 22:01:25
 */
@Service
public class ProModifyRecordServiceImpl implements ProModifyRecordService {


    /**
     * 注入dao
     */
    @Resource
    private ProModifyRecordMapper proModifyRecordMapper;

    @Autowired
    protected ProProductService proProductService;
    @Autowired
    protected ShpUserService userService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ServicesUtil servicesUtil;

    @Override
    public List<ProModifyRecord> listProModifyRecord(ParamModifyRecordSearch modifyRecordSearch) {
        return proModifyRecordMapper.listProModifyRecord(modifyRecordSearch);
    }

    @Override
    public void saveProModifyRecord(ProModifyRecord proModifyRecord) {
        proModifyRecordMapper.saveObject(proModifyRecord);
    }

    @Override
    public VoModifyRecordByPage getModifyRecordByPage(ParamModifyRecordSearch modifyRecordSearch) {
        ProProduct proProduct = proProductService.getProProductByShopIdBizId(modifyRecordSearch.getShopId(), modifyRecordSearch.getBizId());
        if (proProduct == null) {
            return null;
        }
        modifyRecordSearch.setProId(proProduct.getId());
        VoModifyRecordByPage modifyRecordByPage = new VoModifyRecordByPage();
        List<VoModifyRecordByList> modifyRecordByLists = new ArrayList<>();
        PageHelper.startPage(modifyRecordSearch.getPageNum(), 10);
        List<ProModifyRecord> proModifyRecords = this.listProModifyRecord(modifyRecordSearch);
        if (proModifyRecords != null && proModifyRecords.size() > 0) {
            proModifyRecords.forEach(modifyRecord -> {
                VoModifyRecordByList modifyRecordByList = new VoModifyRecordByList();
                modifyRecordByList.setId(modifyRecord.getId());
                String nameFromShop = shpUserShopRefService.getNameFromShop(modifyRecordSearch.getShopId(), modifyRecord.getFkShpUserId());
                modifyRecordByList.setOperateUserName(nameFromShop);
                String attributeName = modifyRecord.getAttributeName();
                Integer userId = modifyRecordSearch.getCurrentUserId();
                Integer shopId = modifyRecordSearch.getShopId();
                boolean hasPermission = true;
                switch (attributeName+"") {
                    case "成本价":
                    case "立即上架":
                    case "保存到仓库":
                        hasPermission = servicesUtil.hasPermission(shopId, userId, ConstantPermission.CHK_PRICE_INIT);
                        break;
                    case "同行价":
                        hasPermission = servicesUtil.hasPermission(shopId, userId, ConstantPermission.CHK_PRICE_TRADE);
                        break;
                    case "代理价":
                        hasPermission = servicesUtil.hasPermission(shopId, userId, ConstantPermission.CHK_PRICE_AGENCY);
                        break;
                    case "销售价":
                        hasPermission = servicesUtil.hasPermission(shopId, userId, ConstantPermission.CHK_PRICE_SALE);
                        break;
                    case "商品备注":
                        hasPermission = servicesUtil.hasPermission(shopId, userId, ConstantPermission.CHK_PRODUCT_ATTRIBUTE);
                        break;
                    default:
                        break;
                }
                if (!hasPermission) {
                    modifyRecord.setBeforeValue("(无查看权限)");
                    modifyRecord.setAfterValue("(无查看权限)");
                }
                if ("修改".equals(modifyRecord.getType())) {

                    if ("商品视频".equals(attributeName) || "商品图片".equals(attributeName) || "保卡图片".equals(attributeName) || "备注图片".equals(attributeName)) {
                        modifyRecordByList.setOperateContent(modifyRecord.getAttributeName() + "已被修改");
                    } else {
                        modifyRecordByList.setOperateContent(attributeName + "：#修改前#" + modifyRecord.getBeforeValue() + "  #修改后#" + modifyRecord.getAfterValue());
                    }
                } else if ("解锁".equals(modifyRecord.getType()) || "锁单".equals(modifyRecord.getType())) {
                    String str = attributeName + modifyRecord.getBeforeValue();
                    modifyRecordByList.setOperateContent(str + " #" + modifyRecord.getAfterValue());
                } else if ("入库".equals(modifyRecord.getType()) && "入库数量".equals(attributeName)) {
                    modifyRecordByList.setOperateContent(attributeName + ":" + modifyRecord.getAfterValue());
                } else if ("修改锁单".equals(modifyRecord.getType())) {
                    modifyRecordByList.setOperateContent("#修改前#" + modifyRecord.getBeforeValue() + "  #修改后#" + modifyRecord.getAfterValue());
                } else if ("编辑锁单原因".equals(modifyRecord.getType())) {
                    modifyRecordByList.setOperateContent(modifyRecord.getBeforeValue() + " | " + modifyRecord.getAfterValue());
                }else if ("保存到仓库".equals(attributeName) || "立即上架".equals(attributeName)) {
                    modifyRecordByList.setOperateContent( modifyRecord.getAfterValue());
                } else {
                    modifyRecordByList.setOperateContent(modifyRecord.getType());
                }
                if (!LocalUtils.isEmptyAndNull(modifyRecord.getSource())){
                    modifyRecordByList.setOperateContent(modifyRecordByList.getOperateContent()+"(来源："+modifyRecord.getSource()+")");
                }
                modifyRecordByList.setOperateModule(modifyRecord.getType());
                modifyRecordByList.setOperateTime(DateUtil.format(modifyRecord.getInsertTime()));
                modifyRecordByLists.add(modifyRecordByList);
            });
        }
        PageInfo pageInfo = new PageInfo(proModifyRecords);
        modifyRecordByPage.setPageNum(pageInfo.getPageNum());
        modifyRecordByPage.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            modifyRecordByPage.setHasNextPage(true);
        } else {
            modifyRecordByPage.setHasNextPage(false);
        }
        modifyRecordByPage.setList(modifyRecordByLists);
        return modifyRecordByPage;
    }

    private boolean hasPermission(int shopId, int userId, String permission) {
        return servicesUtil.hasPermission(shopId, userId, permission);
    }

}
