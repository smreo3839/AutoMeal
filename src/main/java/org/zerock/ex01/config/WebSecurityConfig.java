package org.zerock.ex01.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;
import org.zerock.ex01.security.JwtAuthenticationFilter;
import org.zerock.ex01.security.OAuthSuccessHandler;
import org.zerock.ex01.security.OAuthUserServiceImpl;
import org.zerock.ex01.security.RedirectUrlCookieFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //해줘 ~~
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuthUserServiceImpl oAuthUserService;// 내가 만든 서비스

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;// Success handler

    @Autowired
    private RedirectUrlCookieFilter redirectUrlCookieFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http 시큐리티 빌더 web.xml 대신 HttpSecurity를 사용하여 시큐리티 관련 설정을 하는 것임
        http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
                .and()
                .csrf()// csrf는 현재 사용하지 않으므로 disable
                .disable()
                .httpBasic()// token을 사용하므로 basic 인증 disable
                .disable()
                .sessionManagement()  // session 기반이 아님을 선언
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // /와 /auth/** 경로는 인증 안해도 됨.
                //.antMatchers("/", "/auth/**","/shop/**","/CustomRecipe/**","/RecipeDB/**").permitAll()
                .antMatchers("/", "/auth/**","/oauth2/**","/shop/**","/RecipeDB/nser/**","/CustomRecipeReply/nser/**","/MealPlan/nser/**").permitAll()
                .anyRequest() // /와 /auth/**이외의 모든 경로는 인증 해야됨.
                .authenticated()
                .and()
                .oauth2Login()//oauth2Login 설정
                .redirectionEndpoint()
                .baseUri("/oauth2/callback")//callback uri 설정
                .and()
                .authorizationEndpoint()
                .baseUri("/auth/authorize")//OAuth 2.0흐름을 위한 엔드포인트 추가
                .and()
                .userInfoEndpoint()
                .userService(oAuthUserService)//oAuthUserServiceImpl를 유저 서비스로 등록
                .and()
                .successHandler(oAuthSuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());

        // filter 등록.
        // 매 요청마다
        // CorsFilter 실행한 후에
        // jwtAuthenticationFilter 실행한다.
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
        http.addFilterAfter(
                redirectUrlCookieFilter,
                OAuth2AuthorizationRequestRedirectFilter.class//리다이렉트 되기전에 필터를 실행 시킴
        );
    }
}
