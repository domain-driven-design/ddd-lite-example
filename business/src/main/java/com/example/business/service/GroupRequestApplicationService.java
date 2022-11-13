package com.example.business.service;

import com.example.business.usecase.group.CreateGroupRequestCase;
import com.example.domain.group.model.GroupRequest;
import com.example.domain.group.service.GroupRequestService;
import com.example.domain.iam.user.model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupRequestApplicationService {
    @Autowired
    private GroupRequestService groupRequestService;

    public CreateGroupRequestCase.Response createGroup(CreateGroupRequestCase.Request request, Operator operator) {
        GroupRequest groupRequest = groupRequestService.create(request.getName(), request.getDescription(), operator);

        return CreateGroupRequestCase.Response.from(groupRequest);
    }
}
