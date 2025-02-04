package com.leichen.apiInterface.controller;

import com.apiclient.model.params.IpInfoParams;
import com.apiclient.model.response.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.leichen.apiInterface.utils.ResponseUtils.baseResponse;

@RestController
@RequestMapping("/")
public class ServiceController {

    @GetMapping("/")
    public String getName(String name) {
        return "你的名字是" + name;
    }

    @GetMapping("/ipInfo")
    public ResultResponse getIpInfo(IpInfoParams ipInfoParams) {
        return baseResponse("https://api.vvhan.com/api/ipInfo", ipInfoParams);
    }
}
