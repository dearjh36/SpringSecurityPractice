package com.project.springsecuritypractice.config;

import com.project.springsecuritypractice.note.NoteService;
import com.project.springsecuritypractice.notice.NoticeService;
import com.project.springsecuritypractice.user.User;
import com.project.springsecuritypractice.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// 초기 상태 등록 Config
// h2 디비를 사용하여 서버가 꺼지면 데이터가 전부 사라짐
// test 할 때마다 등록하기 번거롭기 때문에 기본 데이터 등록
// 오라클처럼 디스크 기반의 다른 디비를 사용할 시에는 해당 config 필요 없음
@Configuration
@RequiredArgsConstructor
@Profile(value = "!test")
public class InitializeDefaultConfig {

    private final UserService userService;
    private final NoteService noteService;
    private final NoticeService noticeService;

    // 유저 등록, note 4개 등록
    @Bean
    public void initializeDefaultUser(){

        User user = userService.signup("user1", "user1");
        noteService.saveNote(user, "테스트", "테스트입니다.");
        noteService.saveNote(user, "테스트2", "테스트2입니다.");
        noteService.saveNote(user, "테스트3", "테스트3입니다.");
        noteService.saveNote(user, "여름 여행계획", "여름 여행계획 작성중...");

    }

    // 어드민 등록, notice 2개 등록
    @Bean
    public void initializeDefaultAdmin(){

        userService.signupAdmin("admin1", "admin1");
        noticeService.saveNotice("환영합니다.", "환영합니다 여러분");
        noticeService.saveNotice("노트 작성 방법 공지", "1. 회원가입\n2. 로그인\n3. 노트 작성\n4. 저장\n* 본인 외에는 게시글을 볼 수 없습니다.");

    }

}
