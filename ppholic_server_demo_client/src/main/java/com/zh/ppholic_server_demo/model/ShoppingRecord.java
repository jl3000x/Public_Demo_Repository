package com.zh.ppholic_server_demo.model;

import java.util.Date;
import java.util.Set;

public class ShoppingRecord {
    
    private int id;

    private int total;

    private String information;

    private Date lastUpdate;

	private Set<ShoppingList> shoppingLists;

	public ShoppingRecord() {

    }

    public ShoppingRecord(int id) {
        this.id = id;
    }

    public ShoppingRecord(int id, int total, String information, Date lastUpdate, Set<ShoppingList> shoppingLists) {
        this.id = id;
        this.total = total;
        this.information = information;
        this.lastUpdate = lastUpdate;
        this.shoppingLists = shoppingLists;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(Set<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    @Override
    public String toString() {
        return "ShoppingRecord [id=" + id + ", information=" + information + ", lastUpdate=" + lastUpdate
                + ", shoppingLists=" + shoppingLists + ", total=" + total + "]";
    }
}
