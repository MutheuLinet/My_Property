package com.moringaschool.myproperty.models;
import java.io.Serializable;
import java.util.Objects;

public class Property implements Serializable {
    private int id;
    private String property_name, property_location, manager_name;

    public Property(String property_name, String manager_name, String location) {
        this.property_name = property_name;
        this.manager_name = manager_name;
        this.property_location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equals(getProperty_name(), property.getProperty_name()) && Objects.equals(getProperty_location(), property.getProperty_location()) && Objects.equals(getManager_name(), property.getManager_name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProperty_name(), getProperty_location(), getManager_name());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getProperty_location() {
        return property_location;
    }

    public void setProperty_location(String property_location) {
        this.property_location = property_location;
    }
}
