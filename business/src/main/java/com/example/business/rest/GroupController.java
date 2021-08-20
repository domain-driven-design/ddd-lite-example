package com.example.business.rest;

import com.example.business.service.GroupApplicationService;
import com.example.business.usecase.group.ChangeGroupOwnerCase;
import com.example.business.usecase.group.CreateGroupCase;
import com.example.business.usecase.group.GetGroupCase;
import com.example.business.usecase.group.GetGroupDetailCase;
import com.example.business.usecase.group.GetGroupMemberCase;
import com.example.business.usecase.group.GetMyGroupCase;
import com.example.business.usecase.group.JoinGroupCase;
import com.example.business.usecase.group.UpdateGroupCase;
import com.example.business.usecase.group.UpdateGroupMemberCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.auth.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public CreateGroupCase.Response createGroup(@RequestBody @Valid CreateGroupCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.createGroup(request, authorize);
    }

    @GetMapping("/{id}")
    public GetGroupDetailCase.Response getGroupDetail(@PathVariable String id) {
        return applicationService.getGroupDetail(id);
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
                                                @RequestBody @Valid UpdateGroupCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.updateGroup(id, request, authorize);
    }

    @GetMapping("/{id}/members")
    public Page<GetGroupMemberCase.Response> getGroupMembers(@PathVariable String id, Pageable pageable) {
        return applicationService.getGroupMembers(id, pageable);
    }

    @GetMapping("/{id}/members/management")
    public Page<GetGroupMemberCase.Response> getGroupManagementMembers(@PathVariable String id, Pageable pageable) {
        return applicationService.getGroupManagementMembers(id, pageable);
    }

    @PostMapping("/{id}/members")
    @ResponseStatus(CREATED)
    public JoinGroupCase.Response joinGroup(@PathVariable String id) {
        Authorize authorize = authorizeService.current();
        return applicationService.joinGroup(id, authorize);
    }

    @DeleteMapping("/{id}/members/me")
    public void exitGroup(@PathVariable String id) {
        Authorize authorize = authorizeService.current();
        applicationService.exitGroup(id, authorize);
    }

    @PutMapping("/{id}/members/{userId}")
    public UpdateGroupMemberCase.Response updateMember(@PathVariable String id,
                                                       @PathVariable String userId,
                                                       @RequestBody @Valid UpdateGroupMemberCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.updateMember(id, userId, request, authorize);
    }

    @PutMapping("/{id}/owner")
    public ChangeGroupOwnerCase.Response changeOwner(@PathVariable String id,
                                                     @RequestBody @Valid ChangeGroupOwnerCase.Request request) {
        Authorize authorize = authorizeService.current();
        return applicationService.changeOwner(id, request, authorize);
    }

    @DeleteMapping("/{id}/members/{userId}")
    public void removeMember(@PathVariable String id, @PathVariable String userId) {
        Authorize authorize = authorizeService.current();
        applicationService.removeMember(id, userId, authorize);
    }

}
