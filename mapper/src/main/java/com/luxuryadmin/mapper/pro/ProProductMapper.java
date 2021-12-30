package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.excel.ExVoProduct;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamShopUnionForAdminBySearch;
import com.luxuryadmin.param.biz.ParamSpecificLeaguerProductQuery;
import com.luxuryadmin.param.data.ParamDataRecycleQuery;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.vo.data.VoRecycleClassifyHome;
import com.luxuryadmin.vo.data.VoRecycleProductList;
import com.luxuryadmin.vo.data.VoRecycleUserList;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.share.VoShareShopProduct;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoShpUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 对于b端用户; 一定要根据店铺id进行操作;
 *
 * @author monkey king
 */
@Mapper
public interface ProProductMapper extends BaseMapper {

    /**
     * 根据shopId和bizId查找实体
     *
     * @param shopId
     * @param bizId
     * @return
     */
    ProProduct getProProductByShopIdBizId(
            @Param("shopId") int shopId, @Param("bizId") String bizId);

    /**
     * 根据shopId和bizId查找全部实体
     *
     * @param shopId
     * @param bizId
     * @return
     */
    ProProduct getProProductForDeleteByShopIdBizId(
            @Param("shopId") int shopId, @Param("bizId") String bizId);

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
     * 根据bizId更新实体
     *
     * @param product
     * @return
     */
    int updateProProductByShopIdBizId(ProProduct product);

    /**
     * 根据商品业务id; 删除商品;把状态改成 '-90'
     *
     * @param shopId
     * @param userId 操作者id
     * @param list   业务id
     * @return
     */
    int deleteBatchProProductByShopIdBizId(
            @Param("shopId") int shopId, @Param("userId") int userId,
            @Param("list") List<String> list, @Param("deleteRemark") String deleteRemark);

    /**
     * 根据商品业务id和shopId 发布商品;把状态改成 '20';<br/>
     * 发布前状态必须在[10,19]区间
     *
     * @param shopId
     * @param userId
     * @param list
     * @return
     */
    int releaseBatchProProductByShopIdBizId(
            @Param("shopId") int shopId, @Param("userId") int userId,
            @Param("list") List<String> list);

    /**
     * 根据商品业务id和shopId 下架商品;把状态改成 '11';<br/>
     * 发布前状态必须在[20,39]区间
     *
     * @param shopId
     * @param userId
     * @param list
     * @return
     */
    int backOffBatchProProductByShopIdBizId(
            @Param("shopId") int shopId, @Param("userId") int userId,
            @Param("list") List<String> list);

    /**
     * 根据商品业务id和shopId 把质押商品存放到仓库;把状态改成 '10';
     *
     * @param shopId
     * @param userId
     * @param list
     * @return
     */
    int movePrivateProToStore(
            @Param("shopId") int shopId, @Param("userId") int userId,
            @Param("list") List<String> list);

    /**
     * 根据业务id;获取商品详情;
     *
     * @param shopId
     * @param bizId
     * @return
     */
    VoProductLoad getProductDetailByShopIdBizId(
            @Param("shopId") int shopId, @Param("bizId") String bizId);

    VoProductLoad getProductDetailByShopIdId(@Param("shopId") int shopId, @Param("autoNumber") String autoNumber);

    /**
     * 统计仓库今天上传的总数;
     *
     * @param queryParam
     * @return
     */
    Integer countTodayUpload(ParamProductQuery queryParam);

    /**
     * 统计仓库今天上架的总数;
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
     * @param queryParam 封装参数; shopId,attribute
     * @return
     */

    Map<String, String> countLeftNumAndInitPrice(ParamProductQuery queryParam);

    /**
     * 统计仓库-各分类商品-剩余库存和总成本;
     *
     * @param shopId
     * @param attributeCode 多个属性用英文逗号隔开
     * @return
     */
    List<VoProClassifyForAnalysis> countClassifyNumAndPrice(@Param("shopId") int shopId,
                                                            @Param("attributeCode") String attributeCode);

    /**
     * 统计仓库-各属性商品-剩余库存和总成本;
     *
     * @param shopId
     * @return
     */
    List<VoProClassifyForAnalysis> countAttributeNumAndPrice(int shopId);

    /**
     * 根据多个友商的shopId,获取友商的商品
     *
     * @param shopIds
     * @param queryParam
     * @return
     */
    List<VoLeaguerProduct> listBizLeaguerProduct(
            @Param("shopIds") String shopIds, @Param("param") ParamLeaguerProductQuery queryParam);


    /**
     * 根据友商的shopId,获取(具体)友商的商品
     *
     * @param queryParam
     * @return
     */
    List<VoLeaguerProduct> listSpecificBizLeaguerProduct(@Param("param") ParamSpecificLeaguerProductQuery queryParam);

    /**
     * 根据业务逻辑查找,是否存在该产品
     *
     * @param shopId 店铺id
     * @param bizId  业务逻辑id
     * @return
     */
    int existsProductByBizId(@Param("shopId") int shopId, @Param("bizId") String bizId);


