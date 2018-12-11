package com.wxy.bigdata.wx.utils;

import com.alibaba.fastjson.JSONObject;
import com.wxy.bigdata.wx.global.GlobalParam;
import com.wxy.bigdata.wx.token.AccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/11
 * Time: 10:19
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */
public class WXUtils {

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
         * get请求
         * @param url
         * @return
         */
        public static JSONObject doGetStr(String url){
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            JSONObject jsonObject = null;
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                if(entity != null){
                    String result = EntityUtils.toString(entity,"UTF-8");
                    jsonObject = JSONObject.parseObject(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        /**
         * post请求
         * @param url
         * @param outStr
         * @return
         */
        public static JSONObject doPostStr(String url,String outStr){
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = null;
            try {
                httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
                HttpResponse response = httpClient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity(),"UTF-8");
                jsonObject = JSONObject.parseObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        /**
         * 获取access_token
         * @return
         */
        public static AccessToken getAccessToken(){
            AccessToken token = new AccessToken();
            String url = ACCESS_TOKEN_URL.replace("APPID",GlobalParam.APPID).replace("APPSECRET",GlobalParam.APPSECRET);
            JSONObject jsonObject = doGetStr(url);
            if(jsonObject != null){
                token.setToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInteger("expires_in"));
            }
            return token;
        }

        public static int createMenu(String token,String menu) throws ParseException, IOException{
            int result = 0;
            String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
            JSONObject jsonObject = doPostStr(url, menu);
            if(jsonObject != null){
                result = jsonObject.getInteger("errcode");
            }
            return result;
        }


    }
