package com.cc.conststar.wall.service.impl.impl;

import com.cc.conststar.wall.dao.CommentDao;
import com.cc.conststar.wall.entity.*;
import com.cc.conststar.wall.service.impl.CommentService;
import org.apache.ibatis.annotations.Param;
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

        commentDao.updateById(userDetail);
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
            int isCheck = comment.getIsCheck();
            if (isCheck == 1){
                comment.setUserName("匿名用户");
                comment.setHeadUrl("http://1.14.75.234:8082/pic/niming.jpg");
            }
        }
        return comments;
    }

    @Override
    public List<CommentsVO> getCommentsByUserId(long id) {
        List<CommentsVO> comments = commentDao.getCommentsByUserId(id);
        return comments;
    }

    @Override
    public CommentsVO getCommentById(int id) {
        CommentsVO commentById = commentDao.getCommentById(id);
        int isCheck = commentById.getIsCheck();
        if (isCheck == 1){
            commentById.setUserName("匿名用户");
            commentById.setHeadUrl("http://1.14.75.234:8082/pic/niming.jpg");
        }
        return commentById;
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

    @Override
    public void insertreply(Reply reply,String openId) {
        UserDetail userDetail = commentDao.queryByOpenId(openId);
        if (null == userDetail ){
            if (null == userDetail.getId()){
                return;
            }
        }
        reply.setUserId(userDetail.getId());
        commentDao.insertreply(reply);
    }

    @Override
    public void updateReply(Reply reply) {
        commentDao.updateReply(reply);
    }

    @Override
    public List<Reply> getReplys(long commentId) {
        List<Reply> replys = commentDao.getReplys(commentId);
        return replys;
    }

    @Override
    public List<Long> getPraise(long commentId) {
        List<Long> praise = commentDao.getPraise(commentId);
        return praise;
    }

    @Override
    public int insertPraise(long commentId, long userId) {
        int i = commentDao.insertPraise(commentId, userId);
        return i;
    }

    @Override
    public void addRanking(Ranking ranking, String openId) {

        UserDetail userDetail = commentDao.queryByOpenId(openId);
        if (null == userDetail ){
            if (null == userDetail.getId()){
                return;
            }
        }
        ranking.setUserId(userDetail.getId());
        commentDao.addRanking(ranking);


    }

    @Override
    public void deleteRanking(Long rankingId) {

        Ranking ranking =new Ranking();

        ranking.setId(rankingId);
        ranking.setIsDelete(1);
        int i = commentDao.updateRanking(ranking);

    }

    @Override
    public void updateRanking(Ranking ranking) {
        int i = commentDao.updateRanking(ranking);
    }

    @Override
    public List<Ranking> getRankings(int day) {
        return commentDao.getRankings(day);
    }

    @Override
    public List<BrandsHome> getBrands(int brandNum) {
        return commentDao.getBrands(brandNum);
    }

    @Override
    public int addManufacturer(CarHomeManufacturer carHomeManufacturer) {
        return commentDao.addManufacturer(carHomeManufacturer);
    }

    @Override
    public int addSeries(CarHomeSeries carHomeSeries) {
        return commentDao.addSeries(carHomeSeries);
    }

    @Override
    public List<CarHomeSeries> getSeries(int seriesNum) {
        return commentDao.getSeries(seriesNum);
    }

    @Override
    public int addModel(CarHomeModel carHomeModel) {
        return commentDao.addModel(carHomeModel);
    }

    @Override
    public void deleteError() {
        commentDao.deleteError();
    }

    @Override
    public int insertErrorGo(ErrorGoOn errorGoOn) {
        return commentDao.insertErrorGo( errorGoOn);
    }

    @Override
    public ErrorGoOn getError() {
        return commentDao.getError();
    }
}