    /**
     * 查询分享出去的商品列表
     *
     * @param productQuery
     * @return
     */
    List<VoProductLoad> listShareProductByProId(ParamShareProductQuery productQuery);


    /**
     * 根据分享商品的业务id;获取【分享】商品详情
     *
     * @param shopId
     * @param bizId
     * @return
     */
    VoProductLoad getShareProductDetailByShopIdBizId(
            @Param("shopId") String shopId, @Param("bizId") String bizId);


    /**
     * 条件查询商品
     *
     * @param paramProProduct
     * @return
     */
    List<VoProduct> listShpUserRel(ParamProProduct paramProProduct);

    /**
     * 条件查询商品(count查询)
     *
     * @param paramProProduct
     * @return
     */
    Long listShpUserRel_COUNT(ParamProProduct paramProProduct);

    /**
     * 根据id查询商品详情
     *
     * @param strParseInt
     * @return
     */
    VoProduct getProProductInfo(Integer strParseInt);


    /**
     * 获取友商商品详情
     *
     * @param bizId
     * @return
     */
    VoLeaguerProduct getBizLeaguerProductDetailByBizId(String bizId);

    /**
     * 根据bizId列表获取商品名称
     *
     * @param list
     * @return
     */
    List<VoProductSimple> listProdNameByBizIdList(List<String> list);

    /**
     * 根据店铺ID获取在售商品数量
     *
     * @param shopId
     * @return
     */
    Integer selectOnSaleProductNumByShopId(String shopId);

    /**
     * 根据商品bizId获取店铺ID
     *
     * @param bizId
     * @return
     */
    Integer selectShopIdByBizId(String bizId);

    /**
     * 根据店铺ID获取资产分析横屏统计数据
     * 返回以【商品类型】【属性】为维度的二维表统计数据
     *
     * @param shopId
     * @return
     */
    List<VoProductAnalyzeDetail> countProductAnalyzeDetail(@Param("shopId") int shopId);

    /**
     * 根据店铺ID获取资产分析横屏统计数据
     * 返回以【属性】为维度的统计所有【商品类型】的合计数据
     *
     * @param shopId
     * @return
     */
    List<VoProductAnalyzeDetail> countProductAnalyzeDetailAllClassify(@Param("shopId") int shopId);

    /**
     * 更新所有过期的商品状态
     *
     * @param jobDayStartTime
     */
    void updateProdExpire(Date jobDayStartTime);

    /**
     * 查询所有过期的质押商品bizId
     *
     * @param jobDayStart
     * @return
     */
    List<VoExpireProd> selectAllExpireProdBizId(Date jobDayStart);

    /**
     * 根据shopId和stateCode查询商品数量
     *
     * @param shopId
     * @param stateCode
     * @return
     */
    Integer countProductByStateCode(@Param("shopId") Integer shopId, @Param("stateCode") String stateCode);

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
     * @return
     */
    int deleteProProductByShopId(int shopId);

    /**
     * 删除店铺商品 根据商品id;in查询
     *
     * @param ids
     * @return
     */
    int deleteProProductByIds(String ids);

    /**
     * 统计用户回收成本和件数
     *
     * @param shopId
     * @param userId
     * @param proAttr
     * @param endDateTime
     * @return
     */
    Map<String, Object> countRecycleInitPriceAndNum(
            @Param("shopId") int shopId, @Param("userId") int userId, @Param("proAttr") String proAttr,
            @Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime);

    /**
     * 获取所有店铺的商品
     *
     * @return
     */
    List<ProProduct> listAllShopProProduct();

    /**
     * 根据shopId查询所有上传过的商品数量
     *
     * @param shopId
     * @return
     */
    Integer countAllUploadProductNumByShopId(@Param("shopId") Integer shopId);

    /**
     * 根据shopId查询所有在售商品数量
     *
     * @param shopId
     * @return
     */
    Integer countOnSaleProductNumByShopIdByAdmin(@Param("shopId") Integer shopId);


    /**
     * 获取临时仓里面的商品
     *
     * @param queryParam
     * @return
     */
    List<VoProductLoad> listVoProductLoadByShopIdAndTempId(ParamProTempProductQuery queryParam);

    /**
     * 一键全店上架(把未上架的商品全部上架)
     *
     * @param shopId
     * @param userId
     * @return
     */
    int oneKeyReleaseProduct(@Param("shopId") int shopId, @Param("userId") int userId);


    /**
     * 一键全店下架(把已上架的商品全部下架)
     *
     * @param shopId
     * @param userId
     * @return
     */
    int oneKeyBackOffProduct(@Param("shopId") int shopId, @Param("userId") int userId);


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
    List<VoShareShopProduct> getShareShopProductListByShopId(@Param("shopNumber") Integer shopNumber);


