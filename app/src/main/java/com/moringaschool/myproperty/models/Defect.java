package com.moringaschool.myproperty.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Defect implements Serializable {
    int id;

    private String description , property_name, unit_name, string_uri, tenant_id, manager_name;
    private Timestamp created_at;

    public Defect() {
    }

    public Defect(String description, String property_name, String unit_name, String stingUri, String tenantId, String manager_name) {
        this.description = description;
        this.property_name = property_name;
        this.unit_name = unit_name;
        this.string_uri = stingUri;
        this.tenant_id = tenantId;
        this.manager_name = manager_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Defect defect = (Defect) o;
        return Objects.equals(getDescription(), defect.getDescription()) && Objects.equals(getProperty_name(), defect.getProperty_name()) && Objects.equals(getUnit_name(), defect.getUnit_name()) && Objects.equals(getString_uri(), defect.getString_uri()) && Objects.equals(getTenant_id(), defect.getTenant_id()) && Objects.equals(getManager_name(), defect.getManager_name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getProperty_name(), getUnit_name(), getString_uri(), getTenant_id(), getManager_name());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getString_uri() {
        return string_uri;
    }

    public void setString_uri(String string_uri) {
        this.string_uri = string_uri;
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
