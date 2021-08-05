package com.example.business.service;

import com.example.business.usecase.group.CreateGroupCase;
import com.example.business.usecase.group.GetGroupCase;
import com.example.business.usecase.group.GetGroupDetailCase;
import com.example.business.usecase.group.GetMyGroupCase;
import com.example.business.usecase.group.ChangeGroupOwnerCase;
import com.example.business.usecase.group.JoinGroupCase;
import com.example.business.usecase.group.UpdateGroupCase;
import com.example.business.usecase.group.UpdateGroupMemberCase;
import com.example.domain.auth.model.Authorize;
import com.example.domain.group.model.Group;
import com.example.domain.group.model.GroupMember;
import com.example.domain.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.List;

@Service
public class GroupApplicationService {
    @Autowired
    private GroupService groupService;

    public CreateGroupCase.Response createGroup(CreateGroupCase.Request request, Authorize authorize) {
        Group group = groupService.create(request.getName(), request.getDescription(), authorize.getUserId());
        return CreateGroupCase.Response.from(group);
    }

    public GetGroupDetailCase.Response getGroupDetail(String id) {
        Group group = groupService.get(id);

        return GetGroupDetailCase.Response.from(group);
    }

    public Page<GetGroupCase.Response> getGroupsByPage(Pageable pageable) {
        Page<Group> groupPage = groupService.findAll(null, pageable);

        return groupPage.map(GetGroupCase.Response::from);
    }

    public Page<GetMyGroupCase.Response> getMineGroupsByPage(Pageable pageable, Authorize authorize) {
        Specification<Group> specification = (root, query, criteriaBuilder) -> {
            Join<Group, List<GroupMember>> join = root.join(Group.Fields.members, JoinType.LEFT);
            return criteriaBuilder.equal(join.get(GroupMember.Fields.userId), authorize.getUserId());
        };
        Page<Group> groupPage = groupService.findAll(specification, pageable);

        return groupPage.map(GetMyGroupCase.Response::from);
    }

    public UpdateGroupCase.Response updateGroup(String id, UpdateGroupCase.Request request, Authorize authorize) {
        Group group = groupService.update(id, request.getName(), request.getDescription(), authorize.getUserId());
        return UpdateGroupCase.Response.from(group);
    }

    public JoinGroupCase.Response joinGroup(String id, Authorize authorize) {
        GroupMember member = groupService.addNormalMember(id, authorize.getUserId());
        return JoinGroupCase.Response.from(member);
    }

    public void exitGroup(String id, Authorize authorize) {
        groupService.deleteNormalMember(id, authorize.getUserId());
    }

    public UpdateGroupMemberCase.Response updateMember(String id, String memberId,
                                                       UpdateGroupMemberCase.Request request,
                                                       Authorize authorize) {
        GroupMember groupMember = groupService.changeMemberRole(id, memberId, request.getRole(), authorize.getUserId());

        return UpdateGroupMemberCase.Response.from(groupMember);
    }

    public ChangeGroupOwnerCase.Response changeOwner(String id,
                                                       ChangeGroupOwnerCase.Request request,
                                                       Authorize authorize) {
        GroupMember groupMember = groupService.changeOwner(id, request.getMemberId(), authorize.getUserId());
        return ChangeGroupOwnerCase.Response.from(groupMember);
    }

    public void removeMember(String id, String memberId, Authorize authorize) {
        groupService.deleteMember(id, memberId, authorize.getUserId());
    }
}
