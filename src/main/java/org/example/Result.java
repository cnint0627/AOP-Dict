package org.example;

import com.alibaba.fastjson.JSONObject;

public class Result<T> {
    private int code;
    private String msg;
    private T data;
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Result(");
        sb.append("code: ").append(code).append(", ");
        sb.append("msg: ").append(msg).append(", ");
        sb.append("data: ").append(data).append(")");
        return sb.toString();
    }
}
