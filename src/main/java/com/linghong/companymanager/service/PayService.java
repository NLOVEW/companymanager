package com.linghong.companymanager.service;

import com.alibaba.fastjson.JSON;
import com.linghong.companymanager.pojo.Bill;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.pojo.Wallet;
import com.linghong.companymanager.repository.BillRepository;
import com.linghong.companymanager.repository.WalletRepository;
import com.linghong.companymanager.utils.IDUtil;
import com.nhb.pay.alipay.AliPayConfig;
import com.nhb.pay.alipay.AliPayService;
import com.nhb.pay.alipay.AliTransactionType;
import com.nhb.pay.alipay.AliTransferResult;
import com.nhb.pay.common.bean.PayOrder;
import com.nhb.pay.common.bean.TransferOrder;
import com.nhb.pay.common.http.HttpConfig;
import com.nhb.pay.common.type.MethodType;
import com.nhb.pay.common.utils.SignUtils;
import com.nhb.pay.wxpay.WxPayConfig;
import com.nhb.pay.wxpay.WxPayService;
import com.nhb.pay.wxpay.WxTransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/13 10:34
 * @Version 1.0
 * @Description:
 */
@Service
public class PayService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static AliPayService aliPayService = null;
    private static WxPayService wxPayService = null;
    private static AliPayConfig aliPayConfig = new AliPayConfig();
    private static WxPayConfig wxPayConfig = new WxPayConfig();
    private static HttpConfig httpConfig = new HttpConfig();

    @Resource
    private BillRepository billRepository;
    @Resource
    private WalletRepository walletRepository;
    @Resource
    private RedisService redisService;

    /**
     * 初始化支付信息
     */
    @PostConstruct
    public void init() {
        //todo 上线时 修改所有信息 支付宝配置文件-----------------------------------
        aliPayConfig.setPid("2088331023996895");
        aliPayConfig.setAppId("2018102461775492");
        aliPayConfig.setKeyPublic("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvdeVq5T74nKjZSbZ6hFblv3sNvHJYJ6/FIkE93yIYDV5LZDaKe8Am0up0R4nmMoUFU1CQW2dQt91VLJjOvZaEnvyfOwqTRcPoPFhNgs+ruhtUc6AFr9C0Kff/MqkD1c/S6C822lYOISGaQoPvMz1blyqfqADg2akYVgGybpxaTtsEqYBdg0YoRmr6sS0jO0/IunEkW1amEmH5Es+3Z543xFMjxV0SkWqyRkaQ4POqwNuUjRLbvIzYVexHEb3bRtVl6iTpFwgRc4mKpHmEXKnUvvXV8jdujczpA5X+YkLQ+WX237gju0jNrNNyr+wi/+e7buIhqqVtAY9O0EK6aXXzwIDAQAB");
        aliPayConfig.setKeyPrivate("c8ElOKH2XDlpMDSxu4ix2Q==");
        aliPayConfig.setNotifyUrl("http://www.alipay.com");
        aliPayConfig.setReturnUrl("http://2509d113.all123.net/aliPayBack");
        aliPayConfig.setSignType(SignUtils.RSA.name());
        aliPayConfig.setSeller("2088331023996895");
        aliPayConfig.setInputCharset("utf-8");
        //最大连接数
        httpConfig.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfig.setDefaultMaxPerRoute(10);
        aliPayService = new AliPayService(aliPayConfig, httpConfig);

        //todo 微信配置文件
        wxPayConfig.setMchId("1520802911");
        wxPayConfig.setAppid("wx4c0ba224b534431d");
        //wxPayConfig.setKeyPublic("ea566adfebbd9e2e76440ffc7ea64464");
        //wxPayConfig.setSecretKey("8e0a59e9a61d7072f745512239fa4286");
        wxPayConfig.setSecretKey("8e0a59e9a61d7072f745512239fa4286");
        wxPayConfig.setNotifyUrl("http://g9auk5.natappfree.cc/pay/wxRechargeCallBack");
        wxPayConfig.setReturnUrl("http://g9auk5.natappfree.cc/pay/wxRechargeCallBack");
        wxPayConfig.setSignType(SignUtils.MD5.name());
        wxPayConfig.setInputCharset("utf-8");
        wxPayService = new WxPayService(wxPayConfig);
    }


    public List<Bill> getBills(User user, Company company) {
        if (user != null) {
            return billRepository.findAllByUser_UserId(user.getUserId());
        } else {
            return billRepository.findAllByCompany_CompanyId(company.getCompanyId());
        }

    }

    public Bill getDetailBill(Long billId) {
        return billRepository.findById(billId).get();
    }

    public Wallet getWallet(User user, Company company) {
        if (user != null) {
            return walletRepository.findByUser_UserId(user.getUserId());
        } else {
            return walletRepository.findByCompany_CompanyId(company.getCompanyId());
        }
    }

    public String aliRecharge(BigDecimal price, User user, Company company) {
        PayOrder payOrder = new PayOrder("充值", "充值", price, IDUtil.getOrderId(), AliTransactionType.WAP);
        Map<String, Object> orderInfo = aliPayService.orderInfo(payOrder);
        Bill bill = new Bill();
        bill.setOutTradeNo(payOrder.getOutTradeNo());
        bill.setPrice(payOrder.getPrice());
        bill.setType(1);
        bill.setIntroduce("通过支付宝  充值" + bill.getPrice() + "元");
        bill.setTime(new Date());
        if (user != null) {
            bill.setUser(user);
        } else {
            bill.setCompany(company);
        }
        redisService.set(payOrder.getOutTradeNo(), bill);
        return aliPayService.buildRequest(orderInfo, MethodType.POST);
    }

    public boolean aliRechargeCallBack(HttpServletRequest request) {
        //获取支付方返回的对应参数
        Map<String, Object> params = null;
        try {
            params = aliPayService.getParameter2Map(request.getParameterMap(), request.getInputStream());
            if (null == params) {
                return false;
            }
            //校验
            if (aliPayService.verify(params)) {
                String outTradeNo = (String) params.get("out_trade_no");
                Bill bill = (Bill) redisService.get(outTradeNo);
                if (bill != null) {
                    if (bill.getCompany() != null) {
                        Wallet wallet = walletRepository.findByCompany_CompanyId(bill.getCompany().getCompanyId());
                        wallet.setCompany(bill.getCompany());
                        wallet.setUpdateTime(new Date());
                        wallet.setBalance(wallet.getBalance().add(bill.getPrice()));
                    } else {
                        Wallet wallet = walletRepository.findByUser_UserId(bill.getUser().getUserId());
                        wallet.setUser(bill.getUser());
                        wallet.setUpdateTime(new Date());
                        wallet.setBalance(wallet.getBalance().add(bill.getPrice()));
                    }
                    billRepository.save(bill);
                    redisService.del(outTradeNo);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String wxRecharge(BigDecimal price,
                             User user,
                             Company company,
                             HttpServletRequest request) {
        PayOrder payOrder = new PayOrder("钱包充值", "钱包充值", price, IDUtil.getOrderId(), WxTransactionType.MWEB);
        StringBuffer requestURL = request.getRequestURL();
        //设置网页地址
       // payOrder.setWapUrl(requestURL.substring(0, requestURL.indexOf("/") > 0 ?
               // requestURL.indexOf("/") : requestURL.length()));
        //payOrder.setWapName("钱包充值");
        logger.info("payService{}",wxPayService.getPayConfigStorage().getAppid()+"--"+wxPayService.getPayConfigStorage().getSecretKey()+"--"+wxPayService.getPayConfigStorage().getMchId());
        Map<String, Object> orderInfo = wxPayService.orderInfo(payOrder);
        Bill bill = new Bill();
        bill.setType(1);
        bill.setOutTradeNo(payOrder.getOutTradeNo());
        bill.setIntroduce("通过微信充值 "+price+" 元");
        bill.setTime(new Date());
        if (user != null) {
            bill.setUser(user);
        } else {
            bill.setCompany(company);
        }
        redisService.set(payOrder.getOutTradeNo(),bill);
        return wxPayService.buildRequest(orderInfo,MethodType.POST );
    }


    public boolean wxRechargeCallBack(HttpServletRequest request) {
        //获取支付方返回的对应参数
        Map<String, Object> params = null;
        try {
            params = wxPayService.getParameter2Map(request.getParameterMap(), request.getInputStream());
            if (null == params) {
                return false;
            }
            //校验
            if (wxPayService.verify(params)) {
                String outTradeNo = (String) params.get("out_trade_no");
                Bill bill = (Bill) redisService.get(outTradeNo);
                if (bill != null) {
                    if (bill.getCompany() != null) {
                        Wallet wallet = walletRepository.findByCompany_CompanyId(bill.getCompany().getCompanyId());
                        wallet.setCompany(bill.getCompany());
                        wallet.setUpdateTime(new Date());
                        wallet.setBalance(wallet.getBalance().add(bill.getPrice()));
                    } else {
                        Wallet wallet = walletRepository.findByUser_UserId(bill.getUser().getUserId());
                        wallet.setUser(bill.getUser());
                        wallet.setUpdateTime(new Date());
                        wallet.setBalance(wallet.getBalance().add(bill.getPrice()));
                    }
                    billRepository.save(bill);
                    redisService.del(outTradeNo);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean aliTransfer(TransferOrder transferOrder, User user, Company company) {
        transferOrder.setOutNo(IDUtil.getOrderId());
        Wallet wallet = null;
        if (user != null){
            wallet = walletRepository.findByUser_UserId(user.getUserId());
        }else {
            wallet = walletRepository.findByCompany_CompanyId(company.getCompanyId());
        }
        if (wallet.getBalance().compareTo(transferOrder.getAmount()) >= 0) {
            AliTransferResult transResult = JSON.parseObject(JSON.toJSONString(aliPayService.transfer(transferOrder)),AliTransferResult.class);
            if (transResult.getAlipay_fund_trans_toaccount_transfer_response().getCode().equals("10000")){
                Bill bill = new Bill();
                bill.setType(4);
                bill.setUser(user);
                bill.setTime(new Date());
                bill.setOutTradeNo(transferOrder.getOutNo());
                bill.setPrice(transferOrder.getAmount());
                bill.setIntroduce("钱包余额转账到支付宝 "+bill.getPrice()+" 元");
                billRepository.save(bill);
                wallet.setBalance(wallet.getBalance().subtract(transferOrder.getAmount()));
                return true;
            }
        }
        return false;
    }
}
