package com.wxy.bigdata.wx.utils;

import com.alibaba.fastjson.JSON;
import com.wxy.bigdata.common.utils.CommonUtils;
import com.wxy.bigdata.pc.hello.controller.HelloController;
import com.wxy.bigdata.wx.token.AccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/11
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */
@Service
public class WXFeatureUtils {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    private  Long lastAccessTime = null;
    private  String lastJsapiTicket = null;

    private  Long lastAccessTicketTime = null;
    private  String lastAccessTicket = null;


    /**
     * 获取最终签名以及参数
     * @throws Exception
     */
    public Map<String,String> getAutograph(HttpServletRequest req, String url) throws Exception{
        Map<String,String> params=new HashMap<String,String>();
        params.put("noncestr", create_nonce_str());
        params.put("jsapi_ticket",this.getJsTitcket());
        params.put("timestamp",create_timestamp());
        params.put("url",url);
        String signature=getSign(params);
        params.put("signature", signature);
        System.out.println(signature);
        return params;
    }

    /**
     * 生成时间戳
     * @return
     */
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 生成随机字符串
     * @return
     */
    public static String create_nonce_str() {return CommonUtils.generateUUID(false);}

    /**
     * 获取js-sdk需要用到的ticket
     */
    public String getJsTitcket(){
        String titcket="";

        if(lastAccessTime == null)
            lastAccessTime = new Date().getTime();
        else
        {
            Long now = new Date().getTime();
            //2小时过期
            if(now - lastAccessTime > 7200 * 1000)
            {
                lastAccessTime = new Date().getTime();
            }
            else if(lastJsapiTicket != null)
            {
                return titcket=lastJsapiTicket;
            }
        }

        AccessToken access_token = WXUtils.getAccessToken();
        String token=access_token.getToken();
        String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token+"&type=jsapi";
        String message=dogetStr(url);
        Map<String, String> weixin = JSON.parseObject(message,Map.class);
        lastJsapiTicket=weixin.get("ticket");
        return weixin.get("ticket");

    }

    /**
     * 给微信端发送get请求
     * @param url
     * @return
     */
    public static String dogetStr(String url){
        DefaultHttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(url);
        String result=null;
        try {
            HttpResponse reponse=httpClient.execute(httpGet);
            HttpEntity entity=reponse.getEntity();
            if(entity!=null){
                result=EntityUtils.toString(entity,"UTF-8");
            }
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     *得到签名
     */
    public static String getSign(Map<String, String> params) throws Exception {
        Map<String, String> sortMap = new TreeMap<String, String>();
        sortMap.putAll(params);
        //以k1=v1&k2=v2...方式拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> s : sortMap.entrySet()) {
            String k = s.getKey();
            String v = s.getValue();
            // 过滤空值
            if (CommonUtils.isNull(v)) {
                continue;
            }
            builder.append(k).append("=").append(v).append("&");
        }
        if (!sortMap.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("string1", builder);
        String shaStr = builder.toString();
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(shaStr.getBytes());
        StringBuffer signature = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            signature.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return signature.toString();
    }

}