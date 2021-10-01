package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.config.ShopmeUserDetails;
import com.jabirinc.shopmebackend.user.UserService;
import com.jabirinc.shopmebackend.utils.FileUploadUtil;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * Created by Getinet on 9/30/21
 */
@Controller
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser,
                              Model model) {

        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);
        model.addAttribute("user", user);
        return "users/account_form";
    }

    @PostMapping("/account/update")
    public String updateDetails(User user, RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                                @RequestParam("userImage") MultipartFile multipartFile) throws IOException {

        User savedUser = user;
        if (!multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            savedUser = userService.updateAccount(user);

            String uploadDir = FileUploadUtil.UPLOAD_DIRECTORY + savedUser.getId();

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            savedUser = userService.updateAccount(user);
        }

        loggedUser.setUser(savedUser);
        redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
        return "redirect:/account";
    }
}
