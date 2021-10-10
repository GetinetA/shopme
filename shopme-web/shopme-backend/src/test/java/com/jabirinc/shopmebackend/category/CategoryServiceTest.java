package com.jabirinc.shopmebackend.category;

import com.jabirinc.shopmecommon.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *      ==== How to write correct unit test ====
 * While writing junit test for a service or controller method, we shall keep in mind that:
 *
 *      - A unit test is supposed to test only a certain part of code (i.e. code written in service class),
 *      so we shall mock all the dependencies injected and used in service class.
 *      - If the test utilizes other dependencies (e.g. database/network) then it is integration testing and not
 *      unit testing.
 *      - We should not use any webserver otherwise it will make the unit testing slow.
 *      - Each unit test should be independent of other tests.
 *      - By definition, unit tests should be fast.
 *
 * @ExtendWith(MockitoExtension.class) – [ Junit 5 ]
 * MockitoExtension initializes mocks and handles strict stubbings. It is equivalent of the MockitoJUnitRunner [Junit 4].
 *
 * @ExtendWith(SpringExtension.class) - [ Junit 5 ]
 * SpringExtension integrates the Spring TestContext Framework into JUnit 5's Jupiter programming model.
 *
 * @Mock is used for mock creation. It makes the test class more readable. In test class, to process mockito
 * annotations, MockitoAnnotations.initMocks(testClass) must be used at least once.
 * If you are using  ExtendWith(MockitoExtension.class)/RunWith(MockitoJUnitRunner.class) then explicit usage of
 * MockitoAnnotations.initMocks() is not necessary.
 * Mock does not matter which application framework we use. We can achieve this mocking behavior using @Mock whether
 * we use Spring Boot or any other framework like Jakarta EE, Quarkus, Micronaut, Helidon, etc.
 * Mocks are initialized before each test method.
 * Use @Mock in unit testing where spring test context is not needed. When unit testing your business logic (only
 * using JUnit and Mockito).
 *
 * @MockBean same as @Mock but related to Spring context. Both annotations create mock objects but with a slightly
 * different purpose. In other words, with respect to Spring framework, we want to test our layer (web, service
 * or repository) in isolation and don't care what's going on inside the other layers for such a test. That's why we
 * se @MockBean here to place a mocked version of a component (from other layer) inside the context to satisfy
 * our class under test.
 *
 * Use @MockBean when you write a test that is backed by a Spring Test Context and you want to add or replace a
 * bean with a mocked version of it.
 *
 * @InjectMocks also creates the mock implementation, additionally injects the dependent mocks that are marked with
 * the annotations @Mock into it.
 *
 * Created by Getinet on 10/9/21
 */

@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repo;

    @InjectMocks
    private CategoryService service;

    @BeforeEach
    void setUp() {
        //// -> have to init to avoid NPE whencategoryRepository.findByName(name) in CategoryService
        //MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCheckUniqueInNewModeReturnDuplicateName() {
        Integer id = null;
        String name = "Computers";
        String alias = "abc";
        Category category = Category.create(id, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(category);
        //Mockito.when(repo.findByAlias(alias)).thenReturn(null);

        String result = service.isCategoryUnique(id, name, alias);
        Assertions.assertThat(result).isEqualTo("Duplicate Name");

    }

    @Test
    void testCheckUniqueInNewModeReturnDuplicateAlias() {
        Integer id = null;
        String name = "ACME";
        String alias = "computers";
        Category category = Category.create(id, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);

        String result = service.isCategoryUnique(id, name, alias);
        Assertions.assertThat(result).isEqualTo("Duplicate Alias");
    }


    @Test
    void testCheckUniqueInNewModeReturnOK() {
        Integer id = null;
        String name = "ACME";
        String alias = "computers";
        //Category category = Category.create(id, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);

        String result = service.isCategoryUnique(id, name, alias);
        Assertions.assertThat(result).isEqualTo("OK");

    }

    @Test
    void testCheckUniqueInEditModeReturnDuplicateName() {
        Integer id = 1;
        String name = "Computers";
        String alias = "abc";
        Category category = Category.create(2, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(category);
        //Mockito.when(repo.findByAlias(alias)).thenReturn(null);

        String result = service.isCategoryUnique(id, name, alias);

        Assertions.assertThat(result).isEqualTo("Duplicate Name");

    }

    @Test
    void testCheckUniqueInEditModeReturnDuplicateAlias() {
        Integer id = 1;
        String name = "ACME";
        String alias = "computers";
        Category category = Category.create(2, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);

        String result = service.isCategoryUnique(id, name, alias);
        Assertions.assertThat(result).isEqualTo("Duplicate Alias");

    }

    @Test
    void testCheckUniqueInEditModeReturnOK() {
        Integer id = 1;
        String name = "ACME";
        String alias = "computers";

        Category category = Category.create(id, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);

        String result = service.isCategoryUnique(id, name, alias);
        Assertions.assertThat(result).isEqualTo("OK");

    }


}