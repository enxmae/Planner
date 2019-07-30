package com.example.planner.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Permission implements Serializable {

    @Expose
    private Long id;

    @SerializedName("created_at")
    private Long createdAt;

    @SerializedName("updated_at")
    private Long updatedAt;

    @Expose
    @SerializedName("entity_id")
    private String entityId;

    @Expose
    private String name;

    @Expose
    private String ownerId;

    @Expose
    private String userId;

    public Permission() {

    }

    public Permission(String entityId, String name, String ownerId, String userId) {
        this.entityId = entityId;
        this.name = name;
        this.ownerId = ownerId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
