package com.ma.common.service;

import com.alibaba.fastjson.JSON;
import com.obs.services.model.PutObjectResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;

public interface ObsService {
    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param objectName
     * @param inputStream
     * @return
     */
    PutObjectResult putOb(String objectName, InputStream inputStream);

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param json
     * @throws IOException
     */
    void downOb(JSON json) throws IOException;

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param fileNameBucket
     * @return
     */
    String getFileUrl(String fileNameBucket);
    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileNameBucket
     * @return
     */
    Map deleteFile(String fileNameBucket);
}
