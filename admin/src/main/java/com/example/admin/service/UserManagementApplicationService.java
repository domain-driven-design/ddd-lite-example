package com.example.admin.service;

import com.example.admin.usecases.GetUserDetailCase;
import com.example.admin.usecases.GetUsersCase;
import com.example.domain.user.model.User;
import com.example.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementApplicationService {
    @Autowired
    private UserRepository repository;

    public List<GetUsersCase.Response> getUsers() {
        return repository.findAll().stream()
                .map(GetUsersCase.Response::from)
                .collect(Collectors.toList());
    }

    public GetUserDetailCase.Response getUserDetail(String id) {
        User user = repository.getOne(id);
        return GetUserDetailCase.Response.from(user);
    }
}
