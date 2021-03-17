package com.github.artfly.web;

import com.github.artfly.di.Autowired;
import com.github.artfly.di.Component;
import com.github.artfly.log.Logger;
import com.github.artfly.repo.User;
import com.github.artfly.repo.UserRepository;

@Component
public class UserController {

    private final UserRepository repository;
    private final Logger logger;

    @Autowired
    public UserController(UserRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public User addUser(String name) {
        User user = new User(name);
        logger.log("Adding user with name: " + user.getName());
        return json(repository.addUser(user));
    }

    public User findUser(int id) {
        return json(repository.findById(id));
    }

    public User findUser(String name) {
        return json(repository.findByName(name));
    }

    private User json(User user) {
        return user;
    }

}
