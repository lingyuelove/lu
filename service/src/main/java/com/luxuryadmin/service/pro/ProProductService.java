package com.luxuryadmin.service.pro;

import com.github.pagehelper.Page;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.excel.ExVoProduct;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamSpecificLeaguerProductQuery;
import com.luxuryadmin.param.data.ParamDataRecycleQuery;
import com.luxuryadmin.param.data.ParamDataSaleRankQuery;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.data.VoRecycleClassifyHome;
import com.luxuryadmin.vo.data.VoRecycleProductList;
import com.luxuryadmin.vo.data.VoRecycleUserList;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.share.VoShareShopProduct;
import com.luxuryadmin.vo.shp.VoEmployee;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 对于b端用户; 一定要根据店铺id进行操作;<br>
 * 商品相关操作业务层;展示商品,上传商品,修改商品,下架商品;
 *
 * @author monkey king
 * @date 2019-12-23 16:28:50
 */
public interface ProProductService {

    /**
     * 根据产品id获取实体
     *
     * @param id
     * @return
     */
    ProProduct getProProductById(int id);


    /**
     * 根据shopId和bizId查找实体
     *
     * @param shopId
     * @param bizId
     * @return
     */
    ProProduct getProProductByShopIdBizId(int shopId, String bizId);

    ProProduct getProProductForDeleteByShopIdBizId(int shopId, String bizId);

    /**
     * 根据VoProductQueryParam来获取商品列表
     *
     * @param queryParam
     * @return
     */
    List<VoProductLoad> listProProductByVoProductQueryParam(ParamProductQuery queryParam);

    /**
     * 根据VoProductQueryParam来获取商品列表;--用作于excel导出<br/>
     * 和页面查询条件一样,显示的列不一样;
     *
     * @param queryParam
     * @return
     */
    List<ExVoProduct> listProProductByVoProductQueryParamExcelExport(ParamProductQuery queryParam);

    /**
     * 添加商品;返回商品ID
     *
     * @param pro
     * @param proDetail
     * @return
     */
    int saveProProductReturnId(ProProduct pro, ProDetail proDetail, String productClassifySunAddLists);

    /**
     * 修改商品
     *
     * @param pro
     * @param proDetail
     * @return
     */
    int updateProProduct(ProProduct pro, ProDetail proDetail, String productClassifySunAddLists);

    /**
     * 在列表修改商品价格
     *
     * @param productUploadPrice
     * @return
     */
    Integer updateProProductPrice(ParamProductUploadPrice productUploadPrice, Integer shopId, Integer userId);

    /**
     * 删除商品; 把商品状态改成"-90",已删除状态;
     *
     * @param shopId
     * @param userId
     * @param list
     * @return
     */
    int deleteBatchProProductByShopIdBizId(int shopId, int userId, List<String> list, String deleteRemark);

    /**
     * 根据业务id发布商品;(可批量)
     *
     * @param shopId
     * @param userId
     * @param list
     * @return
     */
    int releaseBatchProProductByShopIdBizId(int shopId, int userId, List<String> list);

    /**
     * 根据商品业务id和shopId 下架商品;把状态改成 '11';<br/>
     * 发布前状态必须在[20,39]区间
     *
     * @param shopId
     * @param userId
     * @param list
     * @return
     */
    int backOffBatchProProductByShopIdBizId(int shopId, int userId, List<String> list);

    /**
     * 根据商品业务id和shopId 把质押商品存放到仓库;把状态改成 '10';
     *
     * @param shopId
     * @param userId
     * @param list
     * @return
     */
    int movePrivateProToStore(int shopId, int userId, List<String> list);

    /**
     * 根据业务id;获取商品详情;
     *
     * @param shopId
     * @param bizId
     * @return
     */
    VoProductLoad getProductDetailByShopIdBizId(int shopId, String bizId);

    /**
     * 根据主键id;获取商品详情;
     * @param shopId
     * @param id
     * @return
     */
    VoProductLoad getProductDetailByShopIdId(int shopId, String id);
    /**
     * 统计仓库今天上传的总数; 已删除和质押商品 不计入其中
     *
     * @param queryParam
     * @return
     */
    Integer countTodayUpload(ParamProductQuery queryParam);

    /**
     * 统计仓库今天已上架的总数; 已删除和质押商品 不计入其中
     *
     * @param queryParam
     * @return
     */
    Integer countTodayOnRelease(ParamProductQuery queryParam);

    /**
     * 统计 质押商品 今日到期的总数;
     *
     * @param queryParam
     * @return
     */
    Integer countTodayExpire(ParamProductQuery queryParam);

    /**
     * 统计仓库的剩余库存和总成本;已删除和已售出和质押商品 不计入其中
     *
     * @param queryParam
     * @return
     */
    Map<String, String> countLeftNumAndInitPrice(ParamProductQuery queryParam);


