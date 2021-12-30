package com.luxuryadmin.intercepter;

import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.context.Cont;
import com.luxuryadmin.service.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;

@Component
@Log4j2
public class TokenIntercepter extends AbstractZyInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TokenService tokenService;

    public static final String[] FILTER_PATH_ARRAY = new String[]{
            "/swagger-ui",
            "/swagger-resources",
            "/callback",
            "/springfox-swagger-ui",
            "/notVipProt"
    };

    @Override
    public String[] getPathPatterns() {
        return new String[]{"/**"};
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer url = request.getRequestURL();
        log.info("请求url：" + url);
        if (isFiltrationUrl(url.toString())) {
            return super.preHandle(request, response, handler);
        }
        String token = request.getHeader("baoyang-token");
        if (StringUtil.isBlank(token)) {
            throw new MyException(EnumCode.ERROR_TOKEN_DISABLE);
        }
        String redisValue = redisUtil.get(tokenService.getSystemUserIdKey(token));
        if (StringUtil.isBlank(redisValue)) {
            throw new MyException(EnumCode.ERROR_TOKEN_DISABLE);
        }
        //查看体验用户是否到期
        String redisEndTime = redisUtil.get(Cont.MP_USER + token + Cont.END_TIME);
        Date date = new Date();
        if (StringUtil.isBlank(redisEndTime)) {
            //查看是否是会员
            if (isVip(token, date)) {
                return super.preHandle(request, response, handler);
            } else {
                throw new MyException(EnumCode.VIP_PAST);
            }
        } else {
            Date parse = DateUtil.parse(redisEndTime);
            if (date.getTime() > parse.getTime()) {
                redisUtil.delete(Cont.MP_USER + token + Cont.END_TIME);
                if (isVip(token, date)) {
                    return super.preHandle(request, response, handler);
                } else {
                    throw new MyException(EnumCode.VIP_PAST);
                }
            }
        }
        return super.preHandle(request, response, handler);
    }

    public boolean isVip(String token, Date date) throws ParseException {
        String username = redisUtil.get(Cont.MP_USER + token + Cont.USERNAME);
        String payEndTime = redisUtil.get(Cont.MP_USER + username + Cont.PAY_END_TIME);
        if (StringUtil.isNotBlank(payEndTime)) {
            Date parse = DateUtil.parse(payEndTime);
            if (parse.getTime() > date.getTime()) {
                return true;
            }
        } else {
            String isSdjVip = redisUtil.get(Cont.MP_USER + username + Cont.SDJ_VIP);
            if (StringUtil.isNotBlank(isSdjVip)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否
     *
     * @param url
     * @return
     */
    public Boolean isFiltrationUrl(String url) {
        for (String s : FILTER_PATH_ARRAY) {
            if (url.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
