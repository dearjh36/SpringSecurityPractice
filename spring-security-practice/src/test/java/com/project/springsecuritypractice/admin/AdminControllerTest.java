package com.project.springsecuritypractice.admin;

import com.project.springsecuritypractice.user.User;
import com.project.springsecuritypractice.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class AdminControllerTest {

    @Autowired
    private UserRepository userRepository;
    private MockMvc mockMvc;
    private User user;
    private User admin;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity()) // spring security 적용
                .alwaysDo(print())
                .build();

        // ROLE_USER 권한이 있는 유저 생성
        user = userRepository.save(new User("user1", "user1", "ROLE_USER"));
        // ROLE_ADMIN 권한이 있는 관리자 생성
        admin = userRepository.save(new User("admin1", "admin1", "ROLE_ADMIN"));

    }

    @Test
    void getNoteForAdmin_인증없음() throws Exception{
        mockMvc.perform(get("/admin").with(csrf())) // csrf 토큰 추가
                .andExpect(redirectedUrlPattern("**/login"))
                .andExpect(status().is3xxRedirection()); // login이 안되어있으므로 로그인 페이지로 redirect
    }

    @Test
    void getNoteForAdmin_어드민인증있음() throws Exception{
        mockMvc.perform(get("/admin").with(csrf()).with(user(admin)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNoteForAdmin_유저인증있음() throws Exception{
        mockMvc.perform(get("/admin").with(csrf()).with(user(user)))
                .andExpect(status().isForbidden()); // 접근 거부
    }
}