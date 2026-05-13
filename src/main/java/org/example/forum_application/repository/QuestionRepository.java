package org.example.forum_application.repository;

import org.example.forum_application.model.Question;
import org.example.forum_application.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findAllByOrderByCreatedAtDesc();
}
