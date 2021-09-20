package com.example.domain.group.repository;

import com.example.domain.group.model.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRequestRepository extends JpaRepository<GroupRequest, String>,
        JpaSpecificationExecutor<GroupRequest> {
}
