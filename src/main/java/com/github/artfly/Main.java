package com.github.artfly;

import com.github.artfly.di.ObjectsGraph;
import com.github.artfly.log.ConsoleLogger;
import com.github.artfly.log.Logger;
import com.github.artfly.repo.User;
import com.github.artfly.web.UserController;

public class Main {

    public static void main(String[] args) {
        ObjectsGraph objectsGraph = new ObjectsGraph();
        objectsGraph.setup(Logger.class, (clazz, graph) -> new ConsoleLogger());
        UserController userController = objectsGraph.create(UserController.class);

        User peter = userController.addUser("Peter");
        User louise = userController.addUser("Louise");
        System.out.println(userController.findUser(peter.getId()));
    }
}
