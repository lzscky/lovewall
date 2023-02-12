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

    List<CommentsVO> getCommentsByUserId(long id);
    CommentsVO getCommentById(int id);

    void insertBack(BackImg backImg);

    void updateBack(BackImg backImg);

    BackImg getBackImg( long userId);

    void insertreply(Reply reply,String openId);
    void updateReply(Reply reply);
    List<Reply> getReplys(long commentId);

    List<Long> getPraise(long commentId);

    int insertPraise( long commentId, long userId);

    void addRanking(Ranking ranking,String openId);

    void deleteRanking(Long rankingId);

    void updateRanking(Ranking ranking);

    List<Ranking> getRankings( int day);


    /**
     * 汽车之家爬
     */
    List<BrandsHome> getBrands(int brandNum);
    int addManufacturer(CarHomeManufacturer carHomeManufacturer);

    int addSeries(CarHomeSeries carHomeSeries);

    List<CarHomeSeries> getSeries(int seriesNum);

    int addModel(CarHomeModel carHomeModel);

    void deleteError();

    int insertErrorGo(ErrorGoOn errorGoOn);

    ErrorGoOn getError();

    int addCarHomeKeys(CarKeysWords carKeysWords);

    int addVehicleConfigParam(CarHomeBasicParam carHomeBasicParam);

    int addVehicleSafeParam(CarHomeSecurityConfig carHomeSecurityConfig);

    int addVehicleColorParam(CarHomeExteriorColor carHomeExteriorColor);

    int addVehicleInnerColorParam(CarHomeExteriorColor carHomeExteriorColor);

    List<ModelByGroupBySeries> getModelIdGroupBySeriesId(int begin);

}
