package com.ma.provider.mapper;

import com.ma.common.pojo.FileLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileLinkMapper {

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileName
     * @return
     * 获得FileLink
     */
    String getFileLink(@Param("fileName") String fileName);

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @return
     * 获得当前用户所有文件
     */
    List<FileLink> getAllFile(@Param("uuid") String uuid);

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileName
     * @return
     * 根据fileName查询文件
     */
    FileLink queryFile(@Param("fileName") String fileName);

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileLink
     * @return
     * 插入FileLink进入数据库
     */
    int insertFile(FileLink fileLink);

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @return
     */
    List<String> getAllFileLink(String uuid);

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileLink
     * @return
     */
    int deleteFileLink(String fileLink);

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileLink
     * @return
     */
    int getNumberOfFileLink(@Param("fileLink") String fileLink);


    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @param fileLink
     * @return
     * 根据用户uuid和fileName查询
     */
    List<FileLink> queryFileNew(@Param("uuid")String uuid, @Param("fileLink") String fileLink);


    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @return
     */
    long getFileSize(@Param("uuid") String uuid);
}
