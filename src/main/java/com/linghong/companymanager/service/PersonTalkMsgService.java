package com.linghong.companymanager.service;

import com.linghong.companymanager.constant.UrlConstant;
import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.repository.*;
import com.linghong.companymanager.utils.FastDfsUtil;
import com.linghong.companymanager.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/11 10:55
 * @Version 1.0
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PersonTalkMsgService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private PersonTalkMsgRepository personTalkMsgRepository;
    @Resource
    private PersonTalkMsgOrderRepository personTalkMsgOrderRepository;
    @Resource
    private DiscussMessageRepository discussMessageRepository;
    @Resource
    private WalletRepository walletRepository;
    @Resource
    private BillRepository billRepository;


    public boolean pushPersonTalkMsg(PersonTalkMsg personTalkMsg,
                                     String base64Image,
                                     User user,
                                     Company company) {

        if (user != null && user.getAuth()) {
            personTalkMsg.setFromUser(user);
        } else if (company != null && company.getAuth()){
            personTalkMsg.setFromCompany(company);
        }else {
            return false;
        }
        if (base64Image != null) {
            String[] split = base64Image.split("。");
            for (String imagePath : split) {
                logger.info("split:{}",imagePath);
                personTalkMsg.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(imagePath));
            }
        }
        personTalkMsg.setPersonTalkMsgId(IDUtil.getId());
        personTalkMsg.setPushTime(new Date());
        logger.info("personTalk:{}",personTalkMsg.toString());
        personTalkMsgRepository.save(personTalkMsg);
        logger.info("上传成功");
        return true;
    }

    public List<PersonTalkMsg> getPersonTalkMsgByCompanyId(Long companyId) {
        List<PersonTalkMsg> list1 = personTalkMsgRepository.findAllByFromCompany_CompanyId(companyId);
        List<PersonTalkMsg> list2 = personTalkMsgRepository.findAllByFromUser_FromCompany_CompanyId(companyId);
        if (list1 != null && list1.size() > 0 && list2 != null && list2.size() > 0) {
            List<PersonTalkMsg> talkMsgs = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
            return talkMsgs;
        } else if (list1 != null && list1.size() > 0) {
            return list1;
        } else {
            return list2;
        }
    }

    public List<PersonTalkMsg> getPersonTalkMsgByUserId(Long userId) {
        return personTalkMsgRepository.findAllByFromUser_UserId(userId);
    }

    public PersonTalkMsg getCompletePersonTalkMsgById(String personTalkMsgId) {
        PersonTalkMsg personTalkMsg = personTalkMsgRepository.findById(personTalkMsgId).get();
        return personTalkMsg;
    }

    public boolean pushDiscussMessage(DiscussMessage discussMessage,
                                      String personTalkMsgId,
                                      String baseImage,
                                      User user,
                                      Company company) {
        if (user != null) {
            discussMessage.setFromUser(user);
        } else {
            discussMessage.setFromCompany(company);
        }
        if (baseImage != null) {
            String[] split = baseImage.split("。");
            for (String imagePath : split) {
                logger.info("split:{}",imagePath);
                discussMessage.setImagePath(UrlConstant.IMAGE_URL + new FastDfsUtil().uploadBase64Image(imagePath));
            }
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
        PersonTalkMsg personTalkMsg = personTalkMsgRepository.findById(personTalkMsgId).get();
        Set<DiscussMessage> discussMessages = personTalkMsg.getDiscussMessages();
        discussMessages.add(discussMessage);
        personTalkMsg.setDiscussMessages(discussMessages);
        return true;
    }

    public boolean signUp(String personTalkMsgId,
                          User user,
                          Company company) {
        PersonTalkMsg personTalkMsg = personTalkMsgRepository.findById(personTalkMsgId).get();
        PersonTalkMsgOrder personTalkMsgOrder = new PersonTalkMsgOrder();
        personTalkMsgOrder.setPersonTalkMsg(personTalkMsg);
        personTalkMsgOrder.setPushTime(new Date());
        personTalkMsgOrder.setStatus(0);
        personTalkMsgOrder.setPersonTalkMsgOrderId(IDUtil.getOrderId());
        //获取发布者的钱包信息
        Wallet wallet = null;
        Bill bill = new Bill();
        bill.setType(2);
        bill.setTime(new Date());
        bill.setPrice(personTalkMsg.getPrice());
        bill.setIntroduce("申请报名 收入出 " + personTalkMsg.getPrice());
        if (personTalkMsg.getFromCompany() != null) {
            wallet = walletRepository.findByCompany_CompanyId(personTalkMsg.getFromCompany().getCompanyId());
            bill.setCompany(personTalkMsg.getFromCompany());
        } else {
            wallet = walletRepository.findByUser_UserId(personTalkMsg.getFromUser().getUserId());
            bill.setUser(personTalkMsg.getFromUser());
        }
        //判断报名者是否认证通过
        if (user != null && user.getAuth()) {
            personTalkMsgOrder.setFromUser(user);
            //判断是否收费的活动
            if (personTalkMsg.getPrice() != null) {
                Wallet userWallet = walletRepository.findByUser_UserId(user.getUserId());
                if (userWallet.getBalance().compareTo(personTalkMsg.getPrice()) < 0) {
                    return false;
                } else {
                    wallet.setBalance(wallet.getBalance().add(personTalkMsg.getPrice()));
                    userWallet.setBalance(userWallet.getBalance().subtract(personTalkMsg.getPrice()));
                    Bill userBill = new Bill();
                    userBill.setUser(user);
                    userBill.setType(0);
                    userBill.setTime(new Date());
                    userBill.setPrice(personTalkMsg.getPrice());
                    userBill.setIntroduce("申请报名 支出 " + personTalkMsg.getPrice());
                    billRepository.save(userBill);
                    billRepository.save(bill);
                }
            }
            personTalkMsgOrderRepository.save(personTalkMsgOrder);
            return true;
        } else if (company != null && company.getAuth()) {
            personTalkMsgOrder.setFromCompany(company);
            //判断是否收费的活动
            if (personTalkMsg.getPrice() != null) {
                Wallet companyWallet = walletRepository.findByCompany_CompanyId(company.getCompanyId());
                if (companyWallet.getBalance().compareTo(personTalkMsg.getPrice()) < 0) {
                    return false;
                } else {
                    wallet.setBalance(wallet.getBalance().add(personTalkMsg.getPrice()));
                    companyWallet.setBalance(companyWallet.getBalance().subtract(personTalkMsg.getPrice()));
                    Bill companyBill = new Bill();
                    companyBill.setCompany(company);
                    companyBill.setType(0);
                    companyBill.setTime(new Date());
                    companyBill.setPrice(personTalkMsg.getPrice());
                    companyBill.setIntroduce("申请报名 支出 " + personTalkMsg.getPrice());
                    billRepository.save(companyBill);
                    billRepository.save(bill);
                }
            }
            personTalkMsgOrderRepository.save(personTalkMsgOrder);
            return true;
        }
        return false;
    }

    public List<PersonTalkMsgOrder> getSignUpOrder(User user, Company company) {
        if (user != null) {
            List<PersonTalkMsgOrder> talkMsgOrders = personTalkMsgOrderRepository.findAllByPersonTalkMsg_FromUser_UserId(user.getUserId());
            return talkMsgOrders;
        } else {
            List<PersonTalkMsgOrder> talkMsgOrders = personTalkMsgOrderRepository.findAllByPersonTalkMsg_FromCompany_CompanyId(company.getCompanyId());
            return talkMsgOrders;
        }
    }

    public boolean dealSignUpOrder(String personTalkMsgOrderId, Integer status) {
        PersonTalkMsgOrder talkMsgOrder = personTalkMsgOrderRepository.findById(personTalkMsgOrderId).get();
        talkMsgOrder.setStatus(status);
        //拒绝报名 并且退还费用
        if (status.equals(2) && talkMsgOrder.getPersonTalkMsg().getPrice() != null){
            Wallet wallet = null;
            Bill bill = new Bill();
            bill.setIntroduce("拒绝报名申请 退款"+talkMsgOrder.getPersonTalkMsg().getPrice());
            bill.setPrice(talkMsgOrder.getPersonTalkMsg().getPrice());
            bill.setTime(new Date());
            bill.setType(0);
            if (talkMsgOrder.getPersonTalkMsg().getFromUser() != null){
                wallet = walletRepository.findByUser_UserId(talkMsgOrder.getPersonTalkMsg().getFromUser().getUserId());
                bill.setUser(talkMsgOrder.getPersonTalkMsg().getFromUser());
            }else {
                wallet = walletRepository.findByCompany_CompanyId(talkMsgOrder.getPersonTalkMsg().getFromCompany().getCompanyId());
                bill.setCompany(talkMsgOrder.getPersonTalkMsg().getFromCompany());
            }
            wallet.setBalance(wallet.getBalance().subtract(talkMsgOrder.getPersonTalkMsg().getPrice()));
            if (talkMsgOrder.getFromUser() != null){
                Wallet userWallet = walletRepository.findByUser_UserId(talkMsgOrder.getFromUser().getUserId());
                userWallet.setBalance(userWallet.getBalance().add(talkMsgOrder.getPersonTalkMsg().getPrice()));
                userWallet.setUpdateTime(new Date());
                Bill userBill = new Bill();
                userBill.setIntroduce("拒绝报名申请 退款"+talkMsgOrder.getPersonTalkMsg().getPrice());
                userBill.setPrice(talkMsgOrder.getPersonTalkMsg().getPrice());
                userBill.setTime(new Date());
                userBill.setType(2);
                billRepository.save(userBill);
                billRepository.save(bill);
            }else {
                Wallet companyWallet = walletRepository.findByCompany_CompanyId(talkMsgOrder.getFromCompany().getCompanyId());
                companyWallet.setBalance(companyWallet.getBalance().add(talkMsgOrder.getPersonTalkMsg().getPrice()));
                companyWallet.setUpdateTime(new Date());
                Bill companyBill = new Bill();
                companyBill.setIntroduce("拒绝报名申请 退款"+talkMsgOrder.getPersonTalkMsg().getPrice());
                companyBill.setPrice(talkMsgOrder.getPersonTalkMsg().getPrice());
                companyBill.setTime(new Date());
                companyBill.setType(2);
                billRepository.save(companyBill);
                billRepository.save(bill);
            }
            return true;
        }
        return true;
    }

    public Map<String, List<PersonTalkMsgOrder>> getApplyOrder(User user, Company company) {
        if (user != null) {
            List<PersonTalkMsgOrder> talkMsgOrders = personTalkMsgOrderRepository.findAllByFromUser_UserId(user.getUserId());
            Map<String, List<PersonTalkMsgOrder>> collect = talkMsgOrders.stream().collect(Collectors.groupingBy(personTalkMsgOrder -> {
                if (personTalkMsgOrder.getStatus().equals(0)) {
                    return "已报名";
                } else if (personTalkMsgOrder.getStatus().equals(1)) {
                    return "报名成功";
                } else {
                    return "拒绝报名";
                }
            }));
            return collect;
        } else {
            List<PersonTalkMsgOrder> talkMsgOrders = personTalkMsgOrderRepository.findAllByFromCompany_CompanyId(company.getCompanyId());
            Map<String, List<PersonTalkMsgOrder>> collect = talkMsgOrders.stream().collect(Collectors.groupingBy(personTalkMsgOrder -> {
                if (personTalkMsgOrder.getStatus().equals(0)) {
                    return "已报名";
                } else if (personTalkMsgOrder.getStatus().equals(1)) {
                    return "报名成功";
                } else {
                    return "拒绝报名";
                }
            }));
            return collect;
        }
    }

    public List<PersonTalkMsg> getAllPersonTalkMsg() {
        return personTalkMsgRepository.findAll();
    }

    public boolean deletePersonTalkMsgById(String personTalkMsgId) {
        PersonTalkMsg personTalkMsg = personTalkMsgRepository.findById(personTalkMsgId).get();
        personTalkMsgRepository.delete(personTalkMsg);
        return true;
    }
}
