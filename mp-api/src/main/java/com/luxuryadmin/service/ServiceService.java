package com.luxuryadmin.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface ServiceService {


    /**
     * 小程序客服
     *
     * @param isGet
     */
    void wxMpService(boolean isGet, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
