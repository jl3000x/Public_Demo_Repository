package com.zh.ppholic_server_demo.model;

import java.util.Collection;
import java.util.Date;

public class ShoppingList {
    
    private int id;

    private int total;

    private String information;

    private Date lastUpdate;

	private Collection<Subtotal> subtotals;
    
    private ShoppingList theShoppingList;    

	public ShoppingList() {
	}

    public ShoppingList(int theId) {
        this.id = theId;
	}

    public ShoppingList(int total, String information, Date lastUpdate, Collection<Subtotal> subtotals) {
        this.total = total;
        this.information = information;
        this.lastUpdate = lastUpdate;
        this.subtotals = subtotals;
    }    

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public Collection<Subtotal> getSubtotals() {
        return this.subtotals;
    }

    public void setSubtotals(Collection<Subtotal> subtotals) {
        this.subtotals = subtotals;
    }

    public ShoppingList getTheShoppingList() {
        return theShoppingList;
    }

    public void setTheShoppingList(ShoppingList theShoppingList) {
        this.theShoppingList = theShoppingList;
    }

    @Override
    public String toString() {
        return "ShoppingList [id=" + id + ", information=" + information + ", lastUpdate=" + lastUpdate + ", subtotals="
                + subtotals + ", total=" + total + "]";
    }
}
