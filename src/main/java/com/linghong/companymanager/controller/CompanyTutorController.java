package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.CompanyTutor;
import com.linghong.companymanager.pojo.DiscussMessage;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.CompanyTutorService;
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
import java.util.List;
import java.util.Map;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 13:30
 * @Version 1.0
 * @Description: 公司帮手  公司辅导
 */
@Api(tags = {"公司帮手 公司辅导相关接口"})
@RestController
public class CompanyTutorController {
    @Resource
    private CompanyTutorService companyTutorService;

    /**
     * 公司发布 公司辅导  公司助手信息
     * @param companyTutor
     * @param request
     * @return
     */
    @ApiOperation(value = "公司发布 公司辅导  公司助手信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "主题",required = true),
            @ApiImplicitParam(name = "message",value = "主要信息",required = true),
            @ApiImplicitParam(name = "type",value = "类型分类",required = true),
            @ApiImplicitParam(name = "price",value = "价格 需求说会有价格（随意吧）",required = true),
            @ApiImplicitParam(name = "base64Images",value = "base64格式的图片",required = true),
    })
    @PostMapping("/companyTutor/pushCompanyTutor")
    public Response pushCompanyTutor(CompanyTutor companyTutor ,
                                     HttpServletRequest request){
        Company company = (Company) request.getSession().getAttribute("company");
        boolean flag = companyTutorService.pushCompanyTutor(companyTutor,company);
        if (flag){
            return new Response(true,200 ,null ,"发布成功");
        }
        return new Response(false,101 ,null ,"请重新发布" );
    }

    /**
     * 根据公司类型 查找公司帮手信息
     * @param type
     * @return
     */
    @ApiOperation(value = "根据公司类型 查找公司帮手信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "city",value = "城市",required = true),
            @ApiImplicitParam(name = "type",value = "类型分类",required = true),
    })
    @PostMapping("/companyTutor/getCompanyTutorByCompanyType")
    public Response getCompanyTutorByCompanyType(@RequestParam(required = false) String city,
                                                 String type){
        List<CompanyTutor> companyTutors = companyTutorService.getCompanyTutorByCompanyType(city,type);
        return new Response(true,200 ,companyTutors ,"检索结果" );
    }


    /**
     * 根据公司类型 查找公司帮手信息
     * @return
     */
    @ApiOperation(value = "查找法律咨询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "city",value = "城市",required = true),
    })
    @PostMapping("/companyTutor/getLaw")
    public Response getLaw(@RequestParam(required = false) String city){
        Map<String,List<User>> users = companyTutorService.getLaw(city);
        return new Response(true,200 ,users ,"检索结果" );
    }


    /**
     * 对某个公司助手信息进行评论或者回复
     * 参数：companyTutorId  message   discussMessageId(如果是回复 则需要带上此参数)
     * @param discussMessage
     * @param companyTutorId
     * @param baseImage
     * @param request
     * @return
     */
    @ApiOperation(value = "对某个公司助手信息进行评论或者回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyTutorId",value = "发布的公司助手Id",required = true),
            @ApiImplicitParam(name = "companyTutorId",value = "发布的公司助手Id",required = true),
            @ApiImplicitParam(name = "discussMessageId",value = "如果是回复评论信息 则需要带上需要评论的Id",required = true),
    })
    @PostMapping("/companyTutor/pushDiscussMessage")
    public Response pushDiscussMessage(DiscussMessage discussMessage,
                                       String companyTutorId,
                                       @RequestParam(required = false) String baseImage,
                                       HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        boolean flag = companyTutorService.pushDiscussMessage(discussMessage,companyTutorId,baseImage,user,company);
        if (flag){
            return new Response(true,200 ,null ,"评论/回复成功" );
        }
        return new Response(false,101 ,null ,"评论/回复失败" );
    }

}
