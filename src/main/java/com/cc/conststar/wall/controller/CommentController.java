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


    @GetMapping( "/selectRanking")
    public GenericHandler<List<Ranking>> selectRanking(@RequestParam("day") int day) {


        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, commentService.getRankings(day));

    }

    @PostMapping("/updateRanking")
    public GenericHandler<Boolean> updateRanking(@RequestBody Ranking ranking){

        commentService.updateRanking(ranking);
        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, true);
    }

    @GetMapping( "/deleteRanking")
    public GenericHandler<Boolean> deleteRanking(@RequestParam("rankingId") Long rankingId) {

        commentService.deleteRanking(rankingId);
        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, true);

    }

    @PostMapping("/addRanking")
    public GenericHandler<Boolean> addRanking(@RequestBody Ranking ranking){
        String code = ranking.getCode();
        String openId = faced.getOpenId(code);

        commentService.addRanking(ranking, openId);
        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, true);
    }

   /* @GetMapping( "/wechat/getWechatJsSDK")
    public WechatJsSDK getWechatJsSDK(@RequestParam("url") String url,@RequestParam("accessTokenUrl") String accessTokenUrl,
                                      @RequestParam("jsapiTicketUrl") String jsapiTicketUrl) {

        return faced.makeShaSign(url,accessTokenUrl,jsapiTicketUrl);

    }*/
    @GetMapping("/insertPraise")
    public GenericHandler<Boolean> insertPraise(@RequestParam("commentId") long commentId, @RequestParam("code") String code){

        String openId = faced.getOpenId(code);
        UserDetail userDetail = commentService.getUserDetail(openId);
        int i = commentService.insertPraise(commentId, userDetail.getId());

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, true);
    }

    @GetMapping("/getPraise")
    public GenericHandler<Praise> getPraise(@RequestParam("commentId") long commentId,@RequestParam("code") String code){

        String openId = faced.getOpenId(code);
        UserDetail userDetail = commentService.getUserDetail(openId);
        List<Long> praise = commentService.getPraise(commentId);
        Praise praises = new Praise();
        praises.setUserIds(praise);
        praises.setUserId(userDetail.getId());

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, praises);
    }


    @GetMapping("/getReplys")
    public GenericHandler<List<Reply>> getReplys(@RequestParam("commentId") long commentId){

        List<Reply> replys = commentService.getReplys(commentId);

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, replys);
    }

    @PostMapping("/insertreply")
    public GenericHandler<Boolean> insertreply(@RequestBody Reply reply){
        String code = reply.getCode();
        String openId = faced.getOpenId(code);

        commentService.insertreply(reply, openId);
        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, true);
    }

    @PostMapping("/updateReply")
    public GenericHandler<Boolean> updateReply(@RequestBody Reply reply){

        commentService.updateReply(reply);
        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, true);
    }


    @GetMapping("/getUser")
    public GenericHandler<UserDetail> getUser(@RequestParam("code") String code){

        String openId = faced.getOpenId(code);
        UserDetail userDetail = commentService.getUserDetail(openId);
        userDetail.setOpenId("");

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, userDetail);

    }

    @GetMapping("/getCommentsByUserId")
    public GenericHandler<List<CommentsVO>> getCommentsByUserId(@RequestParam("id") long id){

        List<CommentsVO> comments = commentService.getCommentsByUserId(id);

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, comments);

    }

    @GetMapping("/getComments")
    public GenericHandler<List<CommentsVO>> getComments(@RequestParam("day") int day){

        List<CommentsVO> comments = commentService.getComments(day);

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, comments);

    }
    //获取墙详情by ID
    @GetMapping("/getCommentById")
    public GenericHandler<CommentsVO> getCommentById(@RequestParam("objectId") int id){

        CommentsVO commentById = commentService.getCommentById(id);

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, commentById);

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
                                      @RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response){

        ResultResp resultResp = uploadFileFaced.uploadPicture(file, request, response);
        UserDetail userDetail = new UserDetail();
        userDetail.setHeadUrl((String) resultResp.getData().get("url"));
        //System.out.println((String) resultResp.getData().get("url"));
        userDetail.setId(id);
        commentService.editUserDetail(userDetail);
        return resultResp;
    }

    @PostMapping("/editBackImg")
    public ResultResp editBackImg(@RequestParam(value="file",required=false) MultipartFile file,
                                      @RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response){

        ResultResp resultResp = uploadFileFaced.uploadPicture(file, request, response);
        BackImg backImg = new BackImg();
        backImg.setBackUrl((String) resultResp.getData().get("url"));
        backImg.setUserId(id);
        if ( id == 0){
            resultResp.setCode("10004");
            resultResp.setMsg("参数缺失");
            return resultResp;
        }
        commentService.insertBack(backImg);
        return resultResp;
    }



}
