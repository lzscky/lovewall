package com.cc.conststar.wall.faced;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.conststar.wall.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VoiceFaced {


    @Value("${bemfaurl.pushurl}")
    private String pushMessageUrl;

    @Value("${bemfaurl.geturl}")
    private String getMessageUrl;

    @Value("${bemfaurl.getalltopic}")
    private String getAllTopic;

    @Value("${bemfacode.uid}")
    private String uid;

    private final int  MQTTtype = 1;//     1:MQTT协议   3:TCP协议

    private final int  TCPtype = 3;//     1:MQTT协议   3:TCP协议

    public Boolean pushMessage(String topic,String msg){



        String param = "uid=" + uid + "&topic=" + topic + "&type=" + MQTTtype + "&msg=" + msg;
        System.out.println("参数："+param);
        JSONObject jsonObject = HttpUtil.postFormUrlEncoded(pushMessageUrl,param );
        System.out.println(jsonObject);
        if (jsonObject!=null){
            if (jsonObject.getInteger("code") == 0){
                return true;
            }
        }

        return false;

    }

    public List<Map<String,String>> getMessage(String topic,int num){

        List<Map<String,String>> result = new ArrayList<>();
        if (num == 0){
            num = 1;
        }
        String url =getMessageUrl + "?uid=" + uid + "&topic=" + topic + "&type=" + MQTTtype + "&num=" + num;

        return getMaps(result, url);
    }

    public List<Map<String,String>> getAllTopic(){

        List<Map<String,String>> result = new ArrayList<>();

        String url =getAllTopic + "?uid=" + uid  + "&type=" + MQTTtype ;

        return getMaps(result, url);
    }

    private List<Map<String, String>> getMaps(List<Map<String, String>> result, String url) {
        JSONObject jsonObject = HttpUtil.doGet(url);
        if (null != jsonObject && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 0){
            JSONArray data = jsonObject.getJSONArray("data");
            for (Object datum : data) {
                Map<String,String> map = (Map)datum;
                result.add(map);
            }
        }
        return result;
    }

}
