package com.jabirinc.shopmebackend.user.controller;

import com.jabirinc.shopmebackend.config.ShopmeUserDetails;
import com.jabirinc.shopmebackend.exception.UserNotFoundException;
import com.jabirinc.shopmebackend.user.export.AbstractExporter;
import com.jabirinc.shopmebackend.user.export.UserCsvExporter;
import com.jabirinc.shopmebackend.user.export.UserExcelExporter;
import com.jabirinc.shopmebackend.user.export.UserPdfExporter;
import com.jabirinc.shopmebackend.user.UserService;
import com.jabirinc.shopmebackend.utils.FileUploadUtil;
import com.jabirinc.shopmecommon.entity.Role;
import com.jabirinc.shopmecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public String listFirstPage(Model model) {

        return listByPage(1, model, UserService.DEFAULT_SORT_PROP, UserService.SORT_ASC, null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword
                             ) {
        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> listOfUsers = page.getContent();

        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = UserService.SORT_ASC.equalsIgnoreCase(sortDir) ?
                UserService.SORT_DESC : UserService.SORT_ASC;

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("listOfUsers", listOfUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "users/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {

        List<Role> listRoles = userService.loadRoles();

        model.addAttribute("user", new User());
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");
        return "users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal ShopmeUserDetails loggedUser,
                           @RequestParam("userImage") MultipartFile multipartFile) throws IOException {

        User savedUser;
        if (!multipartFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            savedUser = userService.save(user);

            String uploadDir = FileUploadUtil.USER_UPLOAD_DIRECTORY + savedUser.getId();

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            savedUser = userService.save(user);
        }

        // an Admin can edit other users. if editing one's own info, update principal user
        if (loggedUser.getUser().getId().equals(savedUser.getId())) {
            loggedUser.setUser(savedUser);
        }
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

        return getRedirectURLToAffectedUser(savedUser);
    }

    private String getRedirectURLToAffectedUser(User user) {
        // use first part of email as a unique identifier for filtering table after successful save
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
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
            return "users/user_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);

            String uploadDir = FileUploadUtil.USER_UPLOAD_DIRECTORY + id;
            FileUploadUtil.removeDirectory(uploadDir);

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

    @GetMapping(value = "/users/export/csv", produces = AbstractExporter.CONTENT_TYPE_CSV)
    public void exportToCSV(HttpServletResponse response) {

        List<User> listOfUsers = userService.listAll();

        UserCsvExporter userCsvExporter = new UserCsvExporter();
        userCsvExporter.export(listOfUsers, response);

    }

    @GetMapping(value = "/users/export/excel")
    public void exportToExcel(HttpServletResponse response) {

        List<User> listOfUsers = userService.listAll();

        UserExcelExporter userExcelExporter = new UserExcelExporter();
        userExcelExporter.export(listOfUsers, response);

    }

    @GetMapping(value = "/users/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportToPdf(HttpServletResponse response) {

        List<User> listOfUsers = userService.listAll();

        UserPdfExporter userExcelExporter = new UserPdfExporter();
        userExcelExporter.export(listOfUsers, response);

    }
}
