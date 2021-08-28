package com.example.admin.rest;

import com.example.admin.service.UserManagementApplicationService;
import com.example.admin.usecases.GetUserDetailCase;
import com.example.admin.usecases.GetUsersCase;
import com.example.admin.usecases.SuggestUsersCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/users")
public class UserManagementController {
    @Autowired
    private UserManagementApplicationService applicationService;

    @GetMapping
    public Page<GetUsersCase.Response> getUsers(Pageable pageable) {
        return applicationService.getUsers(pageable);
    }

    // TODO 要不要和上面的query合并？
    @GetMapping("/suggest")
    public Page<SuggestUsersCase.Response> suggestUsers(@RequestParam(required = false) String keyword, Pageable pageable) {
        return applicationService.suggestUsers(keyword, pageable);
    }

    @GetMapping("/{id}")
    public GetUserDetailCase.Response getUserDetail(@PathVariable("id") String id) {
        return applicationService.getUserDetail(id);
    }
}
