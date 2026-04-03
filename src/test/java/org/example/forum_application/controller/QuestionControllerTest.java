package org.example.forum_application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.forum_application.model.Question;
import org.example.forum_application.model.QuestionStatus;
import org.example.forum_application.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    // get all quest
    @Test
    void shouldGetAllQuestions() throws Exception {
        Question q = new Question();
        q.setTitle("Test question");

        when(questionService.findAll()).thenReturn(List.of(q));

        mockMvc.perform(get("/question/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test question"));
    }

    // get quest
    @Test
    void shouldGetQuestionById() throws Exception {
        Question q = new Question();
        q.setTitle("Test question");

        when(questionService.findById(1)).thenReturn(q);

        mockMvc.perform(get("/question/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test question"));
    }

    // create quest
    @Test
    void shouldCreateQuestion() throws Exception {
        Question q = new Question();
        q.setTitle("New question");
        q.setStatus(QuestionStatus.RECEIVED);

        when(questionService.create(any(Question.class))).thenReturn(q);

        mockMvc.perform(post("/question/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(q)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New question"));
    }

    // delete quest
    @Test
    void shouldDeleteQuestion() throws Exception {
        doNothing().when(questionService).deleteById(1);

        mockMvc.perform(delete("/question/delete/1"))
                .andExpect(status().isOk());

        verify(questionService).deleteById(1);
    }

    // update quest
    @Test
    void shouldUpdateQuestion() throws Exception {
        Question updated = new Question();
        updated.setTitle("Updated");
        updated.setStatus(QuestionStatus.SOLVED);

        when(questionService.update(eq(1), any(Question.class))).thenReturn(updated);

        mockMvc.perform(put("/question/update/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void shouldReturnNull_whenQuestionNotFound() throws Exception {
        when(questionService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/question/getById/1"))
                .andExpect(status().isOk());
    }
}