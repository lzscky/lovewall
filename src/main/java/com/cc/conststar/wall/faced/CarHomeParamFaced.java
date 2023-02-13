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

                        carHomeManufacturer.setBrandId(brandid);
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
                            //total = total + i1;

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


    long modelNumTotle = 0;
    public long startSaveModelParams() {

        ErrorGoOn error = commentService.getError();
        int errorModelId = error.getModelId();
        int errorModelNum = error.getModelNum();
        List<ModelByGroupBySeries> modelIdGroupBySeriesIdList = commentService.getModelIdGroupBySeriesId(errorModelNum);
        if (null == modelIdGroupBySeriesIdList){
            return 0;
        }

        int modelId = 0;
        int modelNum = 0;
        try {
            for (ModelByGroupBySeries modelByGroupBySeries : modelIdGroupBySeriesIdList) {

                modelId = modelByGroupBySeries.getModelId();
                modelNum += 1;
                String url = "https://car.autohome.com.cn/config/spec/51933.html#pvareaid=58422";
                //String url = "https://car.autohome.com.cn/config/spec/"+modelId+".html#pvareaid=3454541";

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
                //System.out.println("------------每个配置结果------------");
                String keyLink = JSONObject.toJSONString(engine.get("keyLink"));
                String config = JSONObject.toJSONString(engine.get("config"));
                String option = JSONObject.toJSONString(engine.get("option"));
                String bag = JSONObject.toJSONString(engine.get("bag"));
                String color = JSONObject.toJSONString(engine.get("color"));
                String innerColor = JSONObject.toJSONString(engine.get("innerColor"));
                //System.out.println(keyLink);        //key
                //System.out.println(config);       //配置信息 基本参数，车身，发动机，变速箱，地盘转向，车轮制动
                //System.out.println(option);       //主被动安全--冰箱
                //System.out.println(bag);            //选装套件
                //System.out.println(color);          //外观颜色
                //System.out.println(innerColor);        //内饰颜色
                JSONObject configJson = JSON.parseObject(config);
                JSONObject optionJson = JSON.parseObject(option);
                //JSONObject bagJson = JSON.parseObject(bag);
                JSONObject colorJson = JSON.parseObject(color);
                JSONObject innerColorJson = JSON.parseObject(innerColor);

                //基础配置
                int configNum = saveConfig(configJson,modelNum);

                //主被动安全--冰箱
                int optionNum = saveOptionJson(optionJson,modelNum);
                //选装套件              暂未发现有值的车型
                //
                //外观颜色
                int colorNum = saveColorJson(colorJson,modelNum);
                //内饰颜色
                int innerColorNum = saveInnerColorJson(colorJson,modelNum);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        } finally {
            ErrorGoOn errorGoOn = new ErrorGoOn();
            errorGoOn.setModelId(modelId);
            errorGoOn.setModelNum(modelNum);
            commentService.insertErrorGo(errorGoOn);
        }
        return modelNumTotle;

    }
    private int saveInnerColorJson(JSONObject colorJson,int seriesNum){
        int innerColorNum = 0;
        if (colorJson != null){
            if (colorJson.getString("returncode").equals("0")){
                JSONObject result = colorJson.getJSONObject("result");
                Integer total1 = result.getInteger("total");
                if (total1 > 0){
                    JSONObject specitems = result.getJSONObject("specitems");
                    Map<String, Object> specitemsMap = specitems.getInnerMap();
                    for (Object specitem : specitemsMap.values()) {
                        JSONObject specitemJson = JSON.parseObject(specitem.toString());
                        Integer specid = specitemJson.getInteger("specid");
                        JSONObject coloritems = specitemJson.getJSONObject("coloritems");
                        Map<String, Object> coloritemsInnerMap = coloritems.getInnerMap();
                        for (Object colorItem : coloritemsInnerMap.values()) {
                            CarHomeExteriorColor carHomeExteriorColor = foreachColorItem(specid, colorItem);
                            int i = commentService.addVehicleInnerColorParam(carHomeExteriorColor);
                            innerColorNum += i;
                            modelNumTotle += 1;
                            System.out.println("--------------成功保存第"+seriesNum+"款车系的车型内饰颜色参数，第"+innerColorNum+"条数据------总计"+modelNumTotle+"条数据------");
                        }
                    }
                }
            }
        }
        return innerColorNum;
    }

    private CarHomeExteriorColor foreachColorItem(Integer specid, Object colorItem) {
        JSONObject colorItemJson = JSON.parseObject(colorItem.toString());
        Integer clubpicnum = colorItemJson.getInteger("clubpicnum");
        String name = colorItemJson.getString("name");
        Integer picnum = colorItemJson.getInteger("picnum");
        Integer id = colorItemJson.getInteger("id");
        String value = colorItemJson.getString("value");

        CarHomeExteriorColor carHomeExteriorColor = new CarHomeExteriorColor();
        carHomeExteriorColor.setColorId(id);
        carHomeExteriorColor.setClubpicnum(clubpicnum);
        carHomeExteriorColor.setPicnum(picnum);
        carHomeExteriorColor.setSpecid(specid);
        carHomeExteriorColor.setName(name);
        carHomeExteriorColor.setValue(value);
        return carHomeExteriorColor;
    }

    private int saveColorJson(JSONObject colorJson,int seriesNum){
        int colorNum = 0;
        if (colorJson != null){
            if (colorJson.getString("returncode").equals("0")){
                JSONObject result = colorJson.getJSONObject("result");
                Integer total1 = result.getInteger("total");
                if (total1 > 0){
                    JSONObject specitems = result.getJSONObject("specitems");
                    Map<String, Object> specitemsMap = specitems.getInnerMap();
                    for (Object specitem : specitemsMap.values()) {
                        JSONObject specitemJson = JSON.parseObject(specitem.toString());
                        Integer specid = specitemJson.getInteger("specid");
                        JSONObject coloritems = specitemJson.getJSONObject("coloritems");
                        Map<String, Object> coloritemsInnerMap = coloritems.getInnerMap();
                        for (Object colorItem : coloritemsInnerMap.values()) {
                            CarHomeExteriorColor carHomeExteriorColor = foreachColorItem(specid, colorItem);
                            int i = commentService.addVehicleColorParam(carHomeExteriorColor);
                            colorNum += i;
                            modelNumTotle += 1;
                            System.out.println("--------------成功保存第"+seriesNum+"款车系的车型外饰颜色参数，第"+colorNum+"条数据------总计"+modelNumTotle+"条数据------");
                        }
                    }
                }
            }
        }
        return colorNum;
    }

    private int saveOptionJson(JSONObject optionJson,int seriesNum){
        int optionNum = 0;
        if (optionJson != null){
            if (optionJson.getString("returncode").equals("0")){
                JSONObject result = optionJson.getJSONObject("result");
                JSONObject optionconfigtypeitems = result.getJSONObject("configtypeitems");
                Map<String, Object> optioninnerMap = optionconfigtypeitems.getInnerMap();
                for (Object optiono : optioninnerMap.values()) {
                    JSONObject optionJsonObject = JSON.parseObject(optiono.toString());
                    String optionName = optionJsonObject.getString("name");
                    //System.out.println("-------上层名称："+optionName+"---------");
                    JSONObject optionConfigitems = optionJsonObject.getJSONObject("configitems");
                    Map<String, Object> optionConfigMap = optionConfigitems.getInnerMap();
                    for (Object optionConfigValue : optionConfigMap.values()) {
                        JSONObject optionConfigValueJson = JSON.parseObject(optionConfigValue.toString());
                        Integer optionConfigid = optionConfigValueJson.getInteger("configid");
                        Integer optionKeyId = optionConfigValueJson.getInteger("id");
                        String name = optionConfigValueJson.getString("name");
                        String pnid = optionConfigValueJson.getString("pnid");
                        //System.out.println("-----中间名称："+name+"----------");
                        JSONObject valueitems = optionConfigValueJson.getJSONObject("valueitems");
                        Map<String, Object> valueitemsInnerMap = valueitems.getInnerMap();
                        for (Object optionValueItem : valueitemsInnerMap.values()) {
                            JSONObject optionValueItemJson = JSON.parseObject(optionValueItem.toString());
                            JSONObject optionPrice = optionValueItemJson.getJSONObject("price");
                            int optionPriceItemPrice = 0;
                            String priceSubName = null;
                            if ( null != optionPrice){
                                //System.out.println("-----optionPrice非空："+optionPrice+"----------");
                                Map<String, Object> optionPriceInnerMap = optionPrice.getInnerMap();
                                for (Object optionPriceItem : optionPriceInnerMap.values()) {
                                    JSONObject optionPriceItemJson = JSON.parseObject(optionPriceItem.toString());
                                    optionPriceItemPrice = optionPriceItemJson.getInteger("price");
                                    priceSubName = optionPriceItemJson.getString("subname");
                                }
                            }
                            Integer specid = optionValueItemJson.getInteger("specid");
                            JSONObject sublist = optionValueItemJson.getJSONObject("sublist");
                            int subListPrice = 0;
                            String subListSubName = null;
                            int subListSubValue = 0;
                            if ( null != sublist){
                                Map<String, Object> sublistInnerMap = sublist.getInnerMap();
                                for (Object subListValue : sublistInnerMap.values()) {
                                    JSONObject subListValueJson = JSON.parseObject(subListValue.toString());
                                    subListPrice = subListValueJson.getInteger("price");
                                    subListSubName = subListValueJson.getString("subname");
                                    subListSubValue = subListValueJson.getInteger("subvalue");
                                    //System.out.println("-----subListValue："+subListSubName);
                                }
                            }
                            String value = optionValueItemJson.getString("value");

                            CarHomeSecurityConfig carHomeSecurityConfig = new CarHomeSecurityConfig();
                            carHomeSecurityConfig.setTopName(optionName);
                            carHomeSecurityConfig.setConfigId(optionConfigid);
                            carHomeSecurityConfig.setKeyId(optionKeyId);
                            carHomeSecurityConfig.setName(name);
                            carHomeSecurityConfig.setPnid(pnid);
                            carHomeSecurityConfig.setPricePrice(optionPriceItemPrice);
                            carHomeSecurityConfig.setPriceSubname(priceSubName);
                            carHomeSecurityConfig.setSpecid(specid);
                            carHomeSecurityConfig.setSublistPrice(subListPrice);
                            carHomeSecurityConfig.setSublistSubname(subListSubName);
                            carHomeSecurityConfig.setSublistSubvalue(subListSubValue);
                            carHomeSecurityConfig.setValue(value);

                            int i = commentService.addVehicleSafeParam(carHomeSecurityConfig);
                            optionNum += i;
                            modelNumTotle +=1;
                            System.out.println("--------------成功保存第"+seriesNum+"款车系的车型安全配置参数，第"+optionNum+"条数据------总计"+modelNumTotle+"条数据------");
                            //System.out.println("--------数据：车型ID:"+specid+"-----值："+value);

                        }
                    }
                }
            }
        }
        return optionNum;
    }

    private int saveConfig(JSONObject configJson,int seriesNum){
        int configNum = 0;
        if (configJson != null){
            if (configJson.getString("returncode").equals("0")){
                JSONObject result = configJson.getJSONObject("result");
                JSONObject paramtypeitems = result.getJSONObject("paramtypeitems");
                Map<String, Object> innerMap = paramtypeitems.getInnerMap();
                //System.out.println("--------实验："+innerMap.toString());
                for (Object value : innerMap.values()) {
                    JSONObject paramitem = JSON.parseObject(value.toString());
                    String name = paramitem.getString("name");
                    //System.out.println("上层名称："+name+"：");

                    JSONObject paramitems = paramitem.getJSONObject("paramitems");
                    Map<String, Object> innerMap1 = paramitems.getInnerMap();
                    for (Object o : innerMap1.values()) {
                        JSONObject jsonObject1 = JSON.parseObject(o.toString());
                        Integer keyId = jsonObject1.getInteger("id");
                        String keyName = jsonObject1.getString("name");
                        String pnid = jsonObject1.getString("pnid");
                        JSONObject valueitems = jsonObject1.getJSONObject("valueitems");
                        //System.out.println("---------名称："+keyName+"：");
                        Map<String, Object> valueitemsInnerMap = valueitems.getInnerMap();
                        for (Object valueParam : valueitemsInnerMap.values()) {
                            JSONObject valueParamJson = JSON.parseObject(valueParam.toString());
                            Integer specid = valueParamJson.getInteger("specid");
                            String value1 = valueParamJson.getString("value");
                           /* System.out.println("ID结果："+specid);
                            System.out.println("字符结果："+value1);*/
                            CarHomeBasicParam carHomeBasicParam = new CarHomeBasicParam();
                            carHomeBasicParam.setTopName(name);
                            carHomeBasicParam.setKeyId(keyId);
                            carHomeBasicParam.setName(keyName);
                            carHomeBasicParam.setPnid(pnid);
                            carHomeBasicParam.setSpecid(specid);
                            carHomeBasicParam.setValue(value1);
                            int i = commentService.addVehicleConfigParam(carHomeBasicParam);
                            configNum += i;
                            modelNumTotle +=1;
                            System.out.println("------成功保存第"+seriesNum+"款车系的车型基础参数，第"+configNum+"条数据-----总计"+modelNumTotle+"条数据-------");
                        }
                    }
                }
            }
        }
        return configNum;
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


            /*此处为保存关键词，固定334条，已保存
        JSONObject keyLinkJson = JSON.parseObject(keyLink);
        if (keyLinkJson != null){
            Map<String, Object> keyLinkJsonInnerMap = keyLinkJson.getInnerMap();
            for (Object value : keyLinkJsonInnerMap.values()) {
                JSONObject keyLinkParseObject = JSON.parseObject(value.toString());
                Integer id = keyLinkParseObject.getInteger("id");
                String link = keyLinkParseObject.getString("link");
                String name = keyLinkParseObject.getString("name");

                CarKeysWords carKeysWords = new CarKeysWords();
                carKeysWords.setId(id);
                carKeysWords.setLink(link);
                carKeysWords.setName(name);
                int i = commentService.addCarHomeKeys(carKeysWords);
                total +=i;
                System.out.println("-------------第"+total+"条数据---------------");


            }
        }*/

}