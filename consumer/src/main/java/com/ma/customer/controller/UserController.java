package com.ma.customer.controller;

import com.alibaba.fastjson.JSON;
import com.ma.common.pojo.User;
import com.ma.common.service.UserLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserLoginService userLoginService;


    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param httpSession
     * @param userName
     * @param password
     * @param model
     * @return
     * 登陆
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(HttpSession httpSession, @RequestParam("userName") String userName, @RequestParam("password")String password, Model model){
        String info = "";
        User user = userLoginService.getLoginUser(userName);
        if (user.getUserName() != null){
            info = "OK";
        }else {
            info = "无此用户！！";
        }
        if (user.getPassword().equals(password)){
            httpSession.setAttribute("user",user);
            model.addAttribute("user",user);
            model.addAttribute("msg","登陆成功");
            info = "密码输入正确";
            return "redirect:/main.html";
        }else {
            model.addAttribute("msg","error");
            return "index";
        }
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param session
     * @return
     * 登出
     */
    @RequestMapping("/logout")
    public String loginOut(HttpSession session){
        session.invalidate();
        return "redirect:/index.html";
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @return
     */
    @RequestMapping("/toRegister")
    public String routeRegister(){
        return "register";
    }
    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param userName
     * @param nickName
     * @param superUser
     * @param password
     * @param email
     * @param model
     * @return
     */
    @RequestMapping("/register")
    public String registerUser(@RequestParam("userName") String userName,@RequestParam("nickName") String nickName,  @RequestParam("superUser") int superUser,@RequestParam("password")String password,@RequestParam("email")String email,Model model){
        User register = new User();
        for(User user : userLoginService.getAllUser()){
            if (user.getUserName().equals(userName)){
                model.addAttribute("msg","用户已存在");
                return "register";
            }
        }

        register.setUuid(UUID.randomUUID().toString());
        register.setUserName(userName);
        register.setNickName(nickName);
        register.setSuperUser(superUser);
        register.setPassword(password);
        register.setEmail(email);
        register.setRegistrationDate(new Date(System.currentTimeMillis()));

        userLoginService.registerUser(register);
        model.addAttribute("msg","register success");
        return "redirect:/index.html";
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUserInfo")
    public Object getUserInfo(HttpSession session){
        User user =(User) session.getAttribute("user");
        JSON json =(JSON) JSON.toJSON(user);
        return json;
    }

    /**
     * 携带信息跳转
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/toUpdateUser")
    public String tpUpdateUser(HttpSession session,Model model){
        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        return "updateUser";
    }

    /**
     * 更新用户信息
     * @param session
     * @param user
     * @param model
     * @return
     */
    @RequestMapping("/updateUser")
    public String updateUser(HttpSession session,User user,Model model){
        User userOld =(User) session.getAttribute("user");

        user.setUuid(userOld.getUuid());
        user.setRegistrationDate(userOld.getRegistrationDate());
        user.setSuperUser(userOld.getSuperUser());
        userLoginService.updateUser(user);
        User loginUser = userLoginService.getLoginUser(userOld.getUuid());

        model.addAttribute("user",loginUser);
        session.setAttribute("user",loginUser);
        return "redirect:/main.html";
    }

}
