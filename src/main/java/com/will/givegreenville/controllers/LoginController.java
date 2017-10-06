package com.will.givegreenville.controllers;

import com.will.givegreenville.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    // login page
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(Model model,
                            HttpServletRequest request) {
        model.addAttribute("user", new User());
        Object message = request.getSession().getAttribute("error");
        model.addAttribute("errors", message);
        request.getSession().removeAttribute("error");
        return "login";
    }
}
