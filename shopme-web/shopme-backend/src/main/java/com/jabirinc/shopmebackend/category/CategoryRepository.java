package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Getinet on 10/3/21
 */
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
}
