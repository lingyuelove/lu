package com.luxuryadmin.service.util;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.ord.OrdAddress;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProLockRecord;
import com.luxuryadmin.entity.pro.ProModifyRecord;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.param.ord.ParamOrderQuery;
import com.luxuryadmin.param.ord.ParamOrderUpload;
import com.luxuryadmin.service.pro.ProLockRecordService;
import com.luxuryadmin.service.pro.ProModifyRecordService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.vo.ord.VoOrderLoad;
import com.luxuryadmin.vo.pro.VoProRedisNum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 格式化VO实体
 *
 * @author monkey king
 * @Date 2021-04-21 21:05:33
 */
@Component
@Slf4j
public class OrdOrderBaseController extends BaseController {


    @Autowired
    private ProModifyRecordService proModifyRecordService;
    @Autowired
    protected ProProductService proProductService;
    @Autowired
    private ProLockRecordService proLockRecordService;
    /**
     * 格式化订单图片
     *
     * @param voOrderLoadList
     */
    protected void formatVoOrderLoad(List<VoOrderLoad> voOrderLoadList) {
        if (!LocalUtils.isEmptyAndNull(voOrderLoadList)) {
            String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
            for (VoOrderLoad vo : voOrderLoadList) {
                formatVoOrderLoad(vo, userPerms);
            }
        }
    }

    protected void formatVoOrderLoad(VoOrderLoad order, String userPerms) {
        String smallImg = order.getSmallImg();
        if (!LocalUtils.isEmptyAndNull(smallImg)) {
            order.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
        }
        //兼容线上版本,2.5.1版本之后可删除该代码;
        order.setAttributeCodeCn(order.getAttributeUs());
        //处理扣款凭证图片字段
        String deductVoucherImgUrl = order.getDeductVoucherImgUrl();
        List<String> deductVoucherImgUrlList = new ArrayList<>();
        order.setDeductVoucherImgUrlList(deductVoucherImgUrlList);
        if (!LocalUtils.isEmptyAndNull(deductVoucherImgUrl)) {
            String[] imgList = deductVoucherImgUrl.split(";");
            String formatDeductVoucherImgUrl = "";
            int length = imgList.length;
            int count = 0;
            for (String imgUrl : imgList) {
                count++;
                formatDeductVoucherImgUrl += servicesUtil.formatImgUrl(imgUrl);
                if (count != length) {
                    //如果不是最后一张图片，添加【分号分隔符】
                    formatDeductVoucherImgUrl += ";";
                }
                deductVoucherImgUrlList.add(servicesUtil.formatImgUrl(imgUrl));
            }
            order.setDeductVoucherImgUrl(formatDeductVoucherImgUrl);
        }
        //查看商品属性(长属性和短属性)
        order.setAttributeCn(servicesUtil.getAttributeCn(order.getAttributeUs(), true));
        order.setAttributeShortCn(servicesUtil.getAttributeCn(order.getAttributeUs(), false));
        //商品分类
        order.setClassifyCn(servicesUtil.getClassifyCn(order.getClassifyUs()));
        String appVersion = getBasicParam().getAppVersion();
        //临时兼容2.4.2版本; 其它版本改用attributeShortCn字段; 2.5.1版本上线后. 可删除此段代码
        if (!LocalUtils.isEmptyAndNull(appVersion) && "2.4.2".equals(appVersion)) {
            order.setAttributeUs(servicesUtil.getAttributeCn(order.getAttributeUs(), false));
        }
        String stateCn = order.getStateCn();
        order.setState(stateCn);
        if ("20".equals(stateCn)) {
            stateCn = "已开单";
        } else if ("-20".equals(stateCn)) {
            stateCn = "已退货";
        } else if ("-90".equals(stateCn)) {
            stateCn = "已删除";
        }
        order.setStateCn(stateCn);

        Integer saleUserId = order.getSaleUserId();
        //如果订单不是自己的,成交价才受权限控制,如果是自己的订单,则任何时候,都可以查看自己的成交价
        if (getUserId() != saleUserId) {
            //查看成交价的权限
            boolean hasPermSalePrice = hasPermWithCurrentUser(userPerms, ConstantPermission.CHK_PRICE_FINISH);
            if (!hasPermSalePrice) {
                order.setSalePrice(null);
            }
        }

        //查看成本价的权限
        boolean hasPermInitPrice = hasPermWithCurrentUser(userPerms, ConstantPermission.CHK_PRICE_INIT);
        if (!hasPermInitPrice) {
            order.setInitPrice(null);
            order.setYearRate(null);
        } else {
            //能查看成本,才能查看年化收益率;
            if (!LocalUtils.isEmptyAndNull(order.getYearRate()) && "20".equals(order.getState())) {
                order.setYearRate("年化:" + order.getYearRate() + "%");
            }
        }
        //全店通 不显示具体店铺销售人员;
        if (getShopId() == 10684) {
            order.setSaleNickname("无");
            order.setInsertNickname("无");
        }
        //判断是否具有删除权限
        boolean flag = order.getDeleteState() != null && "1".equals(order.getDeleteState())
                && order.getUpdateAdmin() != null && order.getUpdateAdmin() == getUserId();
        if (flag) {
            order.setUpdateDeleteState("1");
        } else {
            order.setUpdateDeleteState("0");
        }
    }

