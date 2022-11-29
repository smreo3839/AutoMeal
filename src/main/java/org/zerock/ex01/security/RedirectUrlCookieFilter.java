package org.zerock.ex01.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//프론트에서 받은 리다이렉트 주소를 저장하기 위해 서버 쿠키를 이용 ( 세션을 사용하는법도 있지만 세션은 서버에 저장됨)
@Slf4j
@Component
public class RedirectUrlCookieFilter extends OncePerRequestFilter {
    public static final String REDIRECT_URI_PARAM="redirect_url";
    private static  final int MAX_AGE = 180;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

            if(request.getRequestURI().startsWith("/auth/authorize")){
                try{
                    log.info("request url{}",request.getRequestURI());
                    String redirectUrl= request.getParameter(REDIRECT_URI_PARAM);//redirect_url 를 가져옴

                    Cookie cookie=new Cookie(REDIRECT_URI_PARAM,redirectUrl);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge(MAX_AGE);
                    response.addCookie(cookie);
                }catch(Exception ex){
                    logger.error("Could not set user authentication in security context",ex);
                    log.info("비인가 리퀘스트");
                }
            }
        filterChain.doFilter(request,response);
    }


}
