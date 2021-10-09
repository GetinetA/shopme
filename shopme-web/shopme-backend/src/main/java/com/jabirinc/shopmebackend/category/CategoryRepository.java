package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by Getinet on 10/3/21
 */
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent is NULL")
    public List<Category> listRootCategories();
}
