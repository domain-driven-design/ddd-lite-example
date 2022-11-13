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
import com.example.domain.iam.auth.service.AuthorizeService;
import com.example.domain.iam.user.model.Operator;
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
        Operator operator = authorizeService.getOperator();
        return applicationService.createGroup(request, operator);
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
        Operator operator = authorizeService.getOperator();
        return applicationService.getMineGroupsByPage(pageable, operator);
    }

    @PutMapping("/{id}")
    public UpdateGroupCase.Response updateGroup(@PathVariable String id,
                                                @RequestBody @Valid UpdateGroupCase.Request request) {
        Operator operator = authorizeService.getOperator();
        return applicationService.updateGroup(id, request, operator);
    }

    @GetMapping("/{id}/members")
    public Page<GetGroupMemberCase.Response> getGroupMembers(@PathVariable String id, Pageable pageable) {
        return applicationService.getGroupMembers(id, pageable);
    }

    @GetMapping("/{id}/members/management")
    public Page<GetGroupMemberCase.Response> getGroupManagementMembers(@PathVariable String id, Pageable pageable) {
        return applicationService.getGroupManagementMembers(id, pageable);
    }

    @PostMapping("/{id}/members/me")
    @ResponseStatus(CREATED)
    public JoinGroupCase.Response joinGroup(@PathVariable String id) {
        Operator operator = authorizeService.getOperator();
        return applicationService.joinGroup(id, operator);
    }

    @DeleteMapping("/{id}/members/me")
    public void exitGroup(@PathVariable String id) {
        Operator operator = authorizeService.getOperator();
        applicationService.exitGroup(id, operator);
    }

    @PutMapping("/{id}/members/{userId}")
    public UpdateGroupMemberCase.Response updateMember(@PathVariable String id,
                                                       @PathVariable String userId,
                                                       @RequestBody @Valid UpdateGroupMemberCase.Request request) {
        Operator operator = authorizeService.getOperator();
        return applicationService.updateMember(id, userId, request, operator);
    }

    @PutMapping("/{id}/owner")
    public ChangeGroupOwnerCase.Response changeOwner(@PathVariable String id,
                                                     @RequestBody @Valid ChangeGroupOwnerCase.Request request) {
        Operator operator = authorizeService.getOperator();
        return applicationService.changeOwner(id, request, operator);
    }

    @DeleteMapping("/{id}/members/{userId}")
    public void removeMember(@PathVariable String id, @PathVariable String userId) {
        Operator operator = authorizeService.getOperator();
        applicationService.removeMember(id, userId, operator);
    }

}
