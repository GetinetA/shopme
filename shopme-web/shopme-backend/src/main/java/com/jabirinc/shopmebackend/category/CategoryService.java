package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmebackend.exception.CategoryNotFoundException;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Getinet on 10/8/21
 */
@Transactional
@Service
public class CategoryService {

    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";
    public static final String DEFAULT_SORT_PROP = "name";

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

    public List<Category> findAll(String sortDir) {

        Sort sort = Sort.by(DEFAULT_SORT_PROP);
        if (SORT_ASC.equalsIgnoreCase(sortDir)) {
            sort = sort.ascending();
        } else if(SORT_DESC.equalsIgnoreCase(sortDir)) {
            sort = sort.descending();
        }
        List<Category> rootCategories =
                categoryRepository.listRootCategories(sort);
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {

        List<Category> hierarchicalCategories = new ArrayList<>();
        for(Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.deepCopy(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);
            for(Category subCategory : children) {
                Category copySubCategory = Category.deepCopy(subCategory);
                copySubCategory.setName("--" + copySubCategory.getName());
                hierarchicalCategories.add(copySubCategory);
                listSubHierarchicalCategories(hierarchicalCategories, subCategory, sortDir, 1);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent, String sortDir, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);

        for(Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            Category copySubCategory = Category.deepCopy(subCategory);
            copySubCategory.setName(name);
            hierarchicalCategories.add(copySubCategory);

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, sortDir, newSubLevel);
        }
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> listAllCategoriesUsedInForm() {

        List<Category> categoriesUsedInform = new ArrayList<>();
        Iterable<Category> categoriesFromDB =
                categoryRepository.listRootCategories(Sort.by(DEFAULT_SORT_PROP).ascending());

        for (Category category : categoriesFromDB) {
            if (category.getParent() == null) {

                categoriesUsedInform.add(Category.create(category.getId(), category.getName()));
                Set<Category> children = sortSubCategories(category.getChildren());

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
        Set<Category> children = sortSubCategories(parent.getChildren());

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            categoriesUsedInform.add(Category.create(subCategory.getId(), name + subCategory.getName()));
            listChildren(categoriesUsedInform, subCategory, newSubLevel);
        }
    }

    public String isCategoryUnique(Integer id, String name, String alias) {

        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = categoryRepository.findByName(name);
        if (isCreatingNew ) {
            if (categoryByName != null) {
                return "Duplicate Name";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "Duplicate Alias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "Duplicate Name";
            }
            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "Duplicate Alias";
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, SORT_ASC);
    }
    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {

        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if(SORT_ASC.equalsIgnoreCase(sortDir)) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);
        return sortedChildren;
    }


















}