    /**
     * 更新分享状态
     *
     * @param shopId
     * @param share
     */
    void updateShareStare(@Param("shopId") Integer shopId, @Param("share") String share);

    /**
     * 根据价格类型查询临时仓商品列表
     *
     * @param queryParam
     * @return
     */
    List<VoProductLoad> listVoProductLoadByPriceByPrice(ParamProTempProductQuery queryParam);

    /**
     * 根据价格类型查询临时仓商品价格汇总
     *
     * @param queryParam
     * @return
     */
    Map<String, Object> getProductLoadByPriceByPrice(ParamProTempProductQuery queryParam);

    /**
     * 根据临时仓id修改商品销售价格为空
     *
     * @param proTempId
     */
    void updateSalePriceByTempId(@Param("proTempId") String proTempId);

    /**
     * 查询商品总数
     *
     * @param bizId
     * @return
     */
    Integer getShopNumById(@Param("bizId") String bizId);


    /**
     * 回收分析 --回收排行榜
     *
     * @param dataRecycleQuery
     * @return
     */
    List<VoRecycleUserList> getRecycleUserList(ParamDataRecycleQuery dataRecycleQuery);


    /**
     * 回收列表-- 回收的商品列表
     *
     * @param dataRecycleQuery
     * @return
     */
    List<VoRecycleProductList> getRecycleProductList(ParamDataRecycleQuery dataRecycleQuery);

    /**
     * 回收分析 --产品属性分析
     *
     * @param dataRecycleQuery
     * @return
     */
    List<VoRecycleClassifyHome> getRecycleClassifyHome(ParamDataRecycleQuery dataRecycleQuery);

    /**
     * 回收分析 --产品属性分析商品数量
     *
     * @param dataRecycleQuery
     * @return
     */
    Map<String,Object> getRecycleClassifyCount(ParamDataRecycleQuery dataRecycleQuery);

    /**
     * 商品删除列表
     *
     * @param productOrOrderForDeleteSearch
     * @return
     */
    List<VoProductOrOrderForDelete> getProductForDeleteList(ParamProductOrOrderForDeleteSearch productOrOrderForDeleteSearch);

    /**
     * 商品删除数量
     *
     * @param productOrOrderForDeleteSearch
     * @return
     */
    Integer getProductForDeleteCount(ParamProductOrOrderForDeleteSearch productOrOrderForDeleteSearch);

    /**
     * 获取商家联盟商品集合显示
     *
     * @param productQuery
     * @return
     */
    List<VoLeaguerProduct> getUnionForAppList(ParamLeaguerProductQuery productQuery);

    /**
     * 统计商家联盟的总商品件数和总价格 总价格库存*销售价
     *
     * @param proProduct
     * @return
     */
    Map<String, Object> getUnionForAppByStatistics(ParamProProduct proProduct);

    /**
     * 获取商家联盟商品集合显示后台
     *
     * @param proProduct
     * @return
     */
    List<VoProductLoad> getProductLoadForUnionList(ParamProProduct proProduct);

    /**
     * 获取商品总数
     *
     * @param proProduct
     * @return
     */
    //Integer getProductUnionForAdmin(ParamProProduct proProduct);


    /**
     * 直接查询商家联盟的所有商品;查询出来的结果集,需要java逻辑排除biz_shop_union.id为null的数据,再进行分页<br/>
     * 针对排序做了性能优化, 故不使用mysql分页, 使用java的业务逻辑分页;
     * 调用此方法, 不需要结合分页控件进行分页;
     *
     * @param productQuery
     * @return
     */
    List<VoLeaguerProduct> listUnionProductNoPage(ParamLeaguerProductQuery productQuery);

    /**
     * 更新商家联盟的商品状态
     *
     * @param ids
     * @param unionState 此状态不影响用户前端展示;商家联盟状态:0:不显示 | 1:显示
     * @return
     */
    int updateProUnionState(@Param("ids") String ids, @Param("unionState") String unionState);

    /**
     * 获取店铺商品的回收人员
     *
     * @param shopId
     * @return
     */
    List<VoEmployee> listRecycleUser(int shopId);

    /**
     * 临时仓转入转出记录
     *
     * @param paramTempProductMoveQuery
     * @return
     */
    List<VoMoveProductLoad> listMoveProductToTemp(ParamTempProductMoveQuery paramTempProductMoveQuery);

    /**
     * 根据商品ids获取商品信息
     *
     * @param proIds
     * @return
     */
    List<ProProduct> getProductByIds(@Param("proIds") List<Integer> proIds);

    /**
     * 集合显示传送商品
     * @param conveyProductQuery
     * @return
     */
    List<VoProductLoad> listConveyProduct(ParamConveyProductQuery conveyProductQuery);

    /**
     * 传送商品统计
     * @param conveyProductQuery
     * @return
     */
    Map<String, Object> getConveyProductPriceCensus(ParamConveyProductQuery conveyProductQuery);
}