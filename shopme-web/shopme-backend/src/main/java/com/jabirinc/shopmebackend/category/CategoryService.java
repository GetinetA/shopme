package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmebackend.exception.CategoryNotFoundException;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

    public Category findById(Integer id) {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public List<Category> findAll() {

        List<Category> rootCategories = categoryRepository.listRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {

        List<Category> hierarchicalCategories = new ArrayList<>();
        for(Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.deepCopy(rootCategory));

            Set<Category> children = rootCategory.getChildren();
            for(Category subCategory : children) {
                Category copySubCategory = Category.deepCopy(subCategory);
                copySubCategory.setName("--" + copySubCategory.getName());
                hierarchicalCategories.add(copySubCategory);
                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for(Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            Category copySubCategory = Category.deepCopy(subCategory);
            copySubCategory.setName(name);
            hierarchicalCategories.add(copySubCategory);

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
        }
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> listAllCategoriesUsedInForm() {

        List<Category> categoriesUsedInform = new ArrayList<>();
        Iterable<Category> categoriesFromDB = categoryRepository.findAll();

        for (Category category : categoriesFromDB) {
            if (category.getParent() == null) {

                categoriesUsedInform.add(Category.create(category.getId(), category.getName()));
                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    String subCategoryName = "--" + subCategory.getName();
                    categoriesUsedInform.add(Category.create(subCategory.getId(), subCategoryName));
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

            categoriesUsedInform.add(Category.create(subCategory.getId(), name + subCategory.getName()));
            listChildren(categoriesUsedInform, subCategory, newSubLevel);
        }
    }

}
