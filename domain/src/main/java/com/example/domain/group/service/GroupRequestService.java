package com.example.domain.group.service;

import com.example.domain.group.exception.GroupRequestException;
import com.example.domain.group.model.GroupRequest;
import com.example.domain.group.repository.GroupRequestRepository;
import com.example.domain.user.model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class GroupRequestService {
    @Autowired
    private GroupRequestRepository repository;

    public GroupRequest get(String id) {
        return repository.findById(id).orElseThrow(GroupRequestException::notFound);
    }

    public GroupRequest create(String name, String description, Operator operator) {
        GroupRequest groupRequest = GroupRequest.builder()
                .name(name)
                .description(description)
                .createdBy(operator.getUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return repository.save(groupRequest);
    }
}
