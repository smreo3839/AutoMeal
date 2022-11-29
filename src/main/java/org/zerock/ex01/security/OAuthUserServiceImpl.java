package org.zerock.ex01.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.UserRepository;
import org.zerock.ex01.service.UserService;

import java.util.Map;

@Slf4j
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    public OAuthUserServiceImpl(){
        super();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)throws OAuth2AuthorizationException{
        //부모의 메소드인 loadUser를 호출한다.
        final OAuth2User oAuth2User =super.loadUser(userRequest);//properties에 있는 user-info-uri를 이용해 사용자 정보 가져옴

        try{
            log.info("OAuthUser attributes {}",new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        final String authProvider=userRequest.getClientRegistration().getClientName();//누가 접근했는지 카카오 구글 등
        //가져올 필드를 가져옴 여기서 다중 소셜로그인 가입시 알맞게 파싱하긴 해야함
//        final String nickName=(String) oAuth2User.getAttributes().get("properties");
//        final String image=(String) oAuth2User.getAttributes().get("thumbnail_image");
//        final String email=(String) oAuth2User.getAttributes().get("account_email");

        log.info("Auth provider {}",authProvider);

        String email=null;
        String nickName=null;
        String image=null;
        String phoneNum=null;

        if(authProvider.equals("Google")){
            email =(String) oAuth2User.getAttributes().get("email");
            nickName=(String) oAuth2User.getAttributes().get("name");
            image=(String)oAuth2User.getAttributes().get("picture");
        }

        if(authProvider.equals("kakao")){
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakao_account.get("email");
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            nickName = (String) properties.get("nickname");
            image =(String) properties.get( "thumbnail_image");
        }

        if(authProvider.equals("Naver")){
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("response");
            email = (String) naverAccount.get("email");
            nickName = (String) naverAccount.get("nickname");
            phoneNum= (String)naverAccount.get("mobile");
            image = (String) naverAccount.get("profile_image");
        }

        log.info("nickName: {}", nickName);
        log.info("image: {}", image);
        log.info("email {}",email);
        log.info("authProvider: {}", authProvider);
        User userEntity=null;

        //유저가 존재하지 않으면 회원가입
        if(!userRepository.existsByUserEmail(email)){
            userEntity=User.builder()
                    .userEmail(email)
                    .nickName(nickName)
                    .img(image)
                    .phoneNum(phoneNum)
                    .build();
            userRepository.save(userEntity);
        }else{
            userEntity=userRepository.findByUserEmail(email);
        }
        log.info("Successfully pulled user info userEmail {} authProvider {}",email,authProvider);

       // return oAuth2User;
        return new ApplicationOAuth2User(userEntity.getUserEmail(),oAuth2User.getAttributes());
    }
}
