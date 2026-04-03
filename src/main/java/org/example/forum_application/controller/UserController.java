package org.example.forum_application.controller;

import org.example.forum_application.model.User;
import org.example.forum_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return this.userService.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
}
