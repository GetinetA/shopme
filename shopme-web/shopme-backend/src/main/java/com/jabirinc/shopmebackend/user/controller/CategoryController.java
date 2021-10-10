package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.category.CategoryService;
import com.jabirinc.shopmebackend.config.ShopmeUserDetails;
import com.jabirinc.shopmebackend.exception.CategoryNotFoundException;
import com.jabirinc.shopmebackend.exception.UserNotFoundException;
import com.jabirinc.shopmebackend.user.export.AbstractExporter;
import com.jabirinc.shopmebackend.utils.FileUploadUtil;
import com.jabirinc.shopmecommon.entity.Category;
import com.jabirinc.shopmecommon.entity.Role;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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

    private static final String CATEGORIES_ROOT_REQ_PATH = "/categories";
    private static final String CATEGORIES_REQ_PATH = "categories/categories";
    private static final String CATEGORY_FORM_REQ_PATH = "categories/category_form";
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String listCategories(Model model, @Param("sortDir") String sortDir) {

        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = CategoryService.SORT_ASC;
        }

        String reverseSortDir = sortDir.equalsIgnoreCase(CategoryService.SORT_ASC) ?
                CategoryService.SORT_DESC : CategoryService.SORT_ASC;

        List<Category> listCategories = categoryService.findAll(sortDir);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        return CATEGORIES_REQ_PATH;
    }

    @GetMapping("/new")
    public String newCategory(Model model) {

        List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();
        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");
        return CATEGORY_FORM_REQ_PATH;
    }

    @PostMapping("/save")
    public String saveUser(Category category, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                           @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        Category savedCategory;
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            savedCategory = categoryService.save(category);

            String uploadDir = FileUploadUtil.CATEGORY_UPLOAD_DIRECTORY + savedCategory.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            savedCategory = categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:" + CATEGORIES_ROOT_REQ_PATH;
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,
                           Model model, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.findById(id);

            List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID : " + id + ")");
            return CATEGORY_FORM_REQ_PATH;
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:" + CATEGORIES_ROOT_REQ_PATH;
        }
    }

    @GetMapping("/{id}/enabled/{status}")
    public String UpdateCategoryEenabledStatus(@PathVariable(name = "id") Integer id,
                                           @PathVariable(name = "status") boolean enabled,
                                           RedirectAttributes redirectAttributes) {

        categoryService.updateEnabledStatus(id, enabled);
        String status = enabled? "enabled":"disabled";
        redirectAttributes.addFlashAttribute("message",
                "The category with ID " + id + " has been " + status);

        return "redirect:" + CATEGORIES_ROOT_REQ_PATH;
    }

    @GetMapping(value = "/export/csv", produces = AbstractExporter.CONTENT_TYPE_CSV)
    public void exportToCSV(HttpServletResponse response) {

        //List<Category> listCategories = categoryService.findAll(CategoryService.SORT_ASC);
    }

    @GetMapping(value = "/export/excel")
    public void exportToExcel(HttpServletResponse response) {

        //List<Category> listCategories = categoryService.findAll(CategoryService.SORT_ASC);
    }

    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportToPdf(HttpServletResponse response) {

        //List<Category> listCategories = categoryService.findAll(CategoryService.SORT_ASC);
    }

}
