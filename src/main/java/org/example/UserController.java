package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    public Result<User> getById(int id) {
        return new Result<>(200, null, userService.getById(id));
    }
}
