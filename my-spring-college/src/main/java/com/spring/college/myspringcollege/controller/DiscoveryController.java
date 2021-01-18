package com.spring.college.myspringcollege.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: 芒草<xiongwen>
 * @DATE: 2021/1/18 10:49 上午
 */
@RestController
public class
DiscoveryController {

    @Autowired
    DiscoveryClient discoveryClient;

    public void test() {
        List<String> serviceNames = discoveryClient.getServices();

        List<ServiceInstance> instances = discoveryClient.getInstances(null);
    }
}
