package com.moringaschool.myproperty.models;

import java.util.Objects;

public class PropertyManager {

    private int id;
    private String manager_name ,phone_number,email;

    public PropertyManager(String manager_name, String phone_number, String email) {
        this.manager_name = manager_name;
        this.phone_number = phone_number;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyManager manager = (PropertyManager) o;
        return Objects.equals(manager_name, manager.manager_name) && Objects.equals(phone_number, manager.phone_number) && Objects.equals(email, manager.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manager_name, phone_number, email);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
