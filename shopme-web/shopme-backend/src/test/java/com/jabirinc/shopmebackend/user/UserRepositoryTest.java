package com.jabirinc.shopmebackend.user;

import com.jabirinc.shopmecommon.entity.Role;
import com.jabirinc.shopmecommon.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateNewUserWithOneRole() {

        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User userGeti = new User("geti@jabirinc.com", "geti2020", "Geti", "Ale");
        userGeti.addRole(roleAdmin);

        User savedUser = userRepository.save(userGeti);

        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    void testCreateNewUserWithMultipleRole() {

        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        User userHewi = new User("hewi@jabirinc.com", "hewi2020", "Hewi", "Worku");

        userHewi.addRole(roleEditor);
        userHewi.addRole(roleAssistant);

        User savedUser = userRepository.save(userHewi);
        assertThat(savedUser.getRoles().size()).isGreaterThan(1);

        //Role salesPerson = testEntityManager.find(Role.class, 2);
        //Role shipper = testEntityManager.find(Role.class, 4);

        //User ravi = userRepository.findById(2).orElse(null);
        //ravi.addRole(salesPerson);
        //ravi.addRole(shipper);
        //User savedUser = userRepository.save(ravi);

        //assertThat(savedUser.getRoles().size()).isGreaterThan(1);
    }

    @Test
    void testListAllUsers() {

        Iterable<User> listOfusers = userRepository.findAll();
        listOfusers.forEach(user -> System.out.println(user));
    }

    @Test
    void testUpdateUserDetails() {

        User firstUser = userRepository.findById(1).get();
        firstUser.setEnabled(true);
        firstUser.setEmail("getti@jabirinc.com");

        userRepository.save(firstUser);
    }

    @Test
    void testUpdateUserRole() {

        User huser = userRepository.findById(5).get();
        Role roleAdmin = new Role(1);
        Role roleAssistant = new Role(5);

        huser.getRoles().remove(roleAssistant);
        huser.addRole(roleAdmin);

        userRepository.save(huser);
    }

    @Test
    void testDeleteUser() {
        // delete user: Ravi
        userRepository.deleteById(2);
        User userRet = userRepository.findById(2).orElse(null);

        assertNull(userRet);
    }

    @Test
    void testGetUserByEmail() {
        String email = "getti@jabirinc.com";

        User user = userRepository.getUserByEmail(email);
        Assertions.assertThat(user).isNotNull();
    }

    @Test
    void testCountById() {
        Integer id = 1;
        Long count = userRepository.countById(id);

        Assertions.assertThat(count).isGreaterThan(0);
    }

    @Test
    void testDisableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id, false);

        User user = userRepository.findById(id).get();
        Assertions.assertThat(user.isEnabled()).isFalse();
    }

    @Test
    void testEnableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id, true);

        User user = userRepository.findById(id).get();
        Assertions.assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);
        List<User> listOfUsers = page.getContent();

        listOfUsers.forEach(user -> System.out.println(user));
        Assertions.assertThat(listOfUsers.size()).isEqualTo(pageSize);
    }

    @AfterEach
    void tearDown() {
    }
}