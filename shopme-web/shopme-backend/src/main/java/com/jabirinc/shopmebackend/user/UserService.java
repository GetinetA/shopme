package com.jabirinc.shopmebackend.user;

import com.jabirinc.shopmecommon.entity.Role;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Getinet on 9/13/21
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<Role> loadRoles() {
        return (List<Role>) roleRepository.findAll();
    }
}
