package com.wxy.bigdata.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2018/7/23
 * Time: 17:53
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public enum RetrunCode {

    OK(200, "请求已经成功处理"),
    CREATED(201, "请求已经成功处理，并创建了资源"),
    ACCEPTED(202, "请求已经接受，等待执行"),
    BAD_REQUEST(400,  "请求错误，请修正请求"),
    UNAUTHORIZED(401, "没有被授权或者授权已经失效"),
    PAYMENT_REQUIRED(402, "预留状态"),
    FORBIDDEN(403, "请求被理解，但是拒绝执行"),
    NOT_FOUND(404, "资源未找到"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    NOT_IMPLEMENTED(501, "服务器不支持当前请求的部分功能"),
    BAD_GATEWAY(502,"响应无效"),
    SERVICE_UNAVAILABLE(503,"服务器维护或者过载，拒绝服务"),
    GATEWAY_TIMEOUT(504, "上游服务器超时"),
    HTTP_VERSION_NOT_SUPPORTED(505,  "不支持的HTTP版本"),
    NETWORK_AUTHENTICATION_REQUIRED(511,"需要进行网络授权");

    private int key;
    private String value;
    //自定义的构造函数，参数数量，名字随便自己取
    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    private RetrunCode(int key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    //重新toString方法，默认的toString方法返回的就是枚举变量的名字，和name()方法返回值一样
    @Override
    public String toString()
    {
        return this.key+":"+this.value;
    }

}
