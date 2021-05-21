package com.zh.ppholic_server_demo.entity;

import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="shopping_list")
public class ShoppingList {
    
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
	@JoinTable(name = "shoppinglist_subtotal", 
	            joinColumns = @JoinColumn(name = "shoppinglist_id"), 
	            inverseJoinColumns = @JoinColumn(name = "subtotal_id")) 
	private Collection<Subtotal> subtotals;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "shoppingrecord_shoppinglist", 
	joinColumns = @JoinColumn(name = "shoppinglist_id"), 
	inverseJoinColumns = @JoinColumn(name = "shoppingrecord_id"))
    @JsonIgnore
    private ShoppingRecord theShoppingRecord;    

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

    public ShoppingRecord getTheShoppingRecord() {
        return theShoppingRecord;
    }

    public void setTheShoppingRecord(ShoppingRecord theShoppingRecord) {
        this.theShoppingRecord = theShoppingRecord;
    }

    @Override
    public String toString() {
        return "ShoppingList [id=" + id + ", information=" + information + ", lastUpdate=" + lastUpdate + ", subtotals="
                + subtotals + ", total=" + total + "]";
    }
}
