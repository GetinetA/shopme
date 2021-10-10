package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmecommon.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testCreateRootCategory() {
        String computers = "Computers";
        String electronics = "Electronics";
        //Category category = new Category(computers);
        Category category = new Category(electronics);
        Category savedCategory = categoryRepository.save(category);

        Assertions.assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory() {

        // test #1
        //Category parent = new Category(1); // Computers category
        //Category subCategory = new Category("Desktops", parent);
        //Category savedCategory = categoryRepository.save(subCategory);
        //Assertions.assertThat(savedCategory.getId()).isGreaterThan(0);

        // test #2
        /*Category parent = new Category(1); // Computers category
        Category laptopsSubCategory = new Category("Laptops", parent);
        Category componentsSubCategory = new Category("Computer Components", parent);
        Iterable<Category> savedCategories = categoryRepository
                .saveAll(List.of(laptopsSubCategory, componentsSubCategory));
        Assertions.assertThat(savedCategories.iterator().hasNext()).isTrue();*/

        // test #3
        /*Category parent = new Category(2); // Electronics category
        Category camerasSubCategory = new Category("Cameras", parent);
        Category smartphonesSubCategory = new Category("Smartphones", parent);
        Iterable<Category> savedCategories = categoryRepository
                .saveAll(List.of(camerasSubCategory, smartphonesSubCategory));
        Assertions.assertThat(savedCategories.iterator().hasNext()).isTrue();*/

        // test #4
        /*Category parent = new Category(5); // Computer Components category
        Category camerasSubCategory = new Category("Memory", parent);
        Iterable<Category> savedCategories = categoryRepository.saveAll(List.of(camerasSubCategory));
        Assertions.assertThat(savedCategories.iterator().hasNext()).isTrue();*/

        Category parent = new Category(7);
        Category camerasSubCategory = new Category("iPhones", parent);
        // Java 9
        //Iterable<Category> savedCategories = categoryRepository.saveAll(List.of(camerasSubCategory));
        // Java 8
        Iterable<Category> savedCategories = categoryRepository.saveAll(
                Stream.of(camerasSubCategory).collect(Collectors.toList()));
        Assertions.assertThat(savedCategories.iterator().hasNext()).isTrue();
    }

    @Test
    public void testGetCategory() {

        Category category = categoryRepository.findById(2).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();
        for (Category subCategory : children) {
            System.out.println(subCategory.getName());
        }
        Assertions.assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testGetCategoryHierarchy() {

        Iterable<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if (category.getParent() == null) {
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel) {

        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void testListRootCategories() {

        List<Category> rootCategories =
                categoryRepository.listRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(category -> System.out.println(category.getName()));
        Assertions.assertThat(rootCategories.size()).isGreaterThan(1);
        Assertions.assertThat(rootCategories.size()).isEqualTo(3);
    }

    @Test
    public void testFindByName() {
        String name = "Computers";
        String name2 = "Dresses";
        Category category = categoryRepository.findByName(name);
        Category category2 = categoryRepository.findByName(name2);

        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getName()).isEqualTo(name);

        Assertions.assertThat(category2).isNull();
    }

    @Test
    public void testFindByAlias() {
        String alias = "laptops";
        String alias2 = "dresses";
        Category category = categoryRepository.findByAlias(alias);
        Category category2 = categoryRepository.findByAlias(alias2);

        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getAlias()).isEqualTo(alias);

        Assertions.assertThat(category2).isNull();
    }

    @AfterEach
    void tearDown() {
    }
}