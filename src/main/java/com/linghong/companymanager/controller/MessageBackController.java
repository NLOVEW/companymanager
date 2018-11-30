package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.MessageBack;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.MessageBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/13 10:26
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"信息反馈相关"})
@RestController
public class MessageBackController {
    @Resource
    private MessageBackService messageBackService;

    /**
     * 提交意见反馈
     *
     * messageType  message  base64
     * @param messageBack
     * @param base64
     * @param request
     * @return
     */
    @ApiOperation(value = "提交意见反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageType",value = "反馈信息的类型",required = true),
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "base64",value = "base64格式的图片",required = true),
    })
    @PostMapping("/messageBack/pushMessageBack")
    public Response pushMessageBack(MessageBack messageBack,
                                    @RequestParam(required = false) String base64,
                                    HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = messageBackService.pushMessageBack(messageBack,base64,user,company);
        if (flag){
            return new Response(true,200 ,null ,"提交成功" );
        }
        return new Response(false,101 ,null ,"提交失败" );
    }
}
