package com.example.domain.auth.repository;

import com.example.domain.auth.model.Authorize;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeRepository {
    Authorize create(Authorize authorize);

    Authorize get(String id);

    void delete(String id);
}
