package com.moringaschool.myproperty.models;

import java.sql.Timestamp;
import java.util.Objects;

public class DoneDefect {
    private int id;
    private String name, uri, contactor_name, contractor_phone, contractor_location, manager_name, tenant_id;

    private Timestamp created_at;

    public DoneDefect() {
    }

    public DoneDefect(String name, String uri, String contactor_name, String contractor_phone, String contractor_location, String manager_name, String tenant_id) {
        this.name = name;
        this.uri = uri;
        this.contactor_name = contactor_name;
        this.contractor_phone = contractor_phone;
        this.contractor_location = contractor_location;
        this.manager_name = manager_name;
        this.tenant_id = tenant_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoneDefect that = (DoneDefect) o;
        return Objects.equals(name, that.name) && Objects.equals(uri, that.uri) && Objects.equals(contactor_name, that.contactor_name) && Objects.equals(contractor_phone, that.contractor_phone) && Objects.equals(contractor_location, that.contractor_location) && Objects.equals(manager_name, that.manager_name) && Objects.equals(tenant_id, that.tenant_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uri, contactor_name, contractor_phone, contractor_location, manager_name, tenant_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getContactor_name() {
        return contactor_name;
    }

    public void setContactor_name(String contactor_name) {
        this.contactor_name = contactor_name;
    }

    public String getContractor_phone() {
        return contractor_phone;
    }

    public void setContractor_phone(String contractor_phone) {
        this.contractor_phone = contractor_phone;
    }

    public String getContractor_location() {
        return contractor_location;
    }

    public void setContractor_location(String contractor_location) {
        this.contractor_location = contractor_location;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }}
