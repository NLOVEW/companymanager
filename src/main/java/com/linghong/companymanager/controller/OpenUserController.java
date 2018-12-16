package com.linghong.companymanager.controller;


import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.service.OpenUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/3 09:36
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"第三方登录"})
@RestController
public class OpenUserController {
    @Resource
    private OpenUserService openUserService;

    @ApiOperation(value = "qq登录")
    /**
     * qq登录请求路径
     * @param response
     */
    @GetMapping("/open/qqLogin")
    public void qqLogin(HttpServletResponse response){
        openUserService.qqLogin(response);
    }

    /**
     * QQ登录回调路径
     * @param request
     * @return
     */
    @RequestMapping("/open/qqCallBack")
    public Response qqCallBack(HttpServletRequest request){
        boolean flag = openUserService.qqCallBack(request);
        if (flag){
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(false,101 ,null ,"登录失败" );
    }

    /**
     * 微信登录
     * @param response
     */
    @ApiOperation(value = "微信登录")
    @GetMapping("/open/wxLogin")
    public void wxLogin(HttpServletResponse response){
        openUserService.weChatLogin(response);
    }

    /**
     * 微信登录回调地址
     * @param request
     * @return
     */
    @RequestMapping("/open/wxCallBack")
    public Response wxCallBack(HttpServletRequest request){
        boolean flag = openUserService.wxCallBack(request);
        if (flag){
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(false,101 ,null ,"登录失败" );
    }
}
