package com.etour.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security配置类
 * 
 * @author etour
 * @since 2024-01-01
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用CSRF保护
        http.csrf().disable()
            // 配置请求授权规则
            .authorizeRequests()
            // 允许访问API接口、Swagger UI、API文档、静态资源和Druid监控页面
            .antMatchers("/api/**", "/swagger-ui.html", "/swagger-ui/**", "/v2/api-docs", "/webjars/**", "/druid/**").permitAll()
            // 允许访问所有其他请求
            .anyRequest().permitAll()
            .and()
            // 禁用frame选项，允许页面嵌套显示
            .headers().frameOptions().disable();
    }
}