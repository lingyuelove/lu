package com.luxuryadmin.common.constant;

/**
 * 内置常量
 *
 * @author monkey king
 * @date 2019-12-03 20:55:53
 */
public class ConstantRedisKey {

    /**
     * 平台的所有用户名,key
     */
    public static final String ALL_USERNAME = "_all_username";

    /**
     * redis key值;获取接口是否参数加密开关
     */
    public static final String IS_AES_KEY = "sys_config:param_aes_switch";


    /**
     * redis key值;h5的域名
     */
    public static final String H5DOMAIN = "sys_config:h5domain";

    /**
     * admin token;
     */
    public static final String ADMIN_TOKEN = "admin:token:";
    /**
     * redis key值;获取接口是否签名开关
     */
    public static final String IS_SIGN_KEY = "sys_config:param_sign_switch";

    /**
     * redis key值; 店铺添加员工最大数量限制
     */
    public static final String EMPLOYEE_LIMIT = "sys_config:employee_limit";


    /**
     * redis key值;版本号; 根据当前app上传的版本号和此版本号对比;不一致则更新
     */
    public static final String UPGRADE_VERSION = "sys_config:upgrade:version";


    /**
     * redis key值;下载app的链接
     */
    public static final String UPGRADE_URL = "sys_config:upgrade:url";

    /**
     * redis key值;更新app的提示消息;
     */
    public static final String UPGRADE_MSG = "sys_config:upgrade:msg";


    /**
     * redis key值; 针对ip;24小时内已经获取验证码达到N次,需要进行图形验证码验证
     */
    public static final String IP_SMS_LIMIT = "sys_config:ip_sms_limit";

    /**
     * redis key值; 针对手机号;24小时内已经获取验证码达到N次,需要进行图形验证码验证
     */
    public static final String SMS_LIMIT = "sys_config:sms_limit";

    /**
     * redis key值; 针对手机号;24小时内已经获取验证码达到N次,需要进行图形验证码验证
     */
    public static final String BANNER_INDEX_POP_WINDOW_TIMESTAMP = "sys_config:banner:index_pop_window_timestamp";

    /**
     * 会员费用;默认998一年;
     */
    public static final String VIP_FEE = "sys_config:pay:vip_fee";


    /**
     * 支付开关; on:开 | off:关;  默认是off
     */
    public static final String PAY_SWITCH = "sys_config:pay:switch";

    /**
     * 付费模块地址
     */
    public static final String PAY_URL = "sys_config:pay:url";

    /**
     * 盘点模块地址
     */
    public static final String CHECK_URL = "sys_config:check:url";

    /**
     * 小程序封面地址
     */
    public static final String MP_COVER_IMG = "sys_config:mp_cover_img";


    /**
     * redis key值;shp_user_number表的id值;<br/>
     * 取用户编号时,取的用户编号的id要比此缓存的id大
     */
    public static final String SHP_USER_NUMBER_ID = "shpUserNumber_id";

    /**
     * redis key值;shp_shop_number表的id值;<br/>
     * 取用户编号时,取的用户编号的id要比此缓存的id大
     */
    public static final String SHP_SHOP_NUMBER_ID = "shpShopNumber_id";


    /**
     * 获取-用户手机-注册验证码剩余时间的key值;<br>
     * 拼接用户名使用<br/>
     * eg: SEND_YZM_LEFT_TIME_+"15112304365";
     */
    private static final String SEND_YZM_LEFT_TIME_ = "send_yzm_left_time:";

    /**
     * 用户手机注册-短信验证码的key值;<br>
     * 拼接用户名使用<br/>
     * eg: YZM_SMS_CODE_+EnumSendSmsType.code+"15112304365";
     */
    private static final String YZM_SMS_CODE_ = "yzm:sms_code:";

    /**
     * 获取-用户手机-一天内发送注册验证码总次数的key值;<br>
     * 拼接用户名使用<br/>
     * eg: YZM_DAY_TOTAL_+"15112304365";
     */
    private static final String YZM_DAY_TOTAL_ = "yzm:day_total:";

    /**
     * 获取-用户注册-图形验证码的key值;<br>
     * 拼接用户名使用<br/>
     * eg: IMAGE_CODE_+"15112304365";
     */
    private static final String IMAGE_CODE_ = "image_code:";


    /**
     * shp_user的token-key值;<br/>
     * 拼接用户名使用<br/>
     * eg: SHP_TOKEN_+"15112304365";
     */
    public static final String SHP_TOKEN_ = "shp:token:";

    /**
     * shp_user的shopId-key值;<br/>
     * 拼接用户名使用<br/>
     * eg: SHP_SHOP_+"10000";
     */
    public static final String SHP_SHOP_ID = "shp:shopId:";

    /**
     * 锁单key值;拼接店铺id+商品的业务id组合使用;
     */
    public static final String LOCK_PRODUCT_ = "lock_product:";

    /**
     * redis-key值,总库存;
     */
    public static final String TOTAL_NUM = "totalNum";

    /**
     * redis-key值,可用库存;
     */
    public static final String LEFT_NUM = "leftNum";

