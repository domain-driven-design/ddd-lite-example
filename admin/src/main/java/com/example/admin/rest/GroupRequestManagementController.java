package com.example.admin.rest;

import com.example.admin.service.GroupRequestManagementApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/group-requests")
public class GroupRequestManagementController {
    @Autowired
    private GroupRequestManagementApplicationService applicationService;

}
