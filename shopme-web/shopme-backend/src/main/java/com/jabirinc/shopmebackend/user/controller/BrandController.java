package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.brand.BrandService;
import com.jabirinc.shopmebackend.category.CategoryService;
import com.jabirinc.shopmecommon.entity.Brand;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Getinet on 10/13/21
 */
@Controller
@RequestMapping(value = "/brands")
public class BrandController {

    private static final String BRANDS_ROOT_REQ_PATH = "/brands";
    private static final String BRANDS_REQ_PATH = "brands/brands";
    private static final String BRAND_FORM_REQ_PATH = "brands/brand_form";
    private final BrandService brandService;
    private final CategoryService categoryService;

    @Autowired
    public BrandController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String listAll(Model model) {
        List<Brand> listBrands = brandService.listAll();
        model.addAttribute("listBrands", listBrands);
        return BRANDS_REQ_PATH;
    }

    @GetMapping("/new")
    public String newBrand(Model model) {

        List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();
        model.addAttribute("brand", new Brand());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Brand");
        return BRAND_FORM_REQ_PATH;
    }
}
