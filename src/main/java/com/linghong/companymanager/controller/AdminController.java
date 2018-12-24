package com.linghong.companymanager.controller;

import com.linghong.companymanager.pojo.*;
import com.linghong.companymanager.service.AdminService;
import com.linghong.companymanager.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: luck_nhb
 * @Date: 2018/12/11 15:44
 * @Version 1.0
 * @Description:
 */
@Controller
public class AdminController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private AdminService adminService;

    @GetMapping("/admin/login.html")
    public String login() {
        return "login";
    }


    @PostMapping("/admin/doLogin")
    public String doLogin(String mobilePhone, String password, HttpServletRequest request) {
        Admin admin = adminService.login(mobilePhone, password);
        if (admin != null) {
            request.getSession().setAttribute("admin", admin);
            return "redirect:/admin/index";
        }
        return "login";
    }

    @RequestMapping("/admin/index")
    public String index(Model model) {
        logger.info("执行前");
        Map<String, Object> result = adminService.index();
        logger.info("执行后");
        List<Company> authTrue = (List<Company>) result.get("authTrue");
        model.addAttribute("authTrue", authTrue);
        List<Company> authFalse = (List<Company>) result.get("authFalse");
        model.addAttribute("authFalse", authFalse);
        List<PersonTalkMsg> personTalkMsgs = (List<PersonTalkMsg>) result.get("personTalkMsgs");
        model.addAttribute("personTalkMsgs", personTalkMsgs);
        List<CompanyTalkMsg> companyTalkMsgs = (List<CompanyTalkMsg>) result.get("companyTalkMsgs");
        model.addAttribute("companyTalkMsgs", companyTalkMsgs);
        List<CompanyTutor> companyTutors = (List<CompanyTutor>) result.get("companyTutors");
        model.addAttribute("companyTutors", companyTutors);
        List<CompanyDynamicMessage> dynamicMessages = (List<CompanyDynamicMessage>) result.get("dynamicMessages");
        model.addAttribute("dynamicMessages", dynamicMessages);
        List<MessageBack> messageBacks = (List<MessageBack>) result.get("messageBacks");
        model.addAttribute("messageBacks", messageBacks);
        return "index";
    }

    @PostMapping("/admin/agreeAuthCompany")
    public void agreeAuthCompany(Long companyId, HttpServletResponse response) {
        try {
            adminService.agreeAuthCompany(companyId);
            response.getOutputStream().println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @PostMapping("/admin/disAgreeAuthCompany")
    public void disAgreeAuthCompany(Long companyId, HttpServletResponse response) {
        try {
            adminService.agreeAuthCompany(companyId);
            response.getOutputStream().println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/admin/handlerMessage")
    public void  handlerMessage(String handlerMessage, HttpServletResponse response) {
        try {
            adminService.handlerMessage(handlerMessage);
            response.getOutputStream().println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/admin/updatePassword")
    public String updatePassword(String newPassword, HttpServletRequest request) {
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        admin.setPassword(MD5Util.md5(newPassword));
        adminService.updatePassword(admin);
        return "redirect:/admin/index";
    }
}
