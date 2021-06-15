package com.example.business.rest;

import com.example.business.service.GroupApplicationService;
import com.example.business.usecase.CreateGroupCase;
import com.example.business.usecase.GetGroupCase;
import com.example.business.usecase.GetMyGroupCase;
import com.example.business.usecase.UpdateGroupCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupApplicationService applicationService;
    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping
    @ResponseStatus(CREATED)
    public CreateGroupCase.Response createGroup(@RequestBody CreateGroupCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.createGroup(request, authorize);
    }

    @GetMapping()
    public Page<GetGroupCase.Response> getGroupsByPage(@PageableDefault Pageable pageable) {
        return applicationService.getGroupsByPage(pageable);
    }

    @GetMapping("/mine")
    public Page<GetMyGroupCase.Response> getMyGroupsByPage(@PageableDefault Pageable pageable) {
        Authorize authorize = authorizeService.current();
        return applicationService.getMineGroupsByPage(pageable, authorize);
    }

    @PutMapping("/{id}")
    public UpdateGroupCase.Response updateGroup(@PathVariable String id,
                                                @RequestBody UpdateGroupCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.updateGroup(id, request, authorize);
    }

}