    protected void formatVoOrderLoad(VoOrderLoad order) {
        String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
        formatVoOrderLoad(order, userPerms);
    }

    /**
     * 格式化前端商品列表请求的查询参数;<br/>
     * 多个模糊查找用REGEXP; 多个具体值用IN
     *
     * @param queryParam
     */
    protected void formatQueryParam(ParamOrderQuery queryParam) {
        queryParam.setShopId(getShopId());
        //如果有查询名称参数
        String name = queryParam.getProName();
        if (!LocalUtils.isEmptyAndNull(name)) {
            name = name.trim();
            name = name.replaceAll("\\s+", ".*");
            queryParam.setProName(name);
        }
        //查看托管客户
        if (!LocalUtils.isEmptyAndNull(queryParam.getProName())) {
            String showProductCustomer = ConstantPermission.CHK_PRODUCT_CUSTOMER;
            String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
            if (hasPermWithCurrentUser(userPerms, showProductCustomer)) {
                queryParam.setCustomerUser(queryParam.getProName());
            }
        }
        //对多选的参数(回收人员)进行逗号分开;
        queryParam.setRecycleUserId(LocalUtils.formatParamForSqlInQuery(queryParam.getRecycleUserId()));

        //对多选的参数(销售人员)进行逗号分开;
        queryParam.setSaleUserId(LocalUtils.formatParamForSqlInQuery(queryParam.getSaleUserId()));

        //对多选的参数(销售途径)进行逗号分开;
        queryParam.setSaleChannel(LocalUtils.formatParamForSqlInQuery(queryParam.getSaleChannel()));

        //对多选的参数(订单类型)进行逗号分开;
        queryParam.setOrderType(LocalUtils.formatParamForSqlInQuery(queryParam.getOrderType()));

        //对多选的参数(商品属性)进行逗号分开;
        queryParam.setAttributeCode(LocalUtils.formatParamForSqlInQuery(queryParam.getAttributeCode()));

        //如果有分类参数
        String classifyCode = queryParam.getClassifyCode();
        if (!LocalUtils.isEmptyAndNull(classifyCode)) {
            String[] classifyCodeArray = LocalUtils.splitString(classifyCode, ";");
            StringBuffer newCode = new StringBuffer();
            for (String code : classifyCodeArray) {
                if (code.length() >= 2) {
                    code = code.substring(0, 2);
                }
                newCode.append(code).append("|");
            }
            if (newCode.toString().endsWith("|")) {
                newCode = new StringBuffer(newCode.substring(0, newCode.length() - 1));
            }
            queryParam.setClassifyCode(newCode.toString());
        }


        String sortValue = queryParam.getSortValue();
        final String desc = "DESC";
        //sortKey包含: price, time
        String sortKey = queryParam.getSortKey();
        switch (sortKey) {
            //按照价格排序
            case "price":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKey = "od.`finish_price` DESC , od.id DESC";
                } else {
                    sortKey = "od.`finish_price` ASC , od.id ASC";
                }
                break;
            //按照年化收益率排序
            case "yearRate":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKey = " CAST(IF(od.year_rate='','-99999999999999',od.year_rate) AS SIGNED) DESC, od.id DESC ";
                } else {
                    sortKey = " CAST(IF(od.year_rate='','99999999999999',od.year_rate) AS SIGNED) ASC,  od.id ASC ";
                }
                break;
            //按照时间排序或者按照默认排序;
            case "time":
            default:
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKey = "od.`finish_time` DESC , od.id DESC";
                } else {
                    sortKey = "od.`finish_time` ASC , od.id ASC";
                }
                break;
        }
        queryParam.setSortKey(sortKey);
    }
    /**
     * 针对开单时,需要判断是否解锁锁单商品来进行开单<br/>
     *
     * @param request
     * @param lockId
     * @param pro
     */
    public void lockProConfirmV262(HttpServletRequest request, String lockId, ProProduct pro) {
        ProLockRecord lockPro = proLockRecordService.getProLockRecordById(Integer.parseInt(lockId));
        if (LocalUtils.isEmptyAndNull(lockPro) || 0 != lockPro.getState()) {
            throw new MyException("找不到锁单记录!");
        }
        String bizId = lockPro.getProBizId();
        Integer openTotalNum = lockPro.getLockNum();
        VoProRedisNum proRedisNum = proProductService.getProRedisNum(lockPro.getFkShpShopId(), bizId);
        if (openTotalNum > proRedisNum.getTotalNum()) {
            throw new MyException("总库存不足,开单失败;总库存为:" + proRedisNum.getTotalNum());
        }
        //解锁全部商品权限
        boolean unlockAllProduct = hasPermWithCurrentUser(ConstantPermission.MOD_UNLOCK_PRODUCT);
        //个人解锁权限
        boolean uPermLock = hasPermWithCurrentUser(ConstantPermission.MOD_LOCK_PRODUCT);
        //可以解锁该锁单记录并开单;
        if (uPermLock || unlockAllProduct) {
            int unLockNum1 = proRedisNum.getLockNum();
            proLockRecordService.unlockProductByUserId(lockPro, getUserId(), lockPro.getLockNum(), request);
            VoProRedisNum proRedisNum2 = proProductService.getProRedisNum(lockPro.getFkShpShopId(), bizId);
            int unLockNum2 = proRedisNum2.getLockNum();
            //添加商品记录
            addProModifyRecord(pro.getId(), "解锁", "解锁前数量: " + unLockNum1, "解锁后数量: " + unLockNum2);
        } else {
            throw new MyException(EnumCode.ERROR_NO_SHOP_PERMISSION);
        }
    }
    /**
     * 封装待入库的订单实体;
     *
     * @param vo
     * @param pro 从DB查找出来的ProProduct实体;
     * @return
     */
    public OrdOrder pickOrdOrder(ParamOrderUpload vo, ProProduct pro) {
        if (LocalUtils.isEmptyAndNull(pro)) {
            throw new MyException("找不到商品信息!");
        }

        Integer saleNum = Integer.parseInt(vo.getTotalNum());
        VoProRedisNum proRedisNum = proProductService.getProRedisNum(getShopId(), vo.getBizId());
        Integer redisLeftNum = proRedisNum.getLeftNum();
        int leftNum = redisLeftNum - saleNum;
        if (leftNum < 0) {
            throw new MyException("可用库存不足");
        }
        Integer proStateCode = pro.getFkProStateCode();
        boolean isNotSale = LocalUtils.isBetween(proStateCode, 10, 39);
        if (!isNotSale) {
            throw new MyException("开单失败：商品状态在【未上架】或【已上架】才能开单！");
        }

        //用户id; 先根据用户编号去缓存查找;如果缓存没有,再到数据库查找;开单时,初始化开单人员时,把用户id和用户编号放进缓存
        int shopId = getShopId();
        OrdOrder order = new OrdOrder();
        order.setTotalNum(saleNum);
        order.setState("10");
        order.setType(vo.getOrderType());
        order.setSaleChannel(vo.getSaleChannel());
        order.setFinishPrice(new BigDecimal(vo.getFinalPrice()));
        order.setPreMoney(LocalUtils.formatBigDecimal(vo.getPreMoney()));
        order.setLastMoney(LocalUtils.formatBigDecimal(vo.getLastMoney()));
        order.setFinishTime(vo.getSaleTime());
        order.setUniqueCode(vo.getUniqueCode());
        //销售人员ID
        order.setFkShpUserId(Integer.parseInt(vo.getUserId()));
        order.setFkProProductId(pro.getId());
        order.setFkShpShopId(shopId);
        order.setInsertTime(new Date());
        //记录插入者id,帮别人开单的情况下,两个userId会不一致;
        order.setInsertAdmin(getUserId());
        order.setRemark(vo.getNote());
        order.setAfterSaleGuarantee(vo.getAfterSaleGuarantee());
        order.setDeductVoucherImgUrl(vo.getDeductVoucherImgUrl());
        order.setEntrustState(vo.getEntrustState());
        order.setEntrustRemark(vo.getEntrustRemark());
        order.setEntrustImg(vo.getEntrustImg());
        if (!LocalUtils.isEmptyAndNull(vo.getLockId())){
            order.setFkProLockRecordId(Integer.parseInt(vo.getLockId()));
        }
        return order;
    }

    /**
     * 封装待入库的地址实体类;
     *
     * @param vo
     * @return
     */
    public OrdAddress pickOrdAddress(ParamOrderUpload vo) {
        String note = vo.getNote();
        String customer = vo.getCustomer();
        String contact = vo.getContact();
        String address = vo.getAddress();
        String receiveAddress = vo.getReceiveAddress();
//        boolean isSave = LocalUtils.isEmptyAndNull(note) && LocalUtils.isEmptyAndNull(customer) &&
//                LocalUtils.isEmptyAndNull(contact) && LocalUtils.isEmptyAndNull(address);
        boolean isSave = LocalUtils.isEmptyAndNull(receiveAddress);
        if (!isSave) {
            //有一项不为空;需要添加地址
            OrdAddress ads = new OrdAddress();
            ads.setReceiveAddress(receiveAddress);
//            ads.setName(customer);
//            ads.setPhone(contact);
//            ads.setAddress(address);
            ads.setFkShpShopId(getShopId());
            ads.setInsertTime(new Date());
            if (StringUtil.isNotBlank(vo.getAddressId())) {
                ads.setId(Integer.parseInt(vo.getAddressId()));
            }
            return ads;
        }
        return null;
    }

    /**
     * 添加商品修改记录;<br/>
     * 商品的每次改动,都需要调用此方法
     *
     * @param proId
     * @param type
     * @param beforeValue
     * @param afterValue
     */
    protected void addProModifyRecord(int proId, String type, Object beforeValue, Object afterValue) {
        ProModifyRecord pmr = new ProModifyRecord();
        pmr.setFkShpShopId(getShopId());
        pmr.setFkShpUserId(getUserId());
        pmr.setFkProProductId(proId);
        pmr.setType(type);
        pmr.setBeforeValue(beforeValue.toString());
        pmr.setAfterValue(afterValue.toString());
        pmr.setInsertTime(new Date());
        pmr.setRemark("");
        if ("解锁".equals(type)) {
            pmr.setAttributeName("#开单解锁#");
        }

        proModifyRecordService.saveProModifyRecord(pmr);
    }

}
