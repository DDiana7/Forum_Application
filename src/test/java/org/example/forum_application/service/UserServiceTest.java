package org.example.forum_application.service;

import org.example.forum_application.model.Role;
import org.example.forum_application.model.User;
import org.example.forum_application.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // findAll
    @Test
    void findAll_shouldReturnAllUsers() {
        User u1 = new User("ana", "a@mail.com", "pass");
        User u2 = new User("ion", "i@mail.com", "pass");

        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        var result = userService.findAll();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    // findById - found
    @Test
    void findById_shouldReturnUser_whenExists() {
        User user = new User("ana", "a@mail.com", "pass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1);

        assertNotNull(result);
        assertEquals("ana", result.getUsername());
    }

    // findById - not found
    @Test
    void findById_shouldReturnNull_whenNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.findById(1);

        assertNull(result);
    }

    // createUser
    @Test
    void createUser_shouldSetDefaults_andSave() {
        User user = new User();
        user.setUsername("ana");
        user.setEmail("a@mail.com");
        user.setPassword("pass");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertFalse(result.isBanned());
        assertEquals(Role.USER, result.getRole());

        verify(userRepository).save(user);
    }

    // deleteUser
    @Test
    void deleteUser_shouldCallRepository() {
        User user = new User("ana", "a@mail.com", "pass");

        userService.deleteUser(user);

        verify(userRepository).delete(user);
    }

    // deleteById
    @Test
    void deleteById_shouldCallRepository() {
        userService.deleteById(1);

        verify(userRepository).deleteById(1L);
    }

    // updateUser - success
    @Test
    void updateUser_shouldUpdateFields_whenUserExists() {
        User existing = new User("old", "old@mail.com", "pass");
        existing.setRole(Role.USER);

        User updated = new User("new", "new@mail.com", "newpass");
        updated.setRole(Role.MODERATOR);
        updated.setBanned(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(existing);

        User result = userService.updateUser(1, updated);

        assertNotNull(result);
        assertEquals("new", result.getUsername());
        assertEquals("new@mail.com", result.getEmail());
        assertTrue(result.isBanned());
        assertEquals(Role.MODERATOR, result.getRole());
    }

    // updateUser - not found
    @Test
    void updateUser_shouldReturnNull_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.updateUser(1, new User());

        assertNull(result);
    }
}