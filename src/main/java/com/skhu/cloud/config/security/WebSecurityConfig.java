package com.skhu.cloud.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    } // passwordEncoder 나중에 비밀번호 암호화 할 때 필요하니까 빈으로 생성해놓고 , 싱글톤으로 유지되게끔 한다. (시작할 때 컨테이너에 등록)

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    } // username 확인하고 , 그 다음에 인증을 manage 하는 authenticationManager 도 빈을 주입한다.


    // Spring Security를 사용할 때 H2를 접속하려면 아래 메소드를 작성해주어야 함
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    } // 해당 url , 즉 h2-console 로 시작하는 것은 무시하라는 것 같다.

    /**
     * 여기서 모든 interceptor 를 담당하는 것 같은데,
     * getMapping 만 interceptor 를 배제할 수 없을까?
     * page 는 요청을 해야하니까..
     * 그래도 혹시 이전에는 ~/user/~ 가 아닌 /user/~ 라서 안됐던 것 같기도하다 테스트 해보자.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()//요청에 대한 사용 권한 체크
                .antMatchers("**/admin/**").hasRole("ADMIN")
                .antMatchers("**/user/**").hasRole("USER") // 이 url 에 대해서 인증 요청을 한다는 것이였음
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
    }

}
