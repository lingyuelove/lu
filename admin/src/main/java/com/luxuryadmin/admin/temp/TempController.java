package com.luxuryadmin.admin.temp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.ImageCode;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.aliyun.AliHttpUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProPublic;
import com.luxuryadmin.mapper.shp.ShpUserMapper;
import com.luxuryadmin.mapper.temp.TempMapper;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.login.ParamUsername;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.pro.ProPublicService;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.service.sys.SysConfigService;
import com.luxuryadmin.vo.temp.TempForUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * ??????,???????????????????????????????????????;<br/>
 * ????????????;??????: ?????????????????????,?????????????????????????????????;
 *
 * @author qwy
 * @date 2021-04-15 03:16:37
 */
@Slf4j
@RestController
@RequestMapping(value = "/temp")
@Api(tags = {"ZZZ.????????????????????????"}, description = "/temp | ??????,??????????????????????????????????????? ")
public class TempController extends BaseController {


    @Resource
    private TempMapper tempMapper;
    @Resource
    private ShpUserMapper shpUserMapper;
    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private ShpUserNumberService shpUserNumberService;

    @Autowired
    private ShpShopNumberService shpShopNumberService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ProPublicService proPublicService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    @Autowired
    private ShpPermTplService shpPermTplService;



    /**
     * ??????????????????????????????;<br/>
     * ?????? ????????????, ???????????????,????????????
     *
     * @return Result
     */
    @ApiOperation(
            value = "1.??????App????????????",
            notes = "???????????????????????????????????????;????????????????????????;?????????AES????????????;<br/>",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "startNum", value = "????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "endNum", value = "????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "st", value = "????????????"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "appVersion", value = "?????????"),
    })
    @RequestMapping(value = "/getKindOfCountData", method = RequestMethod.GET)
    public BaseResult getKindOfCountData(
            @Param("startNum") String startNum, @Param("endNum") String endNum,
            @Param("st") String st) {

        if (LocalUtils.isEmptyAndNull(startNum)) {
            startNum = "0";
        }

        if (LocalUtils.isEmptyAndNull(endNum)) {
            endNum = "999999";
        }

        //???????????????, ???????????????, ?????????;
        List<HashMap<String, String>> registerMap = tempMapper.listRegisterNum();
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("registerList", registerMap);

        //??????????????????,??????>0
        HashMap<String, String> initPriceNotNullMap = tempMapper.countOnSaleInitPriceNotNull();
        hashMap.put("initPriceNotNull", initPriceNotNullMap);

        //??????????????????(??????????????????????????????)
        HashMap<String, String> onSaleMap = tempMapper.countOnSale();
        hashMap.put("onSale", onSaleMap);

        //?????????????????????????????????
        HashMap<String, String> countRegister = tempMapper.countRegisterUserAndShop();
        hashMap.put("registerUserAndShop", countRegister);

        //?????????????????????????????????;(??????>0)
        List<HashMap<String, String>> proClassifyInitPriceNotNull = tempMapper.countOnSaleProClassify(true);
        hashMap.put("classifyOnSalePrice", proClassifyInitPriceNotNull);

        //?????????????????????????????????;(??????????????????????????????)
        List<HashMap<String, String>> proClassifyMap = tempMapper.countOnSaleProClassify(false);
        hashMap.put("classifyOnSaleAll", proClassifyMap);

        //???????????????????????????
        List<HashMap<String, String>> activityMap = tempMapper.listActivityUser(st, Integer.parseInt(startNum), Integer.parseInt(endNum));
        hashMap.put("activityUser", activityMap);
        return BaseResult.okResult(hashMap);
    }


    /**
     * ????????????????????????
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "????????????????????????",
            notes = "????????????????????????",
            httpMethod = "GET")
    @RequestMapping(value = "/extendTryEndTime", method = RequestMethod.GET)
    public BaseResult extendTryEndTime(@RequestParam String token, String shopNumber, String tryEndTime) {
        validPerm(token);
        Integer shopId = shpShopService.getShopIdByShopNumber(shopNumber);
        if (LocalUtils.isEmptyAndNull(shopId)) {
            return BaseResult.defaultErrorWithMsg("shop not exists");
        }
        Date date;
        try {
            date = DateUtil.parseShort(tryEndTime);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg("[tryEndTime] format exception! " + tryEndTime);
        }
        String newTryEndTime = DateUtil.formatShort(date);
        if (!newTryEndTime.equals(tryEndTime)) {
            return BaseResult.defaultErrorWithMsg("[tryEndTime] format exception! " + tryEndTime);
        }
        if (date.before(new Date())) {
            return BaseResult.defaultErrorWithMsg("????????????????????????????????????;");
        }
        //????????????;
        shpShopService.extendTryEndTime(shopId, date);
        return BaseResult.okResult(tryEndTime);
    }

    /**
     * ??????????????????
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "??????????????????",
            notes = "??????????????????",
            httpMethod = "GET")
    @RequestMapping(value = "/testUpload", method = RequestMethod.GET)
    public BaseResult testUpload(@RequestParam String token) throws IOException {
        validPerm(token);

        String result = OSSUtil.uploadNetworkFlows("public/123.png", "https://www.xmaibu.com/index/goods/img?model=IW357408&brand=IWC");
        String s = AliHttpUtils.doGet("https://www.xmaibu.com/index/goods/img?model=IW357408&brand=IWC", "");
        String location = json("");
        System.out.println("location====: " + location);
        return BaseResult.okResult(location);
    }


    /**
     * ??????????????????
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "??????????????????",
            notes = "??????????????????",
            httpMethod = "GET")
    @RequestMapping(value = "/testUpload1", method = RequestMethod.GET)
    public void testUpload1(@RequestParam String token, HttpServletResponse response) throws IOException {
        validPerm(token);
        Map<String, String> headers = new HashMap<>(16);
        headers.put("cookie", "Hm_lvt_6cdb2e7d7f6b6e4ba3c6faac6533ab9f=1620714087; PHPSESSID=bb50rft2nffpn6hfhmoc24i06t");
        //String result  =  OSSUtil.uploadNetworkFlows("public/123.png", "https://www.xmaibu.com/index/goods/img?model=IW357408&brand=IWC");
        String s = AliHttpUtils.doGet("https://www.xmaibu.com/index/goods/img?model=IW357408&brand=IWC", headers);
        BufferedImage bi = ImageCode.createImageCode(s);


        String urlSend = "https://www.xmaibu.com/index/goods/img?model=IW357408&brand=IWC";
        URL url = new URL(urlSend);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setInstanceFollowRedirects(false);
        if (!LocalUtils.isEmptyAndNull(headers)) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                httpURLConnection.setRequestProperty(key, headers.get(key));
            }
        }
        String location = httpURLConnection.getHeaderField("location");
        System.out.println("location: ===" + location);
        //ImageIO.write((RenderedImage) httpURLConnection.getInputStream(), "JPG",  response.getOutputStream());

        System.out.println("===readRaw===: " + readRaw(httpURLConnection.getInputStream()));
        //try {
        //    Map<String, String> headers = new HashMap<>(16);
        //    headers.put("cookie", "Hm_lvt_6cdb2e7d7f6b6e4ba3c6faac6533ab9f=1620714087; PHPSESSID=bb50rft2nffpn6hfhmoc24i06t");
        //    HttpResponse httpURLConnection = AliHttpUtils.doGet("https://www.xmaibu.com/index/goods/img?model=IW357408&brand=IWC", "", headers, headers);
        //    if (null != httpURLConnection) {
        //        Header location2 = httpURLConnection.getFirstHeader("Location");
        //        System.out.println("===location2===: " + location2.getValue());
        //    }
        //} catch (Exception e) {
        //    log.error(e.getMessage(), e);
        //}


    }


    /**
     * ??????????????????
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "??????????????????",
            notes = "??????????????????",
            httpMethod = "GET")
    @RequestMapping(value = "/updatePublicImg", method = RequestMethod.GET)
    public String updatePublicImg(@RequestParam String token, HttpServletResponse response) throws IOException {
        validPerm(token);


        for (int k = 1; k <= 20; k++) {
            int finalK = k;
            ThreadUtils.getInstance().executorService.execute(() -> {
                log.info("=====?????????{}?????????;====", finalK);
                int i = 1;
                for (; ; ) {
                    try {
                        Date d = new Date();
                        String endDate = redisUtil.get("_temp_upload_end_time");
                        Date newDate = DateUtil.parse(endDate);
                        if (d.after(newDate)) {
                            log.info("===========================????????????========================");
                            break;
                        }
                    } catch (ParseException ignored) {

                    }

                    log.info("====????????????????????????,??????:{},?????????: {}", finalK, i);
                    PageHelper.startPage(finalK, 1000);
                    List<ProPublic> proPublicList = proPublicService.listAllProPublic();
                    if (LocalUtils.isEmptyAndNull(proPublicList)) {
                        log.info("====????????????????????????=====??????=====,??????:{},?????????: {}", finalK, i);
                        break;
                    }
                    int j = 0;
                    List<ProPublic> newList = new ArrayList<>();
                    for (ProPublic proPublic : proPublicList) {
                        Date d = new Date();
                        String endDate = redisUtil.get("_temp_upload_end_time");
                        j++;
                        String smallImg = proPublic.getSmallImg();
                        try {
                            Date newDate = DateUtil.parse(endDate);
                            if (d.after(newDate)) {
                                log.info("===========================????????????========================");
                                break;
                            }
                            //??????????????????
                            if (LocalUtils.isEmptyAndNull(smallImg) || !smallImg.contains("public")) {
                                continue;
                            }
                            smallImg = smallImg.replaceAll(" ", "%20");
                            String fileName = "public/" + proPublic.getName() + "/" + proPublic.getTypeNo() + "." + LocalUtils.getSuffixType(smallImg);

                            //log.info("======????????????==????????????:{},?????????{}???===????????????:{},???????????????:{}", finalK, j, smallImg, fileName);
                            String result = OSSUtil.uploadNetworkFlows(fileName, smallImg);
                            System.out.println("=====result: " + result);
                            if (!LocalUtils.isEmptyAndNull(result)) {
                                proPublic.setSmallImg("/" + fileName);
                                newList.add(proPublic);
                                if (newList.size() == 5) {
                                    proPublicService.updateBatchSmallImg(newList);
                                    newList.clear();
                                    log.info("======????????????==????????????:{},?????????{}???===????????????:{},???????????????:{}", finalK, j, smallImg, fileName);
                                }

                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }

                    log.info("====????????????????????????,????????????:{},??????????????????{}===", finalK, i);
                    i++;
                }
            });
        }

        return "??????????????????....";
    }

    /**
     * ??????token?????????;
     *
     * @return Result
     */
    @ApiOperation(
            value = "??????token?????????",
            notes = "??????token?????????",
            httpMethod = "GET")
    @RequestMapping(value = "/getTokenInfo", method = RequestMethod.GET)
    public BaseResult getTokenInfo(@Valid ParamToken paramToken, BindingResult result) throws IOException {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        int shopId = getShopId();
        int bossUserId = getBossUserId();
        String username = getUsername();
        int userNumber = getUserNumber();
        String shopName = getShopName();
        String shopNumber = getShopNumber();
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("shopId", shopId);
        map.put("bossUserId", bossUserId);
        map.put("username", username);
        map.put("userNumber", userNumber);
        map.put("shopName", shopName);
        map.put("shopNumber", shopNumber);
        return BaseResult.okResult(map);
    }


    /**
     * ??????username?????????;
     *
     * @return Result
     */
    @ApiOperation(
            value = "??????token?????????",
            notes = "??????token?????????",
            httpMethod = "GET")
    @RequestMapping(value = "/getUsernameInfo", method = RequestMethod.GET)
    public BaseResult getUsernameInfo(@Valid ParamUsername username, BindingResult result) throws IOException {
        servicesUtil.validControllerParam(result);
        int userId = getUserId();
        int shopId = getShopId();
        int bossUserId = getBossUserId();
        int userNumber = getUserNumber();
        String shopName = getShopName();
        String shopNumber = getShopNumber();
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("shopId", shopId);
        map.put("bossUserId", bossUserId);
        map.put("token", getToken());
        map.put("userNumber", userNumber);
        map.put("shopName", shopName);
        map.put("shopNumber", shopNumber);
        return BaseResult.okResult(map);
    }


    public String json(String url) throws IOException {
        long st = System.currentTimeMillis();
        Connection.Response execute = Jsoup.connect(url)
                .followRedirects(false)
                .cookie("cookie", "Hm_lvt_6cdb2e7d7f6b6e4ba3c6faac6533ab9f=1620714087; PHPSESSID=bb50rft2nffpn6hfhmoc24i06t")
                .method(Connection.Method.GET)
                .execute();
        long et = System.currentTimeMillis();
        log.info("======??????location??????====??????====??????: {}", (et - st));
        return execute.headers("location").get(0);
    }

    /**
     * java??????raw
     *
     * @author zengwei
     * @email 1014483974@qq.com
     * @version 2019???3???01??? ??????4:10:04
     */
    public static String readRaw(InputStream inputStream) {

        String result = "";
        try {
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }

            outSteam.close();
            inputStream.close();

            result = new String(outSteam.toByteArray(), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void validPerm(String value) {
        int userId = getUserId();
        if (userId != 10000 && !value.equals("15112304365")) {
            throw new MyException("???????????????");
        }
    }


    @RequestMapping(value = "/exportExcelUser", method = RequestMethod.GET)
    @ApiOperation(
            value = "??????????????????;",
            notes = "??????????????????;",
            httpMethod = "GET")
    public void downExcel(HttpServletResponse response) {
        String fileName = "??????.xls";
        String sheetName = "sheet1";

        List<TempForUser> tempForUserList = shpUserMapper.getTempForUser();
        tempForUserList.forEach(tempForUser -> {
            tempForUser.setUserName(DESEncrypt.decodeUsername(tempForUser.getUserName()));
        });
        String[] names = {"?????????", "????????????", "????????????(?????????)", "????????????"};
        ExcelExport.export(response, tempForUserList, names, "??????");

    }

    @ApiOperation(
            value = "??????????????????;",
            notes = "??????????????????;",
            httpMethod = "GET")
    @RequestMapping(value = "/initYearRate", method = RequestMethod.GET)
    public BaseResult initYearRate(@RequestParam String token, HttpServletResponse response) {
        validPerm(token);
        ordOrderService.initYearRate();
        return BaseResult.okResult("??????????????????......");
    }

    @ApiOperation(
            value = "????????????;state=all ?????? | 0:????????? | 1:?????????",
            notes = "????????????;",
            httpMethod = "GET")
    @RequestMapping(value = "/productCopy", method = RequestMethod.GET)
    public BaseResult productCopy(@RequestParam String token, @RequestParam String state, @RequestParam String from, @RequestParam String to) {
        validPerm(token);
        if (LocalUtils.isEmptyAndNull(state)) {
            return BaseResult.defaultErrorWithMsg("state=all ?????? | 0:????????? | 1:?????????");
        }
        if (LocalUtils.isEmptyAndNull(from) || LocalUtils.isEmptyAndNull(to)) {
            return BaseResult.defaultErrorWithMsg("???????????????????????????");
        }
        //??????pro_product??????;
        //1.???????????????????????????????????????????????????;
        //2.???????????????????????????pro_product???shopId,biz_id????????????,id????????????
        //3.????????????pro_product;
        //??????pro_details??????;
        //1.??????????????????pro_product???id?????????.?????????pro_details??????;
        //2.????????????;
        return BaseResult.okResult("??????????????????......");
    }


    @ApiOperation(
            value = "??????????????????????????????",
            notes = "????????????;",
            httpMethod = "GET")
    @RequestMapping(value = "/addPermForAll", method = RequestMethod.GET)
    public BaseResult addPermForAll(@RequestParam String token, @RequestParam String hasPerm, @RequestParam String newPerm) {
        validPerm(token);
        if (LocalUtils.isEmptyAndNull(hasPerm) || LocalUtils.isEmptyAndNull(newPerm)) {
            return BaseResult.defaultErrorWithMsg("hasPerm:???????????????????????????id;newPerm:????????????????????????id");
        }
        ThreadUtils.getInstance().executorService.execute(() -> shpUserPermissionRefService.addPermForAll(hasPerm, newPerm));
        return BaseResult.okResult("??????????????????......");
    }


    @ApiOperation(
            value = "??????????????????????????????",
            notes = "??????????????????????????????;",
            httpMethod = "GET")
    @RequestMapping(value = "/initNewPerm", method = RequestMethod.GET)
    public BaseResult initNewPerm(@RequestParam String token) {
        validPerm(token);
        ThreadUtils.getInstance().executorService.execute(() -> shpPermUserRefService.initNewPermFromOldPerm());
        return BaseResult.okResult("??????????????????......");
    }



    @ApiOperation(
            value = "????????????????????????",
            notes = "????????????????????????;",
            httpMethod = "GET")
    @RequestMapping(value = "/initNewPermTpl", method = RequestMethod.GET)
    public BaseResult initNewPermTpl(@RequestParam String token) {
        validPerm(token);
        ThreadUtils.getInstance().executorService.execute(() -> shpPermTplService.initNewPermTplFromOldPermTpl());
        return BaseResult.okResult("??????????????????......");
    }





    /**
     * ??????????????????
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(
            value = "??????????????????;",
            notes = "??????????????????;",
            httpMethod = "POST")
    @PostMapping("/moveDaXiangProduct")
    public BaseResult moveDaXiangProduct(
            @RequestParam String shopId) {
        int myShopId = Integer.parseInt(shopId);
        ThreadUtils.getInstance().executorService.execute(() -> {
            long st = System.currentTimeMillis();
            try {
                moveProduct(0, myShopId);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            long et = System.currentTimeMillis();
            log.info("??????????????????,??????: {}", (et - st));
        });
        return BaseResult.defaultOkResultWithMsg("??????????????????????????????,????????????????????????!");
    }


    /**
     * ???????????????????????????,????????????id 10032
     *
     * @param page
     * @throws Exception
     */
    private void moveProduct(int page, int shopId) throws Exception {
        String host = "https://t.imtimi.cn:9010";
        String path = "/lmserver/getgoods?v=V1.0.05";
        String proState = "10";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sort", "1");
        hashMap.put("gstate", "4");
        hashMap.put("owner", "????????????");
        hashMap.put("page", page + "");
        hashMap.put("fopenid", "ogEkq4z58Ypfw3Jet9a8bBknJpr4");

        Map<String, Object> body = new HashMap<>(16);
        body.put("rep", JSONObject.toJSON(hashMap));
        String bodyString = JSONObject.toJSONString(body);
        System.out.println("################bodyString: " + bodyString);
        HttpResponse post = AliHttpUtils.doPost(host, path, new HashMap<>(), hashMap, bodyString);
        int statusCode = post.getStatusLine().getStatusCode();
        log.info("===========================xl????????????,?????????{}???,?????????: {}", page, statusCode);
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(str);
                Object resObj = jsonObject.get("res");
                JSONObject objJSON = JSONObject.parseObject(resObj.toString());
                if ("??????".equals(objJSON.getString("msg"))) {
                    Object list = objJSON.get("list");
                    if (!LocalUtils.isEmptyAndNull(list)) {
                        //??????;
                        JSONArray jsonArray = JSONArray.parseArray(list.toString());
                        if (LocalUtils.isEmptyAndNull(jsonArray)) {
                            log.info("===========0????????????,????????????====?????????{}???", page);
                            return;
                        }
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject obj = JSONObject.parseObject(jsonArray.get(i).toString());
                            String fid = obj.getString("fid");
                            String fname = obj.getString("fname");
                            String fdesc = obj.getString("fdesc");
                            String ficon = obj.getString("ficon");
                            String fcode = obj.getString("fcode");
                            String faddtime = obj.getString("faddtime");
                            //??????:??????,??????,??????,??????,??????
                            String ftype = obj.getString("ftype");

                            //??????(???) ?????????: saleprice ?????????: refprice ?????????: inprice ?????????: commprice
                            String fprice = obj.getString("fprice");
                            JSONObject priceJson = JSONObject.parseObject(fprice);
                            String inprice = priceJson.getString("inprice");
                            inprice = LocalUtils.isEmptyAndNull(inprice) ? "0" : inprice;
                            String commprice = priceJson.getString("commprice");
                            commprice = LocalUtils.isEmptyAndNull(commprice) ? "0" : commprice;
                            String saleprice = priceJson.getString("saleprice");
                            saleprice = LocalUtils.isEmptyAndNull(saleprice) ? "0" : saleprice;
                            String refprice = priceJson.getString("refprice");
                            refprice = LocalUtils.isEmptyAndNull(refprice) ? "0" : refprice;

                            String fextattri = obj.getString("fextattri");
                            //??????
                            JSONObject attrJson = JSONObject.parseObject(fextattri);
                            //??????
                            String lbrand = attrJson.getString("lbrand");
                            String lfrom = attrJson.getString("lfrom");
                            String lenclosure = attrJson.getString("lenclosure");
                            String lnew = attrJson.getString("lnew");
                            String lstore = attrJson.getString("lstore");
                            String recover = attrJson.getString("recover");

                            String desc = fcode + " " + lbrand + " " + fdesc + " " + lnew + " " + lenclosure + ("0".equals(refprice) ? "" : "????????????: ???" + refprice);

                            String defaultRemark = "#????????????#";
                            String remark = defaultRemark + lstore + " " + recover;

                            System.out.println(fname + fdesc);
                            //??????
                            String proAttr;
                            switch (lfrom + "") {
                                case "????????????":
                                case "????????????":
                                case "????????????":
                                    proAttr = "10";
                                    break;
                                case "????????????":
                                    proAttr = "20";
                                    break;
                                default:
                                    proAttr = "40";
                                    break;
                            }
                            //??????: WB,ZB,XB,XX,PS,QT
                            String classify;
                            //??????:??????,??????,??????,??????,??????
                            switch (ftype + "") {
                                case "??????":
                                    classify = "WB";
                                    break;
                                case "??????":
                                    classify = "ZB";
                                    break;
                                case "??????":
                                    classify = "XX";
                                    break;
                                case "??????":
                                    classify = "XB";
                                    break;
                                default:
                                    classify = "QT";
                                    break;
                            }


                            ParamProductUpload param = new ParamProductUpload();
                            param.setAttribute(proAttr);
                            param.setTotalNum("1");
                            param.setState(proState);
                            param.setClassify(classify);
                            param.setInitPrice(LocalUtils.calcNumber(inprice, "*", 100).toString());
                            param.setTradePrice(LocalUtils.calcNumber(commprice, "*", 100).toString());
                            param.setSalePrice(LocalUtils.calcNumber(saleprice, "*", 100).toString());
                            param.setName(fname + "");
                            param.setDescription(desc);
                            param.setSmallImgUrl(ficon);
                            param.setRemark(remark);

                            List<String> imgList = getMoreImg(fid);
                            if (LocalUtils.isEmptyAndNull(imgList)) {
                                param.setProImgUrl(ficon);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (Object img : imgList) {
                                    //???????????????,????????????
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
                            //-9:??????????????????; -8:???????????????;-7excel??????;-6???????????????
                            ProProduct pro = proProductService.pickProProduct(param, true, shopId, -6);
                            try {
                                pro.setInsertTime(DateUtil.parse(faddtime));
                                pro.setUpdateTime(pro.getInsertTime());
                            } catch (ParseException ignored) {
                            }
                            ProDetail proDetail = proProductService.pickProDetail(param, true);
                            proDetail.setInsertTime(pro.getInsertTime());
                            proDetail.setUpdateTime(pro.getInsertTime());
                            proDetail.setRemark(defaultRemark);
                            proProductService.saveProProductReturnId(pro, proDetail, null);

                        }
                        page++;
                        moveProduct(page, shopId);
                    } else {
                        log.info("===========1????????????,????????????====?????????{}???", page);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new MyException("not JSON format! ");
            }
        }
    }

    private List<String> getMoreImg(String pid) throws Exception {
        String host = "https://t.imtimi.cn:9010";
        String path = "/lmserver/getalbum?v=V1.0.05";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pid", pid);

        Map<String, Object> body = new HashMap<>(16);
        body.put("rep", JSONObject.toJSON(hashMap));
        String bodyString = JSONObject.toJSONString(body);
        System.out.println("################bodyString: " + bodyString);
        HttpResponse post = AliHttpUtils.doPost(host, path, new HashMap<>(), hashMap, bodyString);
        int statusCode = post.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            String str = EntityUtils.toString(post.getEntity(), "UTF-8");
            JSONObject jsonObject;
            jsonObject = JSONObject.parseObject(str);
            Object resObj = jsonObject.get("res");
            JSONObject objJSON = JSONObject.parseObject(resObj.toString());
            if ("??????".equals(objJSON.getString("msg"))) {
                Object list = objJSON.get("list");
                return LocalUtils.isEmptyAndNull(list) ? null : (List<String>) list;
            }

        }
        return null;
    }


}

