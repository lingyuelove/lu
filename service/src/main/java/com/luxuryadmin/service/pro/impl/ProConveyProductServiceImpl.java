package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.*;
import com.luxuryadmin.mapper.pro.*;
import com.luxuryadmin.param.ord.ParamOrderCancel;
import com.luxuryadmin.param.ord.ParamOrderUpload;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProConveyProductService;
import com.luxuryadmin.service.pro.ProLockRecordService;
import com.luxuryadmin.service.pro.ProModifyRecordService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.pro.VoConveyProduct;
import com.luxuryadmin.vo.pro.VoProduct;
import com.luxuryadmin.vo.pro.VoProductLoad;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * pro_convey_product serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-22 15:05:52
 */
@Service
@Transactional
public class ProConveyProductServiceImpl implements ProConveyProductService {

    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    private ProProductService proProductService;
    /**
     * 注入dao
     */
    @Resource
    private ProConveyProductMapper proConveyProductMapper;

    @Resource
    private ProProductMapper proProductMapper;
    @Resource
    private ProDetailMapper proDetailMapper;
    @Resource
    private ProConveyMapper proConveyMapper;
    @Autowired
    private ProLockRecordService proLockRecordService;
    @Resource
    private ProLockRecordMapper proLockRecordMapper;
    @Autowired
    private ProModifyRecordService proModifyRecordService;
    @Autowired
    private ShpUserShopRefService shpUserShopRefService;


    @Override
    public VoConveyProduct listConveyProduct(ParamConveyProductQuery conveyProductQuery) {
        String defaultPrice;
        String defaultPriceNow = conveyProductQuery.getDefaultPrice();
        String conveyId = conveyProductQuery.getConveyId().toString();
        //判断redis是否有key值
        if (redisUtil.hasKey(ConstantRedisKey.getProConveyKeyByKey(conveyId))) {
            //获取接收参数---是否为登录后初次请求接口
            defaultPrice = redisUtil.get(ConstantRedisKey.getProConveyKeyByKey(conveyId));
        } else {
            defaultPrice = "tradePrice";
        }
        //判断redis默认价格类型与请求价格类型是否一致
        if (defaultPrice != null && !defaultPrice.equals(defaultPriceNow)) {
            //根据交接传送id修改商品结算售价格为空
            this.updateConveyPrice(conveyId);
            if (LocalUtils.isEmptyAndNull(defaultPriceNow)) {
                defaultPriceNow = "tradePrice";
            }
            redisUtil.set(ConstantRedisKey.getProConveyKeyByKey(conveyId), defaultPriceNow);
        }
        conveyProductQuery.setDefaultPrice(defaultPriceNow);
        List<VoProductLoad> productLoads = proProductMapper.listConveyProduct(conveyProductQuery);
        if (!LocalUtils.isEmptyAndNull(productLoads)){
            productLoads.forEach(voProductLoad -> {
                if (!LocalUtils.isEmptyAndNull(voProductLoad.getProStateName())){
                    voProductLoad.setProStateName("货主已删除");
                }else  if
                (voProductLoad.getTotalNum() ==0 ){
                    if (!LocalUtils.isEmptyAndNull(voProductLoad.getLeftNum()) &&voProductLoad.getLeftNum() >0){
                        voProductLoad.setLockState("1");
                    }else if(!LocalUtils.isEmptyAndNull(voProductLoad.getLeftNum()) &&voProductLoad.getLeftNum() ==0){
                        voProductLoad.setProState("3");
                        voProductLoad.setProStateName("寄卖传送已售罄");
                    }
                }
            });
        }
        VoConveyProduct conveyProduct = new VoConveyProduct();
        conveyProduct.setDefaultPrice(defaultPriceNow);
        //判断商品列表是否为空
        if (LocalUtils.isEmptyAndNull(productLoads)) {
            return conveyProduct;
        }
        conveyProduct.setProductLoads(productLoads);

        return conveyProduct;
    }

    /**
     * 添加寄卖传送商品
     * @param conveyProductAdd
     */
    @Override
    public void addConveyProduct(ParamConveyProductAdd conveyProductAdd) {
        //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
        ProConvey convey = proConveyMapper.getObjectById(conveyProductAdd.getConveyId());
        if (convey == null) {
            //以后优化加逻辑
            return;
        }
        //已确认状态不能继续添加商品
        if ("2".equals(convey.getSendState())){
            throw new MyException("已确认状态不能继续添加商品");
        }
        //判断商品是否为空
        if (LocalUtils.isEmptyAndNull(conveyProductAdd.getProIds())) {
            throw new MyException("暂未选择商品");
        }
        List<String> idList = Arrays.asList(conveyProductAdd.getProIds().split(","));
        conveyProductAdd.setProIdList(idList);
        proConveyProductMapper.addConveyProductList(conveyProductAdd);
        //编辑仓库商品的寄卖传送状态
        String proIds = LocalUtils.formatParamForSqlInQuery(conveyProductAdd.getProIds(), ",");
        conveyProductAdd.setProIds(proIds);
        proConveyProductMapper.updateProductConveyState(conveyProductAdd);
    }

