package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private Random random = new Random();
    @Override
    public User getById(int id) {
        User user = new User(id, "test", random.nextInt(0, 10));
        return user;
    }
}
