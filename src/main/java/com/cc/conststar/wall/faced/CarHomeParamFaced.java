package com.cc.conststar.wall.faced;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cc.conststar.wall.entity.*;
import com.cc.conststar.wall.service.impl.CommentService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 *
 *获取车型参数
 */
@Component
public class CarHomeParamFaced {

    public final static String SCRIPT_PRE = "var rules = '';var document = {};document.createElement = function() {return {sheet: {insertRule: " +
            "function(rule, i) {if (rules.length == 0) {rules = rule;} else {rules = rules + '|' + rule;}}}}};document.getElementsByTagName = " +
            "function() {};document.querySelectorAll = function() {return {};};document.head = {};document.head.appendChild = function() " +
            "{};var window = {};window.decodeURIComponent = decodeURIComponent;window.location = {};window.location.href = 'car.m.autohome.com.cn';";

    public final static Pattern CSS_PATTERN = Pattern.compile("\\.(.*)::before.*content:\"(.*)\".*");


    @Autowired
    CommentService commentService;

    public int startGetHomeVehicle(){

        int total = 0;
        int brandNumE = 0;

        ErrorGoOn error1 = commentService.getError();
        int brandNum = 0;
        if (null != error1){
            brandNum = error1.getBrandNum();
        }
        List<BrandsHome> brands = commentService.getBrands(brandNum);
        Integer brandid = 0;
        try {
            for (BrandsHome brand : brands) {
                Integer id = brand.getId();
                String url = "https://www.autohome.com.cn/ashx/index/GetHomeFindCar.ashx?type=1&brandid="+id+"&v=1";
                String response = getResponseByEncoding(url,"UTF-8");

                System.out.println(response);
                brandNumE +=1;
                JSONObject object = JSON.parseObject(response);
                if (null == object){
                    continue;
                }
                if (object.getInteger("returncode") == 0){
                    JSONObject result = object.getJSONObject("result");
                    String brandname = result.getString("brandname");
                    String brandlogo = result.getString("brandlogo");
                    brandid = result.getInteger("brandid");

                    JSONArray fctlist = result.getJSONArray("fctlist");
                    for (Object o : fctlist) {
                        /**
                         * 厂商
                         */
                        CarHomeManufacturer carHomeManufacturer = new CarHomeManufacturer();
                        JSONObject jsonObject = JSON.parseObject(o.toString());

                        System.out.println(jsonObject.toJSONString());

                        Integer fctid = jsonObject.getInteger("fctid");
                        String fctname = jsonObject.getString("fctname");
                        String fctPy = jsonObject.getString("fctPy");
                        String seriesplace = jsonObject.getString("seriesplace");
                        Integer seriesplacenum = jsonObject.getInteger("seriesplacenum");

                        carHomeManufacturer.setFctid(fctid);
                        carHomeManufacturer.setFctname(fctname);
                        carHomeManufacturer.setFctpy(fctPy);
                        carHomeManufacturer.setSeriesPlace(seriesplace);
                        carHomeManufacturer.setSeriesPlaceNum(seriesplacenum);

                        int i = commentService.addManufacturer(carHomeManufacturer);
                        total = total + i;

                        JSONArray serieslist = jsonObject.getJSONArray("serieslist");
                        for (Object series : serieslist) {
                            /**
                             * 车系
                             */
                            CarHomeSeries carHomeSeries = new CarHomeSeries();
                            JSONObject seriesObject = JSON.parseObject(series.toString());

                            System.out.println(seriesObject.toJSONString());

                            Integer seriesid = seriesObject.getInteger("seriesid");
                            String seriesName = seriesObject.getString("seriesName");
                            String seriesImg = seriesObject.getString("seriesImg");
                            Integer seriesState = seriesObject.getInteger("seriesState");
                            Integer seriesPriceMin = seriesObject.getInteger("seriesPriceMin");
                            Integer seriesPriceMax = seriesObject.getInteger("seriesPriceMax");
                            Integer fctidSer = seriesObject.getInteger("fctid");
                            String fctnameSer = seriesObject.getString("fctname");
                            String seriesplaceSer = seriesObject.getString("seriesplace");
                            Integer rank = seriesObject.getInteger("rank");
                            String pnglogo = seriesObject.getString("pnglogo");
                            Integer newenergySeriesId = seriesObject.getInteger("newenergySeriesId");
                            Integer containbookedspec = seriesObject.getInteger("containbookedspec");
                            Integer newenergy = seriesObject.getInteger("newenergy");
                            Integer levelId = seriesObject.getInteger("levelId");
                            String levelName = seriesObject.getString("levelName");
                            String fctPySer = seriesObject.getString("fctPy");
                            Integer isForeignCar = seriesObject.getInteger("isForeignCar");
                            Integer uvRank = seriesObject.getInteger("uvRank");
                            Integer hotRank = seriesObject.getInteger("hotRank");

                            carHomeSeries.setSeriesid(Long.valueOf(seriesid));
                            carHomeSeries.setSeriesname(seriesName);
                            carHomeSeries.setSeriesimg(seriesImg);
                            carHomeSeries.setSeriesstate(seriesState);
                            carHomeSeries.setSeriespricemin(seriesPriceMin);
                            carHomeSeries.setSeriespricemax(seriesPriceMax);
                            carHomeSeries.setFctid(fctidSer);
                            carHomeSeries.setFctname(fctnameSer);
                            carHomeSeries.setSeriesplace(seriesplaceSer);
                            carHomeSeries.setRank(rank);
                            carHomeSeries.setPnglogo(pnglogo);
                            carHomeSeries.setNewenergyseriesid(newenergySeriesId);
                            carHomeSeries.setContainbookedspec(containbookedspec);
                            carHomeSeries.setNewenergy(newenergy);
                            carHomeSeries.setLevelid(levelId);
                            carHomeSeries.setLevelname(levelName);
                            carHomeSeries.setFctpy(fctPySer);
                            carHomeSeries.setIsforeigncar(isForeignCar);
                            carHomeSeries.setUvrank(uvRank);
                            carHomeSeries.setHotrank(hotRank);
                            carHomeSeries.setBrandId(brandid);

                            int i1 = commentService.addSeries(carHomeSeries);
                            System.out.println("-----当前保存第"+total+"条车系数据-----车系ID："+ seriesid);
                            total = total + i1;

                        }

                    }

                }
            }
        } catch (Exception e) {
            System.out.println("-------------"+e+"-----------");
        } finally {
            ErrorGoOn errorGoOn = new ErrorGoOn();
            errorGoOn.setBrandNum(brandNumE);
            errorGoOn.setBrandId(brandid);
            commentService.insertErrorGo(errorGoOn);
        }

        return total;

    }

