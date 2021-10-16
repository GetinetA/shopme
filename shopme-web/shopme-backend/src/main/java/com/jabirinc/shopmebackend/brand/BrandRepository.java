package com.jabirinc.shopmebackend.brand;

import com.jabirinc.shopmecommon.entity.Brand;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Getinet on 10/11/21
 */
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    Brand findByName(String name);

    Long countById(Integer id);
}
