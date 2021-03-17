package com.github.artfly.repo;

import com.github.artfly.log.Logger;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private final Map<Integer, User> db = new HashMap<>();
    private final Logger logger;
    private int cnt;

    public UserRepository(Logger logger) {
        this.logger = logger;
    }

    public User addUser(User user) {
        int id = createId();
        logger.log("Identifier for user: " + id);
        user.setId(id);
        db.put(id, user);
        return user;
    }

    public User findById(int id) {
        return db.get(id);
    }

    public User findByName(String name) {
        for (User user : db.values()) {
            if (name.equals(user.getName())) return user;
        }
        return null;
    }

    private int createId() {
        return cnt++;
    }

}
