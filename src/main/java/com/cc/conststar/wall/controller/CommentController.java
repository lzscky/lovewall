package com.cc.conststar.wall.controller;


import com.cc.conststar.wall.constant.ResponseEnumConstant;
import com.cc.conststar.wall.entity.*;
import com.cc.conststar.wall.faced.CommentFaced;
import com.cc.conststar.wall.faced.UploadFileFaced;
import com.cc.conststar.wall.handler.FormatHandler;
import com.cc.conststar.wall.handler.GenericHandler;
import com.cc.conststar.wall.service.impl.CommentService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentFaced faced;

    @Autowired
    UploadFileFaced uploadFileFaced;

    @GetMapping("/getUser")
    public GenericHandler<UserDetail> getUser(@RequestParam("code") String code){

        String openId = faced.getOpenId(code);
        UserDetail userDetail = commentService.getUserDetail(openId);

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, userDetail);

    }

    @GetMapping("/getComments")
    public GenericHandler<List<CommentsVO>> getComments(@RequestParam("day") int day){

        List<CommentsVO> comments = commentService.getComments(day);

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, comments);

    }

    @GetMapping("/getOpenId")
    public String getOpenId(@RequestParam("code") String code){

        String openId = faced.getOpenId(code);
        System.out.println(openId);
        return openId;

    }

    @PostMapping("/editUserName")
    public GenericHandler<Boolean> editUserName(@RequestBody UserDetail userDetail){
        System.out.println(userDetail);
        commentService.editUserDetail(userDetail);
        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, true);
    }

    @PostMapping("/addUser")
    public GenericHandler<Boolean> addUser (@RequestBody User user){
        String openId = faced.getOpenId(user.getCode());
        user.setOpenId(openId);
        //System.out.println(user.getNickName());
        commentService.addUser(user);
        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, true);
    }

    @PostMapping("/addUserPhone")
    public Boolean addUserPhone (@RequestBody String code){
        try {
            Boolean aBoolean = faced.addUserPhone(code);
            return aBoolean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/addComment")
    public GenericHandler<Boolean> addComment(@RequestBody Comment body){

        if (body == null || body.getOpenId() == null || body.getContent() == null){
            return FormatHandler.retParam(ResponseEnumConstant.CODE_10004, false);
        }
        Boolean aBoolean = commentService.addComment(body);
        //System.out.println(content);
        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, aBoolean);
    }


    @PostMapping("/test")
    public void test(@RequestBody Map<String,Object> body){

        //System.out.println(body);
        String title = (String) body.get("title");

        //System.out.println(title);

    }

    @PostMapping("/editUserHeadImg")
    public ResultResp editUserHeadImg(@RequestParam(value="file",required=false) MultipartFile file,
                                      @RequestParam(value = "openId") String openId, HttpServletRequest request, HttpServletResponse response){

        ResultResp resultResp = uploadFileFaced.uploadPicture(file, request, response);
        UserDetail userDetail = new UserDetail();
        userDetail.setHeadUrl((String) resultResp.getData().get("url"));
        //System.out.println((String) resultResp.getData().get("url"));
        userDetail.setOpenId(openId);
        commentService.editUserDetail(userDetail);
        return resultResp;
    }

    @PostMapping("/editBackImg")
    public ResultResp editBackImg(@RequestParam(value="file",required=false) MultipartFile file,
                                      @RequestParam(value = "openId") String openId, HttpServletRequest request, HttpServletResponse response){

        ResultResp resultResp = uploadFileFaced.uploadPicture(file, request, response);
        UserDetail userDetail = commentService.getUserDetail(openId);
        BackImg backImg = new BackImg();
        backImg.setBackUrl((String) resultResp.getData().get("url"));
        backImg.setUserId(userDetail.getId());
        commentService.insertBack(backImg);
        return resultResp;
    }





}
