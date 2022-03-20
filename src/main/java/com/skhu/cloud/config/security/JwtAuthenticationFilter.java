package com.skhu.cloud.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * interceptor 로 thymeleaf 에서 반환이 된 , application/x-www-form-urlencoded 형태의 request 에서
     * getInputStream을 할 때 , 에러가 발생한다.
     * 그래서 getInputStream 을 잘 오버라이딩 해서 정의하면 괜찮을 것 같다.
     *
     * 이유는 spring tomcat 개발자들이 filter 에서 inputStream 으로 한번 읽게되면 다시 못 읽게 해서 문제가 발생하는 것이였다.
     * 문제 해결은 getInputStream 을 한번 더 가져올 수 있게끔 하는 것이다.
     *
     * 해결 방법은 모두가 HttpServletWrapper 를 override 해서 변경하는 것이라고 하였다.
     * 그래서 사이트들을 보고 한번 override를 통해서 바꿔보자.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Header에서 JWT를 받아옴
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 유효한 Token인지 확인 (만료 시간을 이용해서 확인한다.)
        if (token != null && jwtTokenProvider.validateToken(token)) {

            System.out.println("Authentication token!"); // 당연히 signup시에 될리가 없음
            // Token이 유효하면 이로부터 유저 정보를 받아옴
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // SecurityContext에 Authentication 객체를 저장함 , 가장 최종 단계이다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 필터에다가 다시 응답과 , 요청을 등록함
        // json 아니면 에러남
        chain.doFilter(request, response);
    }
}