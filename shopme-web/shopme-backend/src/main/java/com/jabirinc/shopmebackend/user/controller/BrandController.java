package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.brand.BrandService;
import com.jabirinc.shopmebackend.category.CategoryService;
import com.jabirinc.shopmebackend.exception.BrandNotFoundException;
import com.jabirinc.shopmebackend.utils.FileUploadUtil;
import com.jabirinc.shopmecommon.entity.Brand;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @PostMapping("/save")
    public String saveUser(Brand brand, RedirectAttributes redirectAttributes,
                           @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        Brand savedBrand;
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            savedBrand = brandService.save(brand);

            String uploadDir = FileUploadUtil.BRAND_UPLOAD_DIRECTORY + savedBrand.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            savedBrand = brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully.");
        return "redirect:" + BRANDS_ROOT_REQ_PATH;
    }

    @GetMapping("/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id,
                               Model model, RedirectAttributes redirectAttributes) {
        try {
            Brand brand = brandService.findById(id);

            List<Category> listCategories = categoryService.listAllCategoriesUsedInForm();

            model.addAttribute("brand", brand);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Brand (ID : " + id + ")");
            return BRAND_FORM_REQ_PATH;
        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:" + BRANDS_ROOT_REQ_PATH;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            brandService.delete(id);

            String uploadDir = FileUploadUtil.BRAND_UPLOAD_DIRECTORY + id;
            FileUploadUtil.removeDirectory(uploadDir);

            redirectAttributes.addFlashAttribute("message",
                    "The brand ID " + id + " has been deleted successfully");
        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:" + BRANDS_ROOT_REQ_PATH;
    }
}
