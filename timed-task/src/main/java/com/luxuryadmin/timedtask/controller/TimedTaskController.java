package com.luxuryadmin.timedtask.controller;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.timedtask.thread.fin.AutoCreateFinSalaryRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/timedTask")
@ApiIgnore
public class TimedTaskController extends BaseController {

    @Autowired
    private AutoCreateFinSalaryRunnable autoCreateFinSalaryRunnable;

    /**
     * @return Result
     */
    @RequestMapping("/hello")
    public BaseResult hello() {
        return BaseResult.okResult("This is timed task!");
    }

    /**
     * @return Result
     */
    @RequestMapping("/startFinSalaryRunnable")
    public BaseResult startFinSalaryRunnable() {
        autoCreateFinSalaryRunnable.run();
        return BaseResult.okResult("start over!");
    }


    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/index.html");
    }
}
