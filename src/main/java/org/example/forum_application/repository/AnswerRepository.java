package org.example.forum_application.repository;

import org.example.forum_application.model.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface AnswerRepository extends CrudRepository<Answer, Long> {
        List<Answer> findByQuestionId(Long questionId);

    }

