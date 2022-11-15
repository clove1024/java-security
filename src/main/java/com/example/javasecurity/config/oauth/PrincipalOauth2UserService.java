package com.example.javasecurity.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.javasecurity.auth.PricipalDetails;
import com.example.javasecurity.model.User;
import com.example.javasecurity.model.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // @Autowired
    // private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getTokenValue: " + userRequest.getAccessToken().getTokenValue());
        System.out.println("userRequest.getClientRegistration: " + userRequest.getClientRegistration());
        
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("userRequest.getAttributes: " + oauth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oauth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String email = oauth2User.getAttribute("email");
        String password = "1234";
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } 
        return  new PricipalDetails(userEntity, oauth2User.getAttributes());

        // return super.loadUser(userRequest);

    }
}
