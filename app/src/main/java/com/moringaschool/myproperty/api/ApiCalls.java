package com.moringaschool.myproperty.api;

import com.moringaschool.myproperty.models.Defect;
import com.moringaschool.myproperty.models.DoneDefect;
import com.moringaschool.myproperty.models.Property;
import com.moringaschool.myproperty.models.PropertyManager;
import com.moringaschool.myproperty.models.Tenant;
import com.moringaschool.myproperty.models.Unit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiCalls {

    @POST("/propertyManager")
    Call<PropertyManager> addManager(
            @Body PropertyManager manager
    );

    @POST("/property")
    Call<Property> addProperty(
            @Body Property property
    );

    @GET("/property")
    Call<List<Property>> getProperties();

    @GET("/managerProperties/{name}")
    Call<List<Property>> getManagerProperties(
            @Path("name") String managerName
    );

    //add a tenant
    @POST("/tenant")
    Call<Tenant> addTenant(
            @Body Tenant tenant
    );

    @GET("/singleTenant/{name}")
    Call<Tenant> getTenant(
            @Path("name") String unitName
    );

    @GET("/Tenants/{name}")
    Call<List<Tenant>> getManagerTenants(
            @Path("name") String unitName
    );

    @PATCH("/updateTenant/{id}")
    Call<Tenant> updateTenant(
            @Path("id") int tenantId,
            @Body Tenant tenant
    );

    @POST("/unit")
    Call<Unit> addUnit(
            @Body Unit unit
    );

    @GET("/units/{name}")
    Call<List<Unit>> propertyUnits(
            @Path("name") String propertyName
    );

    @DELETE("/unit/{id}")
    Call<List<Unit>> deleteUnit(
            @Path("id") int unitId
    );


    @POST("/defect")
    Call<Defect> addDefect(
            @Body Defect defect
    );

    @GET("/defects/{managerName}")
    Call<List<Defect>> managerDefects(
            @Path("managerName") String managerName
    );

    @GET("/tenantDefects/{tenantId}")
    Call<List<Defect>> tenantDefects(
            @Path("tenantId") String tenantId
    );

    @DELETE("/deleteDefect/{id}")
    Call<List<Defect>> deleteDefect(
            @Path("id") int defectId
    );

    @POST("/doneDefect")
    Call<DoneDefect> addDefect(
            @Body DoneDefect defect
    );

    @GET("/doneDefects/{tenantId}")
    Call<List<DoneDefect>> tenantDoneDefects(
            @Path("tenantId") String tenantId
    );

    @GET("/doneDefect/{managerName}")
    Call<List<DoneDefect>> managerDoneDefects(
            @Path("managerName") String managerName
    );

}
