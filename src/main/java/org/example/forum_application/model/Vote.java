package org.example.forum_application.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnore
    private Question question;

    @ManyToOne
    @JsonIgnore
    private Answer answer;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    public Vote() {}

    public Vote(User user, Question question, Answer answer, VoteType voteType) {
        this.user = user;
        this.question = question;
        this.answer = answer;
        this.voteType = voteType;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }
}