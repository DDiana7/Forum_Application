package org.example.forum_application.controller;


import org.example.forum_application.model.Question;
import org.example.forum_application.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/getAll")
    public List<Question> getAllQuestions(){
        return this.questionService.findAll();
    }

    @GetMapping("/getById/{id}")
    public Question getQuestionById(@PathVariable int id){
        return questionService.findById(id);
    }

    @PostMapping("/add")
    public Question addQuestion(@RequestBody Question question){
        return this.questionService.create(question);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteQuestion(@PathVariable int id){
        questionService.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public Question updateQuestion(@PathVariable int id, @RequestBody Question question){
        return this.questionService.update(id, question);
    }
}
