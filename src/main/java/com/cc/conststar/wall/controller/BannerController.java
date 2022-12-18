package com.cc.conststar.wall.controller;

import com.cc.conststar.wall.entity.Banner;
import com.cc.conststar.wall.service.impl.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    BannerService bannerService;

    @GetMapping("/getBanner")
    public List<Banner> getView() {
        List<Banner> url = bannerService.getUrl();
        return url;
    }
}
