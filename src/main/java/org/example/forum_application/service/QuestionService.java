package org.example.forum_application.service;

import org.example.forum_application.model.Question;
import org.example.forum_application.model.QuestionStatus;
import org.example.forum_application.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> findAll() {
        List<Question> questions =  (List<Question>)this.questionRepository.findAll();
        return questions;
    }

    public Question findById(int id) {
        Optional<Question> question =  questionRepository.findById(Long.valueOf(id));
        if(question.isPresent()) {
            return question.get();
        }
        return null;
    }

    // save()
    public Question create(Question question) {

        question.setCreatedAt(LocalDateTime.now());
        question.setStatus(QuestionStatus.RECEIVED);

        return this.questionRepository.save(question);
    }

    public Question update(int id, Question updatedQuestion) {
        Optional<Question> questionOptional =  questionRepository.findById(Long.valueOf(id));

        if(questionOptional.isPresent()) {
            Question questionToUpdate = questionOptional.get();
            questionToUpdate.setTitle(updatedQuestion.getTitle());
            questionToUpdate.setStatus(updatedQuestion.getStatus());
            questionToUpdate.setAuthor(updatedQuestion.getAuthor());
            questionToUpdate.setText(updatedQuestion.getText());
            questionToUpdate.setImageUrl(updatedQuestion.getImageUrl());

            return questionRepository.save(questionToUpdate);

        }
        return null;
    }

    public void deleteQuestion(Question question) {

        this.questionRepository.delete(question);
    }

    public void deleteById(int id) {

        questionRepository.deleteById(Long.valueOf(id));
    }







}
