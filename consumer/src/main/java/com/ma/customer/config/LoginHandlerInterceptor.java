package com.ma.customer.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用户登录成功后,应该有自己的session
        Object session = request.getSession().getAttribute("user");
        if (session == null) {
            request.setAttribute("msg", "权限不够,请先登录");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        } else {
            return true;
        }
    }
}
