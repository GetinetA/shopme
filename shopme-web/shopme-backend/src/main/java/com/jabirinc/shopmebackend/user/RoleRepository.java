package com.jabirinc.shopmebackend.user;

import com.jabirinc.shopmecommon.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by Getinet on 9/11/21
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
