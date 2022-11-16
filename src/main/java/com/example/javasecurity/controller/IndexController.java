package com.example.javasecurity.controller;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.javasecurity.auth.PrincipalDetails;
import com.example.javasecurity.domain.User;
import com.example.javasecurity.domain.UserRepository;

@Controller
public class IndexController {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("test/login =====================================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication: " + principalDetails.getUser());
        System.out.println("authentication : " + authentication.getPrincipal());

        System.out.println("authentication Username: " + principalDetails.getUser().getUsername());
        System.out.println("userDetails Username : " + userDetails.getUser().getUsername());
        return "세션정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String oauthlogin(Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("test/oauth2/login =====================================");
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication: " + oauth2User.getAttributes());
        System.out.println("authentication : " + authentication.getPrincipal());

        System.out.println("*****Authentication Attributes: " + oauth2User.getAttributes());
        System.out.println("*****Oauth2User Attributes : " + oauth.getAttributes());

        return "세션정보 확인하기";
    }

    
    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails: *************" + principalDetails.getUser());
        return "user";
    }
    
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        //encode password
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }

}