    /**
     * redis-key值,锁单数量;
     */
    public static final String LOCK_NUM = "lockNum";

    /**
     * 初始化注册店铺时下发vid值的redisKey值
     */
    public static final String INIT_REGISTER_SHOP = "init:register_shop:";

    /**
     * 初始化开单列表时下发vid值的redisKey值
     */
    public static final String INIT_ISSUE_ORDER = "init:issue_order:";

    /**
     * 初始化创建临时仓下发vid值的redisKey值
     */
    public static final String INIT_CREATE_PRO_TEMP = "init:create_pro_temp:";
    /**
     * 初始化创建临时仓下发vid值的redisKey值
     */
    public static final String INIT_CREATE_PRO_CONVEY = "init:create_pro_convey:";
    /**
     * 初始化开单列表时下发vid值的redisKey值
     */
    public static final String INIT_UPLOAD_PRODUCT = "init:upload_product:";

    /**
     * 初始化账单列表时下发vid值的redisKey值
     */
    public static final String INIT_UPLOAD_FIN_BILL = "init:create_fin_bill:";

    /**
     * 全局店铺权限版本号; 通过比对此版本来决定是否要更新权限缓存
     */
    public static final String SHP_PERM_VERSION = "shp:perm:version";

    /**
     * 临时仓初次请求商品列表页面
     */
    public static final String PRO_TEMP = "pro:temp:";

    /**
     * 寄卖传送初次请求商品列表页面
     */
    public static final String PRO_CONVEY = "pro:convey:";

    /**
     * 用户是否需要更新权限缓存;0:不需要更新; | 1:需要更新;
     */
    public static final String NEED_UPDATE_PERM = "need_update_perm";

    /**
     * 缩略图大小参数--oss
     */
    public static final String IMG_SIZE_SMALL = "_img_size_small";

    /**
     * 产品详情图片大小参数--oss
     */
    public static final String IMG_SIZE_BIG = "_img_size_big";

    /**
     * 商家联盟小程序过期标识符
     */

    public static final String MP_EXPIRE_FLAG = "_mp_expire_flag";

    /**
     * 用来控制首页图标的链接时间戳
     */
    public static final String ICON_TIMESTAMP = "sys_config:icon_timestamp";

    /**
     * 商家联盟试用期时长
     */
    public static final String SHOP_UNION_DAY = "sys_config:shop_union:day";

    /**
     * 商家联盟名单的redisKey,包含买家,卖家
     */
    public static final String SHOP_UNION = "sys_config:shop_union";

    /**
     * 商家联盟名单的redisKey,包含买家,卖家(临时商家,允许查看x天);已经更用SHOP_UNION_SHOP_TEMP来获取；2021-11-01后可去掉此段代码；及相关业务；
     */
    public static final String SHOP_UNION_TEMP = "sys_config:shop_union:temp";

    /**
     * 商家联盟访问商品记录的key;
     */
    public static final String SHOP_UNION_APP_USER_TEMP = "shp:shop_union:appUser:";


    /**
     * 商家联盟的分享权限
     */
    public static final String SHOP_UNION_SHARE_PERM = "shp:shop_union:share_perm";


    /**
     * 商家联盟的分享小程序的默认封面
     */
    public static final String SHOP_UNION_MP_IMG = "shp:shop_union:mp_img";

    /**
     * 商品联盟 填写同行价大于x（redis取值）
     */
    public static final String MPSHOWMINPRICE = "_mp_show_min_price";

    /**
     * 获取-用户手机-注册验证码剩余时间的key值<br/>
     * eg: register_yzm_left_time_15112304365
     *
     * @param username 用户名
     * @return
     */
    public static String getSendYzmLeftTime(String username) {
        return SEND_YZM_LEFT_TIME_ + username;
    }

    /**
     * 获取具体某个用户-注册短信验证码的redis-key值<br/>
     * eg: yzm_sms_code_+EnumSendSmsType.code+"15112304365";
     *
     * @param username 用户名
     * @return
     */
    public static String getYzmSmsCodeKey(String enumSendSmsTypeCode, String username) {
        return YZM_SMS_CODE_ + enumSendSmsTypeCode + ":" + username;
    }

    /**
     * 获取-用户手机-一天内发送注册验证码总次数的key值;<br/>
     * eg: yzm_day_total_15112304365
     *
     * @param username
     * @return
     */
    public static String getYzmDayTotal(String username) {
        return YZM_DAY_TOTAL_ + username;
    }

    /**
     * 获取-用户注册-图形验证码的key值;<br/>
     * eg: image_code_15112304365
     *
     * @param username
     * @return
     */
    public static String getImageCode(String username) {
        return IMAGE_CODE_ + username;
    }

    /**
     * shp_user的token-key值;<br/>
     * eg: shp_token_15112304365<br/>
     * eg: shp_token_9092e6db-9fa8-4351-a556-f36f55bab940
     *
     * @param usernameOrToken
     * @return
     */
    public static String getShpTokenKey(String usernameOrToken) {
        return SHP_TOKEN_ + usernameOrToken;
    }

