package com.stbu.labmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 * 用于配置全局跨域访问策略，解决前后端分离环境下的跨域问题
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * 配置跨域映射规则
     * @param registry Cors注册对象，用于添加跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 配置允许跨域的路径，"/**"表示所有路径
                .allowedOriginPatterns("*")  // 配置允许跨域的源模式，"*"表示所有源
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 配置允许的HTTP方法
                .allowedHeaders("*")  // 配置允许的请求头，"*"表示所有请求头
                .allowCredentials(true)  // 是否允许发送Cookie信息
                .maxAge(3600);  // 预检请求的有效期，单位为秒
    }
}