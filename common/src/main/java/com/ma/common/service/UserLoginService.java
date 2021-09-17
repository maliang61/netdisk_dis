package com.ma.common.service;

import com.ma.common.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserLoginService {
    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param userName
     * @return
     * 获取登陆者信息
     */
    //获取admin信息
    User getLoginUser(@Param("userName") String userName);

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param user
     * @return
     */
    int registerUser(User user);

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @return
     */
    List<User> getAllUser();

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param user
     * @return
     */
    int updateUser(User user);
}
