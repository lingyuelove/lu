package com.luxuryadmin.dto;

import lombok.Data;

@Data
public class WeiTokenDTO {

    private String appid;

    private String secret;

    private String grant_type;
}
