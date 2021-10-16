package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmebackend.exception.CategoryNotFoundException;
import com.jabirinc.shopmebackend.exception.UserNotFoundException;
import com.jabirinc.shopmecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public static final int CATEGORY_PER_PAGE = 4;
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

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNumber, String sortDir, String keyword) {

        Sort sort = Sort.by(DEFAULT_SORT_PROP);
        if (Sort.Direction.ASC.name().equalsIgnoreCase(sortDir)) {
            sort = sort.ascending();
        } else if(Sort.Direction.DESC.name().equalsIgnoreCase(sortDir)) {
            sort = sort.descending();
        }

        // Pageable is zero based
        Pageable pageable = PageRequest.of(pageNumber - 1, CATEGORY_PER_PAGE, sort);

        Page<Category> pageCategories;
        List<Category> results;
        if (keyword != null && !keyword.isEmpty()) {
            pageCategories = categoryRepository.search(keyword, pageable);
            List<Category> searchResult = pageCategories.getContent();
            for (Category category : searchResult) {
                category.setHasChildren(category.getChildren().size() > 0);
            }
            results = searchResult;
        } else {
            pageCategories = categoryRepository.listRootCategories(pageable);
            results = listHierarchicalCategories(pageCategories.getContent(), sortDir);;
        }

        pageInfo.setTotalPages(pageCategories.getTotalPages());
        pageInfo.setTotalElements(pageCategories.getTotalElements());
        //List<Category> rootCategories = pageCategories.getContent();

        //return listHierarchicalCategories(rootCategories, sortDir);
        return results;
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
        return sortSubCategories(children, Sort.Direction.ASC.name());
    }
    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {

        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if(Sort.Direction.ASC.name().equalsIgnoreCase(sortDir)) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);
        return sortedChildren;
    }


    public void updateEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws CategoryNotFoundException {

        Long countById = categoryRepository.countById(id);
        if (countById == null) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
        categoryRepository.deleteById(id);
    }

}
