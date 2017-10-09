package com.will.givegreenville.controllers;

import com.will.givegreenville.models.Post;
import com.will.givegreenville.models.User;
import com.will.givegreenville.repositories.PostRepository;
import com.will.givegreenville.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    // home page lists recent posts
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postRepo.findAll());
        return "index";
    }

    // about page
    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/ask")
    public String askPage(Model model) {
        model.addAttribute("asks", postRepo.findAllByCategory("Ask"));
        return "ask";
    }

    // give page lists all posts with category of give
    @RequestMapping("/give")
    public String givePage(Model model) {
        model.addAttribute("myPosts", postRepo.findAllByCategory("Give"));
        return "give";
    }

    // takes you to create post page
    @RequestMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "create";
    }

    // creates new post
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createPost(@ModelAttribute Post post,
                             Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        post.setAuthor(me);
        postRepo.save(post);
        return "redirect:/";
    }


}
