package com.luxuryadmin.api.login;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.enums.EnumCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录注册Controller
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/")
@ApiIgnore
public class AccessDeniedController extends BaseController {


    /**
     *拒绝访问
     *
     * @return Result
     */
    @RequestMapping("/accessDenied")
    public BaseResult accessDenied() {
        return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
    }

    /**
     *拒绝访问
     *
     * @return Result
     */
    @RequestMapping("/noLogin")
    public BaseResult noLogin() {
        return BaseResult.defaultErrorWithMsg("请先登录!");
    }


    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index.html");
    }
}