    /**
     * 统计仓库-各分类商品-剩余库存和总成本;
     *
     * @param shopId
     * @param attributeCode 多个属性用英文逗号隔开
     * @param flag
     * @return
     */
    VoProForAnalysisShow countClassifyNumAndPrice(int shopId, String attributeCode, Boolean flag);

    /**
     * 统计仓库-各属性商品-剩余库存和总成本;
     *
     * @param shopId
     * @param flag
     * @param totalPrice
     * @return
     */
    List<VoProClassifyForAnalysis> countAttributeNumAndPrice(int shopId, Boolean flag, String totalPrice);


    /**
     * 根据多个友商的shopId,获取友商的商品
     *
     * @param shopIds
     * @param queryParam
     * @return
     */
    List<VoLeaguerProduct> listBizLeaguerProduct(String shopIds, ParamLeaguerProductQuery queryParam);

    /**
     * 根据友商的shopId,获取(具体)友商的商品
     *
     * @param queryParam
     * @return
     */
    List<VoLeaguerProduct> listSpecificBizLeaguerProduct(ParamSpecificLeaguerProductQuery queryParam);

    /**
     * 获取分享商品详情
     *
     * @param shopId
     * @param bizId
     * @return
     */
    VoProductLoad getShareProductDetailByShopIdBizId(String shopId, String bizId);

    /**
     * 获取分享商品详情
     *
     * @param bizId
     * @return
     */
    VoProductLoad getShareProductDetailByShopIdBizId(String bizId);

    /**
     * 是否存在该产品
     *
     * @param shopId 店铺id
     * @param bizId  业务逻辑id
     * @return
     */
    boolean existsProductByBizId(int shopId, String bizId);


    /**
     * 获取redis缓存上的商品数量;<br/>
     *
     * @param shopId
     * @param bizId
     * @return
     */
    VoProRedisNum getProRedisNum(int shopId, String bizId);

    /**
     * 更新redis缓存上的商品可用数量;<br/>
     * 在商品卖出时,请调用此方法更新可用库存;
     *
     * @param shopId
     * @param bizId
     * @param leftNum
     * @return
     */
    VoProRedisNum subtractProRedisLeftNum(int shopId, String bizId, int leftNum);

    /**
     * 更新redis缓存上的商品锁单数量;<br/>
     * 在商品 锁单 和 解锁 方法时,调用此方法;
     *
     * @param shopId
     * @param bizId
     * @param myLockNum
     * @param isLock
     * @return
     */
    VoProRedisNum updateProRedisLockNum(int shopId, String bizId, int myLockNum, boolean isLock);

    /**
     * 根据商品id,清除该商品的所有锁单记录;和商品的锁单redis记录<br/>
     * 发生在更新商品库存时,调用此方法;
     *
     * @param shopId
     * @param bizId
     */
    void clearProLockNumByBizId(int shopId, String bizId);

    /**
     * 条件查询商品
     *
     * @param paramProProduct
     * @return
     */
    List<VoProduct> queryShpUserRelList(ParamProProduct paramProProduct);

    /**
     * 根据id查询商品详情
     *
     * @param id
     * @return
     */
    VoProduct getProProductInfo(String id);

    /**
     * 修改商品状态 上架 下架
     *
     * @param proProduct
     */
    void updateShpShop(ProProduct proProduct);

    /**
     * 获取友商商品详情
     *
     * @param bizId
     * @return
     */
    VoLeaguerProduct getBizLeaguerProductDetailByBizId(String bizId, Integer leaguerShopId, Integer shopId, String type);


    /**
     * 获取联盟商品详情
     *
     * @param bizId
     * @param leaguerShopId
     * @return
     */
    VoLeaguerProduct getUnionProductDetailByBizId(String bizId, Integer leaguerShopId);


    /**
     * 对前端参数进行封装;
     * 1.商品图片,属性,库存;<br/>
     * 2.商品描述,商品名称,适用人群,分类,保卡(保卡年份);<br/>
     * 注意: 满足第一点,方可保存到仓库;<br/>
     * 同时满足第一,第二点,方可立即上架;<br/>
     * 在没有满足上架的条件时,隐藏上架按钮;
     *
     * @param voPro
     * @param isSave true:新增 | false:更新
     * @return
     */
    ProProduct pickProProduct(ParamProductUpload voPro, boolean isSave, Integer shopId, Integer userId);

    /**
     * 对前端参数进行封装;
     *
     * @param voPro
     * @param isSave isSave true:新增 | false:更新
     * @return
     */
    ProDetail pickProDetail(ParamProductUpload voPro, boolean isSave);

    /**
     * 删除Redis商品库存缓存
     *
     * @param shopId
     * @param bizId
     */
    void deleteProRedisLockNum(int shopId, String bizId);

    /**
     * 根据店铺ID查看【在售】商品数量
     *
     * @param shopId
     * @return
     */
    Integer getOnSaleProductNumByShopId(String shopId);

    /**
     * 根据店铺ID查看【在售】商品数量
     *
     * @param shopId
     * @return
     */
    Integer getOnSaleProductNumByShopIdByAdmin(Integer shopId);

