package org.example.forum_application.service;

import org.example.forum_application.model.Question;
import org.example.forum_application.model.QuestionStatus;
import org.example.forum_application.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    // findAll
    @Test
    void findAll_shouldReturnAllQuestions() {
        Question q1 = new Question();
        Question q2 = new Question();

        when(questionRepository.findAll()).thenReturn(Arrays.asList(q1, q2));

        var result = questionService.findAll();

        assertEquals(2, result.size());
        verify(questionRepository).findAll();
    }

    //findById - found
    @Test
    void findById_shouldReturnQuestion_whenExists() {
        Question question = new Question();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        Question result = questionService.findById(1);

        assertNotNull(result);
    }

    // findById - not found
    @Test
    void findById_shouldReturnNull_whenNotExists() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        Question result = questionService.findById(1);

        assertNull(result);
    }

    // create
    @Test
    void create_shouldSetDefaults_andSave() {
        Question question = new Question();

        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question result = questionService.create(question);

        assertNotNull(result);
        assertEquals(QuestionStatus.RECEIVED, result.getStatus());
        assertNotNull(result.getCreatedAt());

        verify(questionRepository).save(question);
    }

    //update - success
    @Test
    void update_shouldModifyFields_whenExists() {
        Question existing = new Question();
        existing.setTitle("old");

        Question updated = new Question();
        updated.setTitle("new");
        updated.setStatus(QuestionStatus.SOLVED);
        updated.setText("text");
        updated.setImageUrl("img");

        when(questionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(questionRepository.save(any(Question.class))).thenReturn(existing);

        Question result = questionService.update(1, updated);

        assertNotNull(result);
        assertEquals("new", result.getTitle());
        assertEquals(QuestionStatus.SOLVED, result.getStatus());
        assertEquals("text", result.getText());
        assertEquals("img", result.getImageUrl());
    }

    //update - not found
    @Test
    void update_shouldReturnNull_whenNotExists() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        Question result = questionService.update(1, new Question());

        assertNull(result);
    }

    //deleteQuestion
    @Test
    void deleteQuestion_shouldCallRepository() {
        Question question = new Question();

        questionService.deleteQuestion(question);

        verify(questionRepository).delete(question);
    }

    // deleteById
    @Test
    void deleteById_shouldCallRepository() {
        questionService.deleteById(1);

        verify(questionRepository).deleteById(1L);
    }
}