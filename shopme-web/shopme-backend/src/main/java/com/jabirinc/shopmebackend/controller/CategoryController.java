package com.jabirinc.shopmebackend.controller;

import com.jabirinc.shopmebackend.category.CategoryPageInfo;
import com.jabirinc.shopmebackend.category.CategoryService;
import com.jabirinc.shopmebackend.exception.CategoryNotFoundException;
import com.jabirinc.shopmebackend.export.AbstractExporter;
import com.jabirinc.shopmebackend.export.CategoryCsvExporter;
import com.jabirinc.shopmebackend.utils.FileUploadUtil;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
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

        return listByPage(model, 1, sortDir, null);
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = Sort.Direction.ASC.name();
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();

        List<Category> listCategories = categoryService.listByPage(pageInfo, pageNum, sortDir, keyword);

        long startCount = (pageNum - 1) * CategoryService.CATEGORY_PER_PAGE + 1;
        long endCount = startCount + CategoryService.CATEGORY_PER_PAGE - 1;
        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }

        String reverseSortDir = Sort.Direction.ASC.name().equalsIgnoreCase(sortDir) ?
                Sort.Direction.DESC.name() : Sort.Direction.ASC.name();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", CategoryService.DEFAULT_SORT_PROP);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

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

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);

            String uploadDir = FileUploadUtil.CATEGORY_UPLOAD_DIRECTORY + id;
            FileUploadUtil.removeDirectory(uploadDir);

            redirectAttributes.addFlashAttribute("message",
                    "The category ID " + id + " has been deleted successfully");
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:" + CATEGORIES_ROOT_REQ_PATH;
    }

    @GetMapping(value = "/export/csv", produces = AbstractExporter.CONTENT_TYPE_CSV)
    public void exportToCSV(HttpServletResponse response) {

        List<Category> listOfCategories = categoryService.listAllCategoriesUsedInForm();

        CategoryCsvExporter categoryCsvExporter = new CategoryCsvExporter();
        categoryCsvExporter.export(listOfCategories, response);
    }

    @GetMapping(value = "/export/excel")
    public void exportToExcel(HttpServletResponse response) {

        //implement as needed;
    }

    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportToPdf(HttpServletResponse response) {

        //implement as needed;
    }

}
