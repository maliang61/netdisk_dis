package com.ma.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * author:author:"maliangcheng" <2516749060@qq.com>
 * 实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private String uuid;
    private String userName;
    private String nickName;
    private String password;
    private String email;
    private int superUser;
    private Date registrationDate;
}
