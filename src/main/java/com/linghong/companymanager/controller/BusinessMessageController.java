package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.BusinessMessage;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.DiscussMessage;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.BusinessMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/10 19:21
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"行业信息相关接口"})
@RestController
public class BusinessMessageController {
    @Resource
    private BusinessMessageService businessMessageService;

    /**
     * 发布行业信息
     * 参数 ：title message base64Images
     * @param businessMessage
     * @param request
     * @return
     */
    @ApiOperation(value = "发布行业信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "主题",required = true),
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "base64Images",value = "base64格式的图片",required = true),
    })
    @PostMapping("/businessMessage/pushBusinessMessage")
    public Response pushBusinessMessage(BusinessMessage businessMessage,
                                        @RequestParam(required = false) String base64Images,
                                        HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = businessMessageService.pushBusinessMessage(businessMessage,user,company,base64Images);
        if (flag){
            return new Response(true,200 ,null ,"发布成功" );
        }
        return new Response(false,101 ,null ,"发布失败,请重试" );
    }

    /**
     * 根据公司Id获取此公司的行业信息
     * @param companyId
     * @return
     */
    @ApiOperation(value = "根据公司Id获取此公司的行业信息")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/businessMessage/getBusinessMessagesByCompanyId/{companyId}")
    public Response getBusinessMessagesByCompanyId(@PathVariable Long companyId){
        List<BusinessMessage> businessMessages = businessMessageService.getBusinessMessagesByCompanyId(companyId);
        return new Response(true,200 ,businessMessages ,"此公司所有的行业信息" );
    }

    /**
     * 根据行业信息Id获取其详细信息
     * @param businessMessageId
     * @return
     */
    @ApiOperation(value = "根据行业信息Id获取其详细信息")
    @ApiImplicitParam(name = "businessMessageId",value = "行业信息Id",required = true)
    @GetMapping("/businessMessage/getBusinessMessageByBusinessMessageId/{businessMessageId}")
    public Response getBusinessMessageByBusinessMessageId(@PathVariable String businessMessageId){
        BusinessMessage businessMessage = businessMessageService.getBusinessMessageByBusinessMessageId(businessMessageId);
        return new Response(true,200 ,businessMessage ,"获取单个详细信息" );
    }

    /**
     * 对行业信息进行评论
     * 参数：businessMessageId message baseImage discussMessageId(当回复评论信息带上discussMessageId
     *                               公司对员工   公司对公司  员工对公司  员工对员工)
     * @param discussMessage
     * @param request
     * @return
     */
    @ApiOperation(value = "对行业信息进行评论  如果回复评论信息带上discussMessageId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessMessageId",value = "行业信息Id",required = true),
            @ApiImplicitParam(name = "message",value = "评论信息",required = true),
            @ApiImplicitParam(name = "baseImage",value = "base64格式的图片",required = true),
            @ApiImplicitParam(name = "discussMessageId",value = "需要回复的评论信息Id",required = false)
    })
    @PostMapping("/businessMessage/pushDiscussMessage")
    public Response pushDiscussMessage(DiscussMessage discussMessage,
                                       String businessMessageId,
                                       @RequestParam(required = false) String baseImage,
                                       HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = businessMessageService.pushDiscussMessage(discussMessage,businessMessageId,baseImage,user,company);
        if (flag) {
            return new Response(true,200 ,null ,"评论/回复成功" );
        }
        return new Response(false, 101, null,"请重试" );
    }

    /**
     * 获取所有公司的行业信息
     * @return
     */
    @ApiOperation(value = "获取所有公司的行业信息")
    @GetMapping("/businessMessage/getAllBusinessMessage")
    public Response getAllBusinessMessage(){
        List<BusinessMessage> businessMessages = businessMessageService.getAllBusinessMessage();
        return new Response(true, 200, businessMessages,"数据" );
    }
}
