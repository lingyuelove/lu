package com.luxuryadmin.api.share;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.param.login.ParamIntoImg;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpShopService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一键图片导入
 *
 * @author monkey king
 * @date 2020-08-14 22:11:58
 */
@Slf4j
@RestController
@RequestMapping(value = "/tool", method = RequestMethod.GET)
@ApiIgnore
public class WsxcController extends ProProductBaseController {

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ShpShopService shpShopService;

    private int page;

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
    @GetMapping("/putIntoImg")
    public BaseResult putIntoImg(@RequestParam Map<String, String> params,
                                 @Valid ParamIntoImg paramIntoImg, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String tag = paramIntoImg.getTag();
        if (!LocalUtils.isEmptyAndNull(tag) && (!tag.startsWith("仅包含@") && !tag.startsWith("不包含@"))) {
            return BaseResult.defaultErrorWithMsg("标签格式为: 仅包含@xxx | 不包含@xxx");
        }
        String timestamp = paramIntoImg.getTimestamp();
        String wxKey = "_inputImg_wsxc_" + timestamp;
        String wxValue = redisUtil.get(wxKey);
        if (!LocalUtils.isEmptyAndNull(wxValue)) {
            return BaseResult.defaultErrorWithMsg("后台正在导入数据,时间较长,建议退出此页面!");
        }
        long st = 0;
        long et = 0;
        try {
            String username = paramIntoImg.getUsername();
            String password = paramIntoImg.getPassword();
            String phone = paramIntoImg.getPhone();
            String shopNumber = paramIntoImg.getShopNumber();
            String filterName = paramIntoImg.getFilterName();
            Map<String, String> map = login(username, password);
            String shop_id = map.get("shop_id");
            String token = map.get("token");
            Integer shopId = shpShopService.getShopIdByPhoneAndShopNumber(phone, shopNumber);
            if (LocalUtils.isEmptyAndNull(shopId)) {
                return BaseResult.defaultErrorWithMsg("shop not exists!");
            }
            if (LocalUtils.isEmptyAndNull(token)) {
                return BaseResult.defaultErrorWithMsg(map.get("msg"));
            }
            st = System.currentTimeMillis();
            log.info("=========================导入图片---开始==========================");
            page = 0;
            size = 0;
            redisUtil.setExMINUTES(wxKey, timestamp, 60);
            putImgIntoProduct(token, shop_id, shopId, "", tag, filterName);
            et = System.currentTimeMillis();
            log.info("=========================导入图片---结束,耗时: {}ms ==========================", (et - st));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            redisUtil.delete(wxKey);
        }
        return BaseResult.okResult("导入图片---结束,耗时: " + (et - st) + "ms");
    }


    /**
     * 获取分享产品的店铺信息和分类列表
     *
     * @param phone 手机号
     * @return Result
     */
    @ApiOperation(
            value = "获取分享产品的店铺信息和分类列表",
            notes = "获取分享产品的分类列表",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "phone", value = "手机号")
    })
    @RequestRequire
    @GetMapping("/getShopNumber")
    public BaseResult getShopNumber(@RequestParam String phone) throws Exception {

        if (LocalUtils.isEmptyAndNull(phone)) {
            return BaseResult.defaultErrorWithMsg("phone is not allowed empty!");
        }
        List<Map<String, String>> phoneList = shpShopService.getShopNumberByPhone(phone);
        if (LocalUtils.isEmptyAndNull(phoneList)) {
            return BaseResult.defaultErrorWithMsg("shop not exists!");
        }
        return BaseResult.okResult(phoneList);
    }


    private Map<String, String> login(String username, String password) throws Exception {
        String host = "https://www.wsxcme.com";
        String path = "/service/account/user_phone_operation.jsp?act=phone_login&client_type=ios&token=(null)&version=2710&channel=international";
        Map<String, String> bodys = new HashMap<>(16);
        bodys.put("phone_number", username);
        bodys.put("password", password);
        HttpResponse post = AliHttpUtils.doPost(host, path, "POST", bodys, bodys, bodys);
        Map<String, String> hashMap = new HashMap<>();
        if (post.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(str);
            Object errcode = jsonObject.get("errcode");
            Object errmsg = jsonObject.get("errmsg");
            if ("0".equals(errcode.toString())) {
                System.out.println("=====登录成功=====");
                String shop_id = jsonObject.get("shop_id").toString();
                String union_id = jsonObject.get("union_id").toString();
                String token = jsonObject.get("token").toString();
                System.out.println("======shop_id: " + shop_id);
                System.out.println("======union_id: " + union_id);
                System.out.println("======token: " + token);
                hashMap.put("shop_id", shop_id);
                hashMap.put("union_id", union_id);
                hashMap.put("token", token);
            } else {
                hashMap.put("msg", errmsg.toString());
                log.error("=========" + errmsg + "======");
            }
        }
        return hashMap;
    }


    private void putImgIntoProduct(String token, String shopId, int sdjShopId, String timeStamp, String tag, String filterName) throws Exception {
        page++;
        String host = "https://www.wsxcme.com";
        String path = "/service/album/get_album_themes_list.jsp?token=" + token + "&shop_id=" + shopId + "&act=single_album&time_stamp=" + timeStamp;
        Map<String, String> bodys = new HashMap<>(16);
        HttpResponse post = AliHttpUtils.doPost(host, path, "POST", bodys, bodys, bodys);
        int statusCode = post.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(str);
            Object errcode = jsonObject.get("errcode");
            Object errmsg = jsonObject.get("errmsg");
            if ("0".equals(errcode.toString())) {
                //结果集
                String result = jsonObject.get("result").toString();

                JSONObject resultJson = JSONObject.parseObject(result);
                Object goods_list = resultJson.get("goods_list");
                if (!LocalUtils.isEmptyAndNull(goods_list)) {
                    log.info("============ws数据导入: 当前第{}页,timeStamp: {}", page, timeStamp);
                    JSONArray jsonArray = JSONArray.parseArray(goods_list.toString());
                    if (LocalUtils.isEmptyAndNull(jsonArray)) {
                        log.info("====数据抓取完成======");
                        return;
                    }
                    JSONObject lastGoods = (JSONObject) jsonArray.get(jsonArray.size() - 1);
                    String lastTimestamp = lastGoods.get("time_stamp").toString();
                    for (Object obj : jsonArray) {
                        try {
                            JSONObject objJSON = JSONObject.parseObject(obj.toString());

                            if (!LocalUtils.isEmptyAndNull(tag)) {
                                String[] split = tag.split("@");
                                String tagKey = split[0];
                                String tagValue = split[1];

                                Object tags = objJSON.get("tags");

                                boolean exceptTag = false;
                                if (!LocalUtils.isEmptyAndNull(tags)) {
                                    JSONArray tagsList = JSONArray.parseArray(tags.toString());
                                    for (Object o : tagsList) {
                                        JSONObject priceJson = JSONObject.parseObject(o.toString());
                                        String tagName = priceJson.getString("tagName");

                                        if ("仅包含".equals(tagKey)) {
                                            if (!tagValue.equals(tagName)) {
                                                exceptTag = true;
                                                break;
                                            }
                                        } else if ("不包含".equals(tagKey)) {
                                            if (tagValue.equals(tagName)) {
                                                exceptTag = true;
                                                break;
                                            }
                                        }

                                    }
                                } else {
                                    if ("仅包含".equals(tagKey)) {
                                        exceptTag = true;
                                    }
                                }

                                if (exceptTag) {
                                    continue;
                                }
                            }


                            //商品描述
                            Object title = objJSON.get("title");

                            if (!LocalUtils.isEmptyAndNull(filterName) && !LocalUtils.isEmptyAndNull(title)) {
                                if (!title.toString().contains(filterName)) {
                                    continue;
                                }
                            }

                            Object imgs = objJSON.get("imgs");
                            String smallImgUrl = "";
                            StringBuilder sb = new StringBuilder();
                            if (!LocalUtils.isEmptyAndNull(imgs)) {
                                JSONArray imgList = JSONArray.parseArray(imgs.toString());
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
                            }


                            String initPrice = "0";
                            String tradePrice = "0";
                            String agencyPrice = "0";
                            String salePrice = "0";
                            String batchPrice = "";
                            Object priceArrObj = objJSON.get("priceArr");
                            if (!LocalUtils.isEmptyAndNull(priceArrObj)) {
                                JSONArray priceArrList = JSONArray.parseArray(priceArrObj.toString());
                                for (Object o : priceArrList) {
                                    JSONObject priceJson = JSONObject.parseObject(o.toString());
                                    String priceType = priceJson.getString("priceType");
                                    String price = priceJson.getString("value");
                                    try {
                                        price = LocalUtils.calcNumber(price, "*", 100).toString();
                                    } catch (Exception e) {
                                        log.error(e.getMessage(), e);
                                        price = "0";
                                    }
                                    switch (priceType) {
                                        case "1":
                                            //微商相册的拿货价
                                            initPrice = price;
                                            break;
                                        case "2":
                                            //微商相册的售价
                                            salePrice = price;
                                            break;
                                        case "3":
                                            //微商相册的批发价
                                            DecimalFormat df = new DecimalFormat(",##0.##");
                                            price = df.format(LocalUtils.calcNumber(price, "*", 0.01));
                                            batchPrice = "批发价: " + price;
                                            break;
                                        case "4":
                                            //微商相册的打包价
                                            tradePrice = price;
                                            break;
                                        case "5":
                                            //微商相册的代发价
                                            agencyPrice = price;
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }


                            ParamProductUpload param = new ParamProductUpload();
                            param.setInitPrice(initPrice);
                            param.setTradePrice(tradePrice);
                            param.setAgencyPrice(agencyPrice);
                            param.setSalePrice(salePrice);
                            param.setRemark(batchPrice);
                            param.setAttribute("40");
                            param.setTotalNum("1");
                            param.setState("10");
                            param.setClassify("QT");
                            String description = LocalUtils.isEmptyAndNull(title) ? "" : title.toString();
                            if (description.length() >= 255) {
                                description = description.substring(0, 255);
                            }
                            param.setDescription(description);
                            param.setSmallImgUrl(smallImgUrl);
                            param.setProImgUrl(sb.toString());
                            //-9:微商相册导入; -8:段小狸导入;
                            ProProduct pro = proProductService.pickProProduct(param, true, sdjShopId, -9);
                            ProDetail proDetail = proProductService.pickProDetail(param, true);
                            proProductService.saveProProductReturnId(pro, proDetail,null);
                        } catch (Exception e) {
                            log.error("==================微商相册导入图片错误: " + e.getMessage(), e);
                        }

                    }
                    size += jsonArray.size();
                    log.info("============ws数据导入: 当前页共{}条数据,总共导入: {}", jsonArray.size(), size);
                    //隔1秒翻一页,防止被鉴定为机器请求
                    Thread.sleep(1000);
                    //递归调用,加载所有商品
                    putImgIntoProduct(token, shopId, sdjShopId, lastTimestamp, tag, filterName);
                }
            } else {
                log.info("=========" + errmsg + "======");
            }
        } else {
            log.error("=========微商相册导入失败,状态码: {}======", statusCode);
        }
    }


    public static void main(String[] args) {
        //String img = "https://xcimg.szwego.com/20200622/i1592813635_8531_0.jpg?imageMogr2/auto-orient/thumbnail/!310x310r/quality/100/FORMAT/jpg";
        //System.out.println(img.toString().substring(0, img.toString().indexOf("?")));
        String description = "12345";
        if (description.length() >= 5) {
            description = description.substring(0, 5);
        }
        System.out.println(description);
    }
}
