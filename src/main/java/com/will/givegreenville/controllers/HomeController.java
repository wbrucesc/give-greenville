package com.will.givegreenville.controllers;

import com.will.givegreenville.interfaces.GeoCode;
import com.will.givegreenville.models.*;
import com.will.givegreenville.repositories.ConsiderationRepository;
import com.will.givegreenville.repositories.PostRepository;
import com.will.givegreenville.repositories.UserRepository;
import com.will.givegreenville.storage.StorageService;
import feign.Feign;
import feign.gson.GsonDecoder;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private ConsiderationRepository considerRepo;

    @Autowired
    private StorageService storageService;


    // home page lists recent posts in order of date created (newest to oldest) ***
    @RequestMapping("/")
    public String index(Model model,
                        Principal principal) {

        if (principal != null){
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("posts", postRepo.findAllByActiveIsTrueOrderByCreatedDesc());
            List<Post> listOfPosts = postRepo.findAllByCompletedIsTrueOrderByCreatedDesc();
            List<Post> subPosts = listOfPosts.subList(0, 5);
            model.addAttribute("completed", subPosts);
            return "index";
        }
        model.addAttribute("posts", postRepo.findAllByActiveIsTrueOrderByCreatedDesc());
        List<Post> listOfPosts = postRepo.findAllByCompletedIsTrueOrderByCreatedDesc();
        List<Post> subPosts = listOfPosts.subList(0, 5);
        model.addAttribute("completed", subPosts);
        return "index";
    }

    // about page ***
    @RequestMapping("/about")
    public String about(Model model,
                        Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            return "about";
        }
        return "about";
    }

    // ask page lists all ASK posts ***
    @RequestMapping("/ask")
    public String askPage(Model model,
                          Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("asks", postRepo.findAllByActiveIsTrueAndCategoryOrderByCreatedDesc("Ask"));
            return "ask";
        }
        model.addAttribute("asks", postRepo.findAllByActiveIsTrueAndCategoryOrderByCreatedDesc("Ask"));
        return "ask";
    }

    // give page lists all posts with category of GIVE ***
    @RequestMapping("/give")
    public String givePage(Model model,
                           Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("gives", postRepo.findAllByActiveIsTrueAndCategoryOrderByCreatedDesc("Give"));
            return "give";
        }
        model.addAttribute("gives", postRepo.findAllByActiveIsTrueAndCategoryOrderByCreatedDesc("Give"));
        return "give";
    }

    // flash give page lists ALL FLASH GIVE posts ***
    @RequestMapping("/flash")
    public String flashPage(Model model,
                            Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("flashes", postRepo.findAllByActiveIsTrueAndCategoryOrderByCreatedDesc("Flash Give"));
            return "flashGive";
        }
        model.addAttribute("flashes", postRepo.findAllByActiveIsTrueAndCategoryOrderByCreatedDesc("Flash Give"));
        return "flashGive";
    }

    // takes you to CREATE GENERAL POST FORM ***
    @RequestMapping("/create")
    public String createForm(Model model,
                             Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("post", new Post());
            return "create";
        }
        model.addAttribute("post", new Post());
        return "create";
    }

    // creates new GENERAL (ask, give, flash give) post
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createPost(@ModelAttribute Post post,
                             @RequestParam("file") MultipartFile file,
                             Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        storageService.store(file);
        String fileName = file.getOriginalFilename();
        post.setImagePath(fileName);
        post.setAuthor(me);
        post.setCreated(new Date());
        post.setActive(true);
        postRepo.save(post);
        return "redirect:/";
    }

    // takes you to create ASK POST FORM ***
    @RequestMapping("/createAsk")
    public String askForm(Model model,
                          Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("post", new Post());
            return "createAsk";
        }
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

    // takes you to create GIVE POST FORM ***
    @RequestMapping("/createGive")
    public String giveForm(Model model,
                           Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("post", new Post());
            return "createGive";
        }
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

    // takes you to create FLASH GIVE FORM ***
    @RequestMapping("/createFlash")
    public String flashForm(Model model,
                            Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("post", new Post());
            return "createFlash";
        }
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


    // takes you to create a CONSIDERATION page ***
    @RequestMapping("/consider/{postId}")
    public String considerForm(Model model,
                               @PathVariable("postId") long postId,
                               Principal principal) {

        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            model.addAttribute("consideration", new Consideration());
            model.addAttribute("postId", postId);
            return "consider";
        }
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
        if (userConsideration == null || post.getAuthor() != me) {
            consideration.setUser(me);
            consideration.setPost(post);
            consideration.setCreated(new Date());
            considerRepo.save(consideration);
            return "redirect:/";
        }
        return "redirect:/";
    }

    // POST DETAIL page LISTS post info and ALL CONSIDERATIONS for that post ***
    @RequestMapping("/detail/{postId}")
    public String detail(Model model,
                         @PathVariable("postId") long postId,
                         Principal principal) {
        if (principal != null) {
            Post targetPost = postRepo.findOne(postId);
            model.addAttribute("post", targetPost);
            model.addAttribute("considerations", targetPost.getConsiderations());
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
//        System.out.println("Author is: " + targetPost.getAuthor());

            Key geokey = new Key();
            MapKey mapKey = new MapKey();
            GeoCode geoCode = Feign.builder()
                    .decoder(new GsonDecoder())
                    .target(GeoCode.class, "https://maps.googleapis.com");

            // Reverse geocoding zip code from a post to get latitude and longitude to plug into static map url

            GeoCodeResults geoCodeResults = geoCode.geoCodeResults(targetPost.getLocation(), geokey.getGEO_KEY());
            double postLat = geoCodeResults.getResults().get(0).getGeometry().getLocation().getLat();
            double postLng = geoCodeResults.getResults().get(0).getGeometry().getLocation().getLng();

            String address = geoCodeResults.getResults().get(0).getFormatted_address();
            model.addAttribute("formattedAddress", address);


            String locationUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=12&size=400x400&maptype=roadmap&markers=color:green%7C" + postLat + "," + postLng + "&key=" + mapKey;
            model.addAttribute("map", locationUrl);
            model.addAttribute("chosen", targetPost.getRecipient());
            return "detail";
        }
        Post targetPost = postRepo.findOne(postId);
        model.addAttribute("post", targetPost);
        model.addAttribute("considerations", targetPost.getConsiderations());

        Key geokey = new Key();
        MapKey mapKey = new MapKey();
        GeoCode geoCode = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeoCode.class, "https://maps.googleapis.com");

        // Reverse geocoding zip code from a post to get latitude and longitude to plug into static map url

        GeoCodeResults geoCodeResults = geoCode.geoCodeResults(targetPost.getLocation(), geokey.getGEO_KEY());
        double postLat = geoCodeResults.getResults().get(0).getGeometry().getLocation().getLat();
        double postLng = geoCodeResults.getResults().get(0).getGeometry().getLocation().getLng();

        String address = geoCodeResults.getResults().get(0).getFormatted_address();
        model.addAttribute("formattedAddress", address);


        String locationUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=12&size=400x400&maptype=roadmap&markers=color:green%7C" + postLat + "," + postLng + "&key=" + mapKey;
        model.addAttribute("map", locationUrl);
        model.addAttribute("chosen", targetPost.getRecipient());
        return "detail";
    }

    // search for index page
    @RequestMapping("/indexResults")
    public String searchResults(Model model,
                                @RequestParam("search") String searchString) {
        model.addAttribute("posts", postRepo.findAllByActiveIsTrueAndTitleContainsIgnoreCase(searchString));
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

    // page that lists all posts created by logged in user ***
    @RequestMapping("/myPosts")
    public String myPostsPage(Model model,
                              Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", me);
        model.addAttribute("myPosts", postRepo.findAllByAuthorAndActiveIsTrueOrderByConsiderationsDesc(me));
        model.addAttribute("completed", postRepo.findAllByAuthorAndCompletedIsTrueOrderByCreatedDesc(me));
        return "myPosts";
    }

    // takes you to edit form for selected post from myPosts page ***
    @RequestMapping("/edit/post/{id}")
    public String editForm(Model model,
                           @PathVariable("id") long id,
                           Principal principal) {
        if (principal != null) {
            User me = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", me);
            Post myPost = postRepo.findOne(id);
            if (myPost.getAuthor() == me && myPost.isActive()) {
                model.addAttribute("post", myPost);
                return "update";
            }
            return "redirect:/";
        }
        return "redirect:/";

    }

    // updates post
    @RequestMapping(value = "/edit/post/{id}", method = RequestMethod.POST)
    public String updatePost(@ModelAttribute Post post,
                             Principal principal) {
        User me = userRepo.findByUsername(principal.getName());
        post.setAuthor(me);
        post.setCreated(new Date());
        postRepo.save(post);
        return "redirect:/myPosts";
    }

    // takes you to delete confirmation page for selected post ***
    @RequestMapping("/delete/post/{id}")
    public String postDeleteConfirm(Model model,
                                    @PathVariable("id") long id,
                                    Principal principal) {

        User me = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", me);
        Post postToDelete = postRepo.findOne(id);
        model.addAttribute("post", postToDelete);
        return "confirmDelete";
    }

    // disables post
    @RequestMapping(value = "/delete/post/{id}", method = RequestMethod.POST)
    public String deletePost(@PathVariable("id") long id) {
        Post postToDelete = postRepo.findOne(id);
        postToDelete.setActive(false);
        postRepo.save(postToDelete);
        return "redirect:/myPosts";
    }

    // to grab random recipient/user from list of considerations on a post
    @RequestMapping(value = "/choose/post/{id}")
    public String chooseRecipient(@PathVariable("id") long id,
                                  Model model,
                                  @ModelAttribute Post post) {

        Post targetPost = postRepo.findOne(id);

        List<Consideration> considerations = targetPost.getConsiderations();
//        System.out.println("Considerations: " + considerations);

        Random rand = new Random();
        Consideration randomConsideration = considerations.get(rand.nextInt(considerations.size()));
        User recipient = randomConsideration.getUser();

//        System.out.println(recipient);

        targetPost.setActive(false);
        targetPost.setRecipient(recipient);
        targetPost.setCompleted(true);
        postRepo.save(targetPost);
        model.addAttribute("chosen", recipient);
        String sendTo = recipient.getEmail();
        String replyTo = targetPost.getAuthor().getEmail();
        String title = targetPost.getTitle();
        System.out.println(sendTo);
        SendSimpleMessage(sendTo, replyTo, title);

        return "redirect:/detail/{id}";
    }

    // choose specific user instead of random
    @RequestMapping("/pick/{postId}/{userId}")
    public String chooseSpecific(Model model,
                                 @PathVariable("postId") long postId,
                                 @PathVariable("userId") long userId) {

        Post targetPost = postRepo.findOne(postId);
        User recipient = userRepo.findOne(userId);
        targetPost.setRecipient(recipient);
        String sendTo = recipient.getEmail();
        String replyTo = targetPost.getAuthor().getEmail();
        String title = targetPost.getTitle();
        SendSimpleMessage(sendTo, replyTo, title);
        targetPost.setActive(false);
        targetPost.setCompleted(true);
        postRepo.save(targetPost);
        model.addAttribute("chosen", recipient);

        return "redirect:/detail/{postId}";
    }

    // configuration for mailgun
    public static void SendSimpleMessage(String sendTo, String replyTo, String title) {
        Configuration configuration = new Configuration()
                .domain("mg.willbruce.fun")
                .apiKey(System.getenv("mgkey"))
                .from("Give Greenville", "mailgun@mg.willbruce.fun");

        Mail.using(configuration)
                .to(sendTo)
                .subject("Give Greenville Notification")
                .text("You were chosen on Give Greenville's Post" + "'" + title + "'" +  "Email the user at " + replyTo + " to coordinate further.")
                .build()
                .send();
    }
}
