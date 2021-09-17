package com.ma.provider.service.impl;

import com.ma.common.pojo.User;
import com.ma.common.service.UserLoginService;
import com.ma.provider.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private UserMapper userMapper;

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param userName
     * @return
     * 获取用户信息
     */
    @Override
    public User getLoginUser(String userName) {
        return userMapper.getLoginUser(userName);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param user
     * @return
     */
    @Override
    public int registerUser(User user) {
        return userMapper.registerUser(user);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @return
     */
    @Override
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param user
     * @return
     */
    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }
}
