package org.example.forum_application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.forum_application.model.Tag;
import org.example.forum_application.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    // get all tags
    @Test
    void shouldGetAllTags() throws Exception {
        Tag tag = new Tag("java");

        when(tagService.findAll()).thenReturn(List.of(tag));

        mockMvc.perform(get("/tag/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("java"));
    }

    // get by id
    @Test
    void shouldGetTagById() throws Exception {
        Tag tag = new Tag("spring");

        when(tagService.findById(1)).thenReturn(tag);

        mockMvc.perform(get("/tag/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("spring"));
    }

    // create tag
    @Test
    void shouldCreateTag() throws Exception {
        Tag tag = new Tag("backend");

        when(tagService.createTag(any(Tag.class))).thenReturn(tag);

        mockMvc.perform(post("/tag/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("backend"));
    }

    // delete tag
    @Test
    void shouldDeleteTag() throws Exception {
        doNothing().when(tagService).deleteById(1);

        mockMvc.perform(delete("/tag/delete/1"))
                .andExpect(status().isOk());

        verify(tagService).deleteById(1);
    }

    // update tag
    @Test
    void shouldUpdateTag() throws Exception {
        Tag updated = new Tag("updated");

        when(tagService.updateTag(eq(1), any(Tag.class))).thenReturn(updated);

        mockMvc.perform(put("/tag/update/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated"));
    }

    @Test
    void shouldReturnNull_whenTagNotFound() throws Exception {
        when(tagService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/tag/getById/1"))
                .andExpect(status().isOk());
    }
}