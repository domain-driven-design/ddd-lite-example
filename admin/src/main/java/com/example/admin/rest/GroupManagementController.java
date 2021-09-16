package com.example.admin.rest;

import com.example.admin.service.GroupManagementApplicationService;
import com.example.admin.usecases.group.GetGroupsCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/groups")
public class GroupManagementController {
    @Autowired
    private GroupManagementApplicationService applicationService;

    @GetMapping
    public Page<GetGroupsCase.Response> getQuestions(@RequestParam(required = false) String keyword,
                                                     Pageable pageable) {
        return applicationService.getGroups(keyword, pageable);
    }
}
