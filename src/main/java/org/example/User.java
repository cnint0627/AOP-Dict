package org.example;

public class User {
    int id;
    String name;
    @Dict(dictName = "department")
    int departmentId;
    public User(int id, String name, int departmentId) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getDepartmentId() {
        return departmentId;
    }
}
