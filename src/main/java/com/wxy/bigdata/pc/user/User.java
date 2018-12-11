package com.wxy.bigdata.pc.user;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/11
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */
public class User {

    //用户id
    private  int  id;
    //用户姓名
    private String username;
    //用户密码
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}