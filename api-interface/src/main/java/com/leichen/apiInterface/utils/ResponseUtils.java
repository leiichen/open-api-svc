package com.leichen.apiInterface.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.apiclient.model.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class ResponseUtils {
    public static <T> ResultResponse baseResponse(String url, T params) {
        String response = null;
        try {
            response = get(buildUrl(url, params));
            Map map = JSONUtil.toBean(response, Map.class);
            // 现在接口从https://api.vvhan.com/拿到的
            boolean success = (boolean)map.get("success");
            ResultResponse resultResponse = new ResultResponse();
            if (!success) {
                resultResponse.setData(map);
                return resultResponse;
            }
            map.remove("success");
            resultResponse.setData(map);
            return resultResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> String get(String url) {
        String body = HttpRequest.get(url).execute().body();
        log.info("【interface】：请求地址：{}，响应数据：{}", url, body);
        return body;
    }

    private static <T> String buildUrl(String url, T params) {
        StringBuilder builder = new StringBuilder(url);
        Field[] fields = params.getClass().getDeclaredFields();
        boolean isFirst = true;
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (name.equals("serialVersionUID")) {
                continue;
            }
            try {
                Object value = field.get(params);
                if (isFirst) {
                    builder.append("?").append(name).append("=").append(value);
                    isFirst = false;
                } else {
                    builder.append("&").append(name).append("=").append(value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
