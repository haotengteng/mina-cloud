package cn.mina.cloud.example.sentienl.controller;

import cn.mina.boot.web.common.context.MinaWebResult;
import cn.mina.cloud.example.sentienl.service.FooService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/foo")
@Slf4j
public class FooController {

    @Autowired
    private FooService fooService;

    @GetMapping(path = "hello")
    public MinaWebResult<String> sayHello() {
        return fooService.getFoo("hello");
    }

    private String getLocalIP() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostName() + "-" + addr.getHostAddress();
    }


}
