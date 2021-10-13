package com.jabirinc.shopmebackend.brand;

import com.jabirinc.shopmecommon.entity.Brand;
import com.jabirinc.shopmecommon.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testCreateBrand() {

        // Catgegories : Laptops
        Brand brand1 = createBrand(new Integer[]{6}, "Acer");
        Brand savedBrand1 = brandRepository.save(brand1);
        Assertions.assertThat(savedBrand1).isNotNull();
        Assertions.assertThat(savedBrand1.getId()).isGreaterThan(0);

        // Catgegories : Cellphones, Tablets
        Brand brand2 = createBrand(new Integer[]{4, 7}, "Apple");
        Brand savedBrand2 = brandRepository.save(brand2);
        Assertions.assertThat(savedBrand2).isNotNull();
        Assertions.assertThat(savedBrand2.getId()).isGreaterThan(0);

        // Catgegories : Internal Hard Drive, Memory
        Brand brand3 = createBrand(new Integer[]{24, 29}, "Samsung");
        Brand savedBrand3 = brandRepository.save(brand3);
        Assertions.assertThat(savedBrand3).isNotNull();
        Assertions.assertThat(savedBrand3.getId()).isGreaterThan(0);

    }

    public Brand createBrand(Integer [] catIds, String brandName) {

        Brand brand = Brand.builder().name(brandName)
                .logo("brand-log.png").categories(new HashSet<>()).build();
        for (int i = 0; i < catIds.length; i++) {
            brand.getCategories().add(new Category(catIds[i]));
        }
        return brand;
    }

    @Test
    public void testFindAllBrand() {

        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(System.out::println);
        /*for (Brand brand : brands) {
            System.out.println("Brand ID : " + brand.getId());
            System.out.println("Brand Name : " + brand.getName());
            System.out.println("Brand Categories : " + brand.getCategories());
        }*/

        Assertions.assertThat(brands).isNotEmpty();
    }

    @Test
    public void testFindById() {

        // Exception testing - Using AssertJ
        Assertions.assertThatThrownBy(() -> {
            Brand brand1 = brandRepository.findById(1).get();
        }).isInstanceOf(NoSuchElementException.class);

        // Exception testing - Using JUnit 5
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> {
            Brand brand1 = brandRepository.findById(1).get();
        });

        Brand brand = brandRepository.findById(4).get();

        Assertions.assertThat(brand.getName()).isEqualTo("Acer");
    }

    @Test
    public void testUpdate() {

        String samsungElectronics = "Samsung Electronics";
        Brand brand = brandRepository.findById(6).get();
        brand.setName(samsungElectronics);

        Brand savedBrand = brandRepository.save(brand);

        Assertions.assertThat(savedBrand.getName()).isEqualTo(samsungElectronics);
    }

    @Test
    public void testDelete() {

        Integer appleId = 5;
        brandRepository.deleteById(appleId);

        Assertions.assertThat(brandRepository.findById(appleId).isPresent()).isFalse();
    }

    @AfterEach
    void tearDown() {
    }
}