    @Override
    public void deleteConveyProduct(ParamConveyProductUpdate conveyProductUpdate) {
        //判断商品是否为空
        if (LocalUtils.isEmptyAndNull(conveyProductUpdate.getId())) {
            throw new MyException("暂未选择商品");
        }
        List<String> idList = Arrays.asList(conveyProductUpdate.getId().split(","));
        idList.forEach(s -> {
            ProConveyProduct proConveyProduct = proConveyProductMapper.getObjectById(s);
            proConveyProductMapper.deleteObject(s);
            if (proConveyProduct != null) {
                ProProduct product = new ProProduct();
                product.setId(proConveyProduct.getFkProProductId());
                product.setConveyState("warehouse");
                proProductMapper.updateObject(product);
            }
        });
    }

    /**
     * 更新寄卖传送商品 只能在未确认是进行商品编辑
     * 更新数量时 更新的最大数量只能是仓库库存-锁单数量
     *
     * @param conveyProductUpdate
     */
    @Override
    public void updateConveyProduct(ParamConveyProductUpdate conveyProductUpdate) {
        ProConveyProduct conveyProduct = proConveyProductMapper.getObjectById(conveyProductUpdate.getId());
        if (conveyProduct == null){
            return;
        }
        //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
        ProConvey convey = proConveyMapper.getObjectById(conveyProduct.getFkProConveyId());
        if (convey == null) {
            //以后优化加逻辑
            return;
        }
        if (!"2".equals(convey.getSendState())){
            ProProduct product =(ProProduct)proProductMapper.getObjectById(conveyProduct.getFkProProductId());
            if (LocalUtils.isEmptyAndNull(product)){
                throw new MyException("商品不存在");
            }
            int count = LocalUtils.isEmptyAndNull(conveyProductUpdate.getNum()) ?0 : Integer.parseInt(conveyProductUpdate.getNum()) ;
            //查询关联锁单数量
            int lockConveyProCount = proLockRecordService.countConveyProLockNum(product.getFkShpShopId(),product.getId(),null);
            //当前编辑数量必须小于商品库存数量减去锁单数量
            int nowNum =product.getTotalNum() -lockConveyProCount;
            if (nowNum < count) {
                throw new MyException("不能超过最大可用库存");
            }

            if (count > 0 ) {
                //判断数量是否等于库存数量 当寄卖传送库存为空 寄卖传送编辑商品的数量等于仓库库存的数量 则寄卖传送数量不变
                if(LocalUtils.isEmptyAndNull(conveyProduct.getNum()) &&nowNum != count){
                    conveyProduct.setNum(count);
                }else if (!LocalUtils.isEmptyAndNull(conveyProduct.getNum())){
                    conveyProduct.setNum(count);
                }

            }
            BeanUtils.copyProperties(conveyProductUpdate, conveyProduct);
            conveyProduct.setId(Integer.parseInt(conveyProductUpdate.getId()));
            conveyProduct.setUpdateAdmin(conveyProductUpdate.getUserId());
            if (!LocalUtils.isEmptyAndNull(conveyProductUpdate.getFinishPrice())){
                conveyProduct.setFinishPrice(new BigDecimal(conveyProductUpdate.getFinishPrice()));
            }
            conveyProduct.setUpdateTime(new Date());
            proConveyProductMapper.updateObject(conveyProduct);
        }

    }

    /**
     * 寄卖传送切换价格时 进行寄卖传送商品之前编辑的价格进行清空
     * @param conveyId
     */
    @Override
    public void updateConveyPrice(String conveyId) {

        proConveyProductMapper.updateConveyPrice(Integer.parseInt(conveyId));
    }

    @Override
    public void updateConveyList(Integer conveyId) {
        String defaultPrice;

        //判断redis是否有key值
        if (redisUtil.hasKey(ConstantRedisKey.getProConveyKeyByKey(conveyId.toString()))) {
            //获取接收参数---是否为登录后初次请求接口
            defaultPrice = redisUtil.get(ConstantRedisKey.getProConveyKeyByKey(conveyId.toString()));
        } else {
            defaultPrice = "tradePrice";
        }
        proConveyProductMapper.updateConveyList(conveyId, defaultPrice);
    }

    @Override
    public void addProductForConvey(ProConvey convey,String source) {
        List<ProConveyProduct> conveyProducts = proConveyProductMapper.listConveyProduct(convey.getId());
        if (LocalUtils.isEmptyAndNull(conveyProducts)) {
            throw new MyException("该传送暂无商品，或商品无库存");
        }
        conveyProducts.forEach(conveyProduct -> {
            //寄卖传送商品确认时将转移的库存进行封存 以供之后逻辑使用
            conveyProduct.setOldNum(conveyProduct.getNum());
            //根据寄卖传送商品添加到接收方的仓库中
            Integer proId = addProductForStorehouse(conveyProduct, convey,source);
            //设置接收方的商品id
            conveyProduct.setReceiveProductId(proId);
            proConveyProductMapper.updateObject(conveyProduct);
        });
    }

    /**
     * 根据寄卖传送商品添加到接收方的仓库中
     *仓库无库存的商品不添加到对方店铺
     *添加的总数量如果之前没有设置
     * 默认是是仓库库存-锁单数量
     * 如果之前有编辑寄卖传送商品数量 则新增的是寄卖传送编辑后的数量
     * @param conveyProduct
     * @param convey
     */
    public Integer addProductForStorehouse(ProConveyProduct conveyProduct, ProConvey convey,String source) {
        Integer shopId = convey.getFkReceiveShopId();
        Integer userId = convey.getReceiveUserId();
        ProProduct product = (ProProduct)proProductMapper.getObjectById(conveyProduct.getFkProProductId().toString());
        if (product == null) {
            throw new MyException("商品不存在");
        }
        ProDetail detail = proDetailMapper.getProDetailByProId(conveyProduct.getFkProProductId());
        //新增商品
        Date date =new Date();
        ParamProductUpload paramProductUpload = new ParamProductUpload();
        paramProductUpload.setAttribute(product.getFkProAttributeCode());
        paramProductUpload.setTotalNum(conveyProduct.getNum().toString());
        paramProductUpload.setState("11");
        //判断商品详情是否存在添加商品图片
        if (!LocalUtils.isEmptyAndNull(detail)){
            paramProductUpload.setProImgUrl(detail.getProductImg());
        }
        paramProductUpload.setSmallImgUrl(product.getSmallImg());
        paramProductUpload.setName(conveyProduct.getName());
        paramProductUpload.setDescription(conveyProduct.getDescription());
        paramProductUpload.setSource("寄卖传送");
        paramProductUpload.setInitPrice(conveyProduct.getFinishPrice().toString());
        paramProductUpload.setClassify(product.getFkProClassifyCode());
        paramProductUpload.setShareState("10");
        ProProduct pro = proProductService.pickProProduct(paramProductUpload, true, shopId, userId);
        ProDetail proDetail = proProductService.pickProDetail(paramProductUpload, true);
        pro.setConveyState("convey");
        pro.setFkProAttributeCode("20");
        pro.setRecycleAdmin(userId);
        pro.setFkProConveyId(conveyProduct.getFkProConveyId());
        Integer proId = proProductService.saveProProductReturnId(pro, proDetail, paramProductUpload.getProductClassifyAddLists());
        String receiveUserName = shpUserShopRefService.getNameFromShop(shopId, userId);
        String afterValue ="提取人"+receiveUserName+","+ DateUtil.format(date)+"时间入库寄卖商品";
        //新增操作记录
        ProModifyRecord proModifyRecord = new ProModifyRecord();
        proModifyRecord.setFkShpShopId(shopId);
        proModifyRecord.setFkShpUserId(userId);
        proModifyRecord.setFkProProductId(proId);
        proModifyRecord.setType("寄卖传送");
        proModifyRecord.setAfterValue(LocalUtils.returnFormatString(afterValue));
        proModifyRecord.setInsertTime(date);
        proModifyRecord.setSource(source);
        proModifyRecordService.saveProModifyRecord(proModifyRecord);

        return proId;
    }

