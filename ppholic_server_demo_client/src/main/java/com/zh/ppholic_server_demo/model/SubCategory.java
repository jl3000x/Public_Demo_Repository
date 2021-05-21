package com.zh.ppholic_server_demo.model;

public class SubCategory {
    
    private int id;
    private String name;

    public SubCategory() {

    }

    public SubCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SubCategory [id=" + id + ", name=" + name + "]";
    }
}
