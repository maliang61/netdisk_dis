package com.ma.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//路由控制

@Controller
@RequestMapping("/route")
public class RoutePages {

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @return
     */
    @RequestMapping("/toMain")
    public String toMain(){
        return "redirect:/main.html";
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @return
     */
    @RequestMapping("/toShare")
    public String toShare(){
        return "share";
    }
}
