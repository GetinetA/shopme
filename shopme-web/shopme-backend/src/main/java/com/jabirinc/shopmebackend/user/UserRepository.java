package com.jabirinc.shopmebackend.user;

import com.jabirinc.shopmecommon.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Getinet on 9/12/21
 */
public interface UserRepository extends CrudRepository<User, Integer> {
}