    /**
     * 编辑商品时检查该商品的寄卖传送状态
     * 如果进行库存的修改编辑
     * 如果该商品为发送发商品
     * 已确认状态下库存可编辑的总数量为
     * 寄卖传送发送方锁单关联的接收方锁单数量+发送方关联寄卖传送的锁单数量+寄卖传送剩余库存
     * 未确认状态下编辑库存数量
     * 如果寄卖传送的数量小于要编辑的商品的总库存 则寄卖传送库存不变
     * 反之 寄卖传送数量大于要编辑的商品总库存 则寄卖传送的商品数量跟着改变
     * @param paramProductUpload
     * @param shopId
     * @param userId
     */
    @Override
    public void checkProduct(ParamProductUpload paramProductUpload, Integer shopId, Integer userId) {

        VoProductLoad product = proProductService.getProductDetailByShopIdBizId(shopId, paramProductUpload.getBizId());

        //编辑商品不存在点击保存时提示
        if (LocalUtils.isEmptyAndNull(product)) {
            throw new MyException("该商品不存在");
        }
        //本仓仓库保存商品时不用提示提示
        if ("warehouse".equals(product.getConveyState())) {
            return;
        }
        if (LocalUtils.isEmptyAndNull(paramProductUpload.getTotalNum())) {
            paramProductUpload.setTotalNum("0");
        }

        //发送方保存商品需要判断数量 如果该商品在未确认的寄卖传送 则修改相应的库存 如果已确认 则修改的库存不能小于寄卖传送的库存
        if ("conveySend".equals(product.getConveyState())) {
            ProConveyProduct conveyProduct = proConveyProductMapper.getByProductId(product.getProId(), "send");
            //如果寄卖传送商品为空 则修改仓库商品为商品类型
            if (conveyProduct == null) {
                ProProduct proProduct = new ProProduct();
                proProduct.setId(product.getProId());
                proProduct.setConveyState("warehouse");
                proProductService.updateProProduct(proProduct);
                return;
            }
            //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
            ProConvey convey = proConveyMapper.getObjectById(conveyProduct.getFkProConveyId());
            if (convey == null) {
                //以后优化加逻辑
                return;
            }
            //查询发送方关联锁单数量
            int lockConveyProCount = proLockRecordService.countConveyProLockNum(shopId,product.getProId(),"lockType");

            //已确认转移修改库存
            if ("2".equals(convey.getSendState())) {
                //编辑商品库存为空
                if (LocalUtils.isEmptyAndNull(paramProductUpload.getTotalNum())  ) {
                    throw new MyException("该商品已在寄卖转移中存有库存，所以不能设置为空");
                }
                //查询接收关联锁单数量
                int lockReConveyProCount = proLockRecordService.countConveyProLockNum(convey.getFkReceiveShopId(),conveyProduct.getReceiveProductId(),"lock");
                if (LocalUtils.isEmptyAndNull(lockReConveyProCount)){
                    lockReConveyProCount = 0;
                }
                //查询商品编辑的库存是否大于寄卖传送的数量  寄卖传送当前库存+关联锁单库存必须大于要修改的库存
                Integer nowTotal = Integer.parseInt(paramProductUpload.getTotalNum()) - conveyProduct.getNum()-lockConveyProCount-lockReConveyProCount;
                if (nowTotal < 0) {
                    //该商品在寄卖转移中的库存+关联锁单库大于当前要修改的库存，请重新设置库存
                    throw new MyException("寄卖转移的库存大于当前库存，请重新设置库存");
                }
            } else {
                //未确认转移修改库存
                if (LocalUtils.isEmptyAndNull(conveyProduct.getNum())) {
                    return;
                }
                //编辑商品库存不为空
                Integer nowTotal = Integer.parseInt(paramProductUpload.getTotalNum()) - conveyProduct.getNum();
                if (nowTotal < 0) {
                    conveyProduct.setNum(Integer.parseInt(paramProductUpload.getTotalNum()));
                    proConveyProductMapper.updateObject(conveyProduct);
                }

            }
        }
        //接收方判断编辑信息
        if ("convey".equals(product.getConveyState())) {
            if (LocalUtils.isEmptyAndNull(paramProductUpload.getTotalNum())) {
                paramProductUpload.setTotalNum("0");
            }
            if (product.getTotalNum() != Integer.parseInt(paramProductUpload.getTotalNum()) || !product.getAttributeUs().equals(paramProductUpload.getAttribute())) {
                throw new MyException("该商品是寄卖转移接收的商品，所以不能编辑商品属性或者数量");
            }
        }
    }

    /**
     * 订单寄卖传送的新增关联如果商品为寄卖传送
     * 在已确定状态下
     * 判断是发送方还是接收方
     * 接收方：
     * 此订单关联新增锁单
     * 扣除寄卖传送的库存
     * 发送方：
     * 判断商品的仓库库存-锁单库存是否大于寄卖传送库存
     * 1.大于 判断商品的仓库库存-锁单库存-当前开单库存是否大于寄卖传送
     * 1.1当开单后剩余数量大于寄卖传送数量 不进行关联锁单
     * 1.2 当开单后剩余数量小于寄卖传送数量 只进行部分锁单
     * 1.2.1 锁单的数量为 寄卖传送数量-仓库库存-锁单库存
     * 2.其他情况
     * 2.1接收方正常进行关联锁单
     * @param ordOrder
     * @param orderUpload
     * @param request
     */
    @Override
    public void addOrderCheck(OrdOrder ordOrder, ParamOrderUpload orderUpload, HttpServletRequest request) {
        if (!LocalUtils.isEmptyAndNull(ordOrder.getFkProLockRecordId())){
            return;
        }
        VoProductLoad product = proProductService.getProductDetailByShopIdBizId(orderUpload.getShopId(), orderUpload.getBizId());
        //商品不存在开单时提示
        if (LocalUtils.isEmptyAndNull(product)) {
            throw new MyException("该商品不存在");
        }
        //仓库商品
        if ("warehouse".equals(product.getConveyState())) {
            return;
        }
        String conveyLockType = "order";
        //获取锁单数量
        int lockCount = proLockRecordService.getProductLockNumByProId(product.getProId());
        //获取可用总数量 商品总数量-锁单数量
        int totalNum = product.getTotalNum() - lockCount;
        Boolean ture = LocalUtils.isEmptyAndNull(orderUpload.getLockId());
        //判断是否为发送方的商品 是发送方
        if ("conveySend".equals(product.getConveyState())) {
            ProConveyProduct conveyProduct = proConveyProductMapper.getByProductId(product.getProId(), "send");
            //获取寄卖传送数量
            Integer conveyProductNum = conveyProduct.getNum();
            //如果寄卖传送商品为空 则修改仓库商品为商品类型
            if (conveyProduct == null) {
                ProProduct proProduct = new ProProduct();
                proProduct.setId(product.getProId());
                proProduct.setConveyState("warehouse");
                proProductService.updateProProduct(proProduct);
                return;
            }
            //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
            ProConvey convey = proConveyMapper.getObjectById(conveyProduct.getFkProConveyId());
            if (convey == null) {
                //以后优化加逻辑 清空相应的寄卖传送商品
                return;
            }

            //已确认转移  添加关联锁单
            if ("2".equals(convey.getSendState())) {
                ProProduct pro = proProductService.getProProductById(conveyProduct.getReceiveProductId());
                if (pro == null) {
                    return;
                }
                //判断商品的库存和寄卖转移的库存是否相同
                //相同 进行同步更新 不相同 判断更新
                if (totalNum == conveyProductNum) {
                    //已确认寄卖转移开单时进行复制锁单 同时减掉相应的库存
                    //判断开单是否为锁单开单 如果为锁单开单 是否关联开单
                    if (ture) {
                        //设置关联锁单下的商品数量
                        Integer num = conveyProductNum - Integer.parseInt(orderUpload.getTotalNum());
                        if (num < 0) {
                            throw new MyException("该商品库存不足");
                        }
                        //设置关联锁单
                        String lockReason = "关联锁单，该商品在发送方已开单";
                        orderUpload.setShopId(convey.getFkReceiveShopId());
                        orderUpload.setUserId(convey.getReceiveUserId().toString());
                        orderUpload.setBizId(pro.getBizId());
                        addLockRecord(orderUpload, null, request, lockReason, ordOrder.getId(), conveyLockType);
                        conveyProduct.setNum(num);
                        proConveyProductMapper.updateObject(conveyProduct);
                    }

                } else {
                    //如果商品库存大于寄卖传送库存 判断开单后商品数量是否小于寄卖传送数量
                    Integer num = totalNum;
                    //如果寄卖传送数量大于开单后剩余商品数量 并且不是锁单开单
                    if (totalNum < conveyProductNum && ture) {
                        Integer lockNum = conveyProductNum -num;
                        //设置关联锁单
                        String lockReason = "关联锁单，该商品在发送方已开单";
                        orderUpload.setShopId(convey.getFkReceiveShopId());
                        orderUpload.setUserId(convey.getReceiveUserId().toString());
                        orderUpload.setBizId(pro.getBizId());
                        orderUpload.setTotalNum(lockNum.toString());
                        addLockRecord(orderUpload, null, request, lockReason, ordOrder.getId(), conveyLockType);
                        conveyProduct.setNum(num);
                        proConveyProductMapper.updateObject(conveyProduct);
                    }
                }

            } else {
                //未确认转移修改库存
                //判断商品数量是否赋值
                if (LocalUtils.isEmptyAndNull(conveyProduct.getNum())) {
                    return;
                } else {
                    Integer num = product.getTotalNum() - conveyProduct.getNum();
                    //如果数量小于转移的数量
                    if (num < 0) {
                        conveyProduct.setNum(product.getTotalNum());
                        proConveyProductMapper.updateObject(conveyProduct);
                    }

                }


            }
        }else if
            //接收方商品 此时一定是已接收状态
        ("convey".equals(product.getConveyState())) {
            ProConveyProduct conveyProduct = proConveyProductMapper.getByProductId(product.getProId(), "receive");

            //如果寄卖传送商品为空 则修改仓库商品为商品类型
            if (conveyProduct == null) {
                ProProduct proProduct = new ProProduct();
                proProduct.setId(product.getProId());
                proProduct.setConveyState("warehouse");
                proProductService.updateProProduct(proProduct);
                return;
            }
            //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
            ProConvey convey = proConveyMapper.getObjectById(conveyProduct.getFkProConveyId());
            if (convey == null) {
                //以后优化加逻辑 清空相应的寄卖传送商品
                return;
            }
            ProProduct pro = proProductService.getProProductById(conveyProduct.getFkProProductId());
            if (pro == null) {
                return;
            }
            //获取寄卖传送数量
            Integer conveyProductNum = conveyProduct.getNum();
            //设置关联锁单下的商品数量
            Integer num = conveyProductNum - Integer.parseInt(orderUpload.getTotalNum());
            if (num < 0) {
                throw new MyException("该商品库存不足");
            }
            if (ture) {
                //设置关联锁单
                String lockReason = "关联锁单，该商品在接收方已开单";
                orderUpload.setShopId(convey.getFkSendShopId());
                orderUpload.setUserId(convey.getInsertAdmin().toString());
                orderUpload.setBizId(pro.getBizId());
                addLockRecord(orderUpload, null, request, lockReason, ordOrder.getId(), conveyLockType);
                conveyProduct.setNum(num);
                proConveyProductMapper.updateObject(conveyProduct);
            }

        }
    }

    /**
     * 方法抽离 寄卖传送关联锁单方法抽离
     * @param orderUpload
     * @param lockParam
     * @param request
     * @param lockReason
     * @param orderId
     * @param conveyLockType
     */
    public void addLockRecord(ParamOrderUpload orderUpload, ParamProductLock lockParam, HttpServletRequest request, String lockReason, Integer orderId, String conveyLockType) {

        if (lockParam == null) {
            lockParam = new ParamProductLock();
            lockParam.setPreFinishMoney(orderUpload.getLastMoney());
            lockParam.setLockReason(lockReason);
            lockParam.setLockNum(orderUpload.getTotalNum());
            lockParam.setShopId(orderUpload.getShopId());
            lockParam.setBizId(orderUpload.getBizId());
            lockParam.setConveyLockType(conveyLockType);
            Integer userId=LocalUtils.isEmptyAndNull(orderUpload.getUserId())?0:Integer.parseInt(orderUpload.getUserId());
            lockParam.setUserId(userId);
            lockParam.setSonRecordId(orderId);

        }
        int lockId = proLockRecordService.lockProductReturnId(lockParam, request);

    }

    /**
     * 寄卖传送关联锁单逻辑的处理
     * 判断商品是否参与寄卖传送
     * 不参与 跳出
     * 参与寄卖传送
     * 判断是否为发送方锁单
     * 1是发送方锁单 判断发送方的仓库数量-之前锁单数量-本次锁单数量是否大于寄卖传送数量
     * 1.1 大于等于 发送方正常锁单 不对寄卖传送接收方关联
     * 1.2 小于 接收方锁一部分商品
     * 2是接收方锁单
     * 发送方正常关联锁单
     * @param plr
     * @param proProduct
     * @param lockParam
     * @param request
     */
    @Override
    public void checkLockPro(ProLockRecord plr, ProProduct proProduct, ParamProductLock lockParam, HttpServletRequest request) {
        lockParam.setAddress(null);
        if ("warehouse".equals(proProduct.getConveyState())) {
            return;
        }
        ProConveyProduct conveyProduct = null;
        if ("conveySend".equals(proProduct.getConveyState())) {
            conveyProduct = proConveyProductMapper.getByProductId(proProduct.getId(), "send");
        }
        if ("convey".equals(proProduct.getConveyState())) {
            conveyProduct = proConveyProductMapper.getByProductId(proProduct.getId(), "receive");
            //判断此寄卖传送发送方是否已删除 已删除的寄卖传送接收方无需做处理
            ProProduct pro = proProductService.getProProductById(conveyProduct.getFkProProductId());
            if (!LocalUtils.isEmptyAndNull(pro) && pro.getFkProStateCode()<0){
                return;
            }
        }
        if (conveyProduct == null) {
            ProProduct product = new ProProduct();
            product.setId(proProduct.getId());
            product.setConveyState("warehouse");
            proProductService.updateProProduct(product);
            return;
        }
        //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
        ProConvey convey = proConveyMapper.getObjectById(conveyProduct.getFkProConveyId());
        if (convey == null) {
            //以后优化加逻辑 清空相应的寄卖传送商品
            return;
        }
        //发送方
        if ("conveySend".equals(proProduct.getConveyState())) {

            //已确认转移进行锁单 分两种情况 库存大于寄卖传送的时候锁单 库存等于寄卖传送时锁单
            if ("2".equals(convey.getSendState())) {
                //判断该商品的扣减库存是否大于剩余库存
                //获取全部锁单数量
                int lockAllCount = proLockRecordService.getProductLockNumByProId(proProduct.getId());
                int nowTotal =proProduct.getTotalNum()-lockAllCount;
                if (nowTotal>=conveyProduct.getNum()){
                    return;
                }
                //如果仓库库存数量-锁单数量 <寄卖传送数量
                //当仓库库存数量和寄卖传送数量一样 正常锁单 否则 关联一部分锁单
                if (nowTotal < conveyProduct.getNum()){
                    int lockNum =   conveyProduct.getNum()-nowTotal;
                    lockParam.setLockNum(lockNum+"");

                }
                //修改库存 修改寄卖转移库存
                lockParam.setSonRecordId(plr.getId());
                lockParam.setConveyLockType("lock");
                lockParam.setLockReason("关联锁单，该商品在发送方已锁单");
                lockParam.setShopId(convey.getFkReceiveShopId());
                lockParam.setUserId(convey.getReceiveUserId());
                ProProduct pro = proProductService.getProProductById(conveyProduct.getReceiveProductId());
                if (pro == null) {
                    return;
                }
                lockParam.setBizId(pro.getBizId());
                addLockRecord(null, lockParam, request, null, null, "lock");
                Integer num = conveyProduct.getNum() - Integer.parseInt(lockParam.getLockNum());
                if (num < 0) {
                    throw new MyException("该商品库存不足");
                }
                conveyProduct.setNum(num);
                proConveyProductMapper.updateObject(conveyProduct);
            } else {
                //未确认转移修改库存

                if (LocalUtils.isEmptyAndNull(conveyProduct.getNum())) {
                    return;
                }
                //获取锁单数量 查询库存剩余数量 跟寄卖传送数量相比较
                int lockCount = proLockRecordService.getProductLockNumByProId(proProduct.getId());
                int totalNum = proProduct.getTotalNum() - lockCount;
                if (totalNum <= 0) {
                    conveyProduct.setNum(0);
                }
                if (totalNum < conveyProduct.getNum()) {
                    conveyProduct.setNum(totalNum);
                }
                proConveyProductMapper.updateObject(conveyProduct);
            }

        }
        //接收方
        if ("convey".equals(proProduct.getConveyState())) {
            lockParam.setSonRecordId(plr.getId());
            lockParam.setConveyLockType("lock");
            lockParam.setLockReason("关联锁单，该商品在接收方已锁单");
            lockParam.setShopId(convey.getFkSendShopId());
            lockParam.setUserId(convey.getInsertAdmin());
            ProProduct pro = proProductService.getProProductById(conveyProduct.getFkProProductId());
            if (pro == null) {
                return;
            }
            lockParam.setBizId(pro.getBizId());
            addLockRecord(null, lockParam, request, null, null, "lock");
            Integer num = conveyProduct.getNum() - Integer.parseInt(lockParam.getLockNum());
            if (num < 0) {
                throw new MyException("该商品库存不足");
            }
            conveyProduct.setNum(num);
            proConveyProductMapper.updateObject(conveyProduct);
        }


    }

