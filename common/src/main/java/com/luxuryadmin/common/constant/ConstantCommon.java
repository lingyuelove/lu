package com.luxuryadmin.common.constant;

/**
 * @author monkey king
 * @date 2019-12-17 18:08:41
 */
public class ConstantCommon {

    /**
     * 系统自动初始化时的备注
     */
    public static final String AUTO_REMARK = "系统自动初始化";

    public static final String ALL = "all";

    public static final String ZERO = "0";

    public static final String ONE = "1";

    public static final String YES = "yes";

    public static final String NO = "no";

    public static final String OK = "ok";

    public static final String DEV = "dev";

    public static final String PRO = "pro";

    public static final String TEST = "test";

    public static final String NO_PERM = "noPerm";

    /**
     * 全小写的iOS
     */
    public static final String IOS = "ios";

    /**
     * 全小写的android
     */
    public static final String ANDROID = "android";

    /**
     * 店铺默认封面
     */
    public static final String DEFAULT_SHOP_COVER_IMG = "/default/shopCoverImg.png";

    /**
     * 店铺默认头像
     */
    public static final String DEFAULT_SHOP_HEAD_IMG = "/default/shopHeadImg.png";

    /**
     * 用户默认头像
     */
    public static final String DEFAULT_USER_HEAD_IMG = "/default/headImg.png";

    /**
     * 短期登录有效时间;(天)
     */
    public static final int SHORT_LOGIN_TIME = 3;

    /**
     * 长期登录有效时间;(天)
     */
    public static final int LONG_LOGIN_TIME = 15;

    /**
     * 锁单时间(分钟)
     */
    public static final int LOCK_PRODUCT_MINUTE = 15;

    /**
     * 阿里云oss域名,容器启动时会为此赋值
     */
    public static String ossDomain;
    /**
     * 运行环境,容器启动时会为此赋值
     */
    public static String springProfilesActive;

    /**
     * 删除状态 - 非删除
     */
    public static final String DEL_OFF = "0";

    /**
     * 删除状态 - 删除
     */
    public static final String DEL_ON = "1";

    /**
     * 是否有效状态 - 无效
     */
    public static final String STATE_OFF = "0";

    /**
     * 是否有效状态 - 有效
     */
    public static final String STATE_ON = "1";

    /**
     * 商品上架 参数
     */
    public static final String PRO_STATE_CODE_UP = "1";
    /**
     * 商品下架 参数
     */
    public static final String PRO_STATE_CODE_DOWN = "0";
    /**
     * 未上架 首次入库
     */
    public static final Integer PRO_STATE_FIRST_PUT = 10;
    /**
     * 上架 首次上架
     */
    public static final Integer PRO_STATE_FIRST_UP = 20;
    /**
     * 未上架 再次入库-上架后人工下架
     */
    public static final Integer PRO_STATE_AGAIN_PUT = 11;
    /**
     * 上架 再次上架
     */
    public static final Integer PRO_STATE_AGAIN_UP = 21;


}
