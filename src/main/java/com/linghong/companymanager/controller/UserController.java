package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/7 10:11
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"员工相关接口"})
@RestController
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 注册
     * 参数：mobilePhone password
     */
    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobilePhone",value = "手机号",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true),
    })
    @PostMapping("/user/register")
    public Response register(User user, HttpServletRequest request) {
        user = userService.register(user);
        if (user != null) {
            request.getSession().setAttribute("user", user);
            return new Response(true, 200, null, "注册成功");
        }
        return new Response(false, 101, null, "手机号已被注册");
    }

    /**
     * 登录
     * 参数  mobilePhone password
     * @param user
     * @param request
     * @return
     */
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobilePhone",value = "手机号",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true),
    })
    @PostMapping("/user/login")
    public Response login(User user, HttpServletRequest request) {
        user = userService.login(user);
        if (user != null){
            request.getSession().setAttribute("user",user );
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(false,101 ,null ,"账号或密码错误" );
    }

    /**
     * 找回密码
     * @param mobilePhone
     * @param password
     * @return
     */
    @ApiOperation(value = "找回密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobilePhone",value = "手机号",required = true),
            @ApiImplicitParam(name = "password",value = "新密码",required = true),
    })
    @PostMapping("/user/findPassword")
    public Response findPassword(String mobilePhone,String password,HttpServletRequest request){
        User user = userService.findPassword(mobilePhone,password);
        if (user != null){
            request.getSession().setAttribute("user",user );
            return new Response(true,200 ,null ,"已修改密码" );
        }
        return new Response(false,101 ,null ,"修改失败，请联系管理员" );
    }

    /**
     * 完善个人信息
     * @param user
     * @param request
     * @return
     */
    @ApiOperation(value = "完善个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "用户名",required = true),
            @ApiImplicitParam(name = "nickName",value = "昵称",required = true),
            @ApiImplicitParam(name = "email",value = "邮箱",required = true),
            @ApiImplicitParam(name = "sex",value = "性别",required = true),
            @ApiImplicitParam(name = "position",value = "职位",required = true),
            @ApiImplicitParam(name = "department",value = "部门",required = true),
            @ApiImplicitParam(name = "companyId",value = "所属公司ID",required = true)
    })
    @PostMapping("/user/perfectUserMessage")
    public Response perfectUserMessage(User user,
                                       Long companyId,
                                       HttpServletRequest request){
        User sessionUser = (User) request.getSession().getAttribute("user");
        user = userService.perfectUserMessage(user,companyId,sessionUser);
        if (user != null){
            request.getSession().setAttribute("user",user );
            return new Response(true,200 , null, "完善成功");
        }
        return new Response(false,101 ,null ,"请重新完善" );
    }

    /**
     * 上传头像
     * @param base64Image
     * @param request
     * @return
     */
    @ApiOperation(value = "上传头像")
    @ApiImplicitParam(name = "base64Image",value = "base64格式",required = true)
    @PostMapping("/user/uploadAvatar")
    public Response uploadAvatar(String base64Image,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        user = userService.uploadAvatar(base64Image,user);
        request.getSession().setAttribute("user",user );
        return new Response(true,200 ,null ,"上传成功" );
    }

    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/user/getCurrentUserMessage")
    public Response getCurrentUserMessage(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        user = userService.getUserByMobilePhone(user.getMobilePhone());
        return new Response(true,200 ,user , "当前用户信息");
    }

    /**
     * 根据userId获取个人信息
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据userId获取个人信息")
    @ApiImplicitParam(name = "userId",value = "员工Id",required = true)
    @GetMapping("/user/getUserById/{userId}")
    public Response getUserById(@PathVariable Long userId){
        User user = userService.getUserById(userId);
        return new Response(true,200 ,user ,"查询到的个人信息" );
    }

    /**
     * 添加业务目标
     * @param userId
     * @param target
     * @return
     */
    @ApiOperation(value = "完善个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户名Id",required = true),
            @ApiImplicitParam(name = "target",value = "目标",required = true)
    })
    @PostMapping("/user/pushBusinessTarget")
    public Response pushBusinessTarget(Long userId,String target){
        boolean flag = userService.pushBusinessTarget(userId,target);
        if (flag){
            return new Response(true,200 ,null ,"添加成功" );
        }
        return new Response(false,101 ,null ,"添加失败" );
    }

}
