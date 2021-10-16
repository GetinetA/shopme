package com.jabirinc.shopmebackend.brand;

import com.jabirinc.shopmecommon.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Getinet on 10/11/21
 */
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    Brand findByName(String name);

    Long countById(Integer id);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    Page<Brand> search(String keyword, Pageable pageable);
    
}
