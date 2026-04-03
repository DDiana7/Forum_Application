package org.example.forum_application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.forum_application.model.Answer;
import org.example.forum_application.service.AnswerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnswerController.class)
class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnswerService answerService;

    @Autowired
    private ObjectMapper objectMapper;

    //get all ans
    @Test
    void shouldGetAllAnswers() throws Exception {
        Answer answer = new Answer();
        answer.setText("Test answer");

        when(answerService.findAll()).thenReturn(List.of(answer));

        mockMvc.perform(get("/answer/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Test answer"));
    }

    // ans by id
    @Test
    void shouldGetAnswerById() throws Exception {
        Answer answer = new Answer();
        answer.setText("Test answer");

        when(answerService.findById(1)).thenReturn(answer);

        mockMvc.perform(get("/answer/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test answer"));
    }

    // quest by id
    @Test
    void shouldGetAnswersByQuestionId() throws Exception {
        Answer answer = new Answer();
        answer.setText("Answer for question");

        when(answerService.findByQuestionId(1)).thenReturn(List.of(answer));

        mockMvc.perform(get("/answer/question/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Answer for question"));
    }

    // create ans
    @Test
    void shouldCreateAnswer() throws Exception {
        Answer answer = new Answer();
        answer.setText("New answer");

        when(answerService.createAnswer(any(Answer.class))).thenReturn(answer);

        mockMvc.perform(post("/answer/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New answer"));
    }

    // delete ans
    @Test
    void shouldDeleteAnswer() throws Exception {
        doNothing().when(answerService).deleteById(1);

        mockMvc.perform(delete("/answer/delete/1"))
                .andExpect(status().isOk());

        verify(answerService).deleteById(1);
    }

    // update ans
    @Test
    void shouldUpdateAnswer() throws Exception {
        Answer updated = new Answer();
        updated.setText("Updated answer");

        when(answerService.updateAnswer(eq(1), any(Answer.class))).thenReturn(updated);

        mockMvc.perform(put("/answer/update/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated answer"));
    }

    @Test
    void shouldReturnNull_whenAnswerNotFound() throws Exception {
        when(answerService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/answer/getById/1"))
                .andExpect(status().isOk());
    }
}