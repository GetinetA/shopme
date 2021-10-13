package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.brand.BrandService;
import com.jabirinc.shopmecommon.entity.Brand;
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
    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping()
    public String listAll(Model model) {
        List<Brand> listBrands = brandService.listAll();
        model.addAttribute("listBrands", listBrands);
        return BRANDS_REQ_PATH;
    }
}
