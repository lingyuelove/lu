package com.luxuryadmin.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.luxuryadmin.common.aop.WebAspect;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义的一些常用但没有分类的工具方法;<br/>
 * eg:重新封装判断为空字符串(增加对"null"字符串的判断)<br/>
 *
 * @author monkey king
 * @ClassName LocalUtils
 * @Date 2019/07/30 1:10
 * @Version 1.0
 */
@Slf4j
public class LocalUtils extends StringUtils {


    /**
     * 获取没有短横线的UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getTimestamp() {
        return DateUtil.format(new Date(), "yyyyMMddHHmmssS");
    }


    /**
     * 实体类转map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> convertBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!"class".equals(key)) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null == value) {
                        map.put(key, null);
                    } else {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("convertBean2Map Error", e);
        }
        return map;
    }

    /**
     * 判断object是否为null或者为"" true:没有数据;<br>
     * true:没有数据;<br/>
     * false:有数据;<br/>
     * <B>注意: 如果为多个空格,则为false; eg: "  "</B>
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isEmptyAndNull(Object obj) {
        return isEmpty(obj) || "null".equalsIgnoreCase(obj.toString());
    }

    /**
     * 判断Map是否为null或者有没有数据;<br>
     * true:没有数据;<br>
     * false:有数据;
     *
     * @param map 数组
     * @return boolean
     */
    public static boolean isEmptyAndNull(Map map) {
        return null == map || map.isEmpty();

    }

    /**
     * 判断数组是否为null或者有没有数据;<br>
     * true:没有数据;<br>
     * false:有数据;
     *
     * @param objs 数组
     * @return boolean
     */
    public static boolean isEmptyAndNull(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    /**
     * List集合里面是否有数据<br>
     * true:没有数据;<br>
     * false:有数据;
     *
     * @param list List集合
     * @return boolean
     */
    public static boolean isEmptyAndNull(Collection<?> list) {
        return null == list || list.isEmpty();
    }


    /**
     * 对两个数进行四则运算; <strong>对于除法的运算,保留2位小数;</strong><br>
     * result = num1+num2;<br>
     * result = num1-num2;<br>
     * result = num1*num2;<br>
     * result = num1/num2;<br>
     *
     * @param num1       式子中的第一个数;
     * @param calcSymbol 运算符号 "+" "-" "*" "/"
     * @param num2       式子中的第二个数;
     * @return BigDecimal
     * @throws Exception 计算异常;
     */
    public static BigDecimal calcNumber(Object num1, String calcSymbol, Object num2)
            throws Exception {
        return calcNumber(num1, calcSymbol, num2, 2);

    }

    /**
     * 对两个数进行四则运算; 自行选择保留位数;不四舍五入,直接截取;
     * result = num1+num2;<br>
     * result = num1-num2;<br>
     * result = num1*num2;<br>
     * result = num1/num2;<br>
     *
     * @param num1       式子中的第一个数;
     * @param calcSymbol 运算符号 "+" "-" "*" "/"
     * @param num2       式子中的第二个数;
     * @param remainNum  保留多少位小数; 如果小于0,则为0;
     * @return BigDecimal
     * @throws Exception 计算异常;
     */
    public static BigDecimal calcNumber(Object num1, String calcSymbol, Object num2, int remainNum)
            throws Exception {
        if (!isEmptyAndNull(num1) && !isEmptyAndNull(num2)) {
            BigDecimal decimal = new BigDecimal(num1.toString());
            BigDecimal decima2 = new BigDecimal(num2.toString());
            if ("+".equals(calcSymbol)) {
                return decimal.add(decima2);
            } else if ("-".equals(calcSymbol)) {
                return decimal.subtract(decima2);
            } else if ("*".equals(calcSymbol)) {
                return decimal.multiply(decima2);
            } else if ("/".equals(calcSymbol)) {
                if (!"0".equals(num2)) {
                    remainNum = Math.max(remainNum, 0);
                    return decimal.divide(decima2, remainNum,
                            BigDecimal.ROUND_DOWN);
                }
            }
        }
        throw new Exception();
    }

    /**
     * 验证是否为价格
     *
     * @param money
     * @return
     */
    public static boolean isPrice(String money) {
        try {
            Double.parseDouble(money);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证手机号码格式是否正确
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNumber(String phone) {
        if (isEmptyAndNull(phone)) {
            return false;
        }
        String phonePattern = "^[1][3456789][0-9]{9}$";
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcherPattern = pattern.matcher(phone);
        return matcherPattern.find();
    }

    /**
     * 验证手机号码格式是否正确
     *
     * @param string
     * @return
     */
    public static boolean isNumber(String string) {
        if (isEmptyAndNull(string)) {
            return false;
        }
        String phonePattern = "^[0-9]+$";
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcherPattern = pattern.matcher(string);
        return matcherPattern.find();
    }

    /**
     * 是否为密码
     *
     * @param password 字符串
     * @return
     */
    public static boolean isPassword(String password) {
        if (isEmptyAndNull(password)) {
            return false;
        }
        String passwordPattern = "^\\w{6,16}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        return pattern.matcher(password).matches();
    }

    /**
     * 密码是否为md5加密过的格式
     *
     * @param password 字符串
     * @return
     */
    public static boolean isPasswordMD5(String password) {
        if (isEmptyAndNull(password)) {
            return false;
        }
        String passwordPattern = "^[a-fA-F0-9]{32}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        return pattern.matcher(password).matches();
    }

    /**
     * 邀请码(4-8)位纯数字
     *
     * @param inviteCode 邀请码
     * @return
     */
    public static boolean isInviteCode(String inviteCode) {
        if (isEmptyAndNull(inviteCode)) {
            return false;
        }
        String inviteCodePattern = "^\\d{4,8}$";
        Pattern pattern = Pattern.compile(inviteCodePattern);
        return pattern.matcher(inviteCode).matches();
    }

    /**
     * 是否短信验证码;<br/>
     * 6位纯数字
     *
     * @param smsCode 邀请码
     * @return
     */
    public static boolean isSmsCode(String smsCode) {
        if (isEmptyAndNull(smsCode)) {
            return false;
        }
        String passwordPattern = "^\\d{6}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        return pattern.matcher(smsCode).matches();
    }

    /**
     * 判断一个数是否在此区间内;<br/>
     * eg: isBetween(3,3,4)→true
     *
     * @param num   要判断的数值
     * @param start 区间开始(含)
     * @param end   区间结束(含)
     * @return true:在 | false:不在
     */
    public static boolean isBetween(double num, double start, double end) {
        return num >= start && num <= end;
    }

    /**
     * 返回端上的数据;
     *
     * @param key
     * @param obj
     * @return
     */
    public static HashMap<String, Object> getHashMap(String key, Object obj) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put(key, obj);
        return map;
    }

    /**
     * 返回端上的数据;
     *
     * @param list
     * @return
     */
    public static HashMap<String, Object> getHashMap(Collection<?> list) {
        return getHashMap("objList", list);
    }

    /**
     * 返回端上的数据;
     *
     * @param obj
     * @return
     */
    public static HashMap<String, Object> getHashMap(Object obj) {
        return getHashMap("obj", obj);
    }

    /**
     * 返回端上的数据;
     *
     * @param objList
     * @return
     */
    public static BaseResult getBaseResult(Collection<?> objList) {
        if (LocalUtils.isEmptyAndNull(objList)) {
            return BaseResult.okResultNoData();
        }
        return BaseResult.okResult(getHashMap("objList", objList));
    }

    public static BaseResult getBaseResult(Object obj) {
        if (LocalUtils.isEmptyAndNull(obj)) {
            return BaseResult.okResultNoData();
        }
        return BaseResult.okResult(getHashMap("obj", obj));
    }


    /**
     * 返回端上的数据;
     *
     * @param token
     * @return
     */
    public static HashMap<String, Object> getHashMap(String token) {
        return getHashMap("token", token);
    }

    /**
     * token返回端上;拼接成json格式;
     *
     * @param token
     * @return
     */
    public static Object getDataJSONString(String token) {
        return JSONObject.toJSON(getHashMap(token));
    }

    /**
     * 将JSON字符串进行排序;按key值的ASCII码排序;(升序);<br/>
     * 举例:<br/> 排序前:
     * {"token":"4267437c-be0d-4959-848d-9b73deb463b8","shopList":[{"shopName":"足浴店","shopNumber":111111,"shopId":10000,"type":"职员"},{"shopName":"水果店","shopNumber":22222,"shopId":10001,"type":"职员"},{"shopName":"十足便利店","shopNumber":88888,"shopId":10002,"type":"职员"}]};
     * <br/>排序后:<br/>
     * <p>
     * {"shopList":[{"shopId":10000,"shopName":"足浴店","shopNumber":111111,"type":"职员"},{"shopId":10001,"shopName":"水果店","shopNumber":22222,"type":"职员"},{"shopId":10002,"shopName":"十足便利店","shopNumber":88888,"type":"职员"}],"token":"4267437c-be0d-4959-848d-9b73deb463b8"};
     *
     * @param oldJSON JSONObject对象
     * @return JSONObject
     */
    public static JSONObject sortJSONObject(final JSONObject oldJSON) {
        return sortJSONObject(oldJSON, false);
    }

    /**
     * 将JSON字符串进行排序;按key值的ASCII码排序;(升序);<br/>
     * 举例:<br/> 排序前:
     * {"token":"4267437c-be0d-4959-848d-9b73deb463b8","shopList":[{"shopName":"足浴店","shopNumber":111111,"shopId":10000,"type":"职员"},{"shopName":"水果店","shopNumber":22222,"shopId":10001,"type":"职员"},{"shopName":"十足便利店","shopNumber":88888,"shopId":10002,"type":"职员"}]};
     * <br/>排序后:<br/>
     * <p>
     * {"shopList":[{"shopId":10000,"shopName":"足浴店","shopNumber":111111,"type":"职员"},{"shopId":10001,"shopName":"水果店","shopNumber":22222,"type":"职员"},{"shopId":10002,"shopName":"十足便利店","shopNumber":88888,"type":"职员"}],"token":"4267437c-be0d-4959-848d-9b73deb463b8"};
     *
     * @param oldJSON        JSONObject对象
     * @param booleanConvert 是否把boolean值用C语言的方式来表达;<br/>
     *                       true:转换 | false:保持原样
     *                       [转换结果: true变为1; false变为0;]
     * @return JSONObject
     */
    public static JSONObject sortJSONObject(final JSONObject oldJSON, boolean booleanConvert) {
        if (isEmptyAndNull(oldJSON)) {
            return null;
        }
        //最外层;要返回的JSONObject(按key值的ASCII码排序)
        JSONObject jsonObjectSort = new JSONObject(true);
        Set<String> setKeys = oldJSON.keySet();
        ArrayList<String> keyList = new ArrayList<String>(setKeys);
        //对JSONObject的key进行排序
        Collections.sort(keyList);
        for (String key : keyList) {
            Object obj = oldJSON.get(key);
            //判断json字符串是否有内嵌JSONArray
            if (obj instanceof JSONArray) {
                try {
                    List<JSONObject> jsonList = new ArrayList<>();
                    JSONArray jsonArray = JSONArray.parseArray(obj.toString());
                    for (Object o : jsonArray) {
                        //递归调用;
                        JSONObject json = sortJSONObject((JSONObject) o, booleanConvert);
                        jsonList.add(json);
                    }
                    jsonObjectSort.put(key, jsonList);
                } catch (Exception e) {
                    System.out.println("======非jsonArray数组格式: " + obj.toString() + " | exceptionMSG: " + e.getMessage());
                    jsonObjectSort.put(key, obj);
                }
                oldJSON.remove(key);
                //判断json字符串是否有内嵌JSONObject
            } else if (obj instanceof JSONObject) {
                //递归调用;
                JSONObject json = sortJSONObject((JSONObject) obj, booleanConvert);
                jsonObjectSort.put(key, json);
            } else {
                //否则的话 直接put进去;
                Object sortObj = oldJSON.get(key);
                if (null != sortObj) {
                    //if括号体这段代码为了兼容iOS前端问题;底层C语言;true为1; false为0;
                    if (booleanConvert && sortObj instanceof Boolean) {
                        System.out.println("======boolean值转换: sortObj(前):" + sortObj);
                        sortObj = (Boolean) sortObj ? 1 : 0;
                        System.out.println("======boolean值转换: sortObj(后):" + sortObj);
                    }
                    //所有数值类型,都以String类型返回到端上;避免出现小数点后面多0或少0的问题
                    if (sortObj instanceof Number) {
                        sortObj = sortObj + "";
                    }

                    jsonObjectSort.put(key, sortObj);
                }
            }
        }
        return jsonObjectSort;
    }


    /**
     * 对以分隔符来作为间隔拼装字符串的.进行分割.转换为数组;
     *
     * @param str       以分隔符进行拼接的字符串,如 abc,cdd,fff,ddd
     * @param separated 拼装的分隔符;
     * @return String[]
     */
    public static String[] splitString(String str, String separated) {
        String[] strs = null;
        if (!isEmptyAndNull(str)) {
            if (str.endsWith(separated)) {
                str = str.substring(0, str.length() - 1);
            }
            strs = str.split(separated);
        }
        return strs;
    }


    /**
     * 把一个数组转化成字符串,用单引号括起来"'",多个之间用逗号隔开","如 '1','2'
     *
     * @param objs Object[] objs 要转换的数组
     * @return String[]
     */
    public static String packString(Object[] objs) {
        StringBuffer strs = new StringBuffer();
        if (!isEmptyAndNull(objs)) {
            for (Object obj : objs) {
                strs.append("'").append(obj).append("',");
            }
            if (strs.toString().endsWith(",")) {
                strs = new StringBuffer(strs.substring(0, strs.length() - 1));
            }
        }
        return strs.toString();
    }

    /**
     * 对多选的参数进行逗号分开;用于sql里面的in查询;<br/>
     * 格式为: 'a','b','c'
     *
     * @param param
     * @return
     */
    public static String formatParamForSqlInQuery(String param) {

        return formatParamForSqlInQuery(param, ";");
    }

    /**
     * 对多选的参数进行逗号分开;用于sql里面的in查询;<br/>
     * 格式为: 'a','b','c'
     *
     * @param param
     * @return
     */
    public static String formatParamForSqlInQuery(String param, String separated) {

        boolean isParamNull = LocalUtils.isEmptyAndNull(param);
        if (!isParamNull) {
            String[] attributeCodeArray = LocalUtils.splitString(param, separated);
            param = LocalUtils.packString(attributeCodeArray);
        }
        return param;
    }

    /**
     * 如果是""(空字符)则返回null;
     *
     * @param string
     * @return
     */
    public static String returnNullOrString(String string) {
        return isEmptyAndNull(string) ? null : string;
    }


    public static String returnFormatString(Object object) {
        return LocalUtils.isEmptyAndNull(object) ? "" : object.toString();
    }

    /**
     * 如果是null则返回""(空字符);
     *
     * @param string
     * @return
     */
    public static String returnEmptyStringOrString(String string) {
        return isEmptyAndNull(string) ? "" : string;
    }

    /**
     * 将手机号码中间4位用星号替代
     *
     * @param phone
     * @return
     */
    public static String returnAsteriskPhone(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 获取手机尾号后4位
     *
     * @param phone
     * @return
     */
    public static String getLastPhoneNumber(String phone) {
        return phone.substring(phone.length() - 4);
    }

    /**
     * 字符串转Integer,null -> null; "" -> 0; "9" -> 9
     *
     * @param str
     * @return
     */
    public static Integer strParseInt(String str) {
        if (null == str) {
            return null;
        } else if ("" == str) {
            return 0;
        } else {
            return Integer.parseInt(str);
        }
    }

    /**
     * 返回已经签名的BaseResult<br/>
     * 多了 timestamp和sign 这两个key
     *
     * @param baseResult
     * @return
     */
    public static BaseResult returnSignBaseResult(BaseResult baseResult) {
        baseResult.setTimestamp(System.currentTimeMillis() + "");
        StringBuffer sb = new StringBuffer();
        sb.append("code=");
        sb.append(baseResult.getCode());
        sb.append(";msg=");
        sb.append(baseResult.getMsg());
        sb.append(";data=");
        Object data = baseResult.getData();
        Object obj = "";
        if (!LocalUtils.isEmptyAndNull(data)) {
            try {
                //String jsonStr = JSON.toJSONString(data, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
                String jsonStr = JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                obj = LocalUtils.sortJSONObject(jsonObject);
            } catch (Exception e) {
                obj = JSONObject.toJSON(data);
            }
        }
        baseResult.setData(obj);
        sb.append(obj.toString());
        sb.append(";timestamp=");
        sb.append(baseResult.getTimestamp());
        String sign = DESEncrypt.md5Hex(sb.toString() + WebAspect.SIGN_KEY);
        log.debug("=========【generatorSign】=========:" + sb.toString());
        log.debug("=========【sign】=========:" + sign);
        baseResult.setSign(sign);
        return baseResult;
    }

    /**
     * 获取请求token
     *
     * @return token
     */
    public static String getToken(HttpServletRequest request) {
        //从header中获取token
        String token = request.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if (LocalUtils.isEmptyAndNull(token)) {
            token = request.getParameter("token");
        }
        return token;
    }

    /**
     * 如果参数为null,则返回null,否则返回BigDecimal格式
     *
     * @param proPrice
     * @return
     */
    public static BigDecimal formatBigDecimal(String proPrice) {
        return LocalUtils.isEmptyAndNull(proPrice) ? null : new BigDecimal(proPrice);
    }

    /**
     * 复制不为null的属性
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }


    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set emptyNames = new HashSet();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }


    /**
     * 获取异常的堆栈信息
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }


    /**
     * 格式化价格各式;无效的0去掉;
     *
     * @param number
     * @return
     */
    public static BigDecimal formatPrice(Object number) {
        return formatPrice(number, 6);
    }

    /**
     * 格式化价格各式;无效的0去掉;
     *
     * @param number
     * @param num    保留多少位小数
     * @return
     */
    public static BigDecimal formatPrice(Object number, int num) {
        num = Math.max(num, 1);
        StringBuilder flag = new StringBuilder();
        for (int i = 0; i < num; i++) {
            flag.append("#");
        }
        DecimalFormat df = new DecimalFormat("0." + flag.toString());
        return new BigDecimal(df.format(new BigDecimal(number + "")));
    }


    /**
     * 格式化价格各式;无效的0去掉;
     * 价格用千位符隔开
     *
     * @param number
     * @return
     */
    public static String formatPriceSpilt(Object number) {
        DecimalFormat df = new DecimalFormat(",##0.##");
        return df.format(new BigDecimal(number + ""));
    }


    /**
     * 获取后缀;不包含"."
     *
     * @param stringFileName
     * @return
     */
    public static String getSuffixType(String stringFileName) {
        return LocalUtils.isEmptyAndNull(stringFileName) ? "" : stringFileName.substring(stringFileName.lastIndexOf(".") + 1);
    }


    /**
     * 计算年化收益率返回的是百分比的值;例如35.6;代表35.6%;
     * <br/>成本价和销售价需要统一单位
     * 不符合计算规则,则返回空字符串
     *
     * @param initPrice 成本价
     * @param salePrice 销售价
     * @param day       库存天数
     * @return
     */
    public static String calcYearRate(BigDecimal initPrice, BigDecimal salePrice, int day) {
        //年化收益率: 利润/成本/库存天数*365*100%
        if (initPrice != null && initPrice.doubleValue() > 0 && salePrice != null && salePrice.doubleValue() > 0) {
            try {
                if (day > 0) {
                    //销售价和成本不为空才计算年化收益率;计算时,保留7位小数;结果时,取两位小数;
                    double profit = LocalUtils.calcNumber(salePrice, "-", initPrice).doubleValue();
                    double profitRate = LocalUtils.calcNumber(profit * 100, "/", initPrice, 7).doubleValue();
                    double dayRate = LocalUtils.calcNumber(profitRate, "/", day, 7).doubleValue();
                    double rate = LocalUtils.calcNumber(dayRate, "*", 365).doubleValue();
                    return LocalUtils.formatPrice(rate, 2).toString();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return "";
    }

    public static int formatVersion(String appVersion) {
        return Integer.parseInt(appVersion.replaceAll("\\.", ""));
    }

    /**
     * list的深拷贝,用于需要区别两个内存地址
     *
     * @param src
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> deepCopy(List<T> src) throws Exception {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    public static void main(String[] args) {
        System.out.println(1.1 + 2.2);
        System.out.println((1.1 + 2.2) == 3.3);
        System.out.println(formatVersion("0.2.3.4"));
        System.out.println(formatPrice("158.6956521", 2).toString());
       /* try {
            BigDecimal bigDecimal = calcNumber(1.1, "+", 2.2);
            System.out.println(bigDecimal.doubleValue() == 3.3);
            System.out.println(1.1 + 2.2);
            System.out.println((1.1 + 2.2) == 3.3);
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.update("123456".getBytes(StandardCharsets.UTF_8));
            System.out.println(new BigInteger(1, md5.digest()).toString(16));

            MessageDigest md55 = MessageDigest.getInstance("md5");
            String a = new BigInteger(1, md55.digest("123456".getBytes(StandardCharsets.UTF_8))).toString(16);

            System.out.println(a);
            System.out.println(new String(new Hex().encode("123456".getBytes(StandardCharsets.UTF_8))));
            System.out.println(new BigInteger(1, new Hex().encode("123456".getBytes(StandardCharsets.UTF_8))).toString(16));
            System.out.println(new Hex().encode(md55.digest("123456".getBytes(StandardCharsets.UTF_8))));

            Map map = new HashMap();
            map.put("key", null);
            System.out.println("====================" + isEmptyAndNull(map));
            System.out.println(isSmsCode("200000"));
            ;
        } catch (Exception e) {
            // e.printStackTrace();
        }*/

        //System.out.println(isBetween(3, 4, 2));
        //System.out.println(getLastPhoneNumber("15112304365"));
        //System.out.println(getTimestamp());
        //System.out.println(getUUID());
        //System.out.println(System.currentTimeMillis() / 1000);
        //System.out.println();
        // System.out.println(getSuffixType("https://www.xmaibu.com/public/uploads//cache//pic/attachment/image/IWC/IW388103-580x580.JPG"));
    }

}
