package com.luxuryadmin.dto;

import lombok.Data;

@Data
public class WeiXinLoginDTO {

    private String appid;

    private String secret;

    private String js_code;

    private String grant_type;
}
