package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.category.CategoryService;
import com.jabirinc.shopmebackend.config.ShopmeUserDetails;
import com.jabirinc.shopmebackend.user.export.AbstractExporter;
import com.jabirinc.shopmecommon.entity.Category;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Getinet on 10/8/21
 */
@RequestMapping("/categories")
@Controller
public class CategoryController {

    private static final String CATEGORIES_DIR = "categories/categories";
    private static final String CATEGORY_FORM_DIR = "categories/category_form";
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String listCategories(Model model) {

        List<Category> listCategories = categoryService.findAll();
        model.addAttribute("listCategories", listCategories);
        return CATEGORIES_DIR;
    }

    @GetMapping("/new")
    public String newCategory(Model model) {

        List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();
        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");
        return CATEGORY_FORM_DIR;
    }

    @PostMapping("/save")
    public String saveUser(Category category, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                           @RequestParam("categoryImage") MultipartFile multipartFile) throws IOException {

        return CATEGORIES_DIR;
    }


    @GetMapping(value = "/export/csv", produces = AbstractExporter.CONTENT_TYPE_CSV)
    public void exportToCSV(HttpServletResponse response) {

        List<Category> listCategories = categoryService.findAll();
    }

    @GetMapping(value = "/export/excel")
    public void exportToExcel(HttpServletResponse response) {

        List<Category> listCategories = categoryService.findAll();
    }

    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportToPdf(HttpServletResponse response) {

        List<Category> listCategories = categoryService.findAll();
    }

}
