package com.cc.conststar.wall.service.impl.impl;

import com.cc.conststar.wall.dao.CommentDao;
import com.cc.conststar.wall.entity.*;
import com.cc.conststar.wall.service.impl.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Override
    public long insertText(Comment body) {

        TextEntity textEntity = new TextEntity();
        textEntity.setText(body.getContent());
        textEntity.setTitle(body.getTitle());
        textEntity.setImgUrl(body.getContentImg());
        commentDao.insertText(textEntity);
        long id = textEntity.getId();

        return id;
    }

    @Override
    public void addUser(User user) {
        UserDetail userDetail = new UserDetail();
        userDetail.setUserName(user.getNickName());
        userDetail.setHeadUrl(user.getAvatarUrl());
        userDetail.setOpenId(user.getOpenId());
        userDetail.setWechatName(user.getNickName());
        userDetail.setSex(user.getGender());
        //userDetail.setPhone();

        UserDetail userDetail1 = commentDao.queryByOpenId(user.getOpenId());
        if (userDetail1 != null){
            return;
        }
        commentDao.insert(userDetail);
        Long id = userDetail.getId();

    }

    @Override
    public UserDetail getUserDetail(String openId) {
        UserDetail userDetail1 = commentDao.queryByOpenId(openId);
        BackImg backImg = commentDao.getBackImg(userDetail1.getId());
        userDetail1.setBackUrl(backImg.getBackUrl());
        userDetail1.setOpenId(openId);
        return userDetail1;
    }

    @Override
    public void editUserDetail(UserDetail userDetail) {

        commentDao.updateByOpenId(userDetail);
    }

    @Override
    public Boolean addComment(Comment body) {

        UserDetail userDetail = commentDao.queryByOpenId(body.getOpenId());
        long id = insertText(body);
        Totle totle = new Totle();
        totle.setCommentId(id);
        totle.setUserId(userDetail.getId());
        if (body.getIsName() == 0){
            totle.setIsCheck(0);
        }else {
            totle.setIsCheck(1);
        }
        int i = commentDao.insertTotle(totle);
        long totleId = totle.getId();

        return true;
    }

    @Override
    public List<CommentsVO> getComments(int day) {
        List<CommentsVO> comments = commentDao.getComments(day);
        for (CommentsVO comment : comments) {
            Date creatTime = comment.getCreatTime();
            Date d = new Date();
            long miseDifference = d.getTime() - creatTime.getTime();
            int t = (int) (miseDifference / 1000 / 60);
            String time = "";
            if (t < 60){
                time = t + "分钟前";
            }else {
                time = t/60 + "小时前";
            }
            comment.setTime(time);
        }
        return comments;
    }

    @Override
    public void insertBack(BackImg backImg) {
        BackImg backImg1 = commentDao.getBackImg(backImg.getUserId());
        if (backImg1 == null){
            commentDao.insertBack(backImg);
        }else {
            commentDao.updateBack(backImg);
        }
    }

    @Override
    public void updateBack(BackImg backImg) {
        commentDao.updateBack(backImg);
    }

    @Override
    public BackImg getBackImg(long userId) {
        BackImg backImg = commentDao.getBackImg(userId);
        return backImg;
    }
}
