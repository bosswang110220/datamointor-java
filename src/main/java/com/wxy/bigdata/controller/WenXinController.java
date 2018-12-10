package com.wxy.bigdata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wxy.bigdata.weixin.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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



}