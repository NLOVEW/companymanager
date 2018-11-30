package com.linghong.companymanager.controller;

import com.linghong.companymanager.dto.Response;
import com.linghong.companymanager.pojo.Bill;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.pojo.Wallet;
import com.linghong.companymanager.service.PayService;
import com.nhb.pay.common.bean.TransferOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/13 10:34
 * @Version 1.0
 * @Description:
 */
@Api(tags = {"支付相关接口"})
@RestController
public class PayController {
    @Resource
    private PayService payService;

    /**
     * 获取本用户所有账单
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "获取本用户所有账单")
    @GetMapping("/pay/getBills")
    public Response getBills(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        List<Bill> bills = payService.getBills(user, company);
        return new Response(true, 200, bills, "账单");
    }

    /**
     * 获取账单详情
     *
     * @param billId
     * @return
     */
    @ApiOperation(value = "获取账单详情")
    @ApiImplicitParam(name = "billId",value = "账单Id",required = true)
    @GetMapping("/pay/getDetailBill/{billId}")
    public Response getDetailBill(@PathVariable Long billId) {
        Bill bill = payService.getDetailBill(billId);
        return new Response(true, 200, bill, "账单详细信息");
    }

    /**
     * 获取本用户的钱包
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "获取本用户的钱包")
    @GetMapping("/pay/getWallet")
    public Response getWallet(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        Wallet wallet = payService.getWallet(user, company);
        return new Response(true, 200, wallet, "钱包");
    }

    /**
     * 支付宝充值
     * @param price
     * @param request
     * @return
     */
    @ApiOperation(value = "支付宝充值")
    @ApiImplicitParam(name = "price",value = "金额",required = true)
    @PostMapping("/pay/aliRecharge")
    public String aliRecharge(BigDecimal price, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        return payService.aliRecharge(price, user, company);
    }

    /**
     * 支付宝 回调函数
     *
     * @return
     */
    @PostMapping("/pay/aliRechargeCallBack")
    public Callable<Response> aliRechargeCallBack(HttpServletRequest request) {
        return () -> {
            boolean flag = payService.aliRechargeCallBack(request);
            if (flag) {
                return new Response(true, 200, null, "充值成功");
            }
            return new Response(false, 500, null, "充值失败");
        };
    }

    /**
     * 微信充值
     *
     * @param price
     * @param request
     * @return
     */
    @ApiOperation(value = "微信充值")
    @ApiImplicitParam(name = "price",value = "金额",required = true)
    @RequestMapping(value = "/pay/wxRecharge.html")
    public String wxRecharge(BigDecimal price, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Company company = (Company) session.getAttribute("company");
        return payService.wxRecharge(price, user, company, request);

    }

    /**
     * 微信 回调函数
     *
     * @return
     */
    @PostMapping("/pay/wxRechargeCallBack")
    public Callable<Response> wxRechargeCallBack(HttpServletRequest request) {
        return () -> {
            boolean flag = payService.wxRechargeCallBack(request);
            if (flag) {
                return new Response(true, 200, null, "充值成功");
            }
            return new Response(false, 500, null, "网络开小差");
        };
    }

    /**
     * 转账到支付宝余额中
     *
     * @param transferOrder
     * @param request
     * @return
     */
    @ApiOperation("转账到支付宝余额中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payeeAccount",value = "目标手机号",required = true),
            @ApiImplicitParam(name = "amount",value = "金额",required = true),
    })
    @PostMapping("/pay/aliTransfer")
    public Callable<Response> aliTransfer(TransferOrder transferOrder, HttpServletRequest request) {
        return () -> {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            Company company = (Company) session.getAttribute("company");
            boolean flag = payService.aliTransfer(transferOrder, user,company);
            if (flag) {
                return new Response(true, 200, null, "成功转账到支付宝账号：" + transferOrder.getPayeeAccount());
            }
            return new Response(false, 500, null, "暂停服务,请稍后再试");
        };
    }
}
