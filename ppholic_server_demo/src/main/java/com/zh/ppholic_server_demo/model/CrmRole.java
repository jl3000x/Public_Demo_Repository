package com.zh.ppholic_server_demo.model;

import java.util.Arrays;
import java.util.Collection;

import com.zh.ppholic_server_demo.entity.Role;

public class CrmRole {
    
    private boolean isEmployee;
    
    private boolean isManager;
    
    private boolean isAdmin;

    private boolean isMember;

    public static final Collection<String > roleItems = Arrays.asList("Admin", "Manager", "Employee", "Member");

    public CrmRole() {

        this.isEmployee = false;
        this.isManager = false;
        this.isAdmin = false;
        this.isMember = true;
    }

    public CrmRole(boolean isEmployee, boolean isManager, boolean isAdmin, boolean isMember) {
        
        this.isEmployee = isEmployee;
        this.isManager = isManager;
        this.isAdmin = isAdmin;
        this.isMember = isMember;
    }

    public CrmRole(Collection<Role> roles) {

        this.isEmployee = false;
        this.isManager = false;
        this.isAdmin = false;
        this.isMember = false;
        
        for (Role tempRole: roles) {
            switch (tempRole.getName()) {
                case "ROLE_EMPLOYEE": 
                    this.isEmployee = true;
                    break;
                case "ROLE_MANAGER": 
                    this.isManager = true;
                    break;    
                case "ROLE_ADMIN": 
                    this.isAdmin = true;
                    break;
                case "ROLE_MEMBER": 
                    this.isMember = true;
                    break;
                default:
                    // if nothing matches the default to sort by lastName
            }
        }
    }

    public boolean isIsEmployee() {
        return this.isEmployee;
    }

    public boolean getIsEmployee() {
        return this.isEmployee;
    }

    public void setIsEmployee(boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    public boolean isIsManager() {
        return this.isManager;
    }

    public boolean getIsManager() {
        return this.isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public boolean isIsAdmin() {
        return this.isAdmin;
    }

    public boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public boolean isIsMember() {
        return isMember;
    }

    public boolean getIsMember() {
        return this.isMember;
    }

    public void setMember(boolean isMember) {
        this.isMember = isMember;
    }

    @Override
    public String toString() {
        return "CrmRole [isAdmin=" + isAdmin + ", isEmployee=" + isEmployee + ", isManager=" + isManager + ", isMember="
                + isMember + "]";
    }
}
