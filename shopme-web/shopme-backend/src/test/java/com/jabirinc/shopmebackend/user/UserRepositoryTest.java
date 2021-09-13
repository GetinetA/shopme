package com.jabirinc.shopmebackend.user;

import com.jabirinc.shopmecommon.entity.Role;
import com.jabirinc.shopmecommon.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
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
    void name() {
    }

    @AfterEach
    void tearDown() {
    }
}