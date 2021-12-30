package com.luxuryadmin.api.share;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.param.login.ParamIntoImg;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpShopService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 一键图片导入
 *
 * @author monkey king
 * @date 2020-08-14 22:11:58
 */
@Slf4j
@RestController
@ApiIgnore
@RequestMapping(value = "/tool", method = RequestMethod.GET)
public class DuanXiaoLiController extends ProProductBaseController {

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private OrdOrderService ordOrderService;

    private int size;

    /**
     * 获取分享产品的店铺信息和分类列表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取分享产品的店铺信息和分类列表",
            notes = "获取分享产品的分类列表",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/putDxlIntoImg")
    public BaseResult putDxlIntoImg(@RequestParam Map<String, String> params,
                                    @Valid ParamIntoImg paramIntoImg, BindingResult result) {

        servicesUtil.validControllerParam(result);
        String timestamp = paramIntoImg.getTimestamp();
        String dxlKey = "_inputImg_dxl_" + timestamp;
        String dxlValue = redisUtil.get(dxlKey);
        if (!LocalUtils.isEmptyAndNull(dxlValue)) {
            return BaseResult.defaultErrorWithMsg("后台正在导入数据,时间较长,建议退出此页面!");
        }
        long st = 0;
        long et = 0;
        try {
            //Thread.sleep(120000);
            String phone = paramIntoImg.getPhone();
            String shopNumber = paramIntoImg.getShopNumber();
            Integer shopId = shpShopService.getShopIdByPhoneAndShopNumber(phone, shopNumber);
            if (LocalUtils.isEmptyAndNull(shopId)) {
                return BaseResult.defaultErrorWithMsg("shop not exists!");
            }
            st = System.currentTimeMillis();
            log.info("=========================导入图片---开始==========================");
            String page = paramIntoImg.getPage();
            String cookie = paramIntoImg.getCookie();
            if (LocalUtils.isEmptyAndNull(cookie)) {
                return BaseResult.defaultErrorWithMsg("cookie not allowed empty!");
            }
            size = 0;
            redisUtil.setExMINUTES(dxlKey, timestamp, 60);
            int newPage = (LocalUtils.isEmptyAndNull(page) ? 1 : Integer.parseInt(page));
            //在售商品入库;
            putDuanXiaoLiIntoProduct(shopId, newPage, cookie, paramIntoImg.getToken(), paramIntoImg.getWareHouseId(), paramIntoImg.getDownOrUp());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redisUtil.delete(dxlKey);
            return BaseResult.defaultErrorWithMsg("请求失败,登录已过期 ");
        }
        et = System.currentTimeMillis();
        log.info("=========================导入图片---结束,耗时: {}ms ==========================", (et - st));
        return BaseResult.okResult("success! total time: " + (et - st) + " ms");
    }


    /**
     * 获取分享产品的店铺信息和分类列表
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取分享产品的店铺信息和分类列表",
            notes = "获取分享产品的分类列表",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/putDxlOrder")
    public BaseResult putDxlOrder(@RequestParam Map<String, String> params,
                                  @Valid ParamIntoImg paramIntoImg, BindingResult result) {

        servicesUtil.validControllerParam(result);
        String timestamp = paramIntoImg.getTimestamp();
        String dxlKey = "_inputImg_dxl_" + timestamp;
        String dxlValue = redisUtil.get(dxlKey);
        if (!LocalUtils.isEmptyAndNull(dxlValue)) {
            return BaseResult.defaultErrorWithMsg("后台正在导入数据,时间较长,建议退出此页面!");
        }
        long st = 0;
        long et = 0;
        try {
            //Thread.sleep(120000);
            String phone = paramIntoImg.getPhone();
            String shopNumber = paramIntoImg.getShopNumber();
            Integer shopId = shpShopService.getShopIdByPhoneAndShopNumber(phone, shopNumber);
            if (LocalUtils.isEmptyAndNull(shopId)) {
                return BaseResult.defaultErrorWithMsg("shop not exists!");
            }
            st = System.currentTimeMillis();
            log.info("=========================导入图片---开始==========================");
            String page = paramIntoImg.getPage();
            String cookie = paramIntoImg.getCookie();
            String token = paramIntoImg.getToken();
            if (LocalUtils.isEmptyAndNull(cookie)) {
                return BaseResult.defaultErrorWithMsg("cookie not allowed empty!");
            }
            if (LocalUtils.isEmptyAndNull(token)) {
                return BaseResult.defaultErrorWithMsg("token not allowed empty!");
            }
            size = 0;
            redisUtil.setExMINUTES(dxlKey, timestamp, 60);
            int newPage = (LocalUtils.isEmptyAndNull(page) ? 1 : Integer.parseInt(page));
            //订单迁移;
            String bossUserIdValue = redisUtil.get(ConstantRedisKey.getBossUserIdRedisKeyByShopId(shopId));
            int bossUserId = Integer.parseInt(bossUserIdValue);
            putDuanXiaoLiIntoProductAndConfirmOrder(shopId, bossUserId, newPage, cookie, token);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redisUtil.delete(dxlKey);
            return BaseResult.defaultErrorWithMsg("请求失败,登录已过期 ");
        }
        et = System.currentTimeMillis();
        log.info("=========================导入图片---结束,耗时: {}ms ==========================", (et - st));
        return BaseResult.okResult("success! total time: " + (et - st) + " ms");
    }


    private static String login(String username, String password) throws Exception {
        String host = "https://api.duanxiaoli.com";
        String path = "/api/shop/users/login.json";
        Map<String, String> bodys = new HashMap<>(16);
        bodys.put("login", username);
        bodys.put("password", password);
        HttpResponse post = AliHttpUtils.doPost(host, path, "POST", bodys, bodys, bodys);
        if (post.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(str);
            Object errcode = jsonObject.get("errcode");
            Object errmsg = jsonObject.get("errmsg");
            if ("0".equals(errcode.toString())) {
                System.out.println("=====登录成功=====");
                String token = jsonObject.get("token").toString();
                System.out.println("======token: " + token);
                return token;
            } else {
                System.out.println("=========" + errmsg + "======");
            }
        }
        return "";
    }


    private void putDuanXiaoLiIntoProduct(int sdjShopId, int page, String cookie, String token, String wareHouseId, String downOrUp) throws Exception {
        String host = "https://api.duanxiaoli.com";
        String path = "/wap/products?page=" + page + "&keyword=&search%5Bmeta_sort%5D=dates.asc&search%5Bexist_count_eq%5D=&search%5Bexist_count_gte%5D=1&need_census=&from=";
        String proState = "10";
        if (!LocalUtils.isEmptyAndNull(wareHouseId)) {
            path = "/wap/products?page=" + page + "&keyword=&search%5Bmeta_sort%5D=dates.asc&search%5Bwarehouse_id_in%5D%5B%5D=" + wareHouseId + "&search%5Bexist_count_eq%5D=&search%5Bexist_count_gte%5D=1&need_census=&from=";
        }
        if (ConstantCommon.ONE.equals(downOrUp)) {
            proState = "20";
        }
        Map<String, String> body = new HashMap<>(16);
        body.put("Referer", "http://api.duanxiaoli.com/wap/products?page=" + page + "&keyword=&search%5Bmeta_sort%5D=dates.desc&need_census=&from=");
        body.put("Cookie", cookie);
        body.put("Accept", "application/json, text/javascript, */*; q=0.01");
        body.put("X-Requested-With", "XMLHttpRequest");
        HttpResponse post = AliHttpUtils.doGet(host, path, body, body);
        int statusCode = post.getStatusLine().getStatusCode();
        log.info("===========================xl数据抓取,店铺id: {}, 当前第{}页,状态码: {}", sdjShopId, page, statusCode);
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(str);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new MyException("not JSON format! ");
            }
            Object code = jsonObject.get("code");
            if ("true".equals(code.toString())) {
                //结果集
                Object goods_list = jsonObject.get("data");
                if (!LocalUtils.isEmptyAndNull(goods_list)) {
                    JSONArray jsonArray = JSONArray.parseArray(goods_list.toString());
                    if (LocalUtils.isEmptyAndNull(jsonArray)) {
                        log.info("====数据抓取完成======");
                        return;
                    }
                    Object datasObj = jsonObject.get("datas");
                    JSONArray datas = JSONArray.parseArray(datasObj.toString());
                    for (int i = 0; i < jsonArray.size(); i++) {
                        try {
                            Object imgs = datas.get(i);
                            List<String> list = (List<String>) imgs;
                            //缩略图
                            String smallImg = list.get(0);
                            //来源
                            String source = list.get(1);
                            String[] split = source.split("·");
                            source = split[1];
                            //分类
                            String classify = list.get(2);
                            String[] classifyArray = classify.split(" ");
                            classify = classifyArray[classifyArray.length - 1];
                            //商品分类;例如: WB,ZB,XB,XX,PS,QT
                            if (!LocalUtils.isEmptyAndNull(classify)) {
                                if (classify.contains("手表") || classify.contains("名表")) {
                                    classify = "WB";
                                } else if (classify.contains("箱包")) {
                                    classify = "XB";
                                } else if (classify.contains("首饰") || classify.contains("珠宝")) {
                                    classify = "ZB";
                                } else if (classify.contains("鞋靴")) {
                                    classify = "XX";
                                } else if (classify.contains("配饰")) {
                                    classify = "PS";
                                } else {
                                    classify = "QT";
                                }
                            }
                            String proAttr = "40";
                            if (!LocalUtils.isEmptyAndNull(source)) {
                                if (source.contains("寄售")) {
                                    proAttr = "20";
                                } else if (source.contains("回收") || source.contains("新品")) {
                                    proAttr = "10";
                                }
                            }

                            JSONObject objJSON = JSONObject.parseObject(jsonArray.get(i).toString());
                            //商品描述
                            Object name = objJSON.get("name");
                            //数量
                            Object num = objJSON.get("exist_count");
                            //独立编码
                            Object unicode = objJSON.get("no");
                            //成本价
                            Object purchase_price = objJSON.get("purchase_price");
                            purchase_price = LocalUtils.isEmptyAndNull(purchase_price) ? 0 : purchase_price;
                            //同行价
                            Object peer_price = objJSON.get("peer_price");
                            peer_price = LocalUtils.isEmptyAndNull(peer_price) ? 0 : peer_price;
                            //销售价
                            Object sell_price = objJSON.get("sell_price");
                            sell_price = LocalUtils.isEmptyAndNull(sell_price) ? 0 : sell_price;
                            //专柜价直接写在备注里,不需要转换✖100
                            Object wages = objJSON.get("wages");
                            wages = LocalUtils.isEmptyAndNull(wages) ? 0 : wages;
                            Object original_no = objJSON.get("original_no");
                            Object mark = objJSON.get("market");
                            //获取商品详情图片
                            Object id = objJSON.get("id");
                            HashMap<String, Object> map = getDxlWapProDetail(token, id + "");
                            Object imgListObj = map.get("imgList");
                            ParamProductUpload param = new ParamProductUpload();
                            param.setAttribute(proAttr);
                            param.setTotalNum((LocalUtils.isEmptyAndNull(num) ? "1" : num.toString()));
                            param.setState(proState);
                            param.setClassify(classify);
                            param.setInitPrice(LocalUtils.calcNumber(purchase_price, "*", 100).toString());
                            param.setTradePrice(LocalUtils.calcNumber(peer_price, "*", 100).toString());
                            param.setSalePrice(LocalUtils.calcNumber(sell_price, "*", 100).toString());
                            param.setUniqueCode(original_no + "");
                            param.setName(name + "");
                            param.setSmallImgUrl(smallImg);
                            param.setSource(source);
                            param.setUniqueCode(unicode + "");
                            param.setRemark(source + ("0".equals(wages.toString()) ? "" : "；专柜价: " + wages));
                            if (Integer.parseInt(num.toString()) > 1) {
                                param.setRemark(param.getRemark() + ";数量: " + num);
                            }
                            param.setDescription(map.get("description").toString());
                            param.setRemark(param.getRemark() + map.get("remark").toString());

                            List<String> imgList = LocalUtils.isEmptyAndNull(imgListObj) ? null : (List<String>) imgListObj;
                            if (LocalUtils.isEmptyAndNull(imgList)) {
                                param.setProImgUrl(smallImg);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (Object img : imgList) {
                                    //获取缩略图,取第一张
                                    int index = img.toString().indexOf("?");
                                    if (index > -1) {
                                        img = img.toString().substring(0, index);
                                    }
                                    sb.append(img);
                                    sb.append(";");
                                }
                                String sbStr = sb.toString();
                                if (!LocalUtils.isEmptyAndNull(sbStr)) {
                                    sbStr = sbStr.substring(0, sbStr.length() - 1);
                                }
                                param.setProImgUrl(sbStr);
                            }

                            String proDescription = param.getDescription();
                            if (proDescription.length() >= 255) {
                                proDescription = proDescription.substring(0, 255);
                            }

                            String proRemark = param.getRemark();
                            if (proRemark.length() >= 255) {
                                proRemark = proRemark.substring(0, 255);
                            }
                            param.setDescription(proDescription);
                            param.setRemark(proRemark);
                            //-9:微商相册导入; -8:段小狸导入;
                            ProProduct pro = proProductService.pickProProduct(param, true, sdjShopId, -8);
                            ProDetail proDetail = proProductService.pickProDetail(param, true);
                            proProductService.saveProProductReturnId(pro, proDetail,null);
                        } catch (Exception e) {
                            log.error("==================段小狸相册导入图片错误: " + e.getMessage(), e);
                        }

                    }
                    size += jsonArray.size();
                    log.info("============lx数据导入: 当前页共{}条数据,总共导入: {}", jsonArray.size(), size);
                    //隔3秒翻一页,防止被鉴定为机器请求
                    Thread.sleep(300);
                    //递归调用,加载所有商品
                    page++;
                    putDuanXiaoLiIntoProduct(sdjShopId, page, cookie, token, wareHouseId, downOrUp);
                }
            }
        }
    }

    private static HashMap<String, Object> getDxlWapProDetail(String token, String dxlProId) {
        List<String> imgList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>(16);
        try {
            if ("616314".equals(dxlProId)) {
                System.out.println("======测试调试点======");
            }
            String host = "http://wap.duanxiaoli.com";
            String path = "/wap/products/" + dxlProId + "?token=" + token + "&version=5.9&source=appStore&platform=ios";
            Map<String, String> body = new HashMap<>(16);
            body.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            body.put("Upgrade-Insecure-Requests", "1");
            HttpResponse post = AliHttpUtils.doGet(host, path, body, body);
            int statusCode = post.getStatusLine().getStatusCode();
            log.info("========获取商品详情的图片,商品id: {}", dxlProId);
            if (statusCode == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(post.getEntity(), "UTF-8");
                String indexOfKey = "(\"http://dxlimg.duanxiaoli.com";
                String lastIndexOfKey = "$(\"#J_download\").click";
                int firstIndex = str.indexOf(indexOfKey);
                int lastIndex = str.indexOf(lastIndexOfKey);
                try {
                    //获取到图片的集合字符串
                    String resultStr = str.substring(firstIndex, lastIndex);
                    //防止死循环,10000次之后强制终止
                    int i = 0;
                    for (; ; ) {
                        i++;
                        int newFirstIndex = resultStr.indexOf(indexOfKey);
                        int newLastIndex = resultStr.indexOf("\");", newFirstIndex);
                        if ((newFirstIndex + 2) > newLastIndex) {
                            break;
                        }
                        String imgUrlStr = resultStr.substring(newFirstIndex + 2, newLastIndex);
                        imgList.add(imgUrlStr);
                        resultStr = resultStr.substring(newLastIndex);
                        if (newFirstIndex < 0 || i == 10000) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.error("=======获取商品详情信息失败:" + e.getMessage(), e);
                }
                map = getDxlProDetail(str);
                map.put("imgList", imgList);
            }
        } catch (Exception e) {
            log.error("=======获取商品详情图片失败:" + e.getMessage(), e);
        }
        log.info("==============图片地址: {}", imgList.toString());
        return map;
    }


    /**
     * dxl逻辑; 先获取订单列表; 获取订单详情得到商品id,根据商品id,获取商品详情,然后获取商品入库相关信息<br/>
     * sdj逻辑: 先入库,后开单;
     *
     * @throws IOException
     */
    private static void demo(String token, String proId) throws IOException {
        String url = "http://wap.duanxiaoli.com/wap/products/" + proId + "?token=" + token + "&version=5.9.2&source=appStore&platform=ios";
        log.info("url: {}", url);
        Document document = Jsoup.connect(url).get();
        //像js一样，通过标签获取title
        //System.out.println(document.getElementsByTag("title").first());
        System.out.println();
        document.select(".cbls_num").forEach(element -> {
            //System.out.println(element.select(".cbls_ado").next());});
            System.out.println(element.text());
        });
        String str = document.toString();


        String description = "";
        //商品来源方
        String indexStr2 = "<p class=\"lsline_ado\">商品来源方</p>";
        int firstIndex2 = str.indexOf(indexStr2);
        int newLastIndex2 = str.indexOf("</div>", firstIndex2);
        description = str.substring(firstIndex2 + indexStr2.length(), newLastIndex2);
        description = Jsoup.parse(description).text();
        System.err.println(description);

        //附件情况
        String indexStr3 = "<p class=\"lsline_ado\">附件情况</p>";
        int firstIndex3 = str.indexOf(indexStr3);
        int newLastIndex3 = str.indexOf("</div>", firstIndex3);
        description = str.substring(firstIndex3 + indexStr3.length(), newLastIndex3);
        description = Jsoup.parse(description).text();


        System.err.println(description);
    }

    /**
     * dxl逻辑; 先获取订单列表; 获取订单详情得到商品id,根据商品id,获取商品详情,然后获取商品入库相关信息<br/>
     * sdj逻辑: 先入库,后开单;
     *
     * @param sdjShopId
     * @param sdjBossUserId
     * @param page
     * @param cookie
     * @param token
     * @throws Exception
     */
    public void putDuanXiaoLiIntoProductAndConfirmOrder(int sdjShopId, int sdjBossUserId, int page, String cookie, String token) throws Exception {
        // cookie = "_site_session=BAh7C0kiD3Nlc3Npb25faWQGOgZFVEkiJWZiMTc4OGYxZGYyNzQwZGE4NTQ3YjliYzQ5YzAyZjYxBjsAVEkiEWxvZ2luX3NvdXJjZQY7AEZJIg1hcHBTdG9yZQY7AFRJIhFhcHBfcGxhdGZvcm0GOwBGSSIIaW9zBjsAVEkiEGFwcF92ZXJzaW9uBjsARkkiCjUuOS4yBjsAVEkiEXNob3BfdXNlcl9pZAY7AEZpAlcuSSIQX2NzcmZfdG9rZW4GOwBGSSIxcklrVE5OZXp5ai9Wd1ptVkNOUElkcE9SMW9iR1lna2FqcExlVlN1U3k2MD0GOwBG--f86d2d6b03a63525b222a6b5ca2c15d563455f6b";
        String host = "https://api.duanxiaoli.com";
        //String path = "/wap/orders?page=" + page + "&sort%5Bkey%5D=all&search%5Bproducts_brand_id_eq%5D=&from=";
        String path = "/wap/orders?page=" + page + "&sort%5Bkey%5D=all&from=";
        Map<String, String> body = new HashMap<>(16);
        // body.put("Referer", "http://api.duanxiaoli.com/wap/products?page=" + page + "&keyword=&search%5Bmeta_sort%5D=dates.desc&need_census=&from=");
        body.put("Cookie", cookie);
        body.put("Accept", "application/json, text/javascript, */*; q=0.01");
        body.put("X-Requested-With", "XMLHttpRequest");
        HttpResponse post = AliHttpUtils.doGet(host, path, body, body);
        int statusCode = post.getStatusLine().getStatusCode();
        log.info("===========================xl数据抓取[已下架],店铺id: {}, 当前第{}页,状态码: {}", sdjShopId, page, statusCode);
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(str);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new MyException("not JSON format! ");
            }
            Object code = jsonObject.get("code");
            if ("true".equals(code.toString())) {
                //结果集
                Object goods_list = jsonObject.get("data");
                if (!LocalUtils.isEmptyAndNull(goods_list)) {
                    JSONArray jsonArray = JSONArray.parseArray(goods_list.toString());
                    if (LocalUtils.isEmptyAndNull(jsonArray)) {
                        log.info("====数据抓取完成======");
                        return;
                    }
                    Object datasObj = jsonObject.get("datas");
                    JSONArray datas = JSONArray.parseArray(datasObj.toString());
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject objJSON = JSONObject.parseObject(jsonArray.get(i).toString());

                        //订单id
                        Object oid = objJSON.get("oid");
                        //开单金额(元)
                        Object amount = objJSON.get("amount");
                        amount = LocalUtils.isPrice(amount + "") ? amount : 0;
                        BigDecimal finishPrice = LocalUtils.calcNumber(amount, "*", 100);
                        //订单创建时间2020-11-11T19:10:22+08:00 需对时间进行格式化
                        Object createdAt = objJSON.get("created_at");
                        String date = createdAt.toString();
                        //开单数量
                        Object qty = objJSON.get("qty");
                        //描述
                        Object description = objJSON.get("description");


                        //dxl商品id
                        HashMap<String, String> orderDetailMap = getDxlWapOrderDetail(token, oid.toString());
                        String dxlProId = orderDetailMap.get("dxlProId");
                        ParamProductUpload paramPro = getDxlWapProDetailToOrder(token, dxlProId);
                        //-9:微商相册导入; -8:段小狸导入;
                        ProProduct pro = proProductService.pickProProduct(paramPro, true, sdjShopId, -8);
                        pro.setSaleNum(Integer.parseInt(qty.toString()));
                        pro.setFinishPrice(finishPrice);
                        String finishDateStr = date.replace("T", " ").substring(0, date.indexOf("+08:00"));
                        Date finishDate = DateUtil.parse(finishDateStr);
                        pro.setFinishTime(finishDate);
                        pro.setInsertTime(finishDate);
                        pro.setUpdateTime(finishDate);

                        String proDescription = pro.getDescription();
                        if (proDescription.length() >= 255) {
                            proDescription = proDescription.substring(0, 255);
                        }

                        String proRemark = pro.getRemark();
                        if (proRemark.length() >= 255) {
                            proRemark = proRemark.substring(0, 255);
                        }

                        pro.setDescription(proDescription);
                        pro.setRemark(proRemark);
                        ProDetail proDetail = proProductService.pickProDetail(paramPro, true);
                        proProductService.saveProProductReturnId(pro, proDetail,null);


                        OrdOrder order = new OrdOrder();
                        order.setTotalNum(Integer.parseInt(qty.toString()));
                        order.setType("其它订单");
                        order.setSaleChannel("其它");
                        order.setFinishPrice(finishPrice);
                        order.setFinishTime(finishDate);
                        order.setInsertTime(order.getFinishTime());
                        order.setUniqueCode(proDetail.getUniqueCode());
                        //销售人员ID
                        order.setFkShpUserId(sdjBossUserId);
                        order.setFkProProductId(pro.getId());
                        order.setFkShpShopId(sdjShopId);
                        order.setInsertTime(finishDate);
                        order.setUpdateTime(finishDate);
                        //记录插入者id,帮别人开单的情况下,两个userId会不一致;
                        //-9:微商相册导入; -8:段小狸导入;
                        order.setInsertAdmin(-8);
                        order.setRemark(description + orderDetailMap.get("remark"));
                        order.setAfterSaleGuarantee("");
                        ordOrderService.confirmOrder(pro, order, null);
                    }
                    size += jsonArray.size();
                    log.info("============lx数据导入: 当前页共{}条数据,总共导入: {}", jsonArray.size(), size);
                    //隔3秒翻一页,防止被鉴定为机器请求
                    Thread.sleep(300);
                    //递归调用,加载所有商品
                    page++;
                    putDuanXiaoLiIntoProductAndConfirmOrder(sdjShopId, sdjBossUserId, page, cookie, token);
                }
            }
        }
    }

    /**
     * 获取订单详情的商品id
     *
     * @param token
     * @param orderId
     * @return
     */
    private static HashMap<String, String> getDxlWapOrderDetail(String token, String orderId) {
        String resultStr = "";
        HashMap<String, String> map = new HashMap<>(16);
        try {
            String host = "http://wap.duanxiaoli.com";
            String path = "/wap/orders/" + orderId + "?token=" + token + "&version=5.9&source=appStore&platform=ios";
            Map<String, String> body = new HashMap<>(16);
            body.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            body.put("Upgrade-Insecure-Requests", "1");
            HttpResponse post = AliHttpUtils.doGet(host, path, body, body);
            int statusCode = post.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(post.getEntity(), "UTF-8");
                String indexOfKey = ";http://wap.duanxiaoli.com/wap/products/";
                int firstIndex = str.indexOf(indexOfKey);
                int newLastIndexProId = str.indexOf("&", firstIndex);
                //获取商品id
                resultStr = str.substring(firstIndex + indexOfKey.length(), newLastIndexProId);

                //段小狸商品id
                map.put("dxlProId", resultStr);

                //销售客户;
                String saleCustomer = "";
                String indexStr = "<p class=\"lsline_ado\">销售客户</p>";
                int descFirstIndex = str.indexOf(indexStr);
                if (descFirstIndex > -1) {
                    int newLastIndex = str.indexOf("</div>", descFirstIndex);
                    saleCustomer = str.substring(descFirstIndex + indexStr.length(), newLastIndex);
                    saleCustomer = ";销售客户: " + Jsoup.parse(saleCustomer).text();
                    saleCustomer = saleCustomer.replace("历史订单", "");
                }

                //结算方式
                String payWay = "";
                String indexStr1 = "<p class=\"lsline_ado\">结算方式</p>";
                int descFirstIndex1 = str.indexOf(indexStr1);
                if (descFirstIndex1 > -1) {
                    int newLastIndex = str.indexOf("</div>", descFirstIndex1);
                    payWay = str.substring(descFirstIndex1 + indexStr1.length(), newLastIndex);
                    payWay = ";结算方式: " + Jsoup.parse(payWay).text();
                }

                //销售员
                String saleUser = "";
                String indexStr4 = "<p class=\"lsline_ado\">销售员</p>";
                int descFirstIndex4 = str.indexOf(indexStr4);
                if (descFirstIndex4 > -1) {
                    int newLastIndex = str.indexOf("</div>", descFirstIndex4);
                    saleUser = str.substring(descFirstIndex4 + indexStr4.length(), newLastIndex);
                    saleUser = ";销售员: " + Jsoup.parse(saleUser).text();
                }


                //销售途径
                String indexStr3 = "<p class=\"lsline_ado\">销售途径</p>";
                int firstIndex3 = str.indexOf(indexStr3);
                String saleWay = "";
                if (firstIndex3 > -1) {
                    int newLastIndex2 = str.indexOf("</div>", firstIndex3);
                    saleWay = str.substring(firstIndex3 + indexStr3.length(), newLastIndex2);
                    saleWay = Jsoup.parse(saleWay).text();
                    saleWay = ";销售途径: " + saleWay;
                }


                //配送方式
                String sendWay = "";
                String indexStr5 = "<p class=\"lsline_ado\">配送方式</p>";
                int descFirstIndex5 = str.indexOf(indexStr5);
                if (descFirstIndex5 > -1) {
                    int newLastIndex = str.indexOf("</div>", descFirstIndex5);
                    sendWay = str.substring(descFirstIndex5 + indexStr5.length(), newLastIndex);
                    sendWay = ";配送方式: " + Jsoup.parse(sendWay).text();
                }
                map.put("remark", saleCustomer + payWay + saleUser + saleWay + sendWay);
            }
            log.info("========获取订单详情; 订单id: {},商品id: {}", orderId, resultStr);
        } catch (Exception e) {
            log.error("=======获取订单详情失败:" + e.getMessage(), e);
        }
        return map;
    }

    /**
     * 获取商品详情里的成本价,同行价,销售价,专柜价;描述,商品图片;
     *
     * @param token
     * @param dxlProId
     * @return
     */
    private static ParamProductUpload getDxlWapProDetailToOrder(String token, String dxlProId) {
        ParamProductUpload param = new ParamProductUpload();
        List<String> imgList = new ArrayList<>();
        try {
            String host = "http://wap.duanxiaoli.com";
            String path = "/wap/products/" + dxlProId + "?token=" + token + "&version=5.9&source=appStore&platform=ios";
            Map<String, String> body = new HashMap<>(16);
            body.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            body.put("Upgrade-Insecure-Requests", "1");
            HttpResponse post = AliHttpUtils.doGet(host, path, body, body);
            int statusCode = post.getStatusLine().getStatusCode();
            log.info("========获取商品详情的图片,商品id: {}", dxlProId);
            if (statusCode == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(post.getEntity(), "UTF-8");
                //解析html标签
                Document document = Jsoup.parse(str);
                //商品来源
                String source = document.select(".tab_a").first().child(1).select(".lsline_text").text();
                String proAttr = "40";
                if (source.contains("寄售")) {
                    proAttr = "20";
                } else if (source.contains("回收") || source.contains("新品")) {
                    proAttr = "10";
                }
                //名称
                String name = document.select(".tbinfo").first().child(0).text();


                if (name.contains("A757")) {
                    log.info("=====log日志开始====");
                }


                //分类
                String[] split = name.split(" ");
                String classify = split[split.length - 1];
                if (!LocalUtils.isEmptyAndNull(classify)) {
                    if (classify.contains("手表") || classify.contains("名表")) {
                        classify = "WB";
                    } else if (classify.contains("箱包")) {
                        classify = "XB";
                    } else if (classify.contains("首饰") || classify.contains("珠宝")) {
                        classify = "ZB";
                    } else if (classify.contains("鞋靴")) {
                        classify = "XX";
                    } else if (classify.contains("配饰")) {
                        classify = "PS";
                    } else {
                        classify = "QT";
                    }
                }

                Elements elements = document.select(".cbls_num");
                String initPriceStr = "0";
                String tradePriceStr = "0";
                String salePriceStr = "0";
                for (int i = 0; i < elements.size(); i++) {
                    String price = elements.get(i).text().substring(1);
                    switch (i) {
                        case 0:
                            //成本价;
                            initPriceStr = price;
                            initPriceStr = LocalUtils.isPrice(initPriceStr) ? initPriceStr : "0";
                            break;
                        case 1:
                            //同行价;
                            tradePriceStr = price;
                            tradePriceStr = LocalUtils.isPrice(tradePriceStr) ? tradePriceStr : "0";
                            break;
                        case 2:
                            //销售价;
                            salePriceStr = price;
                            salePriceStr = LocalUtils.isPrice(salePriceStr) ? salePriceStr : "0";
                            break;
                        default:
                            break;
                    }
                }


                String indexOfKey = "'http://dxlimg.duanxiaoli.com";
                String lastIndexOfKey = "$(\"#J_download\").click";
                int firstIndex = str.indexOf(indexOfKey);
                int lastIndex = str.indexOf(lastIndexOfKey);
                //获取到图片的集合字符串
                String resultStr = str.substring(firstIndex, lastIndex);
                //防止死循环,10000次之后强制终止
                int i = 0;
                for (; ; ) {
                    i++;
                    int newFirstIndex = resultStr.indexOf(indexOfKey);
                    int newLastIndex = resultStr.indexOf("';", newFirstIndex);
                    String imgUrlStr = resultStr.substring(newFirstIndex + 1, newLastIndex);
                    imgList.add(imgUrlStr);
                    resultStr = resultStr.substring(newLastIndex);
                    if (newFirstIndex < 0 || i == 10000) {
                        break;
                    }
                }
                String smallImgUrl = "";
                StringBuilder sb = new StringBuilder();
                for (Object img : imgList) {
                    //获取缩略图,取第一张
                    if (LocalUtils.isEmptyAndNull(smallImgUrl)) {
                        smallImgUrl = img.toString();
                    }
                    int index = img.toString().indexOf("?");
                    if (index > -1) {
                        img = img.toString().substring(0, index);
                    }
                    sb.append(img);
                    sb.append(";");
                }
                String sbStr = sb.toString();
                if (!LocalUtils.isEmptyAndNull(sbStr)) {
                    sbStr = sbStr.substring(0, sbStr.length() - 1);
                }

                HashMap<String, Object> map = getDxlProDetail(str);

                param.setAttribute(proAttr);
                param.setTotalNum("0");
                param.setState("43");
                param.setClassify(classify);
                param.setInitPrice(LocalUtils.calcNumber(initPriceStr, "*", 100).toString());
                param.setTradePrice(LocalUtils.calcNumber(tradePriceStr, "*", 100).toString());
                param.setSalePrice(LocalUtils.calcNumber(salePriceStr, "*", 100).toString());
                param.setName(name + "");
                param.setDescription(map.get("description").toString());
                //缩略图
                param.setSmallImgUrl(smallImgUrl);
                param.setSource(source);
                param.setRemark(map.get("remark").toString());
                //详情图
                param.setProImgUrl(sbStr);

                if (name.contains("A1107")) {
                    log.info("=====log日志开始====param: " + param.toString());
                }

            }
        } catch (Exception e) {
            log.error("=======获取商品详情图片失败:" + e.getMessage(), e);
        }
        log.info("==============图片地址: {}", imgList.toString());
        return param;
    }


    /**
     * @param str
     * @return
     */
    private static HashMap<String, Object> getDxlProDetail(String str) {

        String remark = "";
        //获取商品备注;
        String indexStr = "<p class=\"lsline_ado\">商品备注</p>";
        int descFirstIndex = str.indexOf(indexStr);
        if (descFirstIndex > -1) {
            int newLastIndex = str.indexOf("</div>", descFirstIndex);
            remark = str.substring(descFirstIndex + indexStr.length(), newLastIndex);
            remark = Jsoup.parse(remark).text();
        }

        String description = "";
        //获取营销描述
        String indexStr1 = "<p class=\"lsline_ado\">营销描述</p>";
        int descFirstIndex1 = str.indexOf(indexStr1);
        if (descFirstIndex1 > -1) {
            int newLastIndex = str.indexOf("</div>", descFirstIndex1);
            description = str.substring(descFirstIndex1 + indexStr1.length(), newLastIndex);
            description = Jsoup.parse(description).text();
        }

        //综合描述
        if (LocalUtils.isEmptyAndNull(description)) {
            String indexStr2 = "<p class=\"lsline_ado\">综合描述</p>";
            int firstIndex2 = str.indexOf(indexStr2);
            if (firstIndex2 > -1) {
                int newLastIndex2 = str.indexOf("</div>", firstIndex2);
                description = str.substring(firstIndex2 + indexStr2.length(), newLastIndex2);
                description = Jsoup.parse(description).getElementsByTag("p").get(1).text();
            }
        } else {
            //如果有营销描述, 则把附件情况也加到描述里面去
            //附件情况
            String indexStr3 = "<p class=\"lsline_ado\">附件情况</p>";
            int firstIndex3 = str.indexOf(indexStr3);
            if (firstIndex3 > -1) {
                int newLastIndex3 = str.indexOf("</div>", firstIndex3);
                String accessory = str.substring(firstIndex3 + indexStr3.length(), newLastIndex3);
                accessory = Jsoup.parse(accessory).text();
                description += ";附件情况: " + accessory;
            }
        }

        //商品来源方
        String indexStr2 = "<span class=\"lsline_from_name\">";
        int firstIndex2 = str.indexOf(indexStr2);
        String sourceUserRemark = "";
        if (firstIndex2 > -1) {
            sourceUserRemark = ";商品来源方: ";
            int newLastIndex2 = str.indexOf("</span>", firstIndex2);
            String sourceUser = str.substring(firstIndex2 + indexStr2.length(), newLastIndex2);
            sourceUser = Jsoup.parse(sourceUser).text();
            sourceUserRemark += sourceUser;
        }

        //入库人
        String indexStr3 = "<p class=\"lsline_ado\">入库人</p>";
        int firstIndex3 = str.indexOf(indexStr3);
        String uploadUserRemark = "";
        if (firstIndex3 > -1) {
            uploadUserRemark = ";入库人: ";
            int newLastIndex2 = str.indexOf("</div>", firstIndex3);
            String uploadUser = str.substring(firstIndex3 + indexStr3.length(), newLastIndex2);
            uploadUser = Jsoup.parse(uploadUser).text();
            uploadUserRemark += uploadUser;
        }

        //店铺货编
        String indexStr4 = "<p class=\"lsline_ado\">店铺货编</p>";
        int firstIndex4 = str.indexOf(indexStr4);
        String shopProNo = "";
        if (firstIndex4 > -1) {
            int newLastIndex2 = str.indexOf("</div>", firstIndex4);
            shopProNo = str.substring(firstIndex4 + indexStr4.length(), newLastIndex2);
            shopProNo = Jsoup.parse(shopProNo).text();
            shopProNo += ";店铺货编: " + shopProNo;
        }

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("remark", remark + shopProNo + sourceUserRemark + uploadUserRemark);
        map.put("description", description);
        map.put("shopProNo", shopProNo);
        return map;
    }


    public static void main(String[] args) throws Exception {
        //System.out.println(getDxlWapProDetail("e2bcc80e211849fda87e30d12409b7cb797aa9da", "262890").toString());
        //demo();
        String token = "363a81d94033f546741bcd0ce69d3277e53384ff";
        String orderId = "266254";
        // String proId = getDxlWapOrderDetail(token, orderId);
        //getDxlWapProDetailToOrder(token, proId);
        demo(token, "266254");

        //String date = "2020-11-11T19:10:22+08:00";
        //String newDate = date.replace("T", " ").substring(0, date.indexOf("+08:00"));
        //System.out.println(DateUtil.parse(newDate));

    }
}
