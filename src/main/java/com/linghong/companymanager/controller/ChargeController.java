package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Charge;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.ChargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/7 10:06
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"记账相关接口"})
@RestController
public class ChargeController {

    @Resource
    private ChargeService chargeService;

    /**
     * 记账
     * 参数：name price  type introduce
     * @param charge
     * @param request
     * @return
     */
    @ApiOperation(value = "记账")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "关键字、主题",required = true),
            @ApiImplicitParam(name = "price",value = "金额",required = true),
            @ApiImplicitParam(name = "type",value = "记账类型 支出  收入等(自定义)",required = true),
            @ApiImplicitParam(name = "introduce",value = "记账信息接受啊",required = true)
    })
    @PostMapping("/charge/pushCharge")
    public Response pushCharge(Charge charge , HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Company company = (Company) request.getSession().getAttribute("company");
        boolean flag = chargeService.pushCharge(charge,user,company);
        if (flag){
            return new Response(true,200 ,null ,"记账成功" );
        }
        return new Response(false,101 , null,"请填写完整信息" );
    }

    /**
     * 获取当前用户的记账记录
     * @param request
     * @return
     */
    @ApiOperation(value = "获取当前用户的记账记录")
    @GetMapping("/charge/getCurrentUserCharge")
    public Response getCurrentUserCharge(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Company company = (Company) request.getSession().getAttribute("company");
        List<Charge> charges = chargeService.getCurrentUserCharge(user,company);
        if (null != charges && charges.size() > 0){
            return new Response(true,200 ,charges ,"个人记账记录" );
        }
        return new Response(false,101 ,null ,"您还没有记账记录" );
    }
}
