package com.ma.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.ma.common.pojo.ParameterSerializa;
import com.ma.common.service.ObsService;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ObsServiceImpl implements ObsService,Serializable{

    ObsClient obsClient;
    @Value("${obs.endPoint}")
    private String endPoint;
    @Value("${obs.ak}")
    private String ak;
    @Value("${obs.sk}")
    private String sk;
    @Value("${obs.bucketName}")
    private String bucketName;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param objectName
     * @param inputStream
     * @return
     * 上传到obs的bucket
     */
    @Override
    public PutObjectResult putOb(String objectName, InputStream inputStream) {
        obsClient = new ObsClient(ak,sk,endPoint);
        ByteArrayInputStream byteArrayInputStream1;
        PutObjectResult putObjectResult = obsClient.putObject(bucketName,objectName,inputStream);
        return putObjectResult;
    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param json
     * @throws IOException
     * rpc需要序列化和反序列化
     */
    @Override
    public void downOb(JSON json) throws IOException {
        obsClient = new ObsClient(ak,sk,endPoint);
        System.out.println(json);
        ParameterSerializa parameterSerializa = json.toJavaObject(ParameterSerializa.class);
        System.out.println(parameterSerializa);
        String objectName = parameterSerializa.getFileLink();
        OutputStream out = parameterSerializa.getOutputStream();
        ObsObject obsObject = obsClient.getObject(bucketName, objectName);
        InputStream input = obsObject.getObjectContent();
        byte[] b = new byte[1024];
        int len;
        while ((len=input.read(b)) != -1){
            out.write(b, 0, len);
            out.flush();
        }
        System.out.println(new String(String.valueOf(out)));
        input.close();
    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param fileNameBucket
     * @return
     * 获取url保存到redis
     */
    @Override
    public String getFileUrl(String fileNameBucket) {
        obsClient = new ObsClient(ak,sk,endPoint);
        long expireSeconds = 3600L;//时间前端可选
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        request.setBucketName(bucketName);
        request.setObjectKey(fileNameBucket);
        request.setRequestDate(new Date());

        boolean exits = obsClient.doesObjectExist(bucketName,fileNameBucket);
        if (exits) {
            TemporarySignatureResponse signature = obsClient.createTemporarySignature(request);
            String link = signature.getSignedUrl();
            System.out.println("Obs获取链接成功:"+link);
            //存储到redis
            //filename_front->mysql->filename_bucket->redis
            redisTemplate.opsForValue().set(fileNameBucket,link);
            return link;
        } else {
            System.out.println("对象不存在!");
            return null;
        }


    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileNameBucket
     * @return
     */
    @Override
    public Map deleteFile(String fileNameBucket) {
        Map<String,Object> resultMap = new HashMap<>();
        obsClient = new ObsClient(ak,sk,endPoint);
        boolean exist = obsClient.doesObjectExist(bucketName, fileNameBucket);
        if (exist){
            resultMap.put("msg","file is exit");
            DeleteObjectResult deleteObjectResult = obsClient.deleteObject(bucketName, fileNameBucket);
            resultMap.put("deleteObjectResult","delete success");
        }else {
            resultMap.put("msg","file isn't in the bucket");
            resultMap.put("deleteObjectResult","delete failed ");
        }
        return resultMap;
    }



}
