package com.zh.ppholic_server_demo.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class Product {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "info")
    private String information;

    @Column(name = "last_update")
    private Date lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "product_subcat", 
	joinColumns = @JoinColumn(name = "product_id"), 
	inverseJoinColumns = @JoinColumn(name = "subcat_id"))
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
