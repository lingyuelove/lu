package com.luxuryadmin.service.pro;


import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProConvey;
import com.luxuryadmin.entity.pro.ProLockRecord;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.param.ord.ParamOrderCancel;
import com.luxuryadmin.param.ord.ParamOrderDelete;
import com.luxuryadmin.param.ord.ParamOrderUpload;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.pro.VoConveyProduct;
import com.luxuryadmin.vo.pro.VoProduct;
import com.luxuryadmin.vo.pro.VoProductLoad;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *pro_convey_product service
 *@author zhangsai
 *@Date 2021-11-22 15:05:52
 */
public interface ProConveyProductService {

    /**
     * 集合显示传送商品
     * @param conveyProductQuery
     * @return
     */
    VoConveyProduct listConveyProduct(ParamConveyProductQuery conveyProductQuery);

    /**
     * 新增传送商品
     * @param conveyProductAdd
     */
    void addConveyProduct(ParamConveyProductAdd conveyProductAdd);

    /**
     * 删除传送商品
     * @param conveyProductUpdate
     */
    void deleteConveyProduct(ParamConveyProductUpdate conveyProductUpdate);

    /**
     * 编辑寄卖传送商品
     * @param conveyProductUpdate
     */
    void updateConveyProduct(ParamConveyProductUpdate conveyProductUpdate);

    /**
     * 修改某寄卖传送的结算价置空
     * @param conveyId
     */
    void updateConveyPrice( String conveyId);


    /**
     * 编辑寄卖传送商品列表的价格 数量
     * @param conveyId
     */
    void updateConveyList( Integer conveyId);

    /**
     * 寄卖传送商品列表添加至接收方的仓库
     * @param convey
     */
    void addProductForConvey( ProConvey convey,String source);

    /**
     * 检查商品是否是寄卖传送商品 是否能够更新
     * @param paramProductUpload
     * @param shopId
     * @param userId
     */
    void checkProduct(ParamProductUpload paramProductUpload, Integer shopId, Integer userId);

    /**
     * 判断订单进行新增操作的关联操作
     * @param ordOrder
     * @param orderUpload
     * @param request
     */
    void addOrderCheck(OrdOrder ordOrder, ParamOrderUpload orderUpload, HttpServletRequest request);

    /**
     * 检查锁单商品
     * @param plr
     * @param proProduct
     * @param lockParam
     * @param request
     */
    void checkLockPro(ProLockRecord plr, ProProduct proProduct,ParamProductLock lockParam, HttpServletRequest request);

    /**
     * 根据删除的订单关联解锁
     * @param ordOrder
     * @param pro
     */
    void cancelOrderForConvey( OrdOrder ordOrder,ProProduct pro, HttpServletRequest request);

    /**
     * 关联解锁
     * @param lockRecord
     */
    void lockRecordForConvey( ProLockRecord lockRecord, HttpServletRequest request);

    /**
     * 获取商品详情已售出已售罄的状态 商品寄卖传送状态等
     * @param vo
     * @param conveyId
     */
    void getProductForConvey(VoProductLoad vo,String conveyId,int shopId);
}
