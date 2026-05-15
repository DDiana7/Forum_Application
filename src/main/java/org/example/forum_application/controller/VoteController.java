package org.example.forum_application.controller;

import org.example.forum_application.model.Answer;
import org.example.forum_application.model.Question;
import org.example.forum_application.model.VoteType;
import org.example.forum_application.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
@CrossOrigin(origins = "http://localhost:4200")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/question/{questionId}")
    public Integer voteQuestion(
            @PathVariable Long questionId,
            @RequestParam Long userId,
            @RequestParam VoteType voteType
    ) {
        Question question = voteService.voteQuestion(questionId, userId, voteType);

        if (question == null) {
            throw new RuntimeException("Vote not allowed");
        }

        return question.getVoteScore();
    }

    @PostMapping("/answer/{answerId}")
    public Integer voteAnswer(
            @PathVariable Long answerId,
            @RequestParam Long userId,
            @RequestParam VoteType voteType
    ) {
        Answer answer = voteService.voteAnswer(answerId, userId, voteType);

        if (answer == null) {
            return null;
        }

        return answer.getVoteScore();
    }
}