    public int startGetHomeVehicleModels() {

        int total = 0;
        int seriesNumE = 0;
        ErrorGoOn error1 = commentService.getError();
        int seriesNum = 0;
        if (null != error1){
            seriesNum = error1.getSeriesNum();
        }
        List<CarHomeSeries> seriesIds = commentService.getSeries(seriesNum);
        commentService.deleteError();
        Long seriesid = 0L;
        try {
            for (CarHomeSeries carHomeSeries : seriesIds) {
                seriesid = carHomeSeries.getSeriesid();
                String url = "https://www.autohome.com.cn/ashx/index/GetHomeFindCar.ashx?type=2&seriesid=" + seriesid + "&v=1&format=json";
                String response = getResponseByEncoding(url, "UTF-8");
                seriesNumE += 1;

                System.out.println(response);
                JSONArray objects = JSON.parseArray(response);

                if (null != objects && !objects.isEmpty()) {
                    for (Object map : objects) {
                        JSONObject jsonObject = JSON.parseObject(map.toString());

                        Integer typeId = jsonObject.getInteger("id");           //20  or  40   or   30
                        String name = jsonObject.getString("name");         //在售   or  停售   or    即将销售
                        JSONArray list = jsonObject.getJSONArray("list");
                        for (Object o : list) {
                            JSONObject parseObject = JSON.parseObject(o.toString());
                            Integer modelId = parseObject.getInteger("id");
                            String modelName = parseObject.getString("name");
                            Integer price = parseObject.getInteger("price");

                            CarHomeModel carHomeModel = new CarHomeModel();
                            carHomeModel.setModelId(modelId);
                            carHomeModel.setModelName(modelName);
                            carHomeModel.setPrice(price);
                            carHomeModel.setSeriesId(seriesid);
                            carHomeModel.setTypeId(typeId);
                            int i = commentService.addModel(carHomeModel);
                            total += i;
                            System.out.println("-----当前保存第"+total+"条车型数据------车型ID："+modelId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("-------------"+e+"-----------");
        } finally {
            ErrorGoOn errorGoOn = new ErrorGoOn();
            errorGoOn.setSeriesNum(seriesNumE);
            errorGoOn.setSeriesId(Math.toIntExact(seriesid));
            commentService.insertErrorGo(errorGoOn);
        }
        return total;
    }



    public int startSaveModelParams() throws Exception {

        int total = 0;

        //String url = "https://car.autohome.com.cn/config/spec/58422.html#pvareaid=58422";
        String url = "https://car.autohome.com.cn/config/spec/51937.html#pvareaid=3454541";
        Connection.Response response = Jsoup.connect(url).validateTLSCertificates(false).ignoreContentType(true).ignoreHttpErrors(true).execute();
        System.out.println(response.statusCode());
        Document document = response.parse();
        Elements scripts = document.select("script:containsData(insertRule)");

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByName("JavaScript");
        Map<String, String> cssKeyValue =  new HashMap<String, String>();
        System.out.println("------------css数据------------");
        scripts.forEach(element -> {
            String script = SCRIPT_PRE + element.html();
            try {
                engine.eval(script);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            String css = (String) engine.get("rules");
            //System.out.println(css);
            for (String str : css.split("\\|")) {
                Matcher cssMatcher = CSS_PATTERN.matcher(str);
                if (cssMatcher.find()) {
                    cssKeyValue.put("<span class='" + cssMatcher.group(1) + "'></span>", cssMatcher.group(2));
                }
            }
        });
        Elements contents = document.select("script:containsData(keyLink)");
        String content = contents.html();
        System.out.println("------------用css混淆的配置数据------------");
        //System.out.println(content);
        //把混淆数据里的样式用上面解析的样式给替代
        for(Map.Entry<String, String> entry : cssKeyValue.entrySet()) {
            content = content.replaceAll(entry.getKey(), entry.getValue());
        }
        System.out.println("------------用css替换后的数据------------");
        //System.out.println(content);
        engine.eval(content);
        System.out.println("------------每个配置结果------------");
        String keyLink = JSONObject.toJSONString(engine.get("keyLink"));
        String config = JSONObject.toJSONString(engine.get("config"));
        String option = JSONObject.toJSONString(engine.get("option"));
        String bag = JSONObject.toJSONString(engine.get("bag"));
        String color = JSONObject.toJSONString(engine.get("color"));
        String innerColor = JSONObject.toJSONString(engine.get("innerColor"));
        System.out.println(keyLink);        //key
        System.out.println(config);       //配置信息 基本参数，车身，发动机，变速箱，地盘转向，车轮制动
        JSONObject jsonObject = JSON.parseObject(config);
        if (jsonObject != null){
            if (jsonObject.getString("returncode").equals("0")){
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject paramtypeitems = result.getJSONObject("paramtypeitems");
                Map<String, Object> innerMap = paramtypeitems.getInnerMap();
                //System.out.println("--------实验："+innerMap.toString());
                for (Object value : innerMap.values()) {
                    JSONObject paramitem = JSON.parseObject(value.toString());
                    String name = paramitem.getString("name");
                    System.out.println("上层名称："+name+"：");
                    JSONObject paramitems = paramitem.getJSONObject("paramitems");
                    Map<String, Object> innerMap1 = paramitems.getInnerMap();
                    for (Object o : innerMap1.values()) {
                        JSONObject jsonObject1 = JSON.parseObject(o.toString());
                        Integer id = jsonObject1.getInteger("id");
                        String name1 = jsonObject1.getString("name");
                        String pnid = jsonObject1.getString("pnid");
                        JSONObject valueitems = jsonObject1.getJSONObject("valueitems");
                        System.out.println("---------名称："+name1+"：");
                        Map<String, Object> valueitemsInnerMap = valueitems.getInnerMap();
                        for (Object valueParam : valueitemsInnerMap.values()) {
                            JSONObject valueParamJson = JSON.parseObject(valueParam.toString());
                            Integer specid = valueParamJson.getInteger("specid");
                            String value1 = valueParamJson.getString("value");
                            System.out.println("ID结果："+specid);
                            System.out.println("字符结果："+value1);
                        }
                    }

                }
            }
        }
        //System.out.println(option);       //主被动安全--冰箱
        //System.out.println(bag);            //选装套件
        //System.out.println(color);          //外观颜色
        //System.out.println(innerColor);        //内饰颜色

        return total;

    }

    public static String getResponseByEncoding(String serverUrl,String encoding) {
        // 用JAVA发起http请求，并返回json格式的结果
        String result = null;
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(500);

            InputStream inputStream = new GZIPInputStream(conn.getInputStream());
            Reader reader = new InputStreamReader(inputStream, "GBK");
            StringBuilder out = new StringBuilder();
            char[] buffer = new char[4096];
            int length;
            while ((length = reader.read(buffer)) > 0) {
                out.append(buffer, 0, length);
            }
            result = out.toString();

            reader.close();
            inputStream.close();
        } catch (Exception e) {
            System.out.println("take:  -----"+e);
        }
        return result;
    }

}