package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author monkey king
 * @date 2020-08-14 18:03:57
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoUserPermTpl {

    private String tplName;

    private String permId;
}