    /**
     * 订单退货寄卖传送关联修改
     * 判断商品是否为寄卖传送商品
     * 不是 直接退出 是 走寄卖传送关联解锁逻辑
     * 是寄卖传送商品 判断此订单是否为锁单开单
     * 1.是锁单开单 查询此订单关联的锁单 查询锁单的关联锁单 进行解锁
     * 2.正常开单 查询此订单的关联锁单 进行解锁
     * @param ordOrder
     * @param pro
     * @param request
     */
    @Override
    public void cancelOrderForConvey(OrdOrder ordOrder, ProProduct pro, HttpServletRequest request) {
        //订单开单 判断是锁单开单还是其他开单
        if (LocalUtils.isEmptyAndNull(ordOrder.getFkProLockRecordId())){
            //仓库
            if ("warehouse".equals(pro.getConveyState())) {
                return;
            }
            ProConveyProduct conveyProduct = null;
            //接收方
            if ("convey".equals(pro.getConveyState())) {
                conveyProduct = proConveyProductMapper.getByProductId(pro.getId(), "receive");
            }
            //发送方
            if ("conveySend".equals(pro.getConveyState())) {
                conveyProduct = proConveyProductMapper.getByProductId(pro.getId(), "send");
            }
            //如果未查询此寄卖传送
            if (conveyProduct == null) {
                ProProduct product = new ProProduct();
                product.setId(pro.getId());
                product.setConveyState("warehouse");
                proProductService.updateProProduct(product);
                return;
            }
            //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
            ProConvey convey = proConveyMapper.getObjectById(conveyProduct.getFkProConveyId());
            if (convey == null) {
                //以后优化加逻辑 清空相应的寄卖传送商品
                return;
            }
            if (LocalUtils.isEmptyAndNull(conveyProduct.getNum())) {
                return;
            }
            Integer num = conveyProduct.getNum() + ordOrder.getTotalNum();
            conveyProduct.setNum(num);
            proConveyProductMapper.updateObject(conveyProduct);
            //查询关联的锁单 进行解锁
            ProLockRecord lockRecord = proLockRecordMapper.getProLockRecordBySonRecordId(ordOrder.getId(), "order");
            if (lockRecord == null) {
                return;
            }
            proLockRecordService.unlockProductByUserId(lockRecord,lockRecord.getLockUserId(), lockRecord.getLockNum(),request);
        }else {
            //此订单为锁单开单 进行关联锁单查询解锁
            ProLockRecord plr = proLockRecordService.getProLockRecordById(ordOrder.getFkProLockRecordId());
            plr.setLockNum(ordOrder.getTotalNum());
            this.lockRecordForConvey(plr,request);
        }

    }

    /**
     * 锁单寄卖传送关联修改
     * 判断商品是否为寄卖传送商品
     * 不是 直接退出 是 走寄卖传送关联解锁逻辑
     * 是寄卖传送商品 是
     * 判断是否为发送方原库存锁单 未关联锁单 则退出
     * 反之 凡关联锁单商品统统进行解锁
     * @param lockRecord
     * @param request
     */
    @Override
    public void lockRecordForConvey(ProLockRecord lockRecord, HttpServletRequest request) {
        ProProduct pro = proProductService.getProProductById(lockRecord.getFkProProductId());
        if (pro == null) {
            return;
        }

        if ("warehouse".equals(pro.getConveyState())) {
            return;
        }
        ProConveyProduct conveyProduct = null;
        //接收方
        if ("convey".equals(pro.getConveyState())) {
            conveyProduct = proConveyProductMapper.getByProductId(pro.getId(), "receive");
        }
        //发送方
        if ("conveySend".equals(pro.getConveyState())) {
            conveyProduct = proConveyProductMapper.getByProductId(pro.getId(), "send");
        }
        //如果未查询此寄卖传送
        if (conveyProduct == null) {
            ProProduct product = new ProProduct();
            product.setId(pro.getId());
            product.setConveyState("warehouse");
            proProductService.updateProProduct(product);
            return;
        }
        //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
        ProConvey convey = proConveyMapper.getObjectById(conveyProduct.getFkProConveyId());
        if (convey == null) {
            //以后优化加逻辑 清空相应的寄卖传送商品
            return;
        }
        if (LocalUtils.isEmptyAndNull(conveyProduct.getNum())) {
            return;
        }


        //查询关联的锁单 进行解锁
        ProLockRecord lockRecordSon = proLockRecordMapper.getProLockRecordBySonRecordId(lockRecord.getId(), "lock");
        if (lockRecordSon == null) {
            return;
        }
        //寄卖传送库存恢复为原库存加关联锁单的库存
        Integer num = conveyProduct.getNum() + lockRecordSon.getLockNum();
        conveyProduct.setNum(num);
        proLockRecordService.unlockProductByUserId(lockRecordSon,lockRecordSon.getLockUserId(), lockRecordSon.getLockNum(),request);
        proConveyProductMapper.updateObject(conveyProduct);
    }

