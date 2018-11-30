package com.linghong.companymanager.service;

import com.linghong.companymanager.pojo.SignIn;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.repository.SignInRepository;
import com.linghong.companymanager.repository.UserRepository;
import com.linghong.companymanager.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: luck_nhb
 * @Date: 2018/11/12 18:38
 * @Version 1.0
 * @Description:
 */
@Service
public class SignInService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private SignInRepository signInRepository;
    @Resource
    private UserRepository userRepository;

    public boolean pushSignIn(User user) {
        SignIn signIn = new SignIn();
        signIn.setFromUser(user);
        signIn.setCreateTime(new Date());
        signIn.setDay(DateUtil.date2SimpleDay(new Date()));
        signInRepository.save(signIn);
        return true;
    }

    public List<SignIn> getUserSignInByUserId(Long userId) {
        return signInRepository.findAllByFromUser_UserId(userId);
    }

    public List<SignIn> getUserSignInByCompanyId(Long companyId) {
        return signInRepository.findAllByFromUser_FromCompany_CompanyId(companyId);
    }

    public Map<String, String> getSignInDistributed(Long companyId) {
        //检索本公司所有打卡记录
        List<SignIn> signIns = signInRepository.findAllByFromUser_FromCompany_CompanyId(companyId);
        //检索本公司总共多少人
        List<User> users = userRepository.findAllByFromCompany_CompanyId(companyId);
        //对所有打卡记录进行日期分类
        Map<String, List<SignIn>> listMap = signIns.stream().collect(Collectors.groupingBy(SignIn::getDay));
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, List<SignIn>> entry : listMap.entrySet()) {
            result.put(entry.getKey(), "应打卡人数:" + users.size() + "  实际打卡人数：" + entry.getValue().size());
        }
        return result;
    }

    public Map getSignInDistributedByTime(Long companyId, Long day) {
        Map result = new HashMap();
        //检索本公司所有打卡记录
        List<SignIn> signIns = signInRepository.findAllByFromUser_FromCompany_CompanyId(companyId);
        //检索本公司总共多少人
        List<User> users = userRepository.findAllByFromCompany_CompanyId(companyId);
        //对所有打卡记录进行日期检索
        Map<Boolean, List<SignIn>> listMap = signIns.stream()
                .collect(Collectors.groupingBy(signIn -> {
                    if (signIn.getDay().equals(DateUtil.date2SimpleDay(day))) {
                        return true;
                    }
                    return false;
                }));
        //获取本次查询时间打卡记录
        List<SignIn> signInList = listMap.get(true);
        result.put("打卡人数/应打卡人数", signInList.size()+"/"+users.size());
        Set<User> signs = new HashSet<>();
        Set<User> unSigns = new HashSet<>();
        for (User user : users){
            boolean flag = false;
            for (SignIn signIn : signInList){
                if (user.getUserId().equals(signIn.getFromUser().getUserId())){
                    flag = true;
                    break;
                }
            }
            if (flag){
                signs.add(user);
            }else {
                unSigns.add(user);
            }
        }
        result.put("已打卡人员",signs );
        result.put("未打卡人员",unSigns );
        return result;
    }
}
