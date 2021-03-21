package com.rainlf.spring.cloud.alibaba.example.serviceconsumer;

import com.rainlf.spring.cloud.alibaba.example.serviceapi.HelloService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : rain
 * @date : 2021/3/20 19:59
 */
@RestController
public class ConsumerController {

    @DubboReference(version = "1.0", check = false)
    private HelloService helloService;

    @GlobalTransactional
    @GetMapping("")
    public String sayHello() {
        return helloService.sayHello();
    }

    @GlobalTransactional
    @GetMapping("/{id}")
    public String sayHelloById(@PathVariable Integer id) {
        if (id == 2) {
            throw new RuntimeException("Exception for seata");
        }
        return helloService.sayHello();
    }
}
