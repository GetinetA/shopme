package com.jabirinc.shopmebackend.controller;

import com.jabirinc.shopmebackend.exception.UserNotFoundException;
import com.jabirinc.shopmebackend.user.UserService;
import com.jabirinc.shopmecommon.entity.Role;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by Getinet on 9/13/21
 */
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listAll(Model model) {
        List<User> listOfUsers = userService.listAll();
        model.addAttribute("listOfUsers",listOfUsers);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {

        List<Role> listRoles = userService.loadRoles();

        model.addAttribute("user", new User());
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {

        //System.out.println(user);
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model, RedirectAttributes redirectAttributes) {

        try {
            User user = userService.findById(id);
            List<Role> listRoles = userService.loadRoles();
            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Edit User (ID : " + id + ")");
            return "user_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                           Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "The user ID " + id + " has been deleted successfully");
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String UpdateUserEenabledStatus(@PathVariable(name = "id") Integer id,
                                           @PathVariable(name = "status") boolean enabled,
                                           RedirectAttributes redirectAttributes) {

        userService.updateEnabledStatus(id, enabled);
        String status = enabled? "enabled":"disabled";
        redirectAttributes.addFlashAttribute("message",
                "The user account with ID " + id + " has been " + status);

        return "redirect:/users";
    }
}
