package com.leichen.backend.config;

import com.leichen.backend.interceptor.GlobalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


/**
 * @author leiichen
 * @Description
 * @create 2024-02-26 10:00
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private GlobalInterceptor globalInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/interfaceInfo/**");
    }

}
