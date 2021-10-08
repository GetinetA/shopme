package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Getinet on 10/8/21
 */
@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll(Sort.by("name").ascending());
    }

    public List<Category> listAllCategoriesUsedInForm() {

        List<Category> categoriesUsedInform = new ArrayList<>();
        Iterable<Category> categoriesFromDB = categoryRepository.findAll();

        for (Category category : categoriesFromDB) {
            if (category.getParent() == null) {
                //System.out.println(category.getName());
                categoriesUsedInform.add(new Category(category.getName()));
                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    String subCategoryName = "--" + subCategory.getName();
                    categoriesUsedInform.add(new Category(subCategoryName));
                    listChildren(categoriesUsedInform, subCategory, 1);
                }
            }
        }

        return categoriesUsedInform;
    }

    private void listChildren(List<Category> categoriesUsedInform, Category parent, int subLevel) {

        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            //System.out.println(subCategory.getName());
            categoriesUsedInform.add(new Category(name + subCategory.getName()));
            listChildren(categoriesUsedInform, subCategory, newSubLevel);
        }
    }
}
