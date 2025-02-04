package com.leichen.project.gateway;

import cn.hutool.core.util.StrUtil;
import com.apiclient.ApiException.ErrorCode;
import com.leichen.apicommon.model.VO.UserVO;
import com.leichen.apicommon.model.entity.InterfaceInfo;
import com.leichen.apicommon.service.inner.InnerInterfaceInfoService;
import com.leichen.apicommon.service.inner.InnerUserService;
import com.leichen.project.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.apiclient.utils.SignUtils.getSign;
import static com.leichen.project.utils.NetUtils.getIp;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 请求白名单
     */
    private final static List<String> WHITE_HOST_LIST = Arrays.asList("127.0.0.1");
    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

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
        return verifyRequest(exchange, chain);
    }

    public Mono<Void> verifyRequest(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String timestamp = headers.getFirst("timestamp");
        if (StrUtil.isAllBlank(accessKey, body, sign, timestamp)) {
            log.error("请求参数不完整: accessKey: {}, body: {}, sign: {}, timestamp: {}", accessKey, body, sign, timestamp);
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        // 黑白名单
//        ServerHttpResponse response = exchange.getResponse();
//        if (!WHITE_HOST_LIST.contains(getIp(request))) {
//            log.warn("非法请求");
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }
        // TODO 身份鉴权（是否有调用次数）
        UserVO userVO = innerUserService.getInvokeUser(accessKey);
        if (userVO == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "请正确配置接口凭证");
        }
        if (!accessKey.equals(userVO.getAccessKey())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "请先获取请求密钥");
        }
        // 效验签名
        if (!getSign(body, userVO.getSecretKey()).equals(sign)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "签名校验失败");
        }
        // TODO 请求的模拟接口是否存在，以及请求方法是否匹配
        String url = request.getURI().getPath().trim();
        String method = Objects.requireNonNull(request.getMethod()).toString();
        InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(url, method);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        // todo 校验请求参数是否与接口信息匹配
        MultiValueMap<String, String> queryParams = request.getQueryParams();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
