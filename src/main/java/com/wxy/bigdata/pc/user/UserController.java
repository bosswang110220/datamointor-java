package com.wxy.bigdata.pc.user;

import com.wxy.bigdata.common.enums.StatusCode;
import com.wxy.bigdata.common.utils.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: WangYN
 * Date: 2018/12/11
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 * Version: V1.0
 */

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
        jsonResult.setMsg(StatusCode.OK.getValue());
        jsonResult.setStatus(StatusCode.OK.getKey());
        return jsonResult;
    }


}