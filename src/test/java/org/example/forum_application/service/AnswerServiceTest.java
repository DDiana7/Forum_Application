package org.example.forum_application.service;

import org.example.forum_application.model.Answer;
import org.example.forum_application.model.Question;
import org.example.forum_application.model.User;
import org.example.forum_application.repository.AnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerService answerService;

    private Answer answer;
    private Question question;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();

        question = new Question();
        question.setId(1L);

        answer = new Answer();
        answer.setId(1L);
        answer.setText("Test answer");   //
        answer.setAuthor(user);          //
        answer.setQuestion(question);    //
    }

    // create ans
    @Test
    void shouldCreateAnswer() {
        when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        Answer saved = answerService.createAnswer(answer);

        assertNotNull(saved);
        assertEquals("Test answer", saved.getText());
        assertNotNull(saved.getAuthor());
        assertNotNull(saved.getQuestion());

        verify(answerRepository, times(1)).save(answer);
    }

    // find by id
    @Test
    void shouldFindAnswerById() {
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        Answer found = answerService.findById(1);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    // find by id when not found
    @Test
    void shouldReturnNullWhenAnswerNotFound() {
        when(answerRepository.findById(1L)).thenReturn(Optional.empty());

        Answer found = answerService.findById(1);

        assertNull(found);
    }

    // ans fara quest
    @Test
    void shouldHandleAnswerWithoutQuestion() {
        Answer invalidAnswer = new Answer();
        invalidAnswer.setText("No question");
        invalidAnswer.setAuthor(user);

        when(answerRepository.save(any())).thenReturn(invalidAnswer);

        Answer saved = answerService.createAnswer(invalidAnswer);

        assertNotNull(saved);
        assertNull(saved.getQuestion());
    }

    // ans fara author
    @Test
    void shouldHandleAnswerWithoutAuthor() {
        Answer invalidAnswer = new Answer();
        invalidAnswer.setText("No author");
        invalidAnswer.setQuestion(question);

        when(answerRepository.save(any())).thenReturn(invalidAnswer);

        Answer saved = answerService.createAnswer(invalidAnswer);

        assertNotNull(saved);
        assertNull(saved.getAuthor());
    }

    // delete ans
    @Test
    void shouldDeleteAnswer() {
        doNothing().when(answerRepository).delete(answer);

        answerService.deleteAnswer(answer);

        verify(answerRepository, times(1)).delete(answer);
    }
}