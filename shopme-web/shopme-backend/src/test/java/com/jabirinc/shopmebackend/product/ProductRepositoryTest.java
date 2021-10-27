package com.jabirinc.shopmebackend.product;

import com.jabirinc.shopmecommon.entity.Brand;
import com.jabirinc.shopmecommon.entity.Category;
import com.jabirinc.shopmecommon.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * public @interface DataJpaTest
 * Annotation for a JPA test that focuses only on JPA components.
 * Using this annotation will disable full auto-configuration and instead apply only configuration relevant to JPA tests.
 * By default, tests annotated with @DataJpaTest are transactional and roll back at the end of each test.
 * They also use an embedded in-memory database (replacing any explicit or usually auto-configured DataSource).
 * The @AutoConfigureTestDatabase annotation can be used to override these settings.
 * SQL queries are logged by default by setting the spring.jpa.show-sql property to true. This can be disabled using
 * the showSql attribute.
 * If you are looking to load your full application configuration, but use an embedded database, you should consider
 * @SpringBootTest combined with @AutoConfigureTestDatabase rather than this annotation.
 * When using JUnit 4, this annotation should be used in combination with @RunWith(SpringRunner.class).
 *
 * public class TestEntityManager
 * Alternative to EntityManager for use in JPA tests. Provides a subset of EntityManager methods that are useful
 * for tests as well as helper methods for common testing tasks such as persist/flush/find.
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testCreateProduct() {

        // product I
        /*Brand brand = testEntityManager.find(Brand.class, 10);
        Category category = testEntityManager.find(Category.class, 15);

        Product product = Product.builder()
                .name("Samsung Galaxy A31").alias("samsung_galaxy_a31")
                .shortDescription("A good smartphone from Samsung")
                .fullDescription("This is a very good smartphone full description")
                .brand(brand).category(category)
                .price(456).createdDate(new Date()).updatedDate(new Date())
                .build();

        Product savedProduct = productRepository.save(product);
        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getId()).isGreaterThan(0);*/


        // product II
        /*Brand brand2 = testEntityManager.find(Brand.class, 38);
        Category category2 = testEntityManager.find(Category.class, 6);

        Product product2 = Product.builder()
                .name("Dell Inspiron 3000").alias("dell_inspiron_3000")
                .shortDescription("Short description for Dell Inspiron 3000")
                .fullDescription("Full description for Dell Inspiron 3000")
                .brand(brand2).category(category2)
                .price(456).cost(400).enabled(true).inStock(true)
                .createdDate(new Date()).updatedDate(new Date())
                .build();

        Product savedProduct2 = productRepository.save(product2);
        Assertions.assertThat(savedProduct2).isNotNull();
        Assertions.assertThat(savedProduct2.getId()).isGreaterThan(0);*/

        // product III
        Brand brand3 = testEntityManager.find(Brand.class, 38);
        Category category3 = testEntityManager.find(Category.class, 6);

        Product product3 = Product.builder()
                .name("Acer Aspire Desktop").alias("acer_aspire_desktop")
                .shortDescription("Short description for Acer Aspire Desktop")
                .fullDescription("Full description for Acer Aspire Desktop")
                .brand(brand3).category(category3)
                .price(678).cost(600).enabled(true).inStock(true)
                .createdDate(new Date()).updatedDate(new Date())
                .build();

        Product savedProduct3 = productRepository.save(product3);
        Assertions.assertThat(savedProduct3).isNotNull();
        Assertions.assertThat(savedProduct3.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts() {

        Iterable<Product> products = productRepository.findAll();

        products.forEach(System.out::println);
    }

    @Test
    public void testGetProduct() {

        Integer id = 1;
        Product product = productRepository.findById(id).get();

        Assertions.assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct() {

        Integer id = 1;
        float price = 499;
        Product product = productRepository.findById(id).get();
        product.setPrice(price);

        productRepository.save(product);
        Product savedProduct = testEntityManager.find(Product.class, id);

        Assertions.assertThat(savedProduct.getPrice()).isEqualTo(price);
    }

    @Test
    public void testDeleteProduct() {

        Integer id = 3;
        productRepository.deleteById(id);
        Optional<Product> optProduct = productRepository.findById(id);

        Assertions.assertThat(!optProduct.isPresent());
        Assertions.assertThat(optProduct.isPresent()).isFalse();
    }
}