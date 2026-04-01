package org.example.forum_application.controller;

import org.example.forum_application.model.Answer;
import org.example.forum_application.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/getAll")
    public List<Answer> getAllAnswers() {
        return this.answerService.findAll();
    }

    @GetMapping("/getById/{id}")
    public Answer getAnswerById(@PathVariable int id) {
        return answerService.findById(id);
    }

    @GetMapping("/question/{questionId}")
    public List<Answer> getAnswerByQuestion(@PathVariable int questionId) {
        return answerService.findByQuestionId(questionId);
    }

    @PostMapping("/add")
    public Answer createAnswer(@RequestBody Answer answer) {
        return this.answerService.createAnswer(answer);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAnswer(@PathVariable int id) {
         answerService.deleteById(id);

    }

    @PutMapping("/update/{id}")
    public Answer updateAnswer(@PathVariable int id, @RequestBody Answer answer) {
        return this.answerService.updateAnswer(id, answer);
    }

}
