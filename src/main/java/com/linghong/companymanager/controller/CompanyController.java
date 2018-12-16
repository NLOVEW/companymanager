package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.CompanyService;
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
 * @Date: 2018/11/8 12:14
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"公司相关接口"})
@RestController
public class CompanyController {
    @Resource
    private CompanyService companyService;

    /**
     * 注册公司信息
     * 参数   mobilePhone password
     *
     * @param company
     * @return
     */
    @ApiOperation(value = "注册公司信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobilePhone",value = "手机号",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true)
    })
    @PostMapping("/company/register")
    public Response register(Company company, HttpServletRequest request) {
        company = companyService.register(company);
        if (company != null){
            request.getSession().setAttribute("company", company);
            return new Response(true, 200, null, "公司注册完成");
        }
        return new Response(false,101 , null, "已被注册");
    }

    /**
     * 公司账号登录
     * @return
     */
    @ApiOperation(value = "公司账号登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobilePhone",value = "手机号",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true)
    })
    @PostMapping("/company/login")
    public Response login(Company company, HttpServletRequest request){
        company = companyService.login(company);
        if (company != null){
            request.getSession().setAttribute("company",company);
            return new Response(true,200 ,null ,"登录成功" );
        }
        return new Response(true,200 ,null ,"密码或用户名不正确" );
    }

    /**
     * 完善公司信息
     * 参数： companyName base64Avatar  userName  idCardNumber  base64BusinessLicense  base64Images
     *
     * @param company
     * @param base64BusinessLicense
     * @param request
     * @return
     */
    @ApiOperation(value = "完善公司信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName",value = "公司名称",required = true),
            @ApiImplicitParam(name = "base64Avatar",value = "头像base64格式",required = true),
            @ApiImplicitParam(name = "userName",value = "法人代表",required = true),
            @ApiImplicitParam(name = "idCardNumber",value = "法人代表身份证号",required = true),
            @ApiImplicitParam(name = "base64BusinessLicense",value = "公司营业执照base64格式",required = true),
            @ApiImplicitParam(name = "base64Images",value = "公司图片base64格式  以中文下的。为分隔",required = true)
    })
    @PostMapping("/company/perfectCompanyMessage")
    public Response perfectCompanyMessage(Company company,
                                          @RequestParam(required = false) String base64Avatar,
                                          @RequestParam(required = false) String base64BusinessLicense,
                                          @RequestParam(required = false) String base64Images,
                                          HttpServletRequest request) {
        Company sessionCompany = (Company) request.getSession().getAttribute("company");
        company = companyService.perfectUserMessage(company, sessionCompany, base64Avatar, base64BusinessLicense, base64Images);
        request.getSession().setAttribute("company", company);
        return new Response(true, 200, null, "公司信息成功完善");
    }

    /**
     * 公司上传头像
     * @param base64Avatar
     * @param request
     * @return
     */
    @ApiOperation(value = "公司上传头像")
    @ApiImplicitParam(name = "base64Avatar",value = "base64编码格式头像",required = true)
    @PostMapping("/company/uploadAvatar")
    public Response uploadAvatar(String base64Avatar,
                                 HttpServletRequest request){
        Company sessionCompany = (Company) request.getSession().getAttribute("company");
        boolean flag = companyService.uploadAvatar(base64Avatar,sessionCompany);
        if (flag){
            return new Response(true,200 ,null ,"上传成功" );
        }
        return new Response(false,101 ,null ,"上传失败" );
    }

    /**
     * 公司用户 获取当前公司信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "公司用户 获取当前公司信息")
    @GetMapping("/company/getCurrentCompanyMessage")
    public Response getCurrentCompanyMessage(HttpServletRequest request) {
        Company company = (Company) request.getSession().getAttribute("company");
        return new Response(true, 200, company, "当前公司信息");
    }

    /**
     * 根据公司Id获取公司详细信息
     *
     * @param companyId
     * @return
     */
    @ApiOperation(value = "根据公司Id获取公司详细信息")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/company/getCompanyByCompanyId/{companyId}")
    public Response getCompanyByCompanyId(@PathVariable Long companyId) {
        Company company = companyService.getCompanyByCompanyId(companyId);
        return new Response(true, 200, company, "公司详细信息");
    }

    /**
     * 根据关键字查找公司信息
     *
     * @param key
     * @return
     */
    @ApiOperation(value = "根据关键字查找公司信息、全国查询  根据城市查询  都是此接口  ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "city",value = "根据城市名查询带上此参数",required = true),
            @ApiImplicitParam(name = "key",value = "关键字",required = true)
    })
    @PostMapping("/company/getCompanyByKey")
    public Response getCompanyByKey( @RequestParam(required = false) String city,
                                    String key) {
        List<Company> companies = companyService.getCompanyByKey(city,key);
        return new Response(true, 200, companies, "检索结果");
    }

    /**
     * 新建部门
     * @param department
     * @return
     */
    @ApiOperation(value = "新建部门")
    @ApiImplicitParam(name = "department",value = "新建部门名称",required = true)
    @PostMapping("/company/department")
    public Response pushDepartments(String department,
                                    HttpServletRequest request){
        Company company = (Company) request.getSession().getAttribute("company");
        boolean flag = companyService.pushDepartments(department,company);
        if (flag){
            return new Response(true,200 ,null ,"新建成功" );
        }
        return new Response(false,101 ,null ,"新建失败" );
    }

    /**
     * 根据公司部门名称获取部门下的人员
     * @param department
     * @return
     */
    @ApiOperation(value = "根据公司部门名称获取部门下的人员")
    @ApiImplicitParam(name = "department",value = "部门名称",required = true)
    @PostMapping("/company/getUserByDepartment")
    public Response getUserByDepartment(String department,
                                        HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        List<User> users = companyService.getUserByDepartment(department,user,company);
        return new Response(true,200 ,users ,"部门员工信息" );
    }

    /**
     * 获取公司名下的所有员工
     * @param companyId
     * @return
     */
    @ApiOperation(value = "获取公司名下的所有员工")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/company/getAllUserByCompanyId/{companyId}")
    public Response getAllUserByCompanyId(@PathVariable Long companyId){
        List<User> users = companyService.getAllUserByCompanyId(companyId);
        return new Response(true,200 ,users ,"本公司所有员工" );
    }

    /**
     * 获取本公司需要认证的员工信息
     * @param request
     * @return
     */
    @ApiOperation(value = "获取本公司需要认证的员工信息")
    @GetMapping("/company/getAuthUser")
    public Response getAuthUser(HttpServletRequest request){
        Company company = (Company) request.getSession().getAttribute("company");
        List<User> users = companyService.getAuthUser(company);
        return new Response(true,200 , users, "未通过的认证申请");
    }

    /**
     * 处理员工入驻申请
     *
     * @param userId
     * @param status  0拒绝 1同意
     * @return
     */
    @ApiOperation(value = "处理员工入驻申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "员工Id",required = true),
            @ApiImplicitParam(name = "status",value = "0拒绝 1同意",required = true)
    })
    @PostMapping("/company/dealAuthUser")
    public Response dealAuthUser(Long userId,
                                 Integer status){
        boolean flag = companyService.dealAuthUser(userId,status);
        if (flag){
            return new Response(true,200 ,null ,"操作成功" );
        }
        return new Response(false,101 ,null ,"操作失败" );
    }

    /**
     * 获取所有的公司信息
     * @return
     */
    @GetMapping("/company/getAllCompany")
    public Response getAllCompany(){
        List<Company> companies = companyService.getAllCompany();
        return new Response(true,200,companies,"公司信息");
    }

    /**
     * 找回密码
     * @param mobilePhone
     * @param password
     * @return
     */
    @ApiOperation(value = "找回密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobilePhone",value = "手机号",required = true),
            @ApiImplicitParam(name = "password",value = "新密码",required = true),
    })
    @PostMapping("/company/findPassword")
    public Response findPassword(String mobilePhone,String password,HttpServletRequest request){
        Company company = companyService.findPassword(mobilePhone,password);
        if (company != null){
            request.getSession().setAttribute("company",company );
            return new Response(true,200 ,null ,"已修改密码" );
        }
        return new Response(false,101 ,null ,"修改失败，请联系管理员" );
    }

}
