package org.example.forum_application.service;

import org.example.forum_application.model.*;
import org.example.forum_application.repository.AnswerRepository;
import org.example.forum_application.repository.QuestionRepository;
import org.example.forum_application.repository.UserRepository;
import org.example.forum_application.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public VoteService(
            VoteRepository voteRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            UserRepository userRepository
    ) {
        this.voteRepository = voteRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }



    public Answer voteAnswer(Long answerId, Long userId, VoteType voteType) {
        Answer answer = answerRepository.findById(answerId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (answer == null || user == null) return null;
        if (answer.getAuthor().getId().equals(userId)) return null;

        Optional<Vote> existingVote =
                voteRepository.findByUser_IdAndAnswer_Id(userId, answerId);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();

            // acelasi vot — blocheaza
            if (vote.getVoteType() == voteType) return null;

            // vot diferit — actualizeaza
            int diff = (voteType == VoteType.UPVOTE) ? 2 : -2;
            answer.setVoteScore(answer.getVoteScore() + diff);
            vote.setVoteType(voteType);
            voteRepository.save(vote);
            return answerRepository.save(answer);
        }

        // primul vot
        Vote vote = new Vote(user, null, answer, voteType);
        voteRepository.save(vote);

        int currentScore = answer.getVoteScore() == null ? 0 : answer.getVoteScore();
        answer.setVoteScore(currentScore + (voteType == VoteType.UPVOTE ? 1 : -1));

        return answerRepository.save(answer);
    }



    public Question voteQuestion(Long questionId, Long userId, VoteType voteType) {
        Question question = questionRepository.findById(questionId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (question == null || user == null) return null;
        if (question.getAuthor().getId().equals(userId)) return null;

        Optional<Vote> existingVote =
                voteRepository.findByUser_IdAndQuestion_Id(userId, questionId);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();

            // acelasi vot — blocheaza
            if (vote.getVoteType() == voteType) return null;

            // vot diferit — actualizeaza
            int diff = (voteType == VoteType.UPVOTE) ? 2 : -2;
            question.setVoteScore(question.getVoteScore() + diff);
            vote.setVoteType(voteType);
            voteRepository.save(vote);
            return questionRepository.save(question);
        }

        // primul vot
        Vote vote = new Vote(user, question, null, voteType);
        voteRepository.save(vote);

        int currentScore = question.getVoteScore() == null ? 0 : question.getVoteScore();
        question.setVoteScore(currentScore + (voteType == VoteType.UPVOTE ? 1 : -1));

        return questionRepository.save(question);
    }


}
