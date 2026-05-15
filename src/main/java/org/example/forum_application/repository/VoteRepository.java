package org.example.forum_application.repository;

import org.example.forum_application.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {

    Optional<Vote> findByUser_IdAndQuestion_Id(Long userId, Long questionId);
    Optional<Vote> findByUser_IdAndAnswer_Id(Long userId, Long answerId);
    void deleteByQuestion_Id(Long questionId);
    void deleteByAnswer_Id(Long answerId);
}
