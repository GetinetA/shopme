package com.jabirinc.shopmebackend.brand;

import com.jabirinc.shopmecommon.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Getinet on 10/11/21
 */
@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> listAll() {
        return (List<Brand>) brandRepository.findAll();
    }
}
