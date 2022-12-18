package com.cc.conststar.wall.service.impl;

import com.cc.conststar.wall.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService {

    long insertText(Comment body);

    void addUser(User user);

    UserDetail getUserDetail(String openId);

    void editUserDetail(UserDetail userDetail);

    public Boolean addComment(Comment body);

    List<CommentsVO> getComments(int day);

    void insertBack(BackImg backImg);

    void updateBack(BackImg backImg);

    BackImg getBackImg( long userId);



}
