package com.zh.ppholic_server_demo.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="shopping_record")
public class ShoppingRecord {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "total")
    private int total;

    @Column(name = "info")
    private String information;

    @Column(name = "last_update")
    private Date lastUpdate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "shoppingrecord_shoppinglist", 
	joinColumns = @JoinColumn(name = "shoppingrecord_id"), 
	inverseJoinColumns = @JoinColumn(name = "shoppinglist_id"))    
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
