package com.example.admin.service;

import com.example.admin.usecases.group.GetGroupsCase;
import com.example.domain.group.model.Group;
import com.example.domain.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupManagementApplicationService {
    @Autowired
    private GroupService groupService;

    public Page<GetGroupsCase.Response> getGroups(String keyword, Pageable pageable) {
        Specification<Group> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (keyword != null) {
                List<Predicate> keywordPredicateList = new ArrayList<>();
                keywordPredicateList.add(criteriaBuilder.like(root.get(Group.Fields.name), '%' + keyword + '%'));
                predicateList.add(criteriaBuilder.or(keywordPredicateList.toArray(new Predicate[0])));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };

        Page<Group> page = groupService.findAll(specification, pageable);

        return page.map(GetGroupsCase.Response::from);
    }
}
