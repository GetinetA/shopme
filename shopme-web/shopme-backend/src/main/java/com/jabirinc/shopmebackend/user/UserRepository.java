package com.jabirinc.shopmebackend.user;

import com.jabirinc.shopmecommon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Getinet on 9/12/21
 */
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);

    Long countById(Integer id);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

    //@Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1% ")
    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName ) LIKE %?1% ")
    Page<User> search(String keyword, Pageable pageable);
}
