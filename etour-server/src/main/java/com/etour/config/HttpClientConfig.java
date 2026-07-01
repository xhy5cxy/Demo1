package com.etour.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP客户端配置类
 * 配置RestTemplate用于HTTP请求
 */
@Configuration
public class HttpClientConfig {

    /**
     * 配置HTTP请求工厂
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时时间（毫秒）
        factory.setConnectTimeout(30000);
        // 读取超时时间（毫秒）
        factory.setReadTimeout(30000);
        return factory;
    }

    /**
     * 配置RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}