package com.leichen.project.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static com.leichen.project.utils.NetUtils.getIp;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 请求白名单
     */
    private final static List<String> WHITE_HOST_LIST = Arrays.asList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 请求日志
        log.info("请求唯一id：" + request.getId());
        log.info("请求方法：" + request.getMethod());
        log.info("请求路径：" + request.getPath());
        log.info("网关本地地址：" + request.getLocalAddress());
        log.info("请求远程地址：" + request.getRemoteAddress());
        log.info("接口请求IP：" + getIp(request));
        log.info("url:" + request.getURI());
        // 黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!WHITE_HOST_LIST.contains(getIp(request))) {
            log.warn("非法请求");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        // TODO 身份鉴权（是否有调用次数）
        HttpHeaders headers = request.getHeaders();
//        headers.getFirst("")
        // TODO 请求的模拟接口是否存在，以及请求方法是否匹配
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
