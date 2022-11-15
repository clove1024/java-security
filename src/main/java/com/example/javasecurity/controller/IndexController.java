package com.example.javasecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.javasecurity.auth.PricipalDetails;
import com.example.javasecurity.model.User;
import com.example.javasecurity.model.UserRepository;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
        @AuthenticationPrincipal PricipalDetails userDetails) {
        PricipalDetails pricipalDetails = (PricipalDetails) authentication.getPrincipal();
        System.out.println("authentication: " + pricipalDetails.getUser());
        System.out.println("pricipalDetails.getUser(): " + userDetails.getUser());
        return "세션 정보 확인하기";
    }
    
    @GetMapping({"/", ""})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PricipalDetails pricipalDetails) {
        System.out.println("pricipalDetails: " + pricipalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/loginForm")
    public String loginFrom() {
        return "loginForm";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);
        System.out.println(user);
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
        return "주요정보 데이터 공개";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth2User Login  =====================================");
        System.out.println("authentication: " + oauth2User.getAttributes());
        System.out.println("oauth2User.getAttributes(): " + oauth.getAttributes());
        System.out.println("oauth2User.getAttributes(): " + oauth.getAttributes().get("name"));
        System.out.println("oauth2User.getAttributes(): " + oauth.getAttributes().get("email"));
        return "OAuth 세션 정보 확인하기";
    }

}
