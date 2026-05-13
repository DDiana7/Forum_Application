package org.example.forum_application.service;

import org.example.forum_application.model.Answer;
import org.example.forum_application.model.Question;
import org.example.forum_application.model.QuestionStatus;
import org.example.forum_application.repository.AnswerRepository;
import org.example.forum_application.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<Answer> findAll() {
        List<Answer> answers = (List<Answer>) this.answerRepository.findAll();
        return answers;
    }

    public Answer findById(int id) {
        Optional<Answer> answer = answerRepository.findById(Long.valueOf(id));
        if (answer.isPresent()) {
            return answer.get();
        }
        return null;
    }

    public List<Answer> findByQuestionId(int questionId) {
        return answerRepository.findByQuestionId(Long.valueOf(questionId));
    }

    public Answer createAnswer(Answer answer) {
        answer.setCreatedAt(LocalDateTime.now());

        Answer savedAnswer = this.answerRepository.save(answer);

        if (answer.getQuestion() != null && answer.getQuestion().getId() != null) {
            Optional<Question> questionOptional = questionRepository.findById(answer.getQuestion().getId());

            if (questionOptional.isPresent()) {
                Question question = questionOptional.get();

                if (question.getStatus() == QuestionStatus.RECEIVED) {
                    question.setStatus(QuestionStatus.IN_PROGRESS);
                    questionRepository.save(question);
                }
            }
        }

        return savedAnswer;
    }

    public Answer updateAnswer(int id, Answer updatedAnswer) {
        Optional<Answer> answerOptional = answerRepository.findById(Long.valueOf(id));

        if (answerOptional.isPresent()) {
            Answer answer = answerOptional.get();
            answer.setText(updatedAnswer.getText());
            answer.setImageUrl(updatedAnswer.getImageUrl());

            return answerRepository.save(answer);
        }

        return null;
    }

    public void deleteAnswer(Answer answer) {
        answerRepository.delete(answer);
    }

    public void deleteById(int id) {
        answerRepository.deleteById(Long.valueOf(id));
    }
}