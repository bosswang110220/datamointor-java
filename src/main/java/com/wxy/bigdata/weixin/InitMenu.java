package com.wxy.bigdata.weixin;


import com.wxy.bigdata.weixin.menu.Button;
import com.wxy.bigdata.weixin.menu.Menu;
import com.wxy.bigdata.weixin.menu.ViewButton;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/5
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */
public class InitMenu {

    /**
     * 组装菜单
     * @return
     */
    public static Menu initMeun(){
        Menu menu= new Menu();

        ViewButton button21 =new ViewButton();
        button21.setName("技术");
        button21.setType("view");
        button21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GlobalParameters.APPID + "&redirect_uri=http%3A%2F%2F"+GlobalParameters.siteDomain+"%2F"+GlobalParameters.systemName+"%2Fcommon%3Fservice%3Dfront.mobile_wx.energyConsume.energyConsume%26type%3Dpage%26data%3D%7BmemberId%3D"+123+"%7D&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect");

        ViewButton button22=new ViewButton();
        button22.setName("课程");
        button22.setType("view");
        button22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GlobalParameters.APPID + "&redirect_uri=http%3A%2F%2F"+GlobalParameters.siteDomain+"%2F"+GlobalParameters.systemName+"%2Fcommon%3Fservice%3Dfront.mobile_wx.zIndex.zIndex%26type%3Dpage%26data%3D%7BmemberId%3D"+123+"%7D&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect");

        ViewButton button23=new ViewButton();
        button23.setName("生活");
        button23.setType("view");
        button23.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GlobalParameters.APPID + "&redirect_uri=http%3A%2F%2F"+GlobalParameters.siteDomain+"%2F"+GlobalParameters.systemName+"%2Fcommon%3Fservice%3Dfront.mobile_wx.dataCollection.scancode.scanManage%26type%3Dpage%26data%3D%7BmemberId%3D"+123+"%7D&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect");

        ViewButton button31=new ViewButton();
        button31.setName("web前端");
        button31.setType("view");
        button31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GlobalParameters.APPID + "&redirect_uri=http%3A%2F%2F"+GlobalParameters.siteDomain+"%2F"+GlobalParameters.systemName+"%2Fcommon%3Fservice%3Dfront.mobile_wx.notice.notice%26type%3Dpage%26data%3D%7BmemberId%3D"+123+"%7D&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect");


        ViewButton button32=new ViewButton();
        button32.setName("web后端");
        button32.setType("view");
        button32.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GlobalParameters.APPID + "&redirect_uri=http%3A%2F%2F"+GlobalParameters.siteDomain+"%2F"+GlobalParameters.systemName+"%2Fcommon%3Fservice%3Dfront.mobile_wx.handbook.handbook%26type%3Dpage%26data%3D%7BmemberId%3D"+123+"%7D&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect");


        ViewButton button33=new ViewButton();
        button33.setName("技术前沿");
        button33.setType("view");
        button33.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GlobalParameters.APPID + "&redirect_uri=http%3A%2F%2F"+GlobalParameters.siteDomain+"%2F"+GlobalParameters.systemName+"%2Fcommon%3Fservice%3Dfront.mobile_wx.contact.contact%26type%3Dpage%26data%3D%7BmemberId%3D"+123+"%7D&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect");

        Button dataButton =new Button();
        dataButton.setName("小一推荐");
        dataButton.setSub_button(new Button[]{button21,button22,button23});

        Button customerButton =new Button();
        customerButton.setName("技术栈");
        customerButton.setSub_button(new Button[]{button31,button32,button33});

        ViewButton company=new ViewButton();
        company.setName("小一说");
        company.setType("view");
        company.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + GlobalParameters.APPID + "&redirect_uri=http%3A%2F%2F"+GlobalParameters.siteDomain+"%2F"+GlobalParameters.systemName+"%2Fcommon%3Fservice%3Dfront.mobile_wx.myUnit.myUnit%26type%3Dpage%26data%3D%7BmemberId%3D"+123+"%7D&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect");


        menu.setButton(new Button[]{dataButton,customerButton,company});
        return menu;


    }

    /**
     * 调试main方法
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        initMeun();
    }


}