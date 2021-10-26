package com.jabirinc.shopmebackend.product;

import com.jabirinc.shopmecommon.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Getinet on 10/26/21
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
}
