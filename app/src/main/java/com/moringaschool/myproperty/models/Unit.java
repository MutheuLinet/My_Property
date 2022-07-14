package com.moringaschool.myproperty.models;


import java.io.Serializable;
import java.util.Objects;

public class Unit implements Serializable {


    private int id;
    private String unit_name,unit_rooms, property_name;

    public Unit(String unitName, String unit_rooms, String property_name) {
        this.unit_name = unitName;
        this.property_name = property_name;
        this.unit_rooms = unit_rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return Objects.equals(getUnit_name(), unit.getUnit_name()) && Objects.equals(getUnit_rooms(), unit.getUnit_rooms()) && Objects.equals(getProperty_name(), unit.getProperty_name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUnit_name(), getUnit_rooms(), getProperty_name());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnitName() {
        return unit_name;
    }

    public void setUnitName(String unitName) {
        this.unit_name = unitName;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getUnit_rooms() {
        return unit_rooms;
    }

    public void setUnit_rooms(String unit_rooms) {
        this.unit_rooms = unit_rooms;
    }
}
