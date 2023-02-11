package com.cc.conststar.wall.dao;

import com.cc.conststar.wall.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDao {

    //获取墙内容
    List<CommentsVO> getComments(@Param("day") int day);

    //获取墙内容
    List<CommentsVO> getCommentsByUserId(@Param("id") long id);

    //获取详情
    CommentsVO getCommentById(@Param("id") int id);

    //获取点赞
    List<Long> getPraise(@Param("commentId") long commentId);

    //点赞加1
    int insertPraise(@Param("commentId") long commentId, @Param("userId") long userId);

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

    int updateById(UserDetail userDetail);

    /**
     * 新增评论
     */
    int insertreply(Reply reply);

    int updateReply(Reply reply);

    //获取评论
    List<Reply> getReplys(@Param("commentId") long commentId);

    /**
     * 新增背景图
     */
    int insertBack(BackImg backImg);

    int updateBack(BackImg backImg);

    BackImg getBackImg(@Param("userId") long userId);

    int addRanking(Ranking ranking);

    int updateRanking(Ranking ranking);

    //获取照片榜
    List<Ranking> getRankings(@Param("day") int day);

    /**
     * 汽车之家爬
     */
    List<BrandsHome> getBrands(@Param("brandNum") int brandNum);

    int addManufacturer(CarHomeManufacturer carHomeManufacturer);
    int addSeries(CarHomeSeries carHomeSeries);

    List<CarHomeSeries> getSeries(@Param("seriesNum") int seriesNum);

    int addModel(CarHomeModel carHomeModel);

    void deleteError();

    int insertErrorGo(ErrorGoOn errorGoOn);

    ErrorGoOn getError();
}
