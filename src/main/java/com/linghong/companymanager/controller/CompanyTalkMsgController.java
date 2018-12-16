package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.service.CompanyTalkMsgService;
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
 * @Date: 2018/11/10 21:39
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"公司互动信息"})
@RestController
public class CompanyTalkMsgController {
    @Resource
    private CompanyTalkMsgService companyTalkMsgService;

    /**
     * 发布公司互动信息
     * 参数：title message base64Image
     *
     * @param companyTalkMsg
     * @param request
     * @return
     */
    @ApiOperation(value = "发布公司互动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "主题",required = true),
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "base64Images",value = "base64格式的图片",required = true),
    })
    @PostMapping("/companyTalkMsg/pushCompanyTalkMsg")
    public Response pushCompanyTalkMsg(CompanyTalkMsg companyTalkMsg,
                                       @RequestParam(required = false) String base64Images,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = companyTalkMsgService.pushPersonTalkMsg(companyTalkMsg, base64Images, user, company);
        if (flag) {
            return new Response(true, 200, null, "成功发布");
        }
        return new Response(false, 101, null, "身份暂未通过审核");
    }

    /**
     * 获取本公司发布的所有企业互动信息
     * 参数：companyId
     *
     * @param companyId
     * @return
     */
    @ApiOperation(value = "获取本公司发布的所有企业互动信息")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/companyTalkMsg/getCompanyTalkMsgByCompanyId/{companyId}")
    public Response getCompanyTalkMsgByCompanyId(@PathVariable Long companyId) {
        List<CompanyTalkMsg> companyTalkMsgs = companyTalkMsgService.getCompanyTalkMsgByCompanyId(companyId);
        return new Response(true, 200, companyTalkMsgs, "查询结果");
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
    @GetMapping("/companyTalkMsg/getCompanyTalkMsgByUserId/{userId}")
    public Response getCompanyTalkMsgByUserId(@PathVariable Long userId) {
        List<CompanyTalkMsg> companyTalkMsgs = companyTalkMsgService.getCompanyTalkMsgByUserId(userId);
        return new Response(true, 200, companyTalkMsgs, "检索结果");
    }


    /**
     * 获取公司互动的详细信息
     * 参数：companyTalkMsgId
     *
     * @param companyTalkMsgId
     * @return
     */
    @ApiOperation(value = "获取公司互动的详细信息")
    @ApiImplicitParam(name = "companyTalkMsgId",value = "公司互动信息Id",required = true)
    @GetMapping("/companyTalkMsg/getCompleteCompanyTalkMsgById/{companyTalkMsgId}")
    public Response getCompleteCompanyTalkMsgById(@PathVariable String companyTalkMsgId) {
        CompanyTalkMsg companyTalkMsg = companyTalkMsgService.getCompleteCompanyTalkMsgById(companyTalkMsgId);
        return new Response(true, 200, companyTalkMsg, "信息信息");
    }

    /**
     * 对公司互动 发表评论
     * 参数：message companyTalkMsgId baseImage  discussMessageId(如果回复评论请带上次参数)
     *
     * @param discussMessage
     * @param companyTalkMsgId
     * @param baseImage
     * @param request
     * @return
     */
    @ApiOperation(value = "对公司互动 发表评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "base64Image",value = "base64格式的图片",required = true),
            @ApiImplicitParam(name = "companyTalkMsgId",value = "公司互动信息Id",required = true),
            @ApiImplicitParam(name = "discussMessageId",value = "如果回复评论请带上需要评论的Id",required = true)
    })
    @PostMapping("/companyTalkMsg/pushDiscussMessage")
    public Response pushDiscussMessage(DiscussMessage discussMessage,
                                       String companyTalkMsgId,
                                       @RequestParam(required = false) String baseImage,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = companyTalkMsgService.pushDiscussMessage(discussMessage, companyTalkMsgId, baseImage, user, company);
        if (flag) {
            return new Response(true, 200, null, "评论/回复成功");
        }
        return new Response(false,101 ,null ,"请重试" );
    }

    /**
     * 获取所有公司的所有公司互动信息
     * @return
     */
    @ApiOperation(value = "获取所有公司的所有公司互动信息")
    @GetMapping("/companyTalkMsg/getAllCompanyTalkMsg")
    public Response getAllCompanyTalkMsg(){
        List<CompanyTalkMsg> companyTalkMsgs = companyTalkMsgService.getAllCompanyTalkMsg();
        return new Response(true,200,companyTalkMsgs,"所有公司的公司互动");
    }

    /**
     * 报名参与某个公司互动活动
     * @param companyTalkMsgId
     * @param request
     * @return
     */
    @ApiOperation(value = "报名参与某个公司互动活动")
    @ApiImplicitParam(name = "companyTalkMsgId",value = "公司互动信息Id",required = true)
    @PostMapping("/companyTalkMsg/signUp")
    public Response signUp(String companyTalkMsgId,
                           HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = companyTalkMsgService.signUp(companyTalkMsgId,user,company);
        if (flag){
            return new Response(true, 200, null,"报名成功，请等待确认" );
        }
        return new Response(false,101 ,null ,"您的身份暂未通过审核或余额不足" );
    }

    /**
     * 查询当前用户发布的公司活动的报名申请
     * @param request
     * @return
     */
    @ApiOperation(value = "查询当前用户发布的公司活动的报名申请")
    @GetMapping("/companyTalkMsg/getSignUpOrder")
    public Response getSignUpOrder(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        List<CompanyTalkMsgOrder> companyTalkMsgOrders = companyTalkMsgService.getSignUpOrder(user,company);
        return new Response(true,200 ,companyTalkMsgOrders ,"查询结果" );
    }

    /**
     * 拒绝或者同意报名申请
     * 参数 ：companyTalkMsgId status 1同意  2拒绝
     * @param companyTalkMsgId
     * @param status
     * @return
     */
    @ApiOperation(value = "拒绝或者同意报名申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyTalkMsgId",value = "公司互动信息Id",required = true),
            @ApiImplicitParam(name = "status",value = "1同意  2拒绝",required = true),
    })
    @PostMapping("/companyTalkMsg/dealSignUpOrder")
    public Response dealSignUpOrder(String companyTalkMsgId,
                                    Integer status){
        boolean flag = companyTalkMsgService.dealSignUpOrder(companyTalkMsgId,status);
        if (flag){
            return new Response(true,200 ,null ,"操作成功" );
        }
        return new Response(false,101 ,null ,"操作失败" );
    }

    /**
     * 查看申请方 申请订单结果
     * @param request
     * @return
     */
    @ApiOperation(value = "查看申请方 申请订单结果")
    @GetMapping("/companyTalkMsg/getApplyOrder")
    public Response getApplyOrder(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        List<CompanyTalkMsgOrder> companyTalkMsgOrders = companyTalkMsgService.getApplyOrder(user,company);
        return new Response(true,200 ,companyTalkMsgOrders ,"查询结果" );
    }

    /**
     * 逻辑删除
     * @param companyTalkMsgId
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/companyTalkMsg/deleteCompanyTalkMsg/{companyTalkMsgId}")
    public Response deleteCompanyTalkMsg(@PathVariable String companyTalkMsgId){
        boolean flag = companyTalkMsgService.deleteCompanyTalkMsg(companyTalkMsgId);
        if (flag){
            return new Response(true,200 , null,"删除成功" );
        }
        return new Response(false,101 , null,"删除失败" );
    }

}
