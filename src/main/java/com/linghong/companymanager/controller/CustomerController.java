package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Customer;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 16:18
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"客户相关接口"})
@RestController
public class CustomerController {
    @Resource
    private CustomerService customerService;

    /**
     * 只允许公司员工进行操作  进行添加客户项目
     * 参数：userName projectName  projectManager businessMessage status
     * @param customer
     * @param base64Image
     * @param request
     * @return
     */
    @ApiOperation(value = "只允许公司员工进行操作  进行添加客户项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "客户名",required = true),
            @ApiImplicitParam(name = "projectName",value = "项目名",required = true),
            @ApiImplicitParam(name = "projectManager",value = "项目管理人",required = true),
            @ApiImplicitParam(name = "businessMessage",value = "商业信息",required = true),
            @ApiImplicitParam(name = "status",value = "状态",required = true),
    })
    @PostMapping("/customer/pushCustomer")
    public Response pushCustomer(Customer customer,
                                 @RequestParam(required = false) String base64Image,
                                 HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        boolean flag = customerService.pushCustomer(user,customer,base64Image);
        if (flag){
            return new Response(true,200 ,null ,"添加成功" );
        }
        return new Response(false,101 ,null ,"添加失败" );
    }

    /**
     * 根据公司Id获取本公司所有的项目情况
     * @param companyId
     * @return
     */
    @ApiOperation(value = "根据公司Id获取本公司所有的项目情况")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/customer/getCustomerByCompanyId/{companyId}")
    public Response getCustomerByCompanyId(@PathVariable Long companyId){
        List<Customer> customers = customerService.getCustomerByCompanyId(companyId);
        return new Response(true,200 ,customers ,"本公司所有的项目信息" );
    }

    /**
     * 根据客户Id获取项目详细信息
     * @param customerId
     * @return
     */
    @ApiOperation(value = "根据客户Id获取项目详细信息")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/customer/getCustomerByCustomerId/{customerId}")
    public Response getCustomerByCustomerId(@PathVariable Long customerId){
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        return new Response(true,200 ,customer ,"项目详细信息" );
    }

    /**
     * 根据员工获取此员工下的项目
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据员工获取此员工下的项目")
    @ApiImplicitParam(name = "userId",value = "用户Id",required = true)
    @GetMapping("/customer/getCustomerByUserId/{userId}")
    public Response getCustomerByUserId(@PathVariable Long userId){
        List<Customer> customers = customerService.getCustomerByUserId(userId);
        return new Response(true,200 ,customers ,"项目列表" );
    }

    /**
     * 修改项目状态
     * @param status
     * @param customerId
     * @return
     */
    @ApiOperation(value = "修改项目状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId",value = "客户Id",required = true),
            @ApiImplicitParam(name = "status",value = "状态",required = true),
    })
    @PostMapping("/customer/addStatus")
    public Response addStatus(String status,Long customerId){
        boolean flag = customerService.addStatus(customerId,status);
        if (flag){
            return new Response(true,200 ,null ,"修改成功" );
        }
        return new Response(false,101 ,null ,"修改失败" );
    }
}
