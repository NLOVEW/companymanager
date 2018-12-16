package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.CompanyDynamicMessage;
import com.linghong.companymanager.pojo.DiscussMessage;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.CompanyDynamicMessageService;
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
 * @Date: 2018/11/12 16:55
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"公司动态信息相关接口"})
@RestController
public class CompanyDynamicMessageController {
    @Resource
    private CompanyDynamicMessageService companyDynamicMessageService;

    /**
     * 仅公司  发表公司动态信息
     * 参数：title  message base64Image
     * @param companyDynamicMessage
     * @param base64Image
     * @param request
     * @return
     */
    @ApiOperation(value = "仅公司  发表公司动态信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "主题",required = true),
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "base64Image",value = "base64格式的图片",required = true),
    })
    @PostMapping("/companyDynamicMessage/pushCompanyDynamicMessage")
    public Response pushCompanyDynamicMessage(CompanyDynamicMessage companyDynamicMessage,
                                              @RequestParam(required = false) String base64Image,
                                              HttpServletRequest request){
        Company company = (Company) request.getSession().getAttribute("company");
        boolean flag = companyDynamicMessageService.pushCompanyDynamicMessage(companyDynamicMessage,base64Image,company);
        if (flag){
            return new Response(true,200 ,null ,"发表成功" );
        }
        return new Response(false,101 ,null ,"发表失败" );
    }

    /**
     * 根据公司ID获取其公司动态信息
     * @param companyId
     * @return
     */
    @ApiOperation(value = "根据公司ID获取其公司动态信息")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/companyDynamicMessage/getCompanyDynamicMessageByCompanyId/{companyId}")
    public Response getCompanyDynamicMessageByCompanyId(@PathVariable Long companyId){
        List<CompanyDynamicMessage> companyDynamicMessages = companyDynamicMessageService.getCompanyDynamicMessageByCompanyId(companyId);
        return new Response(true,200 ,companyDynamicMessages ,"动态信息" );
    }

    /**
     * 根据dynamicMessageId 获取详细信息
     * @param dynamicMessageId
     * @return
     */
    @ApiOperation(value = "根据dynamicMessageId 获取详细信息")
    @ApiImplicitParam(name = "dynamicMessageId",value = "动态信息Id",required = true)
    @GetMapping("/companyDynamicMessage/getCompanyDynamicMessageByDynamicMessageId/{dynamicMessageId}")
    public Response getCompanyDynamicMessageByDynamicMessageId(@PathVariable String dynamicMessageId){
        CompanyDynamicMessage companyDynamicMessage = companyDynamicMessageService.getCompanyDynamicMessageByDynamicMessageId(dynamicMessageId);
        return new Response(true,200 ,companyDynamicMessage ,"详细信息" );
    }

    /**
     * 对公司动态信息进行评论或回复
     * 参数：message dynamicMessageId baseImage  discussMessageId(如果回复评论请带上次参数)
     * @param discussMessage
     * @param dynamicMessageId
     * @param baseImage
     * @param request
     * @return
     */
    @ApiOperation(value = "对公司动态信息进行评论或回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "dynamicMessageId",value = "动态信息Id",required = true),
            @ApiImplicitParam(name = "base64Image",value = "base64格式的图片",required = true),
            @ApiImplicitParam(name = "discussMessageId",value = "如果回复评论请带上需要评价的Id",required = true),
    })
    @PostMapping("/companyDynamicMessage/pushDiscussMessage")
    public Response pushDiscussMessage(DiscussMessage discussMessage,
                                       String dynamicMessageId,
                                       @RequestParam(required = false) String base64Image,
                                       HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = companyDynamicMessageService.pushDiscussMessage(discussMessage,dynamicMessageId,base64Image,user,company);
        if (flag){
            return new Response(true,200 ,null ,"评论/回复成功" );
        }
        return new Response(false,101 ,null ,"发表失败" );
    }

    /**
     * 删除动态信息
     * @param dynamicMessageId
     * @return
     */
    @ApiOperation(value = "删除动态信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dynamicMessageId",value = "动态信息Id",required = true)
    })
    @DeleteMapping("/companyDynamicMessage/deleteCompanyDynamicMessage/{dynamicMessageId}")
    public Response deleteCompanyDynamicMessage(@PathVariable String dynamicMessageId){
        boolean flag = companyDynamicMessageService.deleteCompanyDynamicMessage(dynamicMessageId);
        if (flag){
            return new Response(true,200 , null,"删除成功" );
        }
        return new Response(false,101 , null,"删除失败" );
    }

}