    public static String getAdminTokenKey(String uuid) {
        return ADMIN_TOKEN + uuid;
    }

    public static String getProDefaultRequest(String tempId) {
        return getProKeyByKey(tempId, ":default_request");
    }

    private static String getProKeyByKey(String tempId, String key) {
        return PRO_TEMP + tempId + key;
    }
    public static String getProConveyKeyByKey(String conveyId) {
        return PRO_CONVEY + conveyId + ":default_request";
    }

    private static String getShpTokenByToken(String token, String key) {
        return SHP_TOKEN_ + token + key;
    }

    private static String getShpShopByShopId(Integer shopId, String key) {
        return SHP_SHOP_ID + shopId + key;
    }

    public static String getEmployeeLimitRedisKeyByShopId(Integer shopId) {
        return getShpShopByShopId(shopId, ":employee_limit");
    }

    public static String getBossUserIdRedisKeyByShopId(Integer shopId) {
        return getShpShopByShopId(shopId, ":boss_user_id");
    }

    public static String getShopNameRedisKeyByShopId(Integer shopId) {
        return getShpShopByShopId(shopId, ":shop_name");
    }

    public static String getShopNumberRedisKeyByShopId(Integer shopId) {
        return getShpShopByShopId(shopId, ":shop_number");
    }

    public static String getVipExpireRedisKeyByShopId(Integer shopId) {
        return getShpShopByShopId(shopId, ":vip_expire");
    }

    public static String getShopMemberRedisKeyByShopId(Integer shopId) {
        return getShpShopByShopId(shopId, ":is_member");
    }

    public static String getMemberStateRedisKeyByShopId(Integer shopId) {
        return getShpShopByShopId(shopId, ":member_state");
    }

    public static String getShopIdRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":shop_id");
    }

    public static String getUserNumberRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":user_number");
    }

    public static String getShpUserIdRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":user_id");
    }

    public static String getShpUsernameRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":username");
    }

    public static String getBindWeChatRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":bind_we_chat");
    }

    public static String getBindAppleRedisKeyByToken(String token) {
        return getShpTokenByToken(token, ":bind_apple");
    }

    /**
     * 锁单key值;拼接店铺id+商品的业务id组合使用;<br/>
     * eg: lock_product_10001_abc123
     *
     * @param shopId
     * @param bizId
     * @return
     */
    public static String getLockProductByBizId(int shopId, String bizId) {
        return LOCK_PRODUCT_ + shopId + ":" + bizId;
    }

    /**
     * 注册店铺时下发的vid
     * eg: init_issue_order_10001_abc123
     *
     * @param shopId
     * @param userId
     * @return
     */
    public static String getInitRegisterShopKey(int shopId, int userId) {
        return INIT_REGISTER_SHOP + shopId + ":" + userId;
    }

    /**
     * 锁单key值+shopId+userId商品的业务id组合使用;<br/>
     * eg: init_issue_order_10001_abc123
     *
     * @param shopId
     * @param userId
     * @return
     */
    public static String getInitIssueOrderKey(int shopId, int userId) {
        return INIT_ISSUE_ORDER + shopId + ":" + userId;
    }

    /**
     * 锁单key值+shopId+userId商品的业务id组合使用;<br/>
     * eg: init_issue_order_10001_abc123
     *
     * @param shopId
     * @param userId
     * @return
     */
    public static String getCreateProTempKey(int shopId, int userId) {
        return INIT_CREATE_PRO_TEMP + shopId + ":" + userId;
    }
    public static String getCreateProConveyKey(int shopId, int userId) {
        return INIT_CREATE_PRO_CONVEY + shopId + ":" + userId;
    }
    public static String getCreateFinBillKey(int shopId, int userId) {
        return INIT_UPLOAD_FIN_BILL + shopId + ":" + userId;
    }

    /**
     * 锁单key值+shopId+userId商品的业务id组合使用;<br/>
     * eg: init_upload_product_10001_10000
     *
     * @param shopId
     * @param userId
     * @return
     */
    public static String getInitUploadProductKey(int shopId, int userId) {
        return INIT_UPLOAD_PRODUCT + shopId + ":" + userId;
    }


    /**
     * 获取权限key
     *
     * @param shopId
     * @param userId
     * @return
     */
    public static String getPermKeyByShopIdUserId(int shopId, int userId) {
        return "shp:perm:" + shopId + ":" + userId;
    }

    public static String getNotDownloadProductKey(int shopId, String bizId) {
        return "notDownloadProduct:" + shopId + ":" + bizId;
    }

    public static String getBindOtherLoginKey(String openId) {
        return "bind_other_login:" + openId;
    }


    /**
     * 商家联盟访问商品的记录key
     * @param userName
     * @return
     */
    public static String getShopUnionAppUserTemp(String userName) {
        return SHOP_UNION_APP_USER_TEMP + userName;
    }
    public static void main(String[] args) {
        System.out.println(getLockProductByBizId(10001, "abc"));
    }


}
