package com.ma.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/testObs.html").setViewName("testObs");
        registry.addViewController("/share.html").setViewName("share");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/register.html").setViewName("register");
        registry.addViewController("/main.html").setViewName("main");
        registry.addViewController("/updateUser.html").setViewName("updateUser");
        registry.addViewController("/search.html").setViewName("search");

    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @return
     * 自定义的国际化组件生效
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/index.html", "/", "/user/login","/user/**","/css/**", "/js/**", "/img/**","/layui/css/**","/layui/layui.js");
    }
}
