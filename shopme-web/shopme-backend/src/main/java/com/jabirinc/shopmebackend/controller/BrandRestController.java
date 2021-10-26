package com.jabirinc.shopmebackend.controller;

import com.jabirinc.shopmebackend.brand.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Getinet on 10/16/21
 */
@RestController
public class BrandRestController {

    private final BrandService brandService;

    @Autowired
    public BrandRestController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/brands/check_unique")
    public String checkDuplicateBrand(@Param(value = "id") Integer id,
                                      @Param(value = "name") String name) {
        return brandService.isBrandUnique(id, name);
    }
}
