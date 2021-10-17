package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.brand.BrandService;
import com.jabirinc.shopmebackend.category.CategoryService;
import com.jabirinc.shopmebackend.exception.BrandNotFoundException;
import com.jabirinc.shopmebackend.user.export.AbstractExporter;
import com.jabirinc.shopmebackend.user.export.BrandCsvExporter;
import com.jabirinc.shopmebackend.user.export.CategoryCsvExporter;
import com.jabirinc.shopmebackend.utils.FileUploadUtil;
import com.jabirinc.shopmecommon.entity.Brand;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
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

    @GetMapping("")
    public String listFirstPage(Model model) {

        return listByPage(model, 1, BrandService.DEFAULT_SORT_PROP, Sort.Direction.ASC.name(), null);
    }

    /*@GetMapping()
    public String listAll(Model model) {
        List<Brand> listBrands = brandService.listAll();
        model.addAttribute("listBrands", listBrands);
        return BRANDS_REQ_PATH;
    }*/

    @GetMapping("/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword
    ) {
        Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Brand> listBrands = page.getContent();

        long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
        long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = Sort.Direction.ASC.name().equalsIgnoreCase(sortDir) ?
                Sort.Direction.DESC.name() : Sort.Direction.ASC.name();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return BRANDS_REQ_PATH; //"users/users";
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

    @GetMapping(value = "/export/csv", produces = AbstractExporter.CONTENT_TYPE_CSV)
    public void exportToCSV(HttpServletResponse response) {

        List<Brand> listOfBrands = brandService.listAll();

        BrandCsvExporter brandCsvExporter = new BrandCsvExporter();
        brandCsvExporter.export(listOfBrands, response);
    }
}
