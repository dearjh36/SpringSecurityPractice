package com.project.springsecuritypractice.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void signup(){

        // Given
        String username = "user1";
        String pw = "user1";

        // When
        User user = userService.signup(username,pw);

        // Then
        then(user.getId()).isNotNull(); // id가 NotNull 인지
        then(user.getUsername()).isEqualTo("user1"); //유저이름이 'user1' 인지 검증
        then(user.getPassword()).startsWith("{bcrypt}"); // 패스워드가 {bcrypt}로 시작하는지 검증
        then(user.getAuthorities()).hasSize(1); // Authorities(권한)이 1개인기 검증 (admin Or User)
        then(user.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_USER");
        then(user.isAdmin()).isFalse(); // 어드민이 False인지 검증
        then(user.isAccountNonExpired()).isTrue(); // 사용자의 계정이 만료되어있는지 확인 (로그인 자격이 유효한지)
        then(user.isAccountNonLocked()).isTrue();
        then(user.isEnabled()).isTrue(); // 활성화인지 비활성화인지
        then(user.isCredentialsNonExpired()).isTrue(); // 인증 자격, 즉 비밀번호 만료 확인

    }

    @Test
    void signupAdmin(){
        // Given
        String username = "admin1";
        String pw = "admin12";

        // When
        User user = userService.signupAdmin(username,pw);

        // Then
        then(user.getId()).isNotNull();
        then(user.getUsername()).isEqualTo("admin1");
        then(user.getPassword()).startsWith("{bcrypt}");
        then(user.getAuthorities()).hasSize(1);
        then(user.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_ADMIN");
        then(user.isAdmin()).isTrue();
        then(user.isAccountNonExpired()).isTrue();
        then(user.isAccountNonLocked()).isTrue();
        then(user.isEnabled()).isTrue();
        then(user.isCredentialsNonExpired()).isTrue();

    }

    @Test
    void findByUsername(){
        // Given
        userRepository.save(new User("user1","user1","ROLE_USER"));

        // When
        User user = userService.findByUsername("user1");

        // Then
        then(user.getId()).isNotNull();

    }


}