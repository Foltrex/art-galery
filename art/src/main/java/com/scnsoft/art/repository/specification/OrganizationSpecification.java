package com.scnsoft.art.repository.specification;

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@RequiredArgsConstructor
public class OrganizationSpecification {

    public static Specification<Organization> nameContain(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Organization.Fields.name)), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Organization> statusEquals(Organization.Status status) {
        return (root, query, cb) -> cb.equal(root.get(Organization.Fields.status), status);
    }
    public static Specification<Organization> organizationIdEquals(UUID organizationId) {
        return (root, query, cb) -> cb.equal(root.get(Organization.Fields.id), organizationId);
    }

    public static Specification<Organization> facilityIdEqual(UUID facilityId) {
        return (root, query, cb) -> cb.equal(root.get(Organization.Fields.facilities).get(Facility.Fields.id), facilityId);
    }
}
