package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component(value = "org.example")
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        for (int i = 0; i < 10; i++) {
            DictManager.addDictItem("department", i, String.format("Department%d", i));
        }

        UserController userController = context.getBean(UserController.class);
        for (int i = 0; i < 10; i++) {
            System.out.println(userController.getById(i));
        }
    }
}