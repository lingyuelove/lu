package com.luxuryadmin.service.ord;

import com.luxuryadmin.entity.ord.OrdShareReceipt;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.param.ord.ParamShareReceiptQuery;
import com.luxuryadmin.param.pro.ParamShareProductQuery;
import com.luxuryadmin.vo.ord.VoShareReceipt;
import com.luxuryadmin.vo.pro.VoProductLoad;
import com.luxuryadmin.vo.pro.VoShareProduct;

import java.util.List;

/**
 * 分享商品--业务逻辑层
 *
 * @author monkey king
 * @date 2020-06-10 13:53:08
 */
public interface OrdShareReceiptService {


    /**
     * 根据店铺编号,用户编号和分享批号来获取分享的产品id
     *
     * @param shareReceiptQuery
     * @return
     */
    VoShareReceipt getOrderNoByShareBatch(ParamShareReceiptQuery shareReceiptQuery);


    /**
     * 保存分享凭证记录<br/>
     *
     * @param ordShareReceipt
     * @return 返回分享批号, 如果用户有自定义则返回用户自定义的批号;反之,返回时间戳;
     */
    String saveShareReceipt(OrdShareReceipt ordShareReceipt);

}
