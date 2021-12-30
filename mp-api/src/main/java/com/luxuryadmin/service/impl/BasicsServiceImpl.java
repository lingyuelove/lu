package com.luxuryadmin.service.impl;

import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.service.BasicsService;
import com.luxuryadmin.service.TokenService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service
@Transactional
public class BasicsServiceImpl implements BasicsService {


    @Autowired
    HttpServletRequest request;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取userid
     *
     * @return
     */
    @Override
    public Integer getUserId() {
        String redisValue = getToken();
        if (StringUtil.isBlank(redisValue)) {
            return null;
        }
        return Integer.parseInt(redisValue);
    }

    public String getToken() {
        String token = request.getHeader("baoyang-token");
        return redisUtil.get(tokenService.getSystemUserIdKey(token));
    }


}
