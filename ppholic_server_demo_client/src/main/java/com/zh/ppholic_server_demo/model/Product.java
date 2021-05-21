package com.zh.ppholic_server_demo.model;

import java.util.Date;

public class Product {
    
    private int id;

    private String name;

    private int price;

    private String information;

    private Date lastUpdate;

	private SubCategory subCategory;

	public Product() {
	}

    public Product(int id, String name, int price, String information, Date lastUpdate, SubCategory subCategory) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.information = information;
        this.lastUpdate = lastUpdate;
        this.subCategory = subCategory;
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

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getInformation() {
        return this.information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public SubCategory getSubCategory() {
        return this.subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", information=" + information + ", lastUpdate=" + lastUpdate + ", name=" + name
                + ", price=" + price + ", subCategory=" + subCategory + "]";
    }
}
