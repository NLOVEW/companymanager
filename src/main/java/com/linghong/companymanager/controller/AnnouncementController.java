package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Announcement;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.service.AnnouncementService;
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
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 11:23
 * @Version 1.0
 * @Description:
 */
@Api(value = "公司公告相关接口",tags = {"公司公告相关接口"})
@RestController
public class AnnouncementController {
    @Resource
    private AnnouncementService announcementService;

    /**
     * 公司发布公告 仅公司使用此接口
     * 参数：title message
     * @param announcement
     * @param request
     * @return
     */
    @ApiOperation(value = "公司发布公告 仅公司使用此接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title",value = "主题",required = true),
            @ApiImplicitParam(name = "message",value = "公告信息",required = true)
    })
    @PostMapping("/announcement/pushAnnouncement")
    public Response pushAnnouncement(Announcement announcement, HttpServletRequest request){
        Company company = (Company) request.getSession().getAttribute("company");
        boolean flag = announcementService.pushAnnouncement(announcement,company);
        if (flag){
            return new Response(true,200 ,null ,"发布成功" );
        }
        return new Response(false,101 ,null ,"请重新发布" );
    }

    /**
     * 根据公司Id获取本公司的公告信息
     * @param companyId
     * @return
     */
    @ApiOperation(value = "根据公司Id获取本公司的公告信息")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/announcement/getAnnouncementByCompanyId/{companyId}")
    public Response getAnnouncementByCompanyId(@PathVariable Long companyId){
        List<Announcement> announcements = announcementService.getAnnouncementByCompanyId(companyId);
        return new Response(true,200 ,announcements ,"本公司所有公告" );
    }

    /**
     * 根据announcementId 获取公告的详细信息
     * @param announcementId
     * @return
     */
    @ApiOperation(value = "根据announcementId 获取公告的详细信息")
    @ApiImplicitParam(name = "announcementId",value = "公告Id",required = true)
    @GetMapping("/announcement/getAnnouncementByAnnouncementId/{announcementId}")
    public Response getAnnouncementByAnnouncementId(@PathVariable String announcementId){
        Announcement announcement = announcementService.getAnnouncementByAnnouncementId(announcementId);
        return new Response(true,200, announcement, "公告详细信息");
    }
}
