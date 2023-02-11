package com.cc.conststar.wall.faced;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.cc.conststar.wall.entity.*;
import com.cc.conststar.wall.service.impl.BannerService;
import com.cc.conststar.wall.service.impl.CommentService;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import java.util.List;



@Component
public class CommentFaced {

    @Autowired
    BannerService bannerService;

    @Autowired
    CommentService commentService;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
        String url = getPhoneNumberApi(accessToken);
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

    public String getPhoneNumberApi(String accessToken) {
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

    /*public String getAccToken(String accessTokenUrl){
        String accessToken = "";
        System.out.println("获取accessToken-------------------------");
        if(redisTemplate.hasKey("accessToken") ){
            accessToken = redisTemplate.opsForValue().get("accessToken");
        }else {
            String response = getResponse(accessTokenUrl
                    + "&appid=" + "wx16e93eaddcfe2773" + "&secret=" + "2ada1b8609d6ebed8022532003cb505e");
            JSONObject object = JSON.parseObject(response);
            System.out.println("response:"+object);
            if (object != null && object.getString("access_token") != null ) {
                accessToken = object.getString("access_token");
                int expiresIn = object.getIntValue("expires_in");

                redisTemplate.opsForValue().set("accessToken",accessToken, expiresIn, TimeUnit.SECONDS);
                accessToken = redisTemplate.opsForValue().get("accessToken");
                System.out.println("accessToken"+expiresIn);
            }
            *//*redisTemplate.opsForValue().set("accessToken","accessTokenValue", 120, TimeUnit.SECONDS);
            accessToken = redisTemplate.opsForValue().get("accessToken");*//*

        }

        return accessToken;
    }
    public String getJsapiTicket(String accessToken,String jsapiTicketUrl){
        String jsapiTicket = "";
        System.out.println("jsapiTicket-------------------------");
        if(redisTemplate.hasKey("jsapiTicket") ){
            jsapiTicket = redisTemplate.opsForValue().get("jsapiTicket");
        }else {
            String response = getResponse(jsapiTicketUrl + "?access_token=" + accessToken + "&type=jsapi");
            JSONObject object = JSON.parseObject(response);
            System.out.println("response:"+object);
            if (object != null && object.getString("ticket") != null ) {
                jsapiTicket = object.getString("ticket");
                int expiresIn = object.getIntValue("expires_in");

                redisTemplate.opsForValue().set("jsapiTicket",jsapiTicket, expiresIn, TimeUnit.SECONDS);
                jsapiTicket = redisTemplate.opsForValue().get("jsapiTicket");
                System.out.println("jsapiTicket"+expiresIn);
            }
            *//*redisTemplate.opsForValue().set("jsapiTicket","jsapiTicketValue", 120, TimeUnit.SECONDS);
            jsapiTicket = redisTemplate.opsForValue().get("jsapiTicket");*//*

        }
        return jsapiTicket;
    }

    public WechatJsSDK makeShaSign(String url,String accessTokenUrl,String jsapiTicketUrl){

        WechatJsSDK wechatJsSDK = new WechatJsSDK();

        String timestamp = String.valueOf(System.currentTimeMillis());
        String noncestr = UUID.randomUUID().toString();

        String accToken = getAccToken(accessTokenUrl);
        if (StringUtils.isEmpty(accToken)){
            return null;
        }
        String jsapiTicket = getJsapiTicket(accToken,jsapiTicketUrl);
        if (StringUtils.isEmpty(jsapiTicket)){
            return null;
        }

        String waitSha = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + noncestr + "&timestamp="
                + timestamp + "&url=" + url;
        String shaSign = DigestUtils.sha1Hex(waitSha);

        wechatJsSDK.setAppId("appId");
        wechatJsSDK.setNonceStr(noncestr);
        wechatJsSDK.setTimestamp(Long.parseLong(timestamp));
        wechatJsSDK.setSignature(shaSign);
        return wechatJsSDK;
    }
*/

    public String redisTest(){
        List<Banner> url = bannerService.getUrl();
        String jsonString = JSON.toJSONString(url);
        redisTemplate.opsForList().rightPush("list",jsonString);
        List<String> list = redisTemplate.opsForList().range("list", 0, -1);
        return list.toString();
    }




}
