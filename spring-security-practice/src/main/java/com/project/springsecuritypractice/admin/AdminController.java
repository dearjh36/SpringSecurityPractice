package com.project.springsecuritypractice.admin;

import com.project.springsecuritypractice.note.Note;
import com.project.springsecuritypractice.note.NoteService;
import com.project.springsecuritypractice.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final NoteService noteService;

    // 어드민의 경우 노트 조회
    // @return admin/index.html
    @GetMapping
    public String getNoteForAdmin(Authentication authentication, Model model){

        User user = (User) authentication.getPrincipal();
        List<Note> notes = noteService.findByUser(user);
        model.addAttribute("notes", notes);
        return "admin/index";

    }

}
