package com.cc.conststar.wall.constant;
import lombok.Getter;

@Getter
public enum ResponseEnumConstant {

    //成功状态码 200
    CODE_200(200,"成功"),

    //非异常错误（正常的错误） 201-299
    CODE_201(201,"操作失败"),

    //服务器错误 1000
    CODE_1000(1000,"服务器错误"),

    //参数错误：10001-19999
    CODE_10001(10001,"参数无效"),
    CODE_10002(10002,"参数无效"),
    CODE_10003(10003,"参数类型错误"),
    CODE_10004(10004,"参数缺失"),

    //用户错误：20001-29999
    CODE_20001(20001,"用户未登录"),
    CODE_20002(20002,"用户密码错误"),
    CODE_20003(20003,"账号已被禁用"),
    CODE_20004(20004,"用户不存在"),
    CODE_20005(20005,"用户已存在"),

    //业务错误：30001-39999
    CODE_30001(30001,"某业务出现问题"),

    //系统错误：40001-49999
    CODE_40001(40001,"系统繁忙，请稍后重试"),

    //数据错误：50001-599999
    CODE_50001(50001,"数据未找到"),
    CODE_50002(50002,"数据有误"),
    CODE_50003(50003,"数据已存在"),
    CODE_50004(50004,"查询出错"),

    //接口错误：60001-69999
    CODE_60001(60001,"内部系统接口调用异常"),
    CODE_60002(60002,"外部系统接口调用异常"),
    CODE_60003(60003,"该接口禁止访问"),
    CODE_60004(60004,"接口地址无效"),
    CODE_60005(60005,"接口请求超时"),
    CODE_60006(60006,"接口负载过高"),

    //权限错误：70001-79999
    CODE_70001(70001,"无权限访问");


    int code;
    String desc;
    ResponseEnumConstant(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}