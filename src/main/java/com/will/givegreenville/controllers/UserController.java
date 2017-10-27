package com.will.givegreenville.controllers;

import com.will.givegreenville.models.Role;
import com.will.givegreenville.models.User;
import com.will.givegreenville.repositories.RoleRepository;
import com.will.givegreenville.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepo;

    // signup page
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // creates new user
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupForm(@ModelAttribute User user) {

        Role userRole = roleRepo.findByName("ROLE_USER");
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setRole(userRole);
        user.setActive(true);
        userRepo.save(user);
        return "redirect:/login";
    }

    // takes admin user to create mod page/form
    @RequestMapping(value = "/addMod")
    public String addModeratorForm(Model model,
                               Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            if (me.getRole().getName().equals("ROLE_ADMIN")) {
                model.addAttribute("user", new User());
                return "addMod";
            }
            return "login";
        }
        return "login";
    }

    // creates new moderator
    @RequestMapping(value = "/addMod", method = RequestMethod.POST)
    public String addModerator(@ModelAttribute User user) {
        Role modRole = roleRepo.findByName("ROLE_MODERATOR");
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setRole(modRole);
        user.setActive(true);
        userRepo.save(user);
        return "redirect:/";
    }

}
