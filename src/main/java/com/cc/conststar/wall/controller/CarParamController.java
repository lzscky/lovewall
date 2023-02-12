package com.cc.conststar.wall.controller;


import com.cc.conststar.wall.constant.ResponseEnumConstant;

import com.cc.conststar.wall.faced.CarHomeParamFaced;

import com.cc.conststar.wall.handler.FormatHandler;
import com.cc.conststar.wall.handler.GenericHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/vehicle")
public class CarParamController {


    @Autowired
    CarHomeParamFaced faced;



    @GetMapping("/startGetHomeVehicle")
    public GenericHandler<Integer> startGetHomeVehicle(){

        int i = faced.startGetHomeVehicle();

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, i);

    }
    @GetMapping("/startGetHomeVehicleModels")
    public GenericHandler<Integer> startGetHomeVehicleModels(){

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, faced.startGetHomeVehicleModels());

    }

    @GetMapping("/startSaveModel")
    public GenericHandler<Long> startSaveModel() throws Exception {

        return FormatHandler.retParam(ResponseEnumConstant.CODE_200, faced.startSaveModelParams());

    }



}
