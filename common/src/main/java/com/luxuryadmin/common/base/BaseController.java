package com.luxuryadmin.common.base;

import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.RedisKeyAdminLogin;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * 公共的控制层类<br/>
 * 所有的Controller层; 继承此类; 特殊情况除外(需说明)
 *
 * @author liuguangping
 */
@Slf4j
public class BaseController {

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

    /**
     * 通过注解获取请求的Request，解决跨域获取不到Request问题
     */

    //@ModelAttribute
    //private void setParameter(HttpServletRequest request) {
    //    BaseController.request = request;
    //}


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
     * 获取请求业务流水号
     *
     * @return
     */
    protected String getAdminToken() {
        //从header中获取token
        String token = request.getHeader("Authorization");

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
     * 获取当前登录的shopId
     *
     * @return
     */
    protected int getShopId() {
        String token = getToken();
        String shopIdValue;
        if (!LocalUtils.isEmptyAndNull(token) && token.startsWith("admin")) {
            shopIdValue = redisUtil.get(RedisKeyAdminLogin.getShopIdRedisKeyByToken(getToken()));
        } else {
            shopIdValue = redisUtil.get(ConstantRedisKey.getShopIdRedisKeyByToken(getToken()));
        }
        shopIdValue = LocalUtils.isEmptyAndNull(shopIdValue) ? "0" : shopIdValue;
        return Integer.parseInt(shopIdValue);
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
     * 获取当前登录的userId
     *
     * @return
     */
    protected int getAdminUserId() {
        String token = getAdminToken();
        String  userIdValue = redisUtil.get(ConstantRedisKey.getAdminTokenKey(token));
        userIdValue = LocalUtils.isEmptyAndNull(userIdValue) ? "0" : userIdValue;
        if (ConstantCommon.ZERO.equals(userIdValue)) {
            log.error("%%%%%%%%userId为0%%%%%%%%; token: {}", token);
        }
        return Integer.parseInt(userIdValue);
    }
    /**
     * 获取当前登录的shopNumber
     *
     * @return
     */
    protected int getUserNumber() {
        String token = getToken();
        String userNumberValue;
        if (!LocalUtils.isEmptyAndNull(token) && token.startsWith("admin")) {
            userNumberValue = redisUtil.get(RedisKeyAdminLogin.getUserNumberRedisKeyByToken(token));
        } else {
            userNumberValue = redisUtil.get(ConstantRedisKey.getUserNumberRedisKeyByToken(token));
        }

        userNumberValue = LocalUtils.isEmptyAndNull(userNumberValue) ? "0" : userNumberValue;
        return Integer.parseInt(userNumberValue);
    }


    /**
     * 获取当前登录的username
     *
     * @return
     */
    protected String getUsername() {
        String token = getToken();
        String username;
        if (!LocalUtils.isEmptyAndNull(token) && token.startsWith("admin")) {
            username = RedisKeyAdminLogin.getShpUsernameRedisKeyByToken(token);
        } else {
            username = ConstantRedisKey.getShpUsernameRedisKeyByToken(token);
        }
        return redisUtil.get(username);
    }


    /**
     * 是否绑定微信登录; 0:未绑定 | 1:已绑定
     *
     * @return
     */
    protected String getBindWeChat() {
        String bindWeChat = ConstantRedisKey.getBindWeChatRedisKeyByToken(getToken());
        return LocalUtils.isEmptyAndNull(bindWeChat) ? "0" : bindWeChat;
    }

    /**
     * 是否绑定苹果登录; 0:未绑定 | 1:已绑定
     *
     * @return
     */
    protected String getBindApple() {
        String bindApple = ConstantRedisKey.getBindAppleRedisKeyByToken(getToken());
        return LocalUtils.isEmptyAndNull(bindApple) ? "0" : bindApple;
    }

    /**
     * 获取店铺经营者的userId
     *
     * @return
     */
    protected int getBossUserId() {
        String bossUserIdValue = redisUtil.get(ConstantRedisKey.getBossUserIdRedisKeyByShopId(getShopId()));
        bossUserIdValue = LocalUtils.isEmptyAndNull(bossUserIdValue) ? "0" : bossUserIdValue;
        return Integer.parseInt(bossUserIdValue);
    }

    /**
     * 当前用户是否为经营者
     *
     * @return
     */
    protected boolean isBossForCurrentUser() {
        return getUserId() == getBossUserId();
    }


    /**
     * 获取当前登录的shopNumber
     *
     * @return
     */
    protected String getShopNumber() {
        String shopNumberValue = redisUtil.get(ConstantRedisKey.getShopNumberRedisKeyByShopId(getShopId()));
        return LocalUtils.isEmptyAndNull(shopNumberValue) ? "0" : shopNumberValue;
    }

    /**
     * 获取当前登录的shopName
     *
     * @return
     */
    protected String getVipExpire() {
        String vipExpire = redisUtil.get(ConstantRedisKey.getVipExpireRedisKeyByShopId(getShopId()));
        vipExpire = LocalUtils.isEmptyAndNull(vipExpire) ? "" : vipExpire;
        return vipExpire;
    }

    /**
     * 获取当前登录的shopName
     *
     * @return
     */
    protected String getShopName() {
        String shopNameValue = redisUtil.get(ConstantRedisKey.getShopNameRedisKeyByShopId(getShopId()));
        shopNameValue = LocalUtils.isEmptyAndNull(shopNameValue) ? "0" : shopNameValue;
        return shopNameValue;
    }

    /**
     * 店铺是否会员身份
     *
     * @return
     */
    protected boolean shopIsMember() {
        String isMember = getIsMemberValue();
        return "yes".equals(isMember);
    }

    protected String getIsMemberValue() {
        String memberKey = ConstantRedisKey.getShopMemberRedisKeyByShopId(getShopId());
        String isMember = redisUtil.get(memberKey);
        if (LocalUtils.isEmptyAndNull(isMember)) {
            redisUtil.set(memberKey, ConstantCommon.NO);
            isMember = "no";
        }
        return isMember;
    }


    /**
     * 会员状态: 0:非会员; 1:体验会员;2:正式会员;3:靓号会员
     *
     * @return
     */
    protected String getMemberState() {
        String memberState = redisUtil.get(ConstantRedisKey.getMemberStateRedisKeyByShopId(getShopId()));
        return LocalUtils.isEmptyAndNull(memberState) ? "0" : memberState;
    }

    /**
     * 获取当前request
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        //存在跨域风险，会获取不到Request
        // return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 获取当前请求的session
     *
     * @return
     */
    public HttpSession getSession() {
        //return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
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
//        return getRequest().getRequestURI();
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


    /**
     * 当前登录用户是否拥有当前页面功能权限;<br/>
     * 0:无权限 | 1:有权限
     *
     * @param perm
     * @return 0:无权限 | 1:有权限
     */
    protected String hasPermToPageWithCurrentUser(String userPerms, String perm) {
        return hasPermWithCurrentUser(userPerms, perm) ? "1" : "0";
    }

    /**
     * 当前登录用户是否拥有当前页面功能权限;<br/>
     * 0:无权限 | 1:有权限
     *
     * @param perm
     * @return 0:无权限 | 1:有权限
     */
    protected String hasPermToPageWithCurrentUser(String perm) {
        String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
        return hasPermWithCurrentUser(userPerms, perm) ? "1" : "0";
    }

    /**
     * 当前登录用户是否具备权限
     *
     * @param permission 权限标识符
     * @return
     */
    protected boolean hasPermWithCurrentUser(String userPerms, String permission) {
        return servicesUtil.hasPermission(userPerms, permission);
    }

    /**
     * 当前登录用户是否具备权限
     *
     * @param permission 权限标识符
     * @return
     */
    protected boolean hasPermWithCurrentUser(String permission) {
        String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
        return servicesUtil.hasPermission(userPerms, permission);
    }

    /**
     * 只有经营者才能操作该功能
     */
    protected void onlyBossOperate() {
        int userId = getUserId();
        int bossUserId = getBossUserId();
        if (userId != bossUserId) {
            throw new MyException("只有经营者才能操作!");
        }
    }

    /**
     * 检测当前用户登录的版本是否符合我们预期的版本
     *
     * @param androidVersion
     * @param iosVersion
     * @return
     */
    protected boolean newAndroidAndIOSVersion(String androidVersion, String iosVersion) {
        try {
            BasicParam bp = getBasicParam();
            String platform = bp.getPlatform().toLowerCase();
            String paramAppVersion = bp.getAppVersion();
            //如果是iOS
            if ("ios".equals(platform)) {
                int num = VersionUtils.compareVersion(paramAppVersion, iosVersion);
                return num >= 0;
            } else if ("android".equals(platform)) {
                int num = VersionUtils.compareVersion(paramAppVersion, androidVersion);
                return num >= 0;
            }
            return false;
        } catch (Exception e) {
            log.error("========版本比较错误=====安卓: {}==iOS: {}", androidVersion, iosVersion);
        }
        return false;
    }


    protected String getUserPayUrl() {
        String description = "奢当家店铺年费会员";
        String total = redisUtil.get(ConstantRedisKey.VIP_FEE);
        Map<String, Object> basicMap = LocalUtils.convertBeanToMap(getBasicParam());
        basicMap.put("description", description);
        basicMap.put("total", total);
        StringBuilder sb = new StringBuilder("https://www.luxuryadmin.com/h5/pay/pay.html?");
        sb.append("token=").append(getToken());
        sb.append("&description=").append(description);
        sb.append("&shopName=").append(getShopName());
        sb.append("&total=").append(total);
        sb.append("&sign=").append(DESEncrypt.encodeUsername(JSONObject.toJSONString(basicMap)));
        return sb.toString();
    }

    /**
     * 验证时间范围是否在3个月之内
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     */
    protected void validTimeScope(String startDateTime, String endDateTime) {
        try {
            if (LocalUtils.isEmptyAndNull(startDateTime)
                    || LocalUtils.isEmptyAndNull(endDateTime)) {
                throw new MyException("请选择导出时间范围!");
            }
            Date st = DateUtil.parse(startDateTime);
            Date et = DateUtil.parse(endDateTime);
            if (!DateUtil.isBetweenDate(3, st, et)) {
                throw new MyException("时间范围不允许超过3个月!");
            }
        } catch (ParseException e) {
            throw new MyException("时间格式错误!");
        }
    }

}