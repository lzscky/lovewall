package com.cc.conststar.wall.dao;

import com.cc.conststar.wall.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDao {

    //获取墙内容
    List<CommentsVO> getComments(@Param("day") int day);


    /**
     * 新增表白文本数据
     */
    int insertText(TextEntity textEntity);

    /**
     * 新增表白关联
     */
    int insertTotle(Totle totle);

    UserDetail queryByOpenId(String openId);

    /**
     * 新增数据
     *
     * @param userDetail 实例对象
     * @return 影响行数
     */
    int insert(UserDetail userDetail);

    int updateByOpenId(UserDetail userDetail);

    /**
     * 新增背景图
     */
    int insertBack(BackImg backImg);

    int updateBack(BackImg backImg);

    BackImg getBackImg(@Param("userId") long userId);

}
