package com.cc.conststar.wall.controller;

import com.cc.conststar.wall.constant.ResponseEnumConstant;
import com.cc.conststar.wall.faced.VoiceFaced;
import com.cc.conststar.wall.handler.FormatHandler;
import com.cc.conststar.wall.handler.GenericHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/voiceControl")
public class BemfaApiController {

    @Autowired
    VoiceFaced voiceFaced;



    @GetMapping( "/pushMessage")
    public GenericHandler<Boolean> pushMessage(@RequestParam("topic") String topic, @RequestParam("msg") String msg) {


        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, voiceFaced.pushMessage(topic, msg));

    }

    @GetMapping( "/getMessage")
    public GenericHandler<List<Map<String,String>>> getMessage(@RequestParam("topic") String topic, @RequestParam("num") int num) {

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, voiceFaced.getMessage(topic, num));

    }


}