    /**
     * 寄卖传送获取商品详情逻辑
     * 商品显示的库存为寄卖传送的可用库存 商品详情不显示锁单和可有库存
     * 商品详情添加显示寄卖传送商品的当前状态 已删除 锁单中 已售罄
     * 如果该寄卖传送为已确认的寄卖传送
     * 发送方删除的商品不会显示 接收方会显示该商品已删除的状态 库存不进行修改
     * 显示的名称和描述均为寄卖传送的名称和描述
     * 未确认
     * 发送发不会显示状态
     * @param vo
     * @param conveyId
     * @param shopId
     */
    @Override
    public void getProductForConvey(VoProductLoad vo, String conveyId, int shopId) {
        //如果寄卖传送为空 或该寄卖传送已被删除 则删除整个寄卖传送里的所有商品
        ProConvey convey = proConveyMapper.getObjectById(conveyId);
        if (convey == null) {
            //以后优化加逻辑 清空相应的寄卖传送商品
            return;
        }
        ProConveyProduct conveyProduct = null;
        String defaultPrice;

        //判断redis是否有key值
        if (redisUtil.hasKey(ConstantRedisKey.getProConveyKeyByKey(conveyId))) {
            //获取接收参数---是否为登录后初次请求接口
            defaultPrice = redisUtil.get(ConstantRedisKey.getProConveyKeyByKey(conveyId));
        } else {
            defaultPrice = "tradePrice";
        }
        //判断寄卖传送的状态
        //ios端0 默认为空 所以单独设置状态
        if ("convey".equals(vo.getConveyState())) {
            vo.setConveyStateType("3");
            conveyProduct = proConveyProductMapper.getByProductIdAndType(vo.getProId(), "receive", defaultPrice);
            ProProduct product = (ProProduct)proProductMapper.getObjectById(conveyProduct.getFkProProductId());
            if (LocalUtils.isEmptyAndNull(product)|| product.getFkProStateCode() <0){
                vo.setProStateName("货主已删除");
            }
        } else if
            //发送方
        ("conveySend".equals(vo.getConveyState())) {
            if ("0".equals(convey.getSendState())) {
                vo.setConveyStateType("1");
            }
            if ("1".equals(convey.getSendState())) {
                vo.setConveyStateType("2");
            }
            if ("2".equals(convey.getSendState())) {
                vo.setConveyStateType("3");
            }
            conveyProduct = proConveyProductMapper.getByProductIdAndType(vo.getProId(), "send", defaultPrice);
        }
        if (LocalUtils.isEmptyAndNull(conveyProduct)) {
            return;
        }

        //无需判断是否在本店铺显示 无论是否是本店的商品  数量全部显示可用
        if ("conveySend".equals(vo.getConveyState()) && shopId != vo.getShopId()) {
            //不是本店店铺 商品状态为提取中 则寄卖传送状态为待确认
            vo.setConveyStateType("4");
        }
        vo.setDescription(conveyProduct.getDescription());
        vo.setName(conveyProduct.getName());
        vo.setShowPrice(conveyProduct.getFinishPrice().toString());
        Integer state = Integer.parseInt(vo.getStateUs());
        if (state < 0) {
            vo.setProState("2");
            vo.setProStateName("已删除");
        } else if
            //判断寄卖传送商品无库存 但仓库有库存 则为锁单
        (conveyProduct.getNum() == 0 && vo.getTotalNum() > 0) {
            vo.setProState("1");
            vo.setProStateName("锁单中");
        } else if
            //寄卖传送无库存 仓库无库存
        (conveyProduct.getNum() == 0 && vo.getTotalNum() == 0) {
            vo.setProState("3");
            vo.setProStateName("已售罄");
        } else {
            vo.setProState("0");
        }
        vo.setConveyId(conveyProduct.getFkProConveyId());
        vo.setId(conveyProduct.getId());
        vo.setTotalNum(conveyProduct.getNum());
    }
}
