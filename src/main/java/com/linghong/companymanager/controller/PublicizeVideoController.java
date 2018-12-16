package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.PublicizeVideo;
import com.linghong.companymanager.service.PublicizeVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/8 11:12
 * @Version 1.0
 * @Description: 视频相关
 */
@Api(tags = {"视频相关"})
@RestController
public class PublicizeVideoController {
    @Resource
    private PublicizeVideoService publicizeVideoService;

    /**
     * 上传企业宣传视频
     * @param video
     * @param request
     * @return
     */
    @ApiOperation(value = "上传企业宣传视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "video",value = "file 类型",required = true),
            @ApiImplicitParam(name = "title",value = "标题",required = true),
            @ApiImplicitParam(name = "introduce",value = "简介",required = true)
    })
    @PostMapping("/video/pushVideo")
    public Response pushVideoByCompany(MultipartFile video,
                                       PublicizeVideo publicizeVideo,
                                       HttpServletRequest request){
        Company company = (Company) request.getSession().getAttribute("company");
        boolean flag = publicizeVideoService.pushVideoByCompany(company,video,publicizeVideo);
        if (flag){
            return new Response(true,200 ,null ,"上传成功" );
        }
        return new Response(false,101 ,null ,"上传失败" );
    }

    /**
     * 根据companyId查询本公司的宣传片
     * @param companyId
     * @return
     */
    @ApiOperation(value = "根据companyId查询本公司的宣传片")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("video/getCompanyVideoByCompanyId/{companyId}")
    public Response getCompanyVideoByCompanyId(@PathVariable Long companyId){
        List<PublicizeVideo> videos = publicizeVideoService.getCompanyVideoByCompanyId(companyId);
        return new Response(true,200 ,videos ,"公司宣传片" );
    }

    /**
     * 获取所有公司宣传视频
     * @return
     */
    @ApiOperation(value = "获取所有公司宣传视频")
    @GetMapping("/video/getAllCompanyVideo")
    public Response getAllCompanyVideo(){
        List<PublicizeVideo> videos = publicizeVideoService.getAllCompanyVideo();
        return new Response(true,20 ,videos ,"所有公司的宣传视频" );
    }
}
