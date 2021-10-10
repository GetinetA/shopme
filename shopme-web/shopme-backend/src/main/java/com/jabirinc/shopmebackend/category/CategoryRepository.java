package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


/**
 * Created by Getinet on 10/3/21
 */
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent is NULL")
    List<Category> listRootCategories(Sort sort);

    Category findByName(String name);

    Category findByAlias(String name);

    @Query("UPDATE Category c SET c.enabled = :enabled WHERE c.id = :id")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);
}
