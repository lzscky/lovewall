package com.cc.conststar.wall.service.impl.impl;

import com.cc.conststar.wall.dao.BannerDao;
import com.cc.conststar.wall.entity.Banner;
import com.cc.conststar.wall.service.impl.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    BannerDao bannerDao;

    @Override
    public List<Banner> getUrl() {
        List<Banner> banners = bannerDao.queryAllByLimit();
        return banners;
    }

    @Override
    public void editBanner(Banner banner) {

    }

    @Override
    public void addBanner(Banner banner) {

    }
}
