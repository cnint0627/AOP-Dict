package org.example;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

public interface UserService {
    User getById(int id);
}
