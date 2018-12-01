package com.linghong.companymanager.service;

import com.linghong.companymanager.pojo.OpenUser;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.repository.OpenUserRepository;
import com.linghong.companymanager.repository.UserRepository;
import com.nhb.open.login.bean.QQAccessToken;
import com.nhb.open.login.bean.QQUser;
import com.nhb.open.login.qq.QQService;
import com.nhb.open.login.wechat.WeChatService;
import com.nhb.open.login.weibo.WeiBoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/1 16:23
 * @Version 1.0
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Scope("prototype")
public class OpenUserService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private QQService qqService = new QQService();
    private WeiBoService weiBoService = new WeiBoService();
    private WeChatService weChatService = new WeChatService();
    //todo 相关配置
    @Value("${openUser.qq.appId}")
    private String qqAppId;
    @Value("${openUser.qq.appKey}")
    private String qqAppKey;
    @Value("${openUser.qq.redirectUrl}")
    private String qqRedirectUrl;


    @Resource
    private OpenUserRepository openUserRepository;
    @Resource
    private UserRepository userRepository;

    public void qqLogin(HttpServletResponse response){
        logger.info("回调地址："+qqRedirectUrl);
        qqService.getCode(qqAppId, qqRedirectUrl, response);
    }

    public boolean qqCallBack(HttpServletRequest request) {
        String code = qqService.callBack(request);
        logger.info("code:"+code);
        QQAccessToken accessToken = qqService.getAccessToken(qqAppId, qqAppKey, code, qqRedirectUrl);
        QQUser qqUser = qqService.getUserMessage(accessToken.getOpenid(), qqAppId, accessToken.getAccess_token());
        OpenUser target = openUserRepository.findByOpenId(accessToken.getOpenid());
        if (target != null){
            target.setAccessToken(accessToken.getAccess_token());
            target.setExpiredTime(accessToken.getExpires_in());
            logger.info("已登录数据：{}",target.toString());
            return true;
        }
        User user = new User();
        OpenUser openUser = new OpenUser();
        openUser.setOpenType(0);
        openUser.setOpenId(accessToken.getOpenid());
        openUser.setAccessToken(accessToken.getAccess_token());
        openUser.setNickName(qqUser.getNickname());
        openUser.setAvatar(qqUser.getFigureurl_qq_1());
        openUser.setExpiredTime(accessToken.getExpires_in());
        user.setOpenUser(openUser);
        userRepository.save(user);
        logger.info("新登录数据：{}",openUser.toString());
        return true;
    }
}
