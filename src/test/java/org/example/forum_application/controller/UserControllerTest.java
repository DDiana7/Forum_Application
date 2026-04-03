package org.example.forum_application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.forum_application.model.User;
import org.example.forum_application.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // get all users
    @Test
    void shouldGetAllUsers() throws Exception {
        User user = new User("ana", "ana@mail.com", "pass");

        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/user/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("ana"));
    }

    // get user by id
    @Test
    void shouldGetUserById() throws Exception {
        User user = new User("ana", "ana@mail.com", "pass");

        when(userService.findById(1)).thenReturn(user);

        mockMvc.perform(get("/user/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ana"));
    }

    //create user
    @Test
    void shouldCreateUser() throws Exception {
        User user = new User("ana", "ana@mail.com", "pass");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ana"));
    }

    // delete user
    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteById(1);

        mockMvc.perform(delete("/user/user/1"))
                .andExpect(status().isOk());

        verify(userService).deleteById(1);
    }

    // update user
    @Test
    void shouldUpdateUser() throws Exception {
        User updated = new User("new", "new@mail.com", "pass");

        when(userService.updateUser(eq(1), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/user/user/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new"));
    }

    @Test
    void shouldReturnNull_whenUserNotFound() throws Exception {
        when(userService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/user/user/1"))
                .andExpect(status().isOk());
    }
}