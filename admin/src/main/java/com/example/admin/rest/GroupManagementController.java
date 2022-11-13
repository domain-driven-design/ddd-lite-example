package com.example.admin.rest;

import com.example.admin.service.GroupManagementApplicationService;
import com.example.admin.usecases.group.CreateGroupCase;
import com.example.admin.usecases.group.GetGroupsCase;
import com.example.domain.iam.auth.service.AuthorizeService;
import com.example.domain.iam.user.model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/management/groups")
public class GroupManagementController {
    @Autowired
    private GroupManagementApplicationService applicationService;
    @Autowired
    private AuthorizeService authorizeService;

    @GetMapping
    public Page<GetGroupsCase.Response> getGroups(@RequestParam(required = false) String keyword,
                                                     Pageable pageable) {
        return applicationService.getGroups(keyword, pageable);
    }

    @PostMapping
    public CreateGroupCase.Response createGroup(@RequestBody @Valid CreateGroupCase.Request request) {
        Operator operator = authorizeService.getOperator();

        return applicationService.creteGroup(request, operator);
    }
}
