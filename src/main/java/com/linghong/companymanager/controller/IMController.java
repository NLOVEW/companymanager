package com.linghong.companymanager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linghong.companymanager.pojo.Company;
import com.linghong.companymanager.pojo.SendMsg;
import com.linghong.companymanager.pojo.User;
import com.linghong.companymanager.repository.CompanyRepository;
import com.linghong.companymanager.repository.UserRepository;
import com.linghong.companymanager.service.IMServiceImpl;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@ResponseBody
@Scope("prototype")
@Api(tags = {"即时通信接口   可以问另外前端要  有现成的页面"})
public class IMController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "imServiceImpl")
    private IMServiceImpl imServiceImpl;

    @Resource
    private UserRepository userRepository;
    @Resource
    private CompanyRepository companyRepository;
    /**
     * 点击我的好友按钮
     */
    @RequestMapping("/im/friendsList")
    public Map friendsList(HttpServletRequest request) {
        //创建返回类型
        Map map2 = new HashMap();//map中有本次请求的状态和需要获取的数据
        List list = new ArrayList();//存需要的数据集合
        String mobilePhone = null;
        User user1 = (User) request.getSession().getAttribute("user");
        Company company = (Company) request.getSession().getAttribute("company");
        if (user1 != null){
            mobilePhone = user1.getMobilePhone();
        }else {
            mobilePhone = company.getMobilePhone();
        }
        try {
            //获取到好友
            JSONObject friend = imServiceImpl.findFriend(mobilePhone);
            //取出好友集合
            Object friends = friend.get("friends");
            JSONArray array = JSON.parseArray(friends.toString());
            //遍历好友集合
            for (Object object : array) {
                Map map = new HashMap();
                JSONObject fromObject = (JSONObject) JSON.toJSON(object);
                //获得账号
                String string3 = fromObject.get("faccid").toString();
                User user = userRepository.findByMobilePhone(string3);
                Company company1 = companyRepository.findByMobilePhone(string3);
                if (user != null){
                    map.put("friendMobilePhone",user.getMobilePhone() );
                    map.put("friendName",(user.getNickName() == null) ? user.getUserName() :user.getNickName() );
                    map.put("friendAvar",(user.getAvatar() == null) ? null : user.getAvatar());

                }else {
                    map.put("friendMobilePhone",company1.getMobilePhone() );
                    map.put("friendName",(company1.getCompanyName() == null) ? company1.getUserName() :company1.getCompanyName() );
                    map.put("friendAvar",(company1.getAvatar() == null) ? null : company1.getAvatar());
                }
                list.add(map);
            }
            map2.put("list", list);
            map2.put("states", true);
            return map2;
            //return friendsList;
        } catch (Exception e) {
            System.out.println("IMSController.java-------friendsList----异常");
            map2.put("list", "出现异常");
            map2.put("states", false);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 搜索好友
     * userId
     */
    @RequestMapping("/im/selectUserOne")
    public JSONObject selectUserOne(String mobilePhone) {
        JSONArray array = new JSONArray();
        array.add(mobilePhone);
        try {
            JSONObject jsonObject = imServiceImpl.selectUser(array);
            return jsonObject;
        } catch (Exception e) {
            System.out.println("IMSController.java-------selectUserOne----异常");
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 加好友
     * String accid 发起者的id
     * String faccid 接受者id
     * String type 1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
     * String msg 加好友对应的请求消息，第三方组装，最长256字符
     */
    @RequestMapping("/im/addFriend")
    public JSONObject addFriend(String faccid, String type, String msg, HttpServletRequest request) {
        String mobilePhone = null;
        User user1 = (User) request.getSession().getAttribute("user");
        Company company = (Company) request.getSession().getAttribute("company");
        if (user1 != null){
            mobilePhone = user1.getMobilePhone();
        }else {
            mobilePhone = company.getMobilePhone();
        }
        try {
            return imServiceImpl.addFriend(mobilePhone, faccid, type, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新好友相关信息
     * String accid 必须 发起者accid
     * String faccid  必须 要修改朋友的accid
     * String alias		否 给好友增加备注名，限制长度128
     * String ex		否修改ex字段，限制长度256
     * String serverex	否修改serverex字段，限制长度256此字段client端只读，server端读写
     */
    @RequestMapping("/im/updateFriend")
    public JSONObject updateFriend(String faccid, String alias,
                                   String ex, String serverex, HttpServletRequest request) {
        String mobilePhone = null;
        User user1 = (User) request.getSession().getAttribute("user");
        Company company = (Company) request.getSession().getAttribute("company");
        if (user1 != null){
            mobilePhone = user1.getMobilePhone();
        }else {
            mobilePhone = company.getMobilePhone();
        }
        try {
            return imServiceImpl.updateFriend(mobilePhone, faccid, alias, ex, serverex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除好友
     * String accid发起者accid
     * String faccid  要删除朋友的accid
     */
    @RequestMapping("/im/deleteFriend")
    public JSONObject deleteFriend(String faccid, HttpServletRequest request) {
        String mobilePhone = null;
        User user1 = (User) request.getSession().getAttribute("user");
        Company company = (Company) request.getSession().getAttribute("company");
        if (user1 != null){
            mobilePhone = user1.getMobilePhone();
        }else {
            mobilePhone = company.getMobilePhone();
        }
        try {
            return imServiceImpl.deleteFriend(mobilePhone, faccid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送普通消息
     * from   发送者accid，用户帐号
     * ope   0：点对点个人消息，1：群消息（高级群）
     * to   ope==0是表示accid即用户id，ope==1表示tid即群id
     * body
     * type
     */
    @RequestMapping("/im/sendMsg")
    public JSONObject sendMsg(SendMsg sendMsg, HttpServletRequest request) {
        String mobilePhone = null;
        User user1 = (User) request.getSession().getAttribute("user");
        Company company = (Company) request.getSession().getAttribute("company");
        if (user1 != null){
            mobilePhone = user1.getMobilePhone();
        }else {
            mobilePhone = company.getMobilePhone();
        }
        System.out.println(mobilePhone + "from" + "ope:" + sendMsg.getOpe());
        sendMsg.setFrom(mobilePhone);
        String msg = "{'msg':" + sendMsg.getBody() + "}";
        sendMsg.setBody(msg);
        try {
            return imServiceImpl.sendMsg(sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 单聊云端历史消息查询
     * String from		发送者accid
     * String to		接收者accid
     * String begintime	开始时间，ms
     * String endtime		截止时间，ms
     * String limit		本次查询的消息条数上限(最多100条),小于等于0，或者大于100，会提示参数错误
     */
    @RequestMapping("/im/querySessionMsg")
    public Map querySessionMsg(String to, HttpServletRequest request) {
        String mobilePhone = null;
        User user1 = (User) request.getSession().getAttribute("user");
        Company company = (Company) request.getSession().getAttribute("company");
        if (user1 != null){
            mobilePhone = user1.getMobilePhone();
        }else {
            mobilePhone = company.getMobilePhone();
        }
        String begintime = 0 + "";
        String endtime = new Date().getTime() + "";
        String limit = 90 + "";
        try {
            Map map = new HashMap();
            JSONObject jsonObject1 = imServiceImpl.querySessionMsg(mobilePhone, to, begintime, endtime, limit);
            map.put("msg", jsonObject1);
            //获取发送者头像
            JSONArray array = new JSONArray();
            array.add(mobilePhone);
            JSONObject jsonObject0 = imServiceImpl.selectUser(array);
            logger.info(jsonObject0.toString());
            JSONArray array0 = JSON.parseArray(jsonObject0.get("uinfos").toString());
            for (Object object2 : array0) {
                JSONObject fromObject2 = (JSONObject) JSON.toJSON(object2);
                if (null != fromObject2.get("icon")) {
                    String touXiang = fromObject2.get("icon").toString();//头像
                    map.put("inAvatar", touXiang);
                } else {
                    map.put("inAvatar", null);
                }
            }
            //获取接受者头像
            JSONArray array2 = new JSONArray();
            array2.add(to);
            JSONObject jsonObject = imServiceImpl.selectUser(array2);
 //           JSONArray array3 = JSON.parseArray(jsonObject.get("uinfos").toString());
//            for (Object object2 : array3) {
//                JSONObject fromObject2 = (JSONObject) JSON.toJSON(object2);
//                if (null != fromObject2.get("icon")) {
//                    String touXiang = fromObject2.get("icon").toString();//头像
//                    map.put("toAvatar", touXiang);
//                } else {
//                    map.put("toAvatar", null);
//                }
//            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
