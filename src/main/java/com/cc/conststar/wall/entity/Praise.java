package com.cc.conststar.wall.entity;

import java.util.List;

public class Praise {

    private long userId;

    private List<Long> userIds;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
