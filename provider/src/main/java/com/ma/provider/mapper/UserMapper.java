package com.ma.provider.mapper;

import com.ma.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param userName
     * @return
     */
    User getLoginUser(@Param("userName")String userName);

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
