package com.ma.customer.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import com.ma.common.pojo.*;
import com.ma.common.service.FileLinkService;
import com.ma.common.service.ObsService;
import org.apache.dubbo.serialize.hessian.Hessian2Serialization;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileController implements Serializable {
    @Resource
    private ObsService obsService;
    @Resource
    private FileLinkService fileLinkService;

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param file`
     * @param request
     * @param session
     * @param model
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/upload")
    public Object fileUpload(@RequestParam("file") MultipartFile file , HttpServletRequest request,HttpSession session,Model model) throws IOException {
        //获取文件名 : file.getOriginalFilename();
        String uploadFileName = file.getOriginalFilename();
        Map<String,Object> resultMap = new HashMap<>();
        //如果文件名为空，直接回到首页！
        if ("".equals(uploadFileName)){
            resultMap.put("code",0);
            resultMap.put("msg","fileName Null");
            return "redirect:/testObs.html";
        }
        System.out.println("上传文件名 : "+uploadFileName);

        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload");
        //如果路径不存在，创建一个
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdir();
        }
        System.out.println("上传文件保存地址："+realPath);

        InputStream is = file.getInputStream(); //文件输入流
        //上传到bucket
        User user = (User) session.getAttribute("user");
        List<FileLink> fileList = fileLinkService.getAllFile(user.getUuid());
        int flag = 0;
        for(FileLink fileLink : fileList){
            if(uploadFileName.equals(fileLink.getFileLink())){
                flag = 1;
            }
        }

        if(flag == 1){
            //将linkId随机化
            String[] split = uploadFileName.split("\\.");
            split[0] = split[0]+UUID.randomUUID().toString();
            String uploadFileNameSerializable = split[0]+"."+split[1];
            model.addAttribute("uploadFileName",uploadFileNameSerializable);
            fileLinkService.insertFile(new FileLink("", user.getUuid(), uploadFileNameSerializable, uploadFileName, file.getSize() / 1024, new java.sql.Date(new Date().getTime())));
            resultMap.put("code",1);
            resultMap.put("msg","upload success");
        }else {
            model.addAttribute("uploadFileName",uploadFileName);
            fileLinkService.insertFile(new FileLink("",user.getUuid(),uploadFileName,uploadFileName,file.getSize()/1024, new java.sql.Date(new Date().getTime())));
            obsService.putOb(uploadFileName,is);
            resultMap.put("code",1);
            resultMap.put("msg","upload success");
        }
        is.close();
        return resultMap;
    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload2")
    public String  fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload");
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdir();
        }
        //上传文件地址
        System.out.println("上传文件保存地址："+realPath);

        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(new File(realPath +"/"+ file.getOriginalFilename()));

        return "redirect:/testObs.html";
    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param response
     * @param request
     * @param fileName
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value="/download",method = RequestMethod.POST)
    @CrossOrigin
    public String downloads2(HttpServletResponse response, HttpServletRequest request, @RequestParam("fileName") String fileName) throws IOException{
        //1、设置response 响应头
        response.reset(); //设置页面不缓存,清空buffer
        response.setCharacterEncoding("UTF-8"); //字符编码
        response.setContentType("multipart/form-data"); //二进制传输数据
        //设置响应头

        FileLink fileLink = fileLinkService.queryFile(fileName);
        response.setHeader("Content-Disposition",
                "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));
        //写出文件--输出流
        OutputStream out =response.getOutputStream();
        //序列化
        ParameterSerializa parameter = new ParameterSerializa(fileLink.getFileLink(),out);
        JSON json = (JSON) JSON.toJSON(parameter);
        obsService.downOb(json);
        out.close();
        return null;
    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param fileName
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/share")
    public Object shareFile(String fileName, Model model){
        System.out.println(fileName);
        Map<String, Object> fileUrlMap = new HashMap<>();
        String fileNameBucket = fileLinkService.getFileLink(fileName);
        String fileUrl = obsService.getFileUrl(fileNameBucket);
        fileUrlMap.put("fileUrl",fileUrl);
        model.addAttribute("fileUrl",fileUrl);
        return fileUrlMap;
    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAllFile")
    public Object getAllFile(HttpSession session,HttpServletRequest req,Model model){
        int state = -1;
        String msg = "未知错误！";
        Map<String,Object> data = new HashMap<>();

        if(session.getAttribute("user") != null){
            User user = (User) session.getAttribute("user");

            List<FileLink> fileList = fileLinkService.getAllFile(user.getUuid());
            //List<String> fileList = fileLinkService.getAllFileLink(user.getUuid());
            model.addAttribute("getAllFile","success");
            model.addAttribute("allFile",fileList);

            msg = "用户已登陆";
            state = 1;
            data.put("user", user);
            data.put("fileList",fileList);

        }else{
            model.addAttribute("getAllFile","failed");

            msg = "用户未登录";
            state = 0;

        }
        Map<String,Object> map = new HashMap<>();
        map.put("state",state);
        map.put("msg",msg);
        map.put("data",data);
        JSON json =(JSON) JSON.toJSON(map);
        return json;
    }

    @ResponseBody
    @RequestMapping("/getAllFileByPage")
    public Object getAllFileByPage(HttpSession session,Integer pageNum,Integer pageSize,Model model){
        int state = -1;
        String msg = "未知错误！";
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        if(session.getAttribute("user") != null){
            User user = (User) session.getAttribute("user");

            List<FileLink> fileList = fileLinkService.getAllFileByPage(user.getUuid(),pageNum,pageSize);
            //List<String> fileList = fileLinkService.getAllFileLink(user.getUuid());
            model.addAttribute("getAllFile","success");
            model.addAttribute("allFile",fileList);

            msg = "用户已登陆";
            state = 1;
            map.put("code",0);
            map.put("count",fileLinkService.getAllFile(user.getUuid()).size());
            map.put("data",fileList);
            map.put("user", user);
        }else{
            model.addAttribute("getAllFile","failed");

            msg = "用户未登录";
            state = 0;

        }
        map.put("state",state);
        map.put("msg",msg);
        JSON json =(JSON) JSON.toJSON(map);
        return json;
    }

    /**
     * author:author:"maliangcheng" <2516749060@qq.com>
     * @param fileName
     * @param model
     * @return
     */
    @RequestMapping("/queryFile")
    public String queryFile(@RequestParam("fileName") String fileName,Model model){
        if (fileName != null){
            FileLink fileLink = fileLinkService.queryFile(fileName);
            model.addAttribute("queryFile",fileLink);
            model.addAttribute("msgQuery","querySuccess");
            return "";
        }else {
            model.addAttribute("msgQuery","failed");
            return "";
        }

    }

    @RequestMapping("/queryFileNew")
    public Object queryFileNew(HttpSession session,String fileLink,Model model){
        User user =(User) session.getAttribute("user");
        List<FileLink> fileList = fileLinkService.queryFileNew(user.getUuid(), fileLink);
        model.addAttribute("fileList",fileList);
        return "search";
    }

    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param fileName
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteFile")
    public Object deleteFile(String fileName){
        Map<String,Object> resultMap = new HashMap<>();
        if (fileLinkService.queryFile(fileName) != null) {
            resultMap.put("msg","file in the database");
            String fileNameBucket = fileLinkService.getFileLink(fileName);

            if (fileLinkService.getNumberOfFileLink(fileNameBucket) != 1){
                fileLinkService.deleteFileLink(fileName);
                resultMap.put("deleteResult","success");
            }else {
                int deleteFileLinkOb = fileLinkService.deleteFileLink(fileName);
                Map deleteFileOb = obsService.deleteFile(fileNameBucket);
                Object msg = deleteFileOb.get("msg");
                if (deleteFileLinkOb == 1 && msg.equals("delete success")){
                    resultMap.put("deleteResult","success");
                }else{
                    resultMap.put("deleteResult","failed");
                }
            }

        }else{
            resultMap.put("msg","file not in the database");
        }

        return resultMap;
    }
    /**
     * author:"maliangcheng" <2516749060@qq.com>
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/getFileSize")
    public Object getFileSize(HttpSession session){
        User user =(User) session.getAttribute("user");
        long defaultNum =  1024*100;
        long fileSize = fileLinkService.getFileSize(user.getUuid());
        Map<String,Object> map = new HashMap<>();
        map.put("present",(fileSize*100/defaultNum)+"%");

        return JSON.toJSON(map);
    }
}
