package com.linghong.companymanager.service;


import com.linghong.companymanager.pojo.OpenUser;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.repository.OpenUserRepository;
import com.linghong.companymanager.repository.UserRepository;
import com.nhb.open.login.bean.QQAccessToken;
import com.nhb.open.login.bean.QQUser;
import com.nhb.open.login.bean.WeChatAccessToken;
import com.nhb.open.login.bean.WeChatUser;
import com.nhb.open.login.qq.QQService;
import com.nhb.open.login.wechat.WeChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/3 09:36
 * @Version 1.0
 * @Description:
 */
@Service
public class OpenUserService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private QQService qqService = new QQService();
    private WeChatService weChatService = new WeChatService();
    //todo 第三方登录相关配置
    //qq配置信息
    @Value("${openUser.qq.appId}")
    private String qqAppId;
    @Value("${openUser.qq.appKey}")
    private String qqAppKey;
    @Value("${openUser.qq.redirectUrl}")
    private String qqRedirectUrl;
    //微信配置信息
    @Value("${openUser.wx.appId}")
    private String wxAppId;
    @Value("${openUser.wx.appKey}")
    private String wxAppKey;
    @Value("${openUser.wx.redirectUrl}")
    private String wxRedirectUrl;

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
        User user = userRepository.findByOpenUser_OpenId(accessToken.getOpenid());
        if (user != null){
            OpenUser target = user.getOpenUser();
            target.setAccessToken(accessToken.getAccess_token());
            target.setExpiredTime(accessToken.getExpires_in());
            logger.info("已登录数据：{}",target.toString());
            return true;
        }else {
            OpenUser openUser = new OpenUser();
            openUser.setOpenType(0);
            openUser.setOpenId(accessToken.getOpenid());
            openUser.setAccessToken(accessToken.getAccess_token());
            openUser.setNickName(qqUser.getNickname());
            openUser.setAvatar(qqUser.getFigureurl_qq_1());
            openUser.setExpiredTime(accessToken.getExpires_in());
            user = new User();
            user.setOpenUser(openUser);
            userRepository.save(user);
            logger.info("新登录数据：{}",openUser.toString());
        }
        request.getSession().setAttribute("user",user );
        return true;
    }

    public void weChatLogin(HttpServletResponse response) {
        weChatService.getCode(wxAppId,wxRedirectUrl , response);
    }


    public boolean wxCallBack(HttpServletRequest request) {
        String code = weChatService.callBack(request);
        WeChatAccessToken accessToken = weChatService.getAccessToken(wxAppId, wxAppKey, code);
        WeChatUser userMessage = weChatService.getUserMessage(accessToken.getAccess_token(), accessToken.getOpenid());
        User user = userRepository.findByOpenUser_OpenId(accessToken.getOpenid());
        if (user != null){
            OpenUser target = user.getOpenUser();
            target.setAccessToken(accessToken.getAccess_token());
            target.setAvatar(userMessage.getHeadimgurl());
            target.setExpiredTime(accessToken.getExpires_in());
            target.setNickName(userMessage.getNickname());
            target.setOpenId(userMessage.getOpenid());
            logger.info("已登录数据：{}",target.toString());
        }else {
            OpenUser openUser = new OpenUser();
            openUser.setAccessToken(accessToken.getAccess_token());
            openUser.setAvatar(userMessage.getHeadimgurl());
            openUser.setExpiredTime(accessToken.getExpires_in());
            openUser.setNickName(userMessage.getNickname());
            openUser.setOpenId(userMessage.getOpenid());
            openUser.setOpenType(1);
            user = new User();
            user.setOpenUser(openUser);
            userRepository.save(user);
            logger.info("新登录数据：{}",user.toString());
        }
        request.getSession().setAttribute("user",user );
        return true;
    }

}
