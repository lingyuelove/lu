package com.luxuryadmin.admin.common;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.RedisKeyAdminLogin;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.VersionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * 公共的控制层类<br/>
 * 所有的Controller层; 继承此类; 特殊情况除外(需说明)
 *
 * @author liuguangping
 */
@Slf4j
public class BaseAdminController {

    //private static HttpServletRequest request;
    @Autowired
    HttpServletRequest request;

    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected ServicesUtil servicesUtil;

    private static HttpSession session;

    protected static final int PAGE_SIZE_10 = 10;
    protected static final int PAGE_SIZE_20 = 20;
    protected static final int PAGE_SIZE_30 = 30;
    protected static final int PAGE_SIZE_50 = 50;
    protected static final int PAGE_SIZE_100 = 100;
    protected static final int PAGE_SIZE_500 = 500;
    protected static final int PAGE_SIZE_1000 = 1000;
    protected static final String ALL = "all";

    private int shopId = 0;

    private int userId = 0;

    /**
     * 获取请求业务流水号
     *
     * @return
     */
    protected String getToken() {
        //从header中获取token
        String token = request.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if (LocalUtils.isEmptyAndNull(token)) {
            token = request.getParameter("token");
        }
        return token;
    }

    /**
     * 获取基础参数
     *
     * @return
     */
    protected BasicParam getBasicParam() {
        BasicParam bp = new BasicParam();
        bp.setDeviceId(request.getParameter("deviceId"));
        bp.setApiVersion(request.getParameter("apiVersion"));
        bp.setAppVersion(request.getParameter("appVersion"));
        bp.setTimestamp(request.getParameter("timestamp"));
        bp.setPlatform(request.getParameter("platform"));
        bp.setPhoneType(request.getParameter("phoneType"));
        bp.setPhoneSystem(request.getParameter("phoneSystem"));
        bp.setNetType(request.getParameter("netType"));
        bp.setChannel(request.getParameter("channel"));
        bp.setSign(request.getParameter("sign"));
        bp.setPageNum(getPageNum());
        bp.setPageSize(getPageSize());
        bp.setIp(getIpAddr());
        return bp;
    }


    protected int getPageNum() {
        String pageNumStr = request.getParameter("pageNum");
        return (LocalUtils.isEmptyAndNull(pageNumStr) ? 1 : Integer.parseInt(pageNumStr));
    }

    protected int getPageSize() {
        String pageNumStr = request.getParameter("pageSize");
        int pageSize = (LocalUtils.isEmptyAndNull(pageNumStr) ? PAGE_SIZE_10 : Integer.parseInt(pageNumStr));
        return Math.min(pageSize, PAGE_SIZE_50);
    }

    /**
     * 获取当前登录的userId
     *
     * @return
     */
    protected int getUserId() {
        String token = getToken();
        String userIdValue;
        if (!LocalUtils.isEmptyAndNull(token) && token.startsWith("admin")) {
            //后台登录的token
            userIdValue = redisUtil.get(RedisKeyAdminLogin.getShpUserIdRedisKeyByToken(token));
        } else {
            userIdValue = redisUtil.get(ConstantRedisKey.getShpUserIdRedisKeyByToken(token));
        }
        userIdValue = LocalUtils.isEmptyAndNull(userIdValue) ? "0" : userIdValue;
        if (ConstantCommon.ZERO.equals(userIdValue)) {
            log.error("%%%%%%%%userId为0%%%%%%%%; token: {}", token);
        }
        return Integer.parseInt(userIdValue);
    }

    /**
     * 获取当前request
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        //存在跨域风险，会获取不到Request
        return request;
    }

    /**
     * 获取当前请求的session
     *
     * @return
     */
    public HttpSession getSession() {
        return request.getSession();
    }

    /**
     * 获取当前的项目路径
     * 项目在服务器上的发布路径--<br/>
     * eg: C:\Users\Administrator\AppData\Local\Temp\tomcat-docbase.2088367245567415554.8888\
     *
     * @return
     */
    public String getBasePath() {
        return getRequest().getServletContext().getRealPath("/");
    }

    /**
     * 获取请求的路径<br/>
     * eg: path：/user/listAll
     *
     * @return
     */
    public String getServletPath() {
        return getRequest().getServletPath();
    }

    /**
     * 获取请求的完整路径
     * ip:port/project/path ： http://192.168.1.212:8888/user/listAll
     *
     * @return
     */
    public String getRequestURL() {
        return getRequest().getRequestURL().toString();
    }

    /**
     * 获取请求的相对路径
     * /project/path ： /user/listAll
     *
     * @return
     */
    public String getRequestURI() {
        return getRequest().getRequestURI().toString();
    }

    /**
     * 获取请求的项目路径
     * project ：/api
     *
     * @return
     */
    public String getContextPath() {
        return getRequest().getContextPath();
    }

    /**
     * 获取请求的参数值
     *
     * @param attr
     * @return
     */
    public Object getAttribute(String attr) {
        if (!StringUtils.isEmpty(attr)) {
            return getRequest().getAttribute(attr);
        }
        return "";
    }

    public String getParameter(String attr) {
        if (!StringUtils.isEmpty(attr)) {
            return getRequest().getParameter(attr);
        }
        return "";
    }


    /**
     * 获取请求的IP地址
     *
     * @return
     */
    public String getIpAddr() {
        HttpServletRequest req = getRequest();
        String ipAddress = null;
        ipAddress = req.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = req.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = req.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }

        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;

    }



    /**
     * 全局刷新权限缓存
     */
    protected void refreshPerm() {
        //全局缓存版本号
        String permVersionKey = ConstantRedisKey.SHP_PERM_VERSION;
        String value = redisUtil.get(permVersionKey);
        value = LocalUtils.isEmptyAndNull(value) ? "1" : value;
        int redisPermVersion = Integer.parseInt(value);
        redisPermVersion++;
        redisUtil.set(permVersionKey, redisPermVersion + "");
    }


}