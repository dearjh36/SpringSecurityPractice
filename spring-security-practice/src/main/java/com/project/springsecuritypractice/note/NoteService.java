package com.project.springsecuritypractice.note;

import com.project.springsecuritypractice.user.User;
import com.project.springsecuritypractice.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    /*
    *  노트 조회
    *  유저는 본인의 노트만을 조회 가능
    *  어드민은 모든 노트를 조회 가능
    *
    *  @param user 노트를 찾을 유저
    *  @return 유저가 조회할 수 있는 모든 노트 List
    *
    * */

    @Transactional(readOnly = true)
    public List<Note> findByUser(User user){
        if(user == null){
            throw new UserNotFoundException();
        }
        if (user.isAdmin()){
            return noteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
        return noteRepository.findByUserOrderByIdDesc(user);
    }

    /*
    * 노트 저장
    *
    * @param user       노트 저장하는 유저
    * @param title      제목
    * @param content    내용
    * @return 저장된 노트
    * */
    public Note saveNote(User user, String title, String content){
        if(user == null){
            throw new UserNotFoundException();
        }
        return noteRepository.save(new Note(title,content,user));
    }

    /*
    * 노트 삭제
    *
    * @param user   삭제하려는 노트의 유저
    * @param noteId 삭제하려는 노트 ID
    * */
    public void deleteNote(User user, Long noteId){
        if(user == null){
            throw new UserNotFoundException();
        }
        Note note = noteRepository.findByIdAndUser(noteId, user);
        if (note != null) {
            noteRepository.delete(note);
        }
    }



}
