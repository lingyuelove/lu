package com.luxuryadmin.vo.wx;

import lombok.Data;

@Data
public class VOLogin {
    private String session_key;
    private String openid;
    private String unionid;
}
