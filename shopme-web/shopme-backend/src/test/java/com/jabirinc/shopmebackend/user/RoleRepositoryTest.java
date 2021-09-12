package com.jabirinc.shopmebackend.user;

import com.jabirinc.shopmecommon.entity.Role;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Getinet on 9/11/21
 * We need to use the annotation @DataJpaTest to leverage the Data JPA
 * test of Spring Data.
 *
 * @AutoConfigureTestDatabase
 * By default, Spring Data JPA will run the test against an in-memory
 * database. To test with real database, we need to override the default
 * configuration. We will run the unit test methods against a real database, so we
 * need to use @AutoConfigureTestDatabase annotation.
 *
 * @Rollback
 * By default, Spring Data JPA test will roll back the transaction after the test execution
 * Use @Rollback annotation to tell Spring Data JPA test to commit the changes after running
 * a test
 *
 * ======== Testing Error ===========
 * java.lang.IllegalArgumentException: Not a managed type: class com.jabirinc.shopmecommon.entity.Role
 * Component scan couldn't find Role Entity  & Repository as it is located in a different module - shopme-common.
 * Declare component scan using @EntityScan annotation in
 * com.jabirinc.shopmebackend.ShopmeBackendApplication.java as
 *      @EntityScan({"com.jabirinc.shopmebackend.entity", "com.jabirinc.shopmebackend.user"})
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testCreateFirstRole() {

        Role roleAdmin = new Role("Admin", "manage everything");
        Role savedRole = roleRepository.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    void testCreateRestOfRoles() {

        Role roleSalesperson = new Role("Salesperson",
                "manage product price, customers, shipping, orders and sales report");
        Role roleEditor = new Role("Editor",
                "manage categories, brands, products, articles and menus");
        Role roleShipper = new Role("Shipper",
                "view orders and update order status");
        Role roleAssistant = new Role("Assistant",
                "manage questions and reviews");

        roleRepository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
    }

    @AfterEach
    void tearDown() {
    }
}