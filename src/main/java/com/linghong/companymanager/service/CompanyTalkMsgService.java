package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.repository.*;
import com.linghong.companymanager.utils.FastDfsUtil;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/10 21:39
 * @Version 1.0
 * @Description:
 */
@Service
public class CompanyTalkMsgService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private CompanyTalkMsgRepository companyTalkMsgRepository;
    @Resource
    private CompanyTalkMsgOrderRepository companyTalkMsgOrderRepository;
    @Resource
    private DiscussMessageRepository discussMessageRepository;
    @Resource
    private WalletRepository walletRepository;
    @Resource
    private BillRepository billRepository;

    public boolean pushPersonTalkMsg(CompanyTalkMsg companyTalkMsg,
                                     String base64Image,
                                     User user,
                                     Company company) {
        if (base64Image != null) {
            companyTalkMsg.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(base64Image));
        }
        if (user != null && user.getAuth()) {
            companyTalkMsg.setFromUser(user);
        } else if (company != null && company.getAuth()){
            companyTalkMsg.setFromCompany(company);
        }else {
            return false;
        }
        companyTalkMsg.setCompanyTalkMsgId(IDUtil.getId());
        companyTalkMsg.setPushTime(new Date());
        companyTalkMsgRepository.save(companyTalkMsg);
        return true;
    }

    public List<CompanyTalkMsg> getCompanyTalkMsgByCompanyId(Long companyId) {
        List<CompanyTalkMsg> list1 = companyTalkMsgRepository.findAllByFromCompany_CompanyId(companyId);
        List<CompanyTalkMsg> list2 = companyTalkMsgRepository.findAllByFromUser_FromCompany_CompanyId(companyId);
        if (list1 != null && list1.size() > 0 && list2 != null && list2.size() > 0){
            List<CompanyTalkMsg> talkMsgs = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
            return talkMsgs;
        }else if (list1 != null && list1.size() > 0) {
            return list1;
        } else {
            return list2;
        }
    }

    public List<CompanyTalkMsg> getCompanyTalkMsgByUserId(Long userId) {
        return companyTalkMsgRepository.findAllByFromUser_UserId(userId);
    }

    public CompanyTalkMsg getCompleteCompanyTalkMsgById(String companyTalkMsgId) {
        return companyTalkMsgRepository.findById(companyTalkMsgId).get();
    }

    public boolean pushDiscussMessage(DiscussMessage discussMessage,
                                      String companyTalkMsgId,
                                      String baseImage,
                                      User user,
                                      Company company) {
        if (user != null) {
            discussMessage.setFromUser(user);
        } else {
            discussMessage.setFromCompany(company);
        }
        if (baseImage != null) {
            discussMessage.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(baseImage));
        }
        if (discussMessage.getDiscussMessageId() != null) {
            DiscussMessage target = discussMessageRepository.findById(discussMessage.getDiscussMessageId()).get();
            if (target.getFromCompany() != null) {
                discussMessage.setToCompany(target.getFromCompany());
            } else {
                discussMessage.setToUser(target.getFromUser());
            }
        }
        discussMessage.setPushTime(new Date());
        discussMessage.setDiscussMessageId(IDUtil.getId());
        CompanyTalkMsg companyTalkMsg = companyTalkMsgRepository.findById(companyTalkMsgId).get();
        Set<DiscussMessage> discussMessages = companyTalkMsg.getDiscussMessages();
        discussMessages.add(discussMessage);
        return true;
    }

    public List<CompanyTalkMsg> getAllCompanyTalkMsg() {
        return companyTalkMsgRepository.findAll();
    }

    public boolean signUp(String companyTalkMsgId, User user, Company company) {
        CompanyTalkMsg companyTalkMsg = companyTalkMsgRepository.findById(companyTalkMsgId).get();
        CompanyTalkMsgOrder companyTalkMsgOrder = new CompanyTalkMsgOrder();
        companyTalkMsgOrder.setCompanyTalkMsg(companyTalkMsg);
        companyTalkMsgOrder.setPushTime(new Date());
        companyTalkMsgOrder.setStatus(0);
        companyTalkMsgOrder.setCompanyTalkMsgOrderId(IDUtil.getOrderId());
        //获取发布者的钱包信息
        Wallet wallet = null;
        Bill bill = new Bill();
        bill.setType(2);
        bill.setTime(new Date());
        bill.setPrice(companyTalkMsg.getPrice());
        bill.setIntroduce("申请报名 收入出 " + companyTalkMsg.getPrice());
        if (companyTalkMsg.getFromCompany() != null) {
            wallet = walletRepository.findByCompany_CompanyId(companyTalkMsg.getFromCompany().getCompanyId());
            bill.setCompany(companyTalkMsg.getFromCompany());
        } else {
            wallet = walletRepository.findByUser_UserId(companyTalkMsg.getFromUser().getUserId());
            bill.setUser(companyTalkMsg.getFromUser());
        }
        //判断报名者是否认证通过
        if (user != null && user.getAuth()) {
            companyTalkMsgOrder.setFromUser(user);
            //判断是否收费的活动
            if (companyTalkMsg.getPrice() != null) {
                Wallet userWallet = walletRepository.findByUser_UserId(user.getUserId());
                if (userWallet.getBalance().compareTo(companyTalkMsg.getPrice()) < 0) {
                    return false;
                } else {
                    wallet.setBalance(wallet.getBalance().add(companyTalkMsg.getPrice()));
                    userWallet.setBalance(userWallet.getBalance().subtract(companyTalkMsg.getPrice()));
                    Bill userBill = new Bill();
                    userBill.setUser(user);
                    userBill.setType(0);
                    userBill.setTime(new Date());
                    userBill.setPrice(companyTalkMsg.getPrice());
                    userBill.setIntroduce("申请报名 支出 " + companyTalkMsg.getPrice());
                    billRepository.save(userBill);
                    billRepository.save(bill);
                }
            }
            companyTalkMsgOrderRepository.save(companyTalkMsgOrder);
            return true;
        } else if (company != null && company.getAuth()) {
            companyTalkMsgOrder.setFromCompany(company);
            //判断是否收费的活动
            if (companyTalkMsg.getPrice() != null) {
                Wallet companyWallet = walletRepository.findByCompany_CompanyId(company.getCompanyId());
                if (companyWallet.getBalance().compareTo(companyTalkMsg.getPrice()) < 0) {
                    return false;
                } else {
                    wallet.setBalance(wallet.getBalance().add(companyTalkMsg.getPrice()));
                    companyWallet.setBalance(companyWallet.getBalance().subtract(companyTalkMsg.getPrice()));
                    Bill companyBill = new Bill();
                    companyBill.setCompany(company);
                    companyBill.setType(0);
                    companyBill.setTime(new Date());
                    companyBill.setPrice(companyTalkMsg.getPrice());
                    companyBill.setIntroduce("申请报名 支出 " + companyTalkMsg.getPrice());
                    billRepository.save(companyBill);
                    billRepository.save(bill);
                }
                companyTalkMsgOrderRepository.save(companyTalkMsgOrder);
                return true;
            }
        }
        return false;
    }

    public List<CompanyTalkMsgOrder> getSignUpOrder(User user, Company company) {
        if (user != null) {
            List<CompanyTalkMsgOrder> talkMsgOrders = companyTalkMsgOrderRepository.findAllByCompanyTalkMsg_FromUser_UserId(user.getUserId());
            return talkMsgOrders;
        } else {
            List<CompanyTalkMsgOrder> talkMsgOrders = companyTalkMsgOrderRepository.findAllByCompanyTalkMsg_FromCompany_CompanyId(company.getCompanyId());
            return talkMsgOrders;
        }
    }

    public boolean dealSignUpOrder(String companyTalkMsgId, Integer status) {
        CompanyTalkMsgOrder talkMsgOrder = companyTalkMsgOrderRepository.findById(companyTalkMsgId).get();
        talkMsgOrder.setStatus(status);
        //拒绝报名 并且退还费用
        if (status.equals(2) && talkMsgOrder.getCompanyTalkMsg().getPrice() != null){
            Wallet wallet = null;
            Bill bill = new Bill();
            bill.setIntroduce("拒绝报名申请 退款"+talkMsgOrder.getCompanyTalkMsg().getPrice());
            bill.setPrice(talkMsgOrder.getCompanyTalkMsg().getPrice());
            bill.setTime(new Date());
            bill.setType(0);
            if (talkMsgOrder.getCompanyTalkMsg().getFromUser() != null){
                wallet = walletRepository.findByUser_UserId(talkMsgOrder.getCompanyTalkMsg().getFromUser().getUserId());
                bill.setUser(talkMsgOrder.getCompanyTalkMsg().getFromUser());
            }else {
                wallet = walletRepository.findByCompany_CompanyId(talkMsgOrder.getCompanyTalkMsg().getFromCompany().getCompanyId());
                bill.setCompany(talkMsgOrder.getCompanyTalkMsg().getFromCompany());
            }
            wallet.setBalance(wallet.getBalance().subtract(talkMsgOrder.getCompanyTalkMsg().getPrice()));
            if (talkMsgOrder.getFromUser() != null){
                Wallet userWallet = walletRepository.findByUser_UserId(talkMsgOrder.getFromUser().getUserId());
                userWallet.setBalance(userWallet.getBalance().add(talkMsgOrder.getCompanyTalkMsg().getPrice()));
                userWallet.setUpdateTime(new Date());
                Bill userBill = new Bill();
                userBill.setIntroduce("拒绝报名申请 退款"+talkMsgOrder.getCompanyTalkMsg().getPrice());
                userBill.setPrice(talkMsgOrder.getCompanyTalkMsg().getPrice());
                userBill.setTime(new Date());
                userBill.setType(2);
                billRepository.save(userBill);
                billRepository.save(bill);
            }else {
                Wallet companyWallet = walletRepository.findByCompany_CompanyId(talkMsgOrder.getFromCompany().getCompanyId());
                companyWallet.setBalance(companyWallet.getBalance().add(talkMsgOrder.getCompanyTalkMsg().getPrice()));
                companyWallet.setUpdateTime(new Date());
                Bill companyBill = new Bill();
                companyBill.setIntroduce("拒绝报名申请 退款"+talkMsgOrder.getCompanyTalkMsg().getPrice());
                companyBill.setPrice(talkMsgOrder.getCompanyTalkMsg().getPrice());
                companyBill.setTime(new Date());
                companyBill.setType(2);
                billRepository.save(companyBill);
                billRepository.save(bill);
            }
            return true;
        }
        return false;
    }

    public List<CompanyTalkMsgOrder> getApplyOrder(User user, Company company) {
        if (user != null) {
            List<CompanyTalkMsgOrder> talkMsgOrders = companyTalkMsgOrderRepository.findAllByFromUser_UserId(user.getUserId());
            return talkMsgOrders;
        } else {
            List<CompanyTalkMsgOrder> talkMsgOrders = companyTalkMsgOrderRepository.findAllByFromCompany_CompanyId(company.getCompanyId());
            return talkMsgOrders;
        }
    }
}
