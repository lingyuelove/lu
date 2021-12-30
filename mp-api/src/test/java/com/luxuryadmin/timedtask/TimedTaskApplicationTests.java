package com.luxuryadmin.timedtask;

import com.luxuryadmin.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class TimedTaskApplicationTests {

    @Resource
    private ServiceService serviceService;
    @Test
    void contextLoads() {
//        serviceService.send();
    }

}
