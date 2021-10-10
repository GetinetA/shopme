package com.jabirinc.shopmebackend.user;

import com.jabirinc.shopmebackend.exception.UserNotFoundException;
import com.jabirinc.shopmecommon.entity.Role;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Getinet on 9/13/21
 */
@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE = 4;
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";
    public static final String DEFAULT_SORT_PROP = "firstName";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listAll() {
        return (List<User>) userRepository.findAll(Sort.by(DEFAULT_SORT_PROP).ascending());
    }

    public Page<User> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase(SORT_ASC) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);

        if (keyword != null) {
            return userRepository.findAll(keyword, pageable);
        }

        return userRepository.findAll(pageable);
    }

    public User save(User user) {

        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public User updateAccount(User userInForm) {

        User userInDB = userRepository.findById(userInForm.getId()).get();
        if (!userInForm.getPassword().isEmpty()) {
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }
        if (userInForm.getPhotos() != null) {
            userInDB.setPhotos(userInForm.getPhotos());
        }
        if (!StringUtils.isEmptyOrWhitespace(userInForm.getFirstName())) {
            userInDB.setFirstName(userInForm.getFirstName());
        }
        if (!StringUtils.isEmptyOrWhitespace(userInForm.getLastName())) {
            userInDB.setLastName(userInForm.getLastName());
        }

        return userRepository.save(userInDB);
    }

    public List<Role> loadRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {

        User userByEmail = userRepository.getUserByEmail(email);

        if (userByEmail == null) {
            return true;
        }
        boolean isCreatingNew = (id == null);
        if (isCreatingNew) {
            if (userByEmail != null) {
                System.out.println("isCreatingNew = true");
                return false;
            }
        } else {
            if (userByEmail.getId() != id) {
                System.out.println("isCreatingNew = false");
                return false;
            }
        }

        return true;
    }

    public User findById(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {

        Long countById = userRepository.countById(id);
        if (countById == null) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }

    public User getByEmail(String email) {

        return userRepository.getUserByEmail(email);
    }
}
