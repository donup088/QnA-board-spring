package com.myQnA.QnAboardspring.controller;

import com.myQnA.QnAboardspring.domain.*;
import com.myQnA.QnAboardspring.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("")
    public Answer answerCreate(@PathVariable Long questionId, String content, HttpSession session) {
        if (!HttpSessionUtils.isLogin(session)) {
            return null;
        }

        Question question = questionRepository.findById(questionId).get();
        Answer answer = new Answer(HttpSessionUtils.getUserFromSession(session), question, content);
        question.addAnswer();

        return answerRepository.save(answer);

    }

    @DeleteMapping("/{id}")
    public Result deleteAnswer(@PathVariable Long questionId,@PathVariable Long id, HttpSession session) {
        if (!HttpSessionUtils.isLogin(session)) {
            return Result.fail("로그인해야 합니다.");
        }

        Answer answer = answerRepository.findById(id).get();
        User loginUser = HttpSessionUtils.getUserFromSession(session);

        if (!answer.isSameWriter(loginUser)) {
            return Result.fail("자신의 글만 삭제할 수 있습니다.");
        }

        answerRepository.delete(answer);

        Question question=questionRepository.findById(questionId).get();
        question.deleteAnswer();
        questionRepository.save(question);

        return Result.ok();
    }
}
