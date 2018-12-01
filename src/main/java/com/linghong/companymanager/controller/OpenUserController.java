package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.service.OpenUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/1 16:24
 * @Version 1.0
 * @Description:
 */
@RestController
public class OpenUserController {
    @Resource
    private OpenUserService openUserService;

    @GetMapping("/open/qqLogin")
    public void qqLogin(HttpServletResponse response){
        openUserService.qqLogin(response);
    }

    @RequestMapping("/open/qqCallBack")
    public Response qqCallBack(HttpServletRequest request){
        boolean flag = openUserService.qqCallBack(request);
        if (flag){
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(false,101 ,null ,"登录失败" );
    }
}
