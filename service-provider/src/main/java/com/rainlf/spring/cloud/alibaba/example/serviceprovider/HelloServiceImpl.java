package com.rainlf.spring.cloud.alibaba.example.serviceprovider;

import com.rainlf.spring.cloud.alibaba.example.serviceapi.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author : rain
 * @date : 2021/3/20 19:56
 */
@DubboService(version = "1.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return "hello world";
    }
}
