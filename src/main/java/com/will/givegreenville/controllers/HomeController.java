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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private ConsiderationRepository considerRepo;

    // home page lists recent posts in order of date created (newest to oldest)
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postRepo.OrderByCreatedDesc());
        return "index";
    }

    // about page
    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    // ask page lists all ASK posts
    @RequestMapping("/ask")
    public String askPage(Model model) {
        model.addAttribute("asks", postRepo.findAllByCategoryOrderByCreatedDesc("Ask"));
        return "ask";
    }

    // give page lists all posts with category of GIVE
    @RequestMapping("/give")
    public String givePage(Model model) {
        model.addAttribute("gives", postRepo.findAllByCategoryOrderByCreatedDesc("Give"));
        return "give";
    }

    // flash give page lists ALL FLASH GIVE posts
    @RequestMapping("/flash")
    public String flashPage(Model model) {
        model.addAttribute("flashes", postRepo.findAllByCategoryOrderByCreatedDesc("Flash Give"));
        return "flashGive";
    }

    // takes you to CREATE GENERAL POST FORM
    @RequestMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "create";
    }

    // creates new GENERAL (ask, give, flash give) post
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createPost(@ModelAttribute Post post,
                             Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        post.setAuthor(me);
        post.setCreated(new Date());
        postRepo.save(post);
        return "redirect:/";
    }

    // takes you to create ASK POST FORM
    @RequestMapping("/createAsk")
    public String askForm(Model model) {
        model.addAttribute("post", new Post());
        return "createAsk";
    }

    // creates NEW ASK post
    @RequestMapping(value = "/createAsk", method = RequestMethod.POST)
    public String createAskPost(@ModelAttribute Post post,
                                Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        post.setAuthor(me);
        post.setCategory("Ask");
        post.setCreated(new Date());
        postRepo.save(post);
        return "redirect:/ask";
    }

    // takes you to create GIVE POST FORM
    @RequestMapping("/createGive")
    public String giveForm(Model model) {
        model.addAttribute("post", new Post());
        return "createGive";
    }

    // creates NEW GIVE post
    @RequestMapping(value = "/createGive", method = RequestMethod.POST)
    public String createGivePost(@ModelAttribute Post post,
                                 Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        post.setAuthor(me);
        post.setCategory("Give");
        post.setCreated(new Date());
        postRepo.save(post);
        return "redirect:/give";
    }

    // takes you to create FLASH GIVE FORM
    @RequestMapping("/createFlash")
    public String flashForm(Model model) {
        model.addAttribute("post", new Post());
        return "createFlash";
    }

    // creates a new FLASH GIVE post
    @RequestMapping(value = "/createFlash", method = RequestMethod.POST)
    public String createFlashPost(@ModelAttribute Post post,
                                  Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        post.setAuthor(me);
        post.setCategory("Flash Give");
        post.setCreated(new Date());
        postRepo.save(post);
        return "redirect:/flash";
    }





    // takes you to create a CONSIDERATION page
    @RequestMapping("/consider/{postId}")
    public String considerForm(Model model,
                               @PathVariable("postId") long postId,
                               Principal principal) {
        model.addAttribute("consideration", new Consideration());
        model.addAttribute("postId", postId);
        return "consider";
    }

    // creates a NEW CONSIDER on a post
    @RequestMapping(value = "/consider/{postId}", method = RequestMethod.POST)
    public String consider(@PathVariable("postId") Long postId,
                           @ModelAttribute Consideration consideration,
                           HttpServletRequest request,
                           Principal principal) {
        Post post = postRepo.findOne(postId);
        User me = userRepo.findByUsername(principal.getName());
        Consideration userConsideration = considerRepo.findConsiderationByPostIdAndUser(postId, me);
        if (userConsideration == null) {
            consideration.setUser(me);
            consideration.setPost(post);
            consideration.setCreated(new Date());
            considerRepo.save(consideration);
            return "redirect:/";
        }
        return "redirect:/";
    }

    // detail page LISTS post info and ALL CONSIDERATIONS for that post
    @RequestMapping("/detail/{postId}")
    public String detail(Model model,
                         @PathVariable("postId") long postId) {
        Post targetPost = postRepo.findOne(postId);
        model.addAttribute("post", targetPost);
        model.addAttribute("considerations", targetPost.getConsiderations());
        return "detail";
    }

    // search for index page
    @RequestMapping("/indexResults")
    public String searchResults(Model model,
                                @RequestParam("search") String searchString) {
        model.addAttribute("posts", postRepo.findAllByTitleContainsIgnoreCase(searchString));
        return "index";
    }

    // search for ask page
    @RequestMapping("/askResults")
    public String askSearchResults(Model model,
                                @RequestParam("askSearch") String searchString) {
        model.addAttribute("asks", postRepo.findAllByCategoryAndTitleContainsIgnoreCase("Ask", searchString));
        return "ask";
    }

    // search for give page
    @RequestMapping("/giveResults")
    public String giveSearchResults(Model model,
                                   @RequestParam("giveSearch") String searchString) {
        model.addAttribute("gives", postRepo.findAllByCategoryAndTitleContainsIgnoreCase("Give", searchString));
        return "give";
    }

    // search flash give page
    @RequestMapping("/flashResults")
    public String flashGiveSearchResults(Model model,
                                    @RequestParam("flashSearch") String searchString) {
        model.addAttribute("flashes", postRepo.findAllByCategoryAndTitleContainsIgnoreCase("Flash Give", searchString));
        return "flashGive";
    }


}
