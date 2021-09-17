package com.ma.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.Serializable;

/**
 * author:author:"maliangcheng" <2516749060@qq.com>
 * rpc序列化封装类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterSerializa implements Serializable {
    private String fileLink;
    private OutputStream outputStream;
}
