package com.ma.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;


/**
 * author:author:"maliangcheng" <2516749060@qq.com>
 * 实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileLink implements Serializable{
    private String linkId;
    private String uuid;
    private String fileName;
    private String fileLink;
    private Long fileSize;
    private Date fileDate;
}
