package com.cc.conststar.wall.faced;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cc.conststar.wall.entity.Comment;
import com.cc.conststar.wall.entity.UserDetail;
import com.cc.conststar.wall.service.impl.CommentService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Component
public class CommentFaced {

    @Autowired
    CommentService commentService;

    private String url = "https://api.weixin.qq.com/sns/jscode2session";
    private String appID = "wxcf8ec48a1ef23834";
    private String secret = "a0d9b83b9d2802873e041d4d3317d4dc";




    public String getOpenId(String code){
        String openIdApi = getOpenIdApi(code);
        String response = getResponse(openIdApi);

        JSONObject object = JSON.parseObject(response);
        //System.out.println(object);
        if (object != null && object.getString("session_key") != null
                && object.getString("openid") != null) {
            String sessionKey = object.getString("session_key");
            String openid = object.getString("openid");
            return openid;
        }
        return null;
    }

    public Boolean addUserPhone (String code) throws Exception {
        String accessToken = getAccessToken();
        String url = getgetPhoneNumberApi(accessToken);
        StringBuilder json = new StringBuilder();
        json.append("{\"code\": \""+code+"\"}");
        String s = doPost(url, json.toString());
        JSONObject object = JSON.parseObject(s);
        if (object != null && object.getJSONObject("phone_info") != null) {
            JSONObject phone_info = object.getJSONObject("phone_info");
            String phoneNumber = phone_info.getString("phoneNumber");
            String purePhoneNumber = phone_info.getString("purePhoneNumber");
            //System.out.println("号码是："+ phoneNumber);
            //System.out.println("号码是："+ purePhoneNumber);
            String openId = getOpenId(code);
            UserDetail userDetail = new UserDetail();
            userDetail.setOpenId(openId);
            userDetail.setPhone(phoneNumber);
        }

        return true;
    }

    public String getAccessToken(){
        String openIdApi = getAccessTokenApi();
        String response = getResponse(openIdApi);
        JSONObject object = JSON.parseObject(response);
        //System.out.println(object);
        if (object != null && object.getString("access_token") != null) {
            String accessToken = object.getString("access_token");
            return accessToken;
        }
        return null;
    }


    public static String getResponse(String serverUrl) {
        // 用JAVA发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(500);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (Exception e) {
            System.out.println("take:  -----"+e);
        }
        return result.toString();
    }

    public String getOpenIdApi(String code) {
        if (StringUtils.isEmpty(code)){
            return null;
        }
        StringBuilder builder = new StringBuilder(url);
        builder.append("?appid=").append(appID);
        builder.append("&secret=").append(secret);
        builder.append("&js_code=").append(code);
        return builder.toString();
    }

    public String getAccessTokenApi() {
        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential");
        builder.append("&appid=").append(appID);
        builder.append("&secret=").append(secret);
        return builder.toString();
    }

    public String getgetPhoneNumberApi(String accessToken) {
        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=");
        builder.append(accessToken);
        return builder.toString();
    }
    private String doPost(String url, String jsonString) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        // 构建post请求并设置参数类型为app/json
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        //StringEntity entity = new StringEntity(request.toJSON(), "UTF-8");
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        post.setEntity(entity);// 设置请求的内容

        CloseableHttpResponse response = client.execute(post);// 执行post请求
        int statusCode = response.getStatusLine().getStatusCode(); // 获取响应码

        String result = (statusCode == 200) ? EntityUtils.toString(response.getEntity()) : "";
        response.close();
        client.close();
        return result;
    }

}
