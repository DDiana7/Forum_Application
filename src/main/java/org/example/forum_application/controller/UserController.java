package org.example.forum_application.controller;

import org.example.forum_application.model.User;
import org.example.forum_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping("/register")
    public User addUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }


    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return this.userService.login(user.getEmail(), user.getPassword());
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
