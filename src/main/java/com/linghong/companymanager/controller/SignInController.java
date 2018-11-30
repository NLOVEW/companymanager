package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.SignIn;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.service.SignInService;
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
import java.util.Map;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 18:37
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"打卡相关"})
@RestController
public class SignInController {
    @Resource
    private SignInService signInService;

    /**
     * 员工打卡
     * @param request
     * @return
     */
    @ApiOperation(value = "员工打卡")
    @PostMapping("/signIn/pushSignIn")
    public Response pushSignIn(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        boolean flag = signInService.pushSignIn(user);
        if (flag){
            return new Response(true,200 ,null , "打卡成功");
        }
        return new Response(false,101 ,null , "打卡失败");
    }

    /**
     * 根据userId获取此用户的打卡记录
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据userId获取此用户的打卡记录")
    @ApiImplicitParam(name = "userId",value = "员工Id",required = true)
    @GetMapping("/signIn/getUserSignInByUserId/{userId}")
    public Response getUserSignInByUserId(@PathVariable Long userId){
        List<SignIn> signIns = signInService.getUserSignInByUserId(userId);
        return new Response(true,200 ,signIns , "打卡记录");
    }

    /**
     * 根据公司Id获取员工的打卡记录
     * @param companyId
     * @return
     */
    @ApiOperation(value = "根据公司Id获取员工的打卡记录")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/signIn/getUserSignInByCompanyId/{companyId}")
    public Response getUserSignInByCompanyId(@PathVariable Long companyId){
        List<SignIn> signIns = signInService.getUserSignInByCompanyId(companyId);
        return new Response(true,200 ,signIns , "打卡记录");
    }

    /**
     * 根据公司Id 检索公司打卡情况
     * @param companyId
     * @return
     */
    @ApiOperation(value = "根据公司Id 检索公司打卡情况")
    @ApiImplicitParam(name = "companyId",value = "公司Id",required = true)
    @GetMapping("/signIn/getSignInDistributed/{companyId}")
    public Response getSignInDistributed(@PathVariable Long companyId){
        Map<String,String> result = signInService.getSignInDistributed(companyId);
        return new Response(true,200 ,result , "打卡记录");
    }

    /**
     * 根据公司Id  和打卡时间  查看员工打卡记录
     * @param companyId
     * @param day
     * @return
     */
    @ApiOperation(value = "根据公司Id  和打卡时间  查看员工打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId",value = "公司Id",required = true),
            @ApiImplicitParam(name = "day",value = "查询的日期",required = true),
    })
    @GetMapping("/signIn/getSignInDistributedByTime/{companyId}/{day}")
    public Response getSignInDistributedByTime(@PathVariable Long companyId,
                                               @PathVariable Long day){
        Map result = signInService.getSignInDistributedByTime(companyId,day);
        return new Response(true,200 , result, "打卡记录");
    }
}
