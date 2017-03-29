package com.alonsegal.repository;

import com.alonsegal.domain.UserTenantRelation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Alon Segal on 23/03/2017.
 */
public interface UserTenantRelationRepository extends JpaRepository<UserTenantRelation, Integer> {

    UserTenantRelation findByUsername(String name);
}
