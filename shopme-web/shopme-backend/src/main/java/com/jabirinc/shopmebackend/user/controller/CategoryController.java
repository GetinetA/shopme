package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.category.CategoryService;
import com.jabirinc.shopmebackend.user.export.AbstractExporter;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Getinet on 10/8/21
 */
@RequestMapping("/categories")
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String listCategories(Model model) {

        List<Category> listCategories = categoryService.findAll();
        model.addAttribute("listCategories", listCategories);
        return "categories/categories";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {

        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
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
