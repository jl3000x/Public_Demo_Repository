package com.zh.ppholic_server_demo.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="subtotal")
public class Subtotal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="amount")
    private int amount;

    @Column(name="info")
    private String info;

	@OneToOne(cascade={CascadeType.DETACH, CascadeType.MERGE,
                        CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "shoppinglist_subtotal", 
	            joinColumns = @JoinColumn(name = "subtotal_id"), 
	            inverseJoinColumns = @JoinColumn(name = "shoppinglist_id"))    
    @JsonIgnore   
	private ShoppingList theShoppingList;

    public Subtotal() {

    }

    public Subtotal(Subtotal theSubtotal) {
        this.amount = theSubtotal.amount;
        this.info = theSubtotal.info;
        this.product = theSubtotal.product;
    }

    public Subtotal(Product product) {
        this.amount = 1;
        this.info = "N/A.";
        this.product = product;
    }

    public Subtotal(int amount, Product product) {
        this.amount = amount;
        this.info = "N/A.";
        this.product = product;
    }

    public Subtotal(int amount, String info, Product product) {
        this.amount = amount;
        this.info = info;
        this.product = product;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShoppingList getTheShoppingList() {
        return this.theShoppingList;
    }

    public void setTheShoppingList(ShoppingList theShoppingList) {
        this.theShoppingList = theShoppingList;
    }

    @Override
    public String toString() {
        return "Subtotal [amount=" + amount + ", id=" + id + ", info=" + info + ", product=" + product + "]";
    }
}
