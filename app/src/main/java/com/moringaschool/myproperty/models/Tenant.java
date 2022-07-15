package com.moringaschool.myproperty.models;
import java.io.Serializable;
import java.sql.Timestamp;


import java.text.DateFormat;
import java.util.Objects;

public class Tenant implements Serializable {

    private int id;
    private String tenant_name,tenant_email, tenant_phone, tenant_id, property_name, unit_name,manager_name, join_date;
    private Timestamp joined;

    public Tenant(){}
    public Tenant(String tenant_name, String tenant_email, String tenant_phone, String tenant_id, String property_name, String unit_name, String manager_name) {
        this.tenant_name = tenant_name;
        this.tenant_email = tenant_email;
        this.tenant_phone = tenant_phone;
        this.tenant_id = tenant_id;
        this.property_name = property_name;
        this.unit_name = unit_name;
        this.manager_name = manager_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tenant tenant = (Tenant) o;
        return Objects.equals(getTenant_name(), tenant.getTenant_name()) && Objects.equals(getTenant_email(), tenant.getTenant_email()) && Objects.equals(getTenant_phone(), tenant.getTenant_phone()) && Objects.equals(getTenant_id(), tenant.getTenant_id()) && Objects.equals(getProperty_name(), tenant.getProperty_name()) && Objects.equals(getUnit_name(), tenant.getUnit_name()) && Objects.equals(getManager_name(), tenant.getManager_name()) && Objects.equals(getJoin_date(), tenant.getJoin_date()) && Objects.equals(getJoined(), tenant.getJoined());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenant_name(), getTenant_email(), getTenant_phone(), getTenant_id(), getProperty_name(), getUnit_name(), getManager_name(), getJoin_date(), getJoined());
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenant_name() {
        return tenant_name;
    }

    public void setTenant_name(String tenant_name) {
        this.tenant_name = tenant_name;
    }

    public String getTenant_email() {
        return tenant_email;
    }

    public void setTenant_email(String tenant_email) {
        this.tenant_email = tenant_email;
    }

    public String getTenant_phone() {
        return tenant_phone;
    }

    public void setTenant_phone(String tenant_phone) {
        this.tenant_phone = tenant_phone;
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
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

    public Timestamp getJoined() {
        return joined;
    }

    public void setJoined(Timestamp joined) {
        this.joined = joined;
    }

    public String getJoin_date() {
//        join_date = DateFormat.getDateTimeInstance().format(joined);
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }
}
