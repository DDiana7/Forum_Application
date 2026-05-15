package org.example.forum_application.service;

import org.example.forum_application.model.User;
import org.example.forum_application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        List<User> users = (List<User>) this.userRepository.findAll();
        return users;
    }

    public User findById(int id) {
        Optional<User> user = userRepository.findById(Long.valueOf(id));
//        return avion.orElse(null);

        if (user.isPresent()) {
            return user.get();
        }
        return null;

    }

    public User createUser(User user) {
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setBanned(false);
        user.setRole(org.example.forum_application.model.Role.USER);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteById(int id) {
        Optional<User> userOptional = userRepository.findById(Long.valueOf(id));

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBanned(true);
            user.setUsername("deleted_user_" + user.getId());
            user.setEmail("deleted_user_" + user.getId() + "@deleted.com");
            userRepository.save(user);
        }
    }

    public User updateUser(int id, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(Long.valueOf(id));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setBanned(updatedUser.isBanned());
            user.setRole(updatedUser.getRole());

            return userRepository.save(user);
        }

        return null;
    }

    public User login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.isBanned()) {
                return null;
            }

            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }

        return null;
    }

}
