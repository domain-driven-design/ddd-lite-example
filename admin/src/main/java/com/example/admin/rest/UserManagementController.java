package com.example.admin.rest;

import com.example.admin.service.UserManagementApplicationService;
import com.example.admin.usecases.GetUsersCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/management/users")
@AllArgsConstructor
public class UserManagementController {
    private final UserManagementApplicationService applicationService;

    @GetMapping
    public List<GetUsersCase.Response> getUsers() {
        return applicationService.getUsers();
    }
}
