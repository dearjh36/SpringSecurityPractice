package com.project.springsecuritypractice.config;

import com.project.springsecuritypractice.user.User;
import com.project.springsecuritypractice.user.UserRepository;
import com.project.springsecuritypractice.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;

// Security 설정 Config
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // basic authentication
        http.httpBasic().disable(); // basic authentication filter 비활성화
        // csrf
        http.csrf().disable();
        // remember-me
        http.rememberMe().disable();
        // stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // jwt
        // authorization
        http.authorizeRequests()
                // / 와 /home은 모두에게 허용
                .antMatchers("/","/home","signup").permitAll()
                // hello 페이지는 User 룰을 가진 유저에게만 허용
                .antMatchers("/note").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                .anyRequest().authenticated();
        // login
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll(); // 모두 허용
        // logout
        /*
        http.logout()
                .logoutRequestMatcher(new AndRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
                //.deleteCookies()
*/
    }

    @Override
    public void configure(WebSecurity web){
        // 정적 리소스 spring security 대상에서 제외
        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래와 같은 코드
        //web.ignoring().regexMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // UserDetailService 구현
    // @return UserDetailService
    // 우리가 만든 User 엔티티를 가져오는것 (스피링 시큐리티에게 알려준다는 뜻)
    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        return username -> {
            User user = userService.findByUsername(username);
            if(user == null){
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }

}
