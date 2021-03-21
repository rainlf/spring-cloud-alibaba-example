package com.rainlf.spring.cloud.alibaba.example.serviceconsumer;

import com.rainlf.spring.cloud.alibaba.example.serviceapi.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : rain
 * @date : 2021/3/20 19:59
 */
@RestController
public class ConsumerController {

    @DubboReference(version = "1.0", check = false)
    private HelloService helloService;

    @GetMapping("")
    public String sayHello() {
        return helloService.sayHello();
    }
}
