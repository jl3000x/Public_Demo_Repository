package com.zh.ppholic_server_demo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.zh.ppholic_server_demo.validation.FieldMatch;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "matchingPassword", message = "The password field must match")
})
public class CrmUser {

    private Long id;

    @NotNull(message = "is required.")
    @Size(min = 1, message = "is required.")
    private String userName;
    
    @NotNull(message = "is required.")
    @Size(min = 1, message = "is required.")
    private String password;

    @NotNull(message = "is required.")
    @Size(min = 1, message = "is required.")
    private String matchingPassword;

    @NotNull(message = "is required.")
    @Size(min = 1, message = "is required.")
    private String firstName;

    @NotNull(message = "is required.")
    @Size(min = 1, message = "is required.")
    private String lastName;

    @NotNull(message = "is required.")
    @Size(min = 1, message = "is required.")
    private String email;

    private ShoppingList shoppingList;
    
    private ShoppingRecord shoppingRecord;

    private CrmRole crmRole;

    public CrmUser(){
        this.crmRole = new CrmRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return this.matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public ShoppingRecord getShoppingRecord() {
        return shoppingRecord;
    }

    public void setShoppingRecord(ShoppingRecord shoppingRecord) {
        this.shoppingRecord = shoppingRecord;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public CrmRole getCrmRole() {
        return this.crmRole;
    }

    public void setCrmRole(CrmRole crmRole) {
        this.crmRole = crmRole;
    }

    @Override
    public String toString() {
        return "CrmUser [crmRole=" + crmRole + ", email=" + email + ", firstName=" + firstName + ", id=" + id
                + ", lastName=" + lastName + ", matchingPassword=" + matchingPassword + ", password=" + password
                + ", shoppingList=" + shoppingList + ", shoppingRecord=" + shoppingRecord + ", userName=" + userName
                + "]";
    }
}
