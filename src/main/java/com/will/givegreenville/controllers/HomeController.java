package com.will.givegreenville.controllers;

import com.will.givegreenville.models.Consideration;
import com.will.givegreenville.models.Post;
import com.will.givegreenville.models.User;
import com.will.givegreenville.repositories.ConsiderationRepository;
import com.will.givegreenville.repositories.PostRepository;
import com.will.givegreenville.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private ConsiderationRepository considerRepo;

    // home page lists recent posts
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postRepo.OrderByIdDesc());
        return "index";
    }

    // about page
    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    // ask page lists all ask posts
    @RequestMapping("/ask")
    public String askPage(Model model) {
        model.addAttribute("asks", postRepo.findAllByCategory("Ask"));
        return "ask";
    }

    // give page lists all posts with category of give
    @RequestMapping("/give")
    public String givePage(Model model) {
        model.addAttribute("gives", postRepo.findAllByCategory("Give"));
        return "give";
    }

    // takes you to create post page
    @RequestMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "create";
    }

    // takes you to create ask post form
    @RequestMapping("/createAsk")
    public String askForm(Model model) {
        model.addAttribute("post", new Post());
        return "createAsk";
    }

    // creates new ask post
    @RequestMapping(value = "/createAsk", method = RequestMethod.POST)
    public String createAskPost(@ModelAttribute Post post,
                                Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        post.setAuthor(me);
        post.setCategory("Ask");
        postRepo.save(post);
        return "redirect:/ask";
    }

    // takes you to create give post form
    @RequestMapping("/createGive")
    public String giveForm(Model model) {
        model.addAttribute("post", new Post());
        return "createGive";
    }

    // creates new give post
    @RequestMapping(value = "/createGive", method = RequestMethod.POST)
    public String createGivePost(@ModelAttribute Post post,
                                 Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        post.setAuthor(me);
        post.setCategory("Give");
        postRepo.save(post);
        return "redirect:/give";
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

    // takes you to create a consideration page
    @RequestMapping("/consider/{postId}")
    public String considerForm(Model model,
                               @PathVariable("postId") long postId,
                               Principal principal) {
        model.addAttribute("consideration", new Consideration());
        model.addAttribute("postId", postId);
        return "consider";
    }

    // creates a new consider on a post
    @RequestMapping(value = "/consider/{postId}", method = RequestMethod.POST)
    public String consider(@PathVariable("postId") Long postId,
                           @ModelAttribute Consideration consideration,
                           Principal principal) {
        Post post = postRepo.findOne(postId);
        User me = userRepo.findByUsername(principal.getName());
        consideration.setUser(me);
        consideration.setPost(post);
        considerRepo.save(consideration);
        return "redirect:/";
    }

    // detail page lists post info and all considerations for that post
    @RequestMapping("/detail/{postId}")
    public String detail(Model model,
                         @PathVariable("postId") long postId) {
        Post targetPost = postRepo.findOne(postId);
        model.addAttribute("post", targetPost);
        model.addAttribute("considerations", targetPost.getConsiderations());
        return "detail";
    }


}
