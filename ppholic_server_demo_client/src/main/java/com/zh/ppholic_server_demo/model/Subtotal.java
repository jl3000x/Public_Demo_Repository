package com.zh.ppholic_server_demo.model;

public class Subtotal {
    
    private int id;

    private int amount;

    private String info;

    private Product product;

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
