package com.cc.conststar.wall.faced;


import com.cc.conststar.wall.entity.ResultResp;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class UploadFileFaced {

    public ResultResp uploadPicture(MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        ResultResp result = new ResultResp();
        int maxSize = 1024*1024*2;    //上传最大为2MB
        if (file.getSize()>maxSize) {
            result.setMsg("最大上传限制2Mb");
            return result;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        File targetFile=null;
        String url="";//返回存储路径
        String fileName=file.getOriginalFilename();//获取文件名加后缀
        if(fileName!=null&&fileName!=""){
            //String path = "/data/headimg/";
            String path = "F:/filepath/";
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());//文件后缀
            if (!(fileF.equals(".jpg") || fileF.equals(".jpeg") || fileF.equals(".png") || fileF.equals(".image"))) {
                result.setMsg("只能上传jpg,jpeg,png,image格式");
                return result;
            }
            //新的文件名
            fileName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;
            //获取文件夹路径
            File file1=new File(path);
            //如果文件夹不存在则创建
            if(!file1.exists()  && !file1.isDirectory()){
                file1.mkdirs();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                /*//赋予权限
                String command = "chmod 775 -R " + targetFile;
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(command);*/
                //生成文件地址
                //url="http://XXXXXXXXX.cn"+path+"/"+fileName;
                url="https://img0.baidu.com/it/u=1764313044,42117373&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";
                result.setCode(200+"");
                result.setMsg("图片上传成功");
                System.out.println("图片上传成功 url:"+url);
                map.put("url", url);
                result.setData(map);
            } catch (Exception e) {
                e.printStackTrace();
                result.setMsg("系统异常，图片上传失败");
            }
        }
        return result;
    }

}
