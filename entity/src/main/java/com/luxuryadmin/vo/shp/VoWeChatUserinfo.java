package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 用户授权微信登录得到的微信信息
 *
 * @author monkey king
 * @date 2021-01-26 23:18:28
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoWeChatUserinfo {

    private String openId;

    private String nickname;

    /**
     * 普通用户性别，1 为男性，2 为女性
     */
    private String sex;

    private String province;

    private String city;

    /**
     * 国家，如中国为 CN
     */
    private String country;

    private String headImgUrl;

    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的。
     */
    private String unionId;
}
