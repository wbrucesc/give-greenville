package com.will.givegreenville.controllers;

import com.will.givegreenville.models.Post;
import com.will.givegreenville.models.User;
import com.will.givegreenville.repositories.ConsiderationRepository;
import com.will.givegreenville.repositories.PostRepository;
import com.will.givegreenville.repositories.UserRepository;
import com.will.givegreenville.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ws.rs.Path;
import java.security.Principal;

@Controller
public class ModeratorController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private ConsiderationRepository considerRepo;

    @Autowired
    private StorageService storageService;

    // method that allows moderator to mark a post as flagged
    @RequestMapping(value = "/flag/{postId}", method = RequestMethod.POST)
    public String markAsFlagged(@PathVariable("postId") long postId,
                                Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        Post targetPost = postRepo.findOne(postId);
        if (principal != null && me.getRole().getName().equals("ROLE_MODERATOR")) {
            targetPost.setActive(false);
            targetPost.setFlagged(true);
            postRepo.save(targetPost);
            return "redirect:/";
        }
        return "redirect:/";
    }


    // flagged posts page lists all posts marked as flagged by moderator
    @RequestMapping("/flagged")
    public String flaggedPosts(Model model,
                               Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", me);
        model.addAttribute("flagged", postRepo.findAllByFlaggedTrueOrderByCreated());
        return "flagged";
    }

}
