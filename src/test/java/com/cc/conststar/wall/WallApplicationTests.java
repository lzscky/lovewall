package com.cc.conststar.wall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cc.conststar.wall.faced.CommentFaced;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class WallApplicationTests {

    @Autowired
    CommentFaced commentFaced;

    @Test
    void contextLoads() throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String creatTime = "2022-12-10 4:00:43";
        Date parse = ft.parse(creatTime);
        Date d = new Date();
        long timeDifference = d.getTime()-parse.getTime();
        System.out.println(d.getTime());
        System.out.println(parse.getTime());
        System.out.println(timeDifference);
        int a = (int) (timeDifference / 1000 /60);
        System.out.println(a);

    }
    @Test
    void redisTest(){
        String s = commentFaced.redisTest();
        System.out.println(s);
    }


}
