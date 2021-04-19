package com.example.admin.service;

import com.example.admin.usecases.GetUsersCase;
import com.example.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserManagementApplicationService {
    private final UserRepository repository;

    public List<GetUsersCase.Response> getUsers() {
        return repository.findAll().stream()
                .map(GetUsersCase.Response::from)
                .collect(Collectors.toList());
    }
}