    /**
     * 根据商品bizId获取店铺ID
     *
     * @param bizId
     * @return
     */
    Integer getShopIdByBizId(String bizId);

    /**
     * 根据店铺ID获取资产分析横屏统计数据
     * 返回以【商品类型】【属性】为维度的二维表统计数据
     *
     * @param shopId
     * @return
     */
    List<VoProductAnalyzeDetail> countProductAnalyzeDetail(int shopId);

    /**
     * 质押商品赎回
     *
     * @param proRedeem
     * @return
     */
    int redeemProduct(ProProduct proRedeem, Integer userId);

    /**
     * 任务执行时间
     *
     * @param jobDayStart
     */
    void dailyExpireProd(Date jobDayStart);

    /**
     * 根据{stateCode}查询对应商品的数量
     *
     * @param shopId
     * @param stateCode
     * @return
     */
    Integer countProductByStateCode(Integer shopId, String stateCode);

    /**
     * 获取商品列表中第一个商品的图片URL
     *
     * @param queryParam
     * @return
     */
    String getFirstProdImg(ParamProductQuery queryParam);

    /**
     * 删除店铺商品
     *
     * @param shopId
     */
    void deleteProProductByShopId(int shopId);

    /**
     * 根据商品id来删除; 用于in查询
     *
     * @param ids
     */
    void deleteProProductById(String ids);

    /**
     * 统计用户回收成本和件数
     *
     * @param shopId
     * @param userId
     * @param proAttr
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    Map<String, Object> countRecycleInitPriceAndNum(
            int shopId, int userId, String proAttr, String startDateTime, String endDateTime);

    /**
     * 获取所有店铺的商品
     *
     * @return
     */
    List<ProProduct> listAllShopProProduct();

    /**
     * 更新实体
     *
     * @param product
     */
    void updateProProduct(ProProduct product);

    /**
     * 根据shopId查询所有上传过的商品数量
     *
     * @param shopId
     * @return
     */
    Integer getAllUploadProductNumByShopId(Integer shopId);


    /**
     * 一键全店上架(把未上架的商品全部上架)
     *
     * @param shopId
     * @param userId
     */
    void oneKeyReleaseProduct(int shopId, int userId);


    /**
     * 一键全店下架(把已上架的商品全部下架)
     *
     * @param shopId
     * @param userId
     */
    void oneKeyBackOffProduct(int shopId, int userId);

    /**
     * 获取所有在售商品的bizId
     *
     * @param shopId
     * @return
     */
    List<String> listOnSaleProductBizId(int shopId);

    /**
     * 分享列表集合显示
     *
     * @param shopNumber
     * @return
     */
    List<VoShareShopProduct> getShareShopProductListByShopId(Integer shopNumber);

    /**
     * 更新分享状态
     *
     * @param shopId
     * @param share
     */
    void updateShareStare(Integer shopId, String share);

    /**
     * 回收分析 --回收排行榜
     *
     * @param dataRecycleQuery
     * @return
     */
    List<VoRecycleUserList> getRecycleUserList(ParamDataRecycleQuery dataRecycleQuery, Page page);


    /**
     * 回收列表-- 回收的商品列表
     *
     * @param dataRecycleQuery
     * @return
     */
    List<VoRecycleProductList> getRecycleProductList(ParamDataRecycleQuery dataRecycleQuery, boolean hasPermInitPrice);

    /**
     * 回收分析 --产品属性分析
     *
     * @param dataRecycleQuery
     * @return
     */
    List<VoRecycleClassifyHome> getRecycleClassifyHome(ParamDataRecycleQuery dataRecycleQuery);

    /**
     * 获取回收分析的数据
     * @param dataRecycleQuery
     * @return
     */
    Map<String,Object> getRecycleClassifyCount(ParamDataRecycleQuery dataRecycleQuery);
    /**
     * 获取已删除的商品/订单列表
     *
     * @param productOrOrderForDeleteSearch
     * @return
     */
    VoProductOrOrderForDeletePage getProductOrOrderForDeletePage(ParamProductOrOrderForDeleteSearch productOrOrderForDeleteSearch);

    /**
     * 物理删除商品
     *
     * @param productOrOrderForDelete
     * @param request
     */
    void deleteProductOrOrder(ParamProductOrOrderForDelete productOrOrderForDelete, HttpServletRequest request);

    /**
     * 编辑删除备注
     *
     * @param productOrOrderForUpdate
     * @param request
     */
    void updateProductOrOrder(ParamProductOrOrderForUpdate productOrOrderForUpdate, HttpServletRequest request);

    /**
     * 新增商品取回操作
     *
     * @param recycleProductAdd
     */
    void recycleProductAdd(ParamRecycleProductAdd recycleProductAdd, String recycleState);

    /**
     * 获取店铺商品的回收人员
     *
     * @param shopId
     * @return
     */
    List<VoEmployee> listRecycleUser(int shopId);
}
