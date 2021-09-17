package com.ma.provider.service.impl;

import com.github.pagehelper.PageHelper;
import com.ma.provider.mapper.FileLinkMapper;
import com.ma.common.pojo.FileLink;
import com.ma.common.service.FileLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FileLinkServiceImpl implements FileLinkService {
    @Autowired
    private FileLinkMapper fileLinkMapper;

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileName
     * @return
     * 获得FileLink
     */
    @Override
    public String getFileLink(String fileName) {
        return fileLinkMapper.getFileLink(fileName);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @return
     * 获得当前用户所有文件
     */
    @Override
    public List<FileLink> getAllFile(String uuid) {
        return fileLinkMapper.getAllFile(uuid);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileName
     * @return
     * 根据fileName查询文件
     */
    @Override
    public FileLink queryFile(String fileName) {
        return fileLinkMapper.queryFile(fileName);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileLink
     * @return
     * 插入FileLink进入数据库
     */
    @Override
    public int insertFile(FileLink fileLink) {
        return fileLinkMapper.insertFile(fileLink);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @return
     */
    @Override
    public List<String> getAllFileLink(String uuid) {
        return fileLinkMapper.getAllFileLink(uuid);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileName
     * @return
     */
    @Override
    public int deleteFileLink(String fileName) {
        return fileLinkMapper.deleteFileLink(fileName);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileLink
     * @return
     */
    @Override
    public int getNumberOfFileLink(String fileLink) {
        return fileLinkMapper.getNumberOfFileLink(fileLink);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<FileLink> getAllFileByPage(String uuid,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return fileLinkMapper.getAllFile(uuid);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @param fileLink
     * @return
     */
    @Override
    public List<FileLink> queryFileNew(String uuid, String fileLink) {
        return fileLinkMapper.queryFileNew(uuid,fileLink);
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param uuid
     * @return
     */
    @Override
    public long getFileSize(String uuid) {
        return fileLinkMapper.getFileSize(uuid);
    }

}
