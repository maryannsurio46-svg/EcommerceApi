package com.ws101.surio.EcommerceApi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // This will resolve to src/main/resources/templates/login.html
    }
}