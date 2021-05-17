package com.example.admin.rest;

import com.example.admin.service.UserManagementApplicationService;
import com.example.admin.usecases.CreateAdminCase;
import com.example.admin.usecases.GetUserDetailCase;
import com.example.admin.usecases.GetUsersCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/management/users")
public class UserManagementController {
    @Autowired
    private UserManagementApplicationService applicationService;

    @Autowired
    private AuthorizeService authorizeService;

    @GetMapping
    public List<GetUsersCase.Response> getUsers() {
        return applicationService.getUsers();
    }

    @GetMapping("/{id}")
    public GetUserDetailCase.Response getUserDetail(@PathVariable("id") String id) {
        return applicationService.getUserDetail(id);
    }

    @PostMapping("/admins")
    @ResponseStatus(CREATED)
    public CreateAdminCase.Response createAdmin(@RequestBody CreateAdminCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.createAdmin(request, authorize);
    }
}
