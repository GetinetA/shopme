package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Getinet on 10/10/21
 */
@RestController
public class CategoryRestController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories/check_unique")
    public String checkDuplicateCategory(@Param(value = "id") Integer id,
                                      @Param(value = "name") String name, @Param(value = "alias") String alias) {
        return categoryService.isCategoryUnique(id, name, alias);
    }
}
