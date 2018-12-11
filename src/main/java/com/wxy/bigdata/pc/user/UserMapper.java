package com.wxy.bigdata.pc.user;

import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/11
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */
@Mapper
public interface UserMapper {
    User selectByPrimaryKey(Integer id);
}