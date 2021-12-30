package com.luxuryadmin.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.encrypt.AESUtils;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.aliyun.RobotHelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author monkey king
 * Happy Coding, Happy Life
 * @Description: 接口请求日志
 * <p>
 * -------------------------------------------------jar包引入 Start
 * | - aop
 * <dependency>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-starter-aop</artifactId>
 * </dependency>
 * ..........................................................
 * | - lombok
 * <dependency>
 * <groupId>org.projectlombok</groupId>
 * <artifactId>lombok</artifactId>
 * <scope>provided</scope>
 * </dependency>
 * ..........................................................
 * | - gson
 * <dependency>
 * <groupId>com.google.code.gson</groupId>
 * <artifactId>gson</artifactId>
 * </dependency>
 * -------------------------------------------------jar包引入 End
 * <p>
 * 切点定义{@link #controllerLogPointcut()}
 * 请求日志{@link #controllerLog(ProceedingJoinPoint)}
 * 获取IP{@link #getIpAddress(HttpServletRequest)}
 * @date 2019-12-02 19:44:50
 */
@Slf4j
@Aspect
@Component
public class WebAspect {
    /**
     * 接口基础参数名称;如有追加,直接加在字符串最后面,逗号隔开;无序;
     */
    private static final String BASE_KEY = "deviceId,apiVersion,appVersion,timestamp,platform,phoneType,phoneSystem,netType,channel,sign";

    /**
     * 该接口不受配置影响,一直都是加密返回数据;永远加密接口
     */
    private static final String[] EVER_AES_URL = new String[]{"/sys/getAppSysConfig", "/sys/version/getAppVersionInfo"};

    /**
     * 不加密接口;集合里的内容都是uri的前缀;
     */
    private static final String[] NOT_AES_URL = new String[]{"/tool", "/shop/wxPay","/temp/getKindOfCountData"};


    /**
     * md5编码的盐值
     */
    public static final String SIGN_KEY = "sign_key";


    @Autowired
    private RedisUtil redisUtil;

    /**
     * 正则
     */
    private final String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
    /**
     * \\b表示限定单词边界
     * - 比如:select不通过,1select则可以
     */
    private final Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    @Autowired
    private Gson gson;

    /**
     * 切点定义
     *
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    @Pointcut("execution(* com.*.api..*(..)) || execution(* com.*.apiadmin..*(..)) || execution(* com.*.admin..*(..)) || @annotation(com.luxuryadmin.common.aop.check.RequestRequire)")
    public void controllerLogPointcut() {
    }

    /**
     * 请求日志
     *
     * @param proceedingJoinPoint 切点
     * @return
     * @throws Throwable
     * @date 2019-12-02 19:44:50
     * @author monkey king
     */
    @Around("controllerLogPointcut()")
    public Object controllerLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("*************************************************");
        // 获取被注解的方法
        MethodInvocationProceedingJoinPoint mjp = (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
        MethodSignature signature = (MethodSignature) mjp.getSignature();
        // 获取注解的方法参数列表
        Object[] args = proceedingJoinPoint.getArgs();

        log.info("====== Before request biz handle " + new Timestamp(System.currentTimeMillis()) + " =======");
        String requestURI = httpServletRequest.getRequestURI();
        //log.info("端机IP: " + getIpAddress(httpServletRequest));
        //log.info("请求URL: " + httpServletRequest.getRequestURL());
        //log.info("请求URI: " + requestURI);
        //log.info("请求参数: " + gson.toJson(httpServletRequest.getParameterMap()));
        //log.info("内部方法: " + signature.toString());
        //log.info("方法参数: " + Arrays.toString(args));


        StringBuffer logSb = new StringBuffer();
        logSb.append("       请求信息如下:\n");
        logSb.append("       端机IP: ").append(getIpAddress(httpServletRequest)).append("\n");
        logSb.append("       请求URL: ").append(httpServletRequest.getRequestURL()).append("\n");
        logSb.append("       请求URI: ").append(requestURI).append("\n");
        logSb.append("       请求参数: ").append(gson.toJson(httpServletRequest.getParameterMap())).append("\n");
        logSb.append("       内部方法: ").append(signature.toString()).append("\n");
        logSb.append("       方法参数: ").append(Arrays.toString(args)).append("\n");
        log.info(logSb.toString());
        String appVersion = httpServletRequest.getParameter("appVersion");

        // 获得方法
        Method method = signature.getMethod();

        if (isSign()) {
            //校验基础参数
            baseParamCheck(httpServletRequest);

            //校验前端的签名
            validateSign(httpServletRequest);
        }


        // 获取方法上的注解
        RequestRequire require = method.getAnnotation(RequestRequire.class);
        if (require != null) {
            if (args == null || args.length == 0) {
                log.info("入参校验: 参数不能为空");
                throw new MyException(EnumCode.ERROR_VALIDATE_PARAM, "参数不能为空");
            }

            if (require.check()) {
                dtoCheck(httpServletRequest, signature, args, require);
            } else {
                paramCheck(httpServletRequest, signature, args);
            }
        }

        long startTime = System.currentTimeMillis();
        Object object = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        Object jsonObject = JSONObject.toJSON(object);
        long msTime = (endTime - startTime);
        log.info("请求耗时(ms): " + msTime);
        logSb.append("       请求耗时(ms): " + msTime);
        ThreadUtils.getInstance().executorService.execute(() -> {
            if (msTime > 3000) {
                RobotHelperUtil.timeoutTip(logSb.toString(), null, false);
            }
        });
        //log.info("请求返回值: " + jsonObject);
        log.info("====== After request biz handle " + new Timestamp(System.currentTimeMillis()) + " ========");
        log.info("***********************END**************************");
        log.info("");
        Object newObject;
        BaseResult baseResult;
        if (object instanceof BaseResult) {
            baseResult = ((BaseResult) object);
            //baseResult.setTimestamp(System.currentTimeMillis() + "");
            //返回参数data的JSON格式字符串
            // String dataJSONObject = JSONObject.toJSONString(baseResult.getData());
            String dataJSONObject = JSON.toJSONString(baseResult.getData(), SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
            //String dataJSONObject = JSON.toJSONStringWithDateFormat(baseResult.getData(), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
            //JSONObject sortJSONObject = LocalUtils.sortJSONObject(JSONObject.parseObject(dataJSONObject));
            List<String> codeList = new ArrayList<>();
            codeList.add("ok");
            codeList.add("expired");
            codeList.add("soon");
            codeList.add(EnumCode.OK_NOT_EXIST_USER_SHOP.getCode());
            boolean isOk = codeList.contains(baseResult.getCode());
            //判断是否有必须加密的URI
            List<String> listUri = Arrays.asList(EVER_AES_URL);
            boolean aesURIBoolean = listUri.contains(requestURI);

            List<String> notAesUrlList = Arrays.asList(NOT_AES_URL);

            boolean notAesURIBoolean = false;
            //检测当前的URI是否不需加密;
            for (String notAesUrl : notAesUrlList) {
                if (requestURI.startsWith(notAesUrl)) {
                    notAesURIBoolean = true;
                    break;
                }
            }

            //不在未加密排除的URI内
            if (!notAesURIBoolean) {
                boolean aesBoolean = isOk && isAes(appVersion);
                if (aesURIBoolean || aesBoolean) {
                    String dataJsonString = LocalUtils.isEmptyAndNull(dataJSONObject) ? "" : dataJSONObject;
                    log.debug("=====dataJsonString=========: " + dataJsonString);
                    String aesString = AESUtils.encrypt(dataJsonString);
                    baseResult.setData(aesString);
                }
            }

            //String sign = LocalUtils.generatorSing(baseResult);
            //baseResult.setSign(sign);
            newObject = LocalUtils.returnSignBaseResult(baseResult);
        } else {
            newObject = object;
        }
        return newObject;
    }

    /**
     * 获取IP
     * <p>
     * 不使用request.getRemoteAddr()
     * | - 有可能用户使用了代理软件方式避免真实IP地址
     * <p>
     * 如果通过多级反向代理
     * | - X-Forwarded-For的值不止一个,而是一串IP值
     * | - 用户端的真实IP取X-Forwarded-For中第一个非unknown的有效IP字符串
     * <p>
     * 返回请求行中的资源名称
     * * - request.getRequestURI()
     * 获得客户端发送请求的完整url
     * * - request.getRequestURL().toString()
     * 返回发出请求的IP地址
     * * - request.getRemoteAddr()
     * 返回请求行中的参数部分
     * * - request.getQueryString()
     * 返回发出请求的客户机的主机名
     * * - request.getRemoteHost()
     * 返回发出请求的客户机的端口号
     * * - request.getRemotePort()
     *
     * @param request
     * @return
     * @date 2019-12-02 19:44:50
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = null;

        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        // Proxy-Client-IP：apache 服务代理
        if (ipAddresses == null
                || ipAddresses.length() == 0
                || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        // WL-Proxy-Client-IP：weblogic 服务代理
        if (ipAddresses == null
                || ipAddresses.length() == 0
                || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        // HTTP_CLIENT_IP：有些代理服务器
        if (ipAddresses == null
                || ipAddresses.length() == 0
                || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        // X-Real-IP：nginx服务代理
        if (ipAddresses == null
                || ipAddresses.length() == 0
                || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP");
        }

        // 有些网络通过多层代理,那么获取到的ip就会有多个,一般都是通过(,)分割开来,并且第一个ip为客户端的真实IP
        if (ipAddresses != null
                && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        // 还是不能获取到,最后再通过request.getRemoteAddr()获取
        if (ip == null || ip.length() == 0
                || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    private void dtoCheck(HttpServletRequest httpServletRequest, MethodSignature signature, Object[] args, RequestRequire require) throws Throwable {
        // 以防万一，将中文的逗号替换成英文的逗号
        String fieldNames = require.require().replace("，", ",");

        // 从参数列表中获取参数对象
        Object parameter = null;
        for (Object pa : args) {
            //class相等表示是同一个对象
            if (pa.getClass() == require.parameter()) {
                parameter = pa;
            }
        }

        // 通过反射去和指定的属性值判断是否非空
        // 获得参数的class
        Class aClass = parameter.getClass();

        // 遍历参数，找到是否为空
        for (String name : fieldNames.split(",")) {
            Field declaredField = aClass.getDeclaredField(name);
            String fieldName = declaredField.getName();
            declaredField.setAccessible(true);

            Object fieldObject = declaredField.get(parameter);

            if (fieldObject == null) {
                log.info("入参校验: 参数" + fieldName + "不能为空");
                throw new MyException(EnumCode.ERROR_VALIDATE_PARAM, "参数" + fieldName + "不能为空");
            }

            //字符串等类型还需要做多重判断
            boolean classString = "class java.lang.String".equals(declaredField.getGenericType().toString());
            boolean parameterValidate = (declaredField.get(parameter).toString().trim().length() == 0 || sqlPattern.matcher((String) declaredField.get(parameter)).find());
            if (classString && parameterValidate) {
                log.info("入参校验: 参数为空或参数含不合法字符");
                throw new MyException(EnumCode.ERROR_VALIDATE_PARAM, "参数为空或参数含不合法字符");
            }
        }

    }

    private void paramCheck(HttpServletRequest httpServletRequest, MethodSignature signature, Object... args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object param = args[i];
                //判断是否为Null
                if (param == null) {
                    log.info("入参校验: 参数不能为空");
                    throw new MyException(EnumCode.ERROR_VALIDATE_PARAM, "参数不能为空");
                }
                //字符串等类型还需要做多重判断
                if (param instanceof String && (param.toString().trim().length() == 0 || sqlPattern.matcher((String) param).find())) {
                    Matcher matcher2 = sqlPattern.matcher((String) param);
                    log.info("入参校验: 参数为空或参数含不合法字符,{}",matcher2.groupCount());
                    for(int j=0; j<=matcher2.groupCount(); j++){
                        log.info(j+":"+matcher2.group(j));
                    }
                    throw new MyException(EnumCode.ERROR_VALIDATE_PARAM, "参数为空或参数含不合法字符");
                }
            }
        } else {
            log.info("入参校验: 参数为空或参数含不合法字符");
            throw new MyException(EnumCode.ERROR_VALIDATE_PARAM, "参数为空或参数含不合法字符");
        }
    }

    /**
     * 基础参数校验;每个请求都包含此参数;
     *
     * @param httpServletRequest
     */
    private void baseParamCheck(HttpServletRequest httpServletRequest) {
        Map<String, String[]> map = httpServletRequest.getParameterMap();
        String[] myKeys = BASE_KEY.split(",");
        Arrays.stream(myKeys).forEach(key -> {
            if (!map.containsKey(key)) {
                throw new MyException(EnumCode.ERROR_VALIDATE_PARAM);
            }
        });
    }

    /**
     * 校验端上的签名;
     *
     * @param httpServletRequest
     */
    private void validateSign(HttpServletRequest httpServletRequest) {
        Map<String, String[]> map = httpServletRequest.getParameterMap();
        List<String> keyList = new ArrayList<String>(map.keySet());
        String signStr = "sign";
        if (keyList.contains(signStr)) {
            keyList.remove(signStr);
            Collections.sort(keyList);
            String paramString = "";
            for (String key : keyList) {
                paramString += (key + "=" + httpServletRequest.getParameter(key) + "&");
            }
            paramString = paramString.substring(0, paramString.length() - 1);
            log.info(paramString);
            //md5加盐编码
            String sign = DESEncrypt.md5Hex(paramString + SIGN_KEY);
            log.info("====签名===: " + sign);
            String paramSign = httpServletRequest.getParameter(signStr);
            if (!sign.equals(paramSign)) {
                throw new MyException(EnumCode.ERROR_SIGN);
            }
        } else {
            throw new MyException(EnumCode.ERROR_SIGN);
        }
    }


    /**
     * 是否校验参数签名;
     *
     * @return true:校验签名 | false:不校验签名
     */
    private boolean isSign() {
        try {
            String signValue = "1";
            String isSignValue = redisUtil.get(ConstantRedisKey.IS_SIGN_KEY);
            boolean isSignBoolean = LocalUtils.isEmptyAndNull(isSignValue);
            if (!isSignBoolean && signValue.equals(isSignValue)) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MyException(EnumCode.ERROR_SYSTEM_INIT, "WebAspect");
        }
        return false;
    }

    /**
     * 是否参数AES加密传输
     *
     * @return
     */
    private boolean isAes(String appVersion) {
        //没有版本号则强行对称加密
        if (LocalUtils.isEmptyAndNull(appVersion)) {
            return true;
        }
        //安卓和iOS的版本是否在2.3或以上
        //boolean appAES = VersionUtils.compareVersion(appVersion, "2.3.0") >= 0;
        //if (!appAES) {
        //    return false;
        //}
        String isAesValue = redisUtil.get(ConstantRedisKey.IS_AES_KEY);
        return !ConstantCommon.ZERO.equals(isAesValue);
    }

}
