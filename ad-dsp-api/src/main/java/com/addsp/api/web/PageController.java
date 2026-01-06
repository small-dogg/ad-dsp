package com.addsp.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 페이지 렌더링을 위한 컨트롤러.
 */
@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }

    @GetMapping("/ad-groups")
    public String adGroupsPage() {
        return "ad-groups";
    }

    @GetMapping("/ad-groups/{id}")
    public String adGroupDetailPage() {
        return "ad-group-detail";
    }

    @GetMapping("/settings")
    public String settingsPage() {
        return "settings";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
}
