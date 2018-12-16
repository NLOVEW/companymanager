package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.service.PersonTalkMsgService;
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
 * @Date: 2018/11/11 10:54
 * @Version 1.0
 * @Description: 人脉信息
 */
@Api(tags = {"人脉信息相关"})
@RestController
public class PersonTalkMsgController {
    @Resource
    private PersonTalkMsgService personTalkMsgService;

    /**
     * 发布人脉信息
     * 参数：title message price base64Image
     *
     * @param personTalkMsg
     * @param request
     * @return
     */
    @ApiOperation(value = "发布人脉信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "主题",required = true),
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "base64Images",value = "base64格式的图片",required = true),
            @ApiImplicitParam(name = "price",value = "文档说有价格 ",required = true),
    })
    @PostMapping("/personTalkMsg/pushPersonTalkMsg")
    public Response pushPersonTalkMsg(PersonTalkMsg personTalkMsg,
                                      @RequestParam(required = false) String base64Images,
                                      HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = personTalkMsgService.pushPersonTalkMsg(personTalkMsg, base64Images, user, company);
        if (flag) {
            return new Response(true, 200, null, "成功发布");
        }
        return new Response(false, 101, null, "身份暂未通过审核");
    }

    /**
     * 获取所有的人脉互动
     * @return
     */
    @ApiOperation(value = "获取所有的人脉互动")
    @GetMapping("/personTalkMsg/getAllPersonTalkMsg")
    public Response getAllPersonTalkMsg(){
        List<PersonTalkMsg> personTalkMsgs = personTalkMsgService.getAllPersonTalkMsg();
        return new Response(true,200 ,personTalkMsgs ,"所有的人脉互动" );
    }

    /**
     * 获取本公司员工发布的所有人脉信息
     * 参数：companyId
     *
     * @param companyId
     * @return
     */
    @ApiOperation(value = "获取本公司员工发布的所有人脉信息")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/personTalkMsg/getPersonTalkMsgByCompanyId/{companyId}")
    public Response getPersonTalkMsgByCompanyId(@PathVariable Long companyId) {
        List<PersonTalkMsg> personTalkMsgs = personTalkMsgService.getPersonTalkMsgByCompanyId(companyId);
        return new Response(true, 200, personTalkMsgs, "查询结果");
    }

    /**
     * 获取某个员工发布的人脉信息
     * 参数：userId
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取某个员工发布的人脉信息")
    @ApiImplicitParam(name = "userId",value = "员工Id",required = true)
    @GetMapping("/personTalkMsg/getPersonTalkMsgByUserId/{userId}")
    public Response getPersonTalkMsgByUserId(@PathVariable Long userId) {
        List<PersonTalkMsg> personTalkMsgs = personTalkMsgService.getPersonTalkMsgByUserId(userId);
        return new Response(true, 200, personTalkMsgs, "检索结果");
    }

    /**
     * 获取人脉活动的详细信息
     * 参数：personTalkMsgId
     *
     * @param personTalkMsgId
     * @return
     */
    @ApiOperation(value = "获取人脉活动的详细信息")
    @ApiImplicitParam(name = "personTalkMsgId",value = "人脉信息Id",required = true)
    @GetMapping("/personTalkMsg/getCompletePersonTalkMsgById/{personTalkMsgId}")
    public Response getCompletePersonTalkMsgById(@PathVariable String personTalkMsgId) {
        PersonTalkMsg personTalkMsg = personTalkMsgService.getCompletePersonTalkMsgById(personTalkMsgId);
        return new Response(true, 200, personTalkMsg, "信息信息");
    }

    /**
     * 对人脉活动 发表评论
     * 参数：message personTalkMsgId baseImage  discussMessageId(如果回复评论请带上次参数)
     *
     * @param discussMessage
     * @param personTalkMsgId
     * @param baseImage
     * @param request
     * @return
     */
    @ApiOperation(value = "对人脉活动 发表评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personTalkMsgId",value = "人脉信息Id",required = true),
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "base64Image",value = "base64格式的图片",required = true),
            @ApiImplicitParam(name = "discussMessageId",value = "如果回复评论请带上被评论的Id ",required = true),
    })
    @PostMapping("/personTalkMsg/pushDiscussMessage")
    public Response pushDiscussMessage(DiscussMessage discussMessage,
                                       String personTalkMsgId,
                                       @RequestParam(required = false) String baseImage,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = personTalkMsgService.pushDiscussMessage(discussMessage, personTalkMsgId, baseImage, user, company);
        if (flag) {
            return new Response(true, 200, null, "评论/回复成功");
        }
        return new Response(false,101 ,null ,"请重试" );
    }

    /**
     * 报名参与某个人脉活动
     * @param personTalkMsgId
     * @param request
     * @return
     */
    @ApiOperation(value = "报名参与某个人脉活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personTalkMsgId",value = "人脉信息Id",required = true)
    })
    @PostMapping("/personTalkMsg/signUp")
    public Response signUp(String personTalkMsgId,
                           HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = personTalkMsgService.signUp(personTalkMsgId,user,company);
        if (flag){
            return new Response(true, 200, null,"报名成功，请等待确认" );
        }
        return new Response(false,101 ,null ,"您的身份暂未通过审核或余额不足" );
    }

    /**
     * 查询当前用户发表的人脉活动的报名申请
     * @param request
     * @return
     */
    @ApiOperation(value = "查询当前用户发表的人脉活动的报名申请")
    @GetMapping("/personTalkMsg/getSignUpOrder")
    public Response getSignUpOrder(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        List<PersonTalkMsgOrder> personTalkMsgOrders = personTalkMsgService.getSignUpOrder(user,company);
        return new Response(true,200 ,personTalkMsgOrders ,"查询结果" );
    }

    /**
     * 拒绝或者同意报名申请
     * 参数 ：personTalkMsgOrderId status 1同意  2拒绝
     * @param personTalkMsgOrderId
     * @param status
     * @return
     */
    @ApiOperation(value = "拒绝或者同意报名申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personTalkMsgOrderId",value = "人脉订单Id",required = true),
            @ApiImplicitParam(name = "status",value = "1同意  2拒绝",required = true)
    })
    @PostMapping("/personTalkMsg/dealSignUpOrder")
    public Response dealSignUpOrder(String personTalkMsgOrderId,
                                    Integer status){
        boolean flag = personTalkMsgService.dealSignUpOrder(personTalkMsgOrderId,status);
        if (flag){
            return new Response(true,200 ,null ,"操作成功" );
        }
        return new Response(false,101 ,null ,"操作失败" );
    }

    /**
     * 查看申请方 申请订单
     * @param request
     * @return
     */
    @ApiOperation(value = "查看申请方 申请订单")
    @GetMapping("/personTalkMsg/getApplyOrder")
    public Response getApplyOrder(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        List<PersonTalkMsgOrder> personTalkMsgOrders = personTalkMsgService.getApplyOrder(user,company);
        return new Response(true,200 ,personTalkMsgOrders ,"查询结果" );
    }

    /**
     * 删除
     * @param personTalkMsgId
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/personTalkMsg/deletePersonTalkMsgById/{personTalkMsgId}")
    public Response deletePersonTalkMsgById(@PathVariable String personTalkMsgId){
        boolean flag = personTalkMsgService.deletePersonTalkMsgById(personTalkMsgId);
        if (flag){
            return new Response(true,200 , null,"删除成功" );
        }
        return new Response(false,101 , null,"删除失败" );
    }
}
