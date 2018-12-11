package com.wxy.bigdata.wx;

import com.wxy.bigdata.common.enums.StatusCode;
import com.wxy.bigdata.common.utils.JsonResult;
import com.wxy.bigdata.wx.utils.SignUtil;
import com.wxy.bigdata.wx.utils.WXFeatureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/5
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */
@RestController
public class WenXinController {

    private static final Logger logger = LoggerFactory.getLogger(WenXinController.class);

    @Autowired
    private WXFeatureUtils wxFeatureUtils;

    @RequestMapping("/wx")
    public String WxLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");
        PrintWriter out = resp.getWriter();
        if (SignUtil.checkSignature(signature, timestamp, nonce, "yn911")) {
            out.print(echostr);
            out.close();
        }
        return null;
    }

    @RequestMapping("/wx/sign")
    public JsonResult signConfig(HttpServletRequest req, HttpServletResponse resp,String url) throws IOException {
        JsonResult jsonResult=new JsonResult();
        if ("".equals(url) || url==null){
            jsonResult.setResult("");
            jsonResult.setStatus(StatusCode.BAD_REQUEST.getKey());
            jsonResult.setMsg(StatusCode.BAD_REQUEST.getValue());
           return jsonResult;
       }
        try {
            Map signMap = wxFeatureUtils.getAutograph(req, url);
            if (signMap==null){
                jsonResult.setResult("");
                jsonResult.setStatus(StatusCode.NOT_FOUND.getKey());
                jsonResult.setMsg(StatusCode.NOT_FOUND.getValue());
                return jsonResult;
            }
            jsonResult.setResult(signMap);
            jsonResult.setStatus(StatusCode.OK.getKey());
            jsonResult.setMsg(StatusCode.OK.getValue());
            return jsonResult;

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }


}