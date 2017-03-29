package com.alonsegal.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Alon Segal on 23/03/2017.
 */
@Entity
public class UserTenantRelation {

    private Integer id;
    private String username;
    private String tenant;

    public UserTenantRelation() {
    }

    public UserTenantRelation(Integer id, String username, String tenant) {
        this.id = id;
        this.username = username;
        this.tenant = tenant;
    }

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
