package com.example.domain.group.repository;

import com.example.domain.group.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, String>, JpaSpecificationExecutor<GroupMember> {
}
