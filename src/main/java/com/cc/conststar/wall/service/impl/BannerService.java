package com.cc.conststar.wall.service.impl;

import com.cc.conststar.wall.entity.Banner;

import java.util.List;

public interface BannerService {

    public List<Banner> getUrl();

    public void editBanner(Banner banner);

    public void addBanner(Banner banner);

}
