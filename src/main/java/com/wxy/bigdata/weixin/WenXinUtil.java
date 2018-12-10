package com.wxy.bigdata.weixin;

import com.alibaba.fastjson.JSON;

import com.wxy.bigdata.utils.CommonUtils;
import com.wxy.bigdata.weixin.utils.HttpClientUtil;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.context.annotation.Configuration;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/5
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */

public class WenXinUtil {

    private Long lastAccessTime = null;
    private String lastJsapiTicket = null;
    private Long lastAccessTicketTime = null;
    private String lastAccessTicket = null;
    private String lastaccess_token = null;

    /**
     * 获取最终签名以及参数
     *
     * @throws Exception
     */
    public Map<String, String> getAutograph(HttpServletRequest req, String url) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("noncestr", create_nonce_str());
        params.put("jsapi_ticket", getJsTitcket());
        params.put("timestamp", create_timestamp());
        params.put("url", url);
        String signature = getSign(params);
        params.put("signature", signature);
        System.out.println(signature);
        return params;
    }


    /**
     * 获取js-sdk需要用到的ticket
     */
    public String getJsTitcket() {
        String titcket = "";

        if (lastAccessTime == null) {
            lastAccessTime = new Date().getTime();
        } else {
            Long now = new Date().getTime();
            if (now - lastAccessTime > 7200 * 1000) //2小时过期
            {
                lastAccessTime = new Date().getTime();
            } else if (lastJsapiTicket != null) {
                return titcket = lastJsapiTicket;
            }
        }

        String access_token = GetMpAccessToken(GlobalParameters.APPID, GlobalParameters.APPSECRET);
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
        String message = dogetStr(url);
        Map<String, String> weixin = JSON.parseObject(message, Map.class);
        lastJsapiTicket = weixin.get("ticket");
        return weixin.get("ticket");

    }


    public String GetMpAccessToken(String wx_appId, String wx_appSecret) {
        String ret = null;
        try {
            if (lastAccessTime == null) {
                lastAccessTime = new Date().getTime();
            } else {
                Long now = new Date().getTime();
                if (now - lastAccessTime > 7200 * 1000) //2小时过期
                {
                    lastAccessTime = new Date().getTime();
                } else if (lastaccess_token != null) {
                    return lastaccess_token;
                }
            }
            if (("".equals(wx_appId) && "".equals(wx_appSecret)) || (wx_appId == null && wx_appSecret == null)) {
                return null;
            }

            String url = "https://api.weixin.qq.com/cgi-bin/token?appid=" + wx_appId + "&secret=" + wx_appSecret + "&grant_type=client_credential";
            String message = HttpClientUtil.get(url, "UTF-8");

//            log.info("\n" + CommonUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "\t message:" + message);

            Map rJson = JSON.parseObject(message, Map.class);
            if (rJson.get("access_token") != null || rJson.get("access_token") != "") {
                ret = rJson.get("access_token").toString();
                lastaccess_token = ret;
            }

        } catch (Exception ex) {
//            log.error(ex.getMessage());
        }
        return ret;
    }

    public static String dogetStr(String url) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            HttpResponse reponse = httpClient.execute(httpGet);
            HttpEntity entity = reponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }
        return result;
    }

    /**
     * 生成时间戳
     *
     * @return
     */
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String create_nonce_str() {
        return CommonUtils.generateUUID(false);
    }

    /**
     * 得到签名
     */
    public static String getSign(Map<String, String> params) throws Exception {
        Map<String, String> sortMap = new TreeMap<String, String>();
        sortMap.putAll(params);
        //以k1=v1&k2=v2...方式拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> s : sortMap.entrySet()) {
            String k = s.getKey();
            String v = s.getValue();
            if (CommonUtils.isNull(v)) {// 过滤空值
                continue;
            }
            builder.append(k).append("=").append(v).append("&");
        }
        if (!sortMap.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        Map<String, Object> maps = new HashMap<String, Object>();
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

    /**
     * 对得到的数据进行sha1编码
     *
     * @param maps
     * @return
     * @throws DigestException
     */
    public static String SHA1(Map<String, Object> maps) throws DigestException {
        //获取信息摘要 - 参数字典排序后字符串
        String decrypt = getOrderByLexicographic(maps);
        try {
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decrypt.getBytes());
            //获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            throw new DigestException("签名错误！");
        }
    }

    private static String getOrderByLexicographic(Map<String, Object> maps) {
        return splitParams(lexicographicOrder(getParamsName(maps)), maps);
    }

    /**
     * 拼接排序好的参数名称和参数值
     *
     * @param paramNames 排序后的参数名称集合
     * @param maps       参数key-value map集合
     * @return String 拼接后的字符串
     */
    private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
        StringBuilder paramStr = new StringBuilder();
        for (String paramName : paramNames) {
            paramStr.append(paramName);
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                if (paramName.equals(entry.getKey())) {
                    paramStr.append(String.valueOf(entry.getValue()));
                }
            }
        }
        return paramStr.toString();
    }

    /**
     * 获取参数名称 key
     *
     * @param maps 参数key-value map集合
     * @return
     */
    private static List<String> getParamsName(Map<String, Object> maps) {
        List<String> paramNames = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            paramNames.add(entry.getKey());
        }
        return paramNames;
    }

    /**
     * 参数名称按字典排序
     *
     * @param paramNames 参数名称List集合
     * @return 排序后的参数名称List集合
     */
    private static List<String> lexicographicOrder(List<String> paramNames) {
        Collections.sort(paramNames);
        return paramNames;
    }
}