package com.scnsoft.art.repository.specification;

import com.scnsoft.art.entity.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class OrganizationSpecification {

    public static Specification<Organization> nameContain(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Organization> statusEquals(Organization.Status status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
