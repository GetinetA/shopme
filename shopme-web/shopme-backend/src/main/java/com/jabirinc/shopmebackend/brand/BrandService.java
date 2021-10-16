package com.jabirinc.shopmebackend.brand;

import com.jabirinc.shopmebackend.exception.BrandNotFoundException;
import com.jabirinc.shopmecommon.entity.Brand;
import com.jabirinc.shopmecommon.entity.Category;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Getinet on 10/11/21
 */
@Service
public class BrandService {

    public static final String DEFAULT_SORT_PROP = "name";
    public static final int BRANDS_PER_PAGE = 10;

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> listAll() {
        return (List<Brand>) brandRepository.findAll();
    }

    public Page<Brand> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = Sort.Direction.ASC.name().equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, BRANDS_PER_PAGE, sort);

        if (keyword != null) {
            return brandRepository.search(keyword, pageable);
        }

        return brandRepository.findAll(pageable);
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand findById(Integer id) {

        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {

        Long countById = brandRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }
        brandRepository.deleteById(id);
    }

    public String isBrandUnique(Integer id, String name) {

        boolean isCreatingNew = (id == null || id == 0);
        Brand brandByName = brandRepository.findByName(name);
        if (isCreatingNew ) {
            if (brandByName != null) {
                return "Duplicate";
            }
        } else {
            if (brandByName != null && brandByName.getId() != id) {
                return "Duplicate";
            }
        }
        return "OK";
    }
}
