package com.wxy.bigdata.controller;

import com.wxy.bigdata.entity.User;
import com.wxy.bigdata.enums.RetrunCode;
import com.wxy.bigdata.service.UserService;
import com.wxy.bigdata.utils.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Integer", paramType = "path")
    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public JsonResult getUserById (@PathVariable(value = "id") Integer id) {
        JsonResult jsonResult=new JsonResult();
        User user=userService.selectByPrimaryKey(id);
        jsonResult.setResult(user);
        jsonResult.setMsg(RetrunCode.OK.getValue());
        jsonResult.setStatus(RetrunCode.OK.getKey());
       return jsonResult;
    }


    }
