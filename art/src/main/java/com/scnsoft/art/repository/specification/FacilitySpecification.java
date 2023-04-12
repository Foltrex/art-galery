package com.scnsoft.art.repository.specification;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;
import com.scnsoft.art.dto.FacilityFilter;
import com.scnsoft.art.entity.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.entity.Facility;

@RequiredArgsConstructor
public class FacilitySpecification implements Specification<Facility>{
    private final FacilityFilter filter;

    @Override
    public Predicate toPredicate(Root<Facility> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var predicates = new ArrayList<Predicate>();
        if(filter.getId() != null) {
            predicates.add(cb.equal(root.get(Facility.Fields.id), filter.getId() ));
        }
        if (filter.getCityId() != null) {
            Join<Facility, Address> address = root.join(Facility.Fields.address);
            Join<Address, City> city = address.join(Address.Fields.city);
            predicates.add(cb.equal(city.get(City.Fields.id), filter.getCityId()));
        }
        if (!Strings.isNullOrEmpty(filter.getFacilityName())) {
            String facilityName = "%" + filter.getFacilityName() + "%";
            predicates.add(cb.like(root.get(Facility.Fields.name), facilityName));
        }
        if (filter.getIsActive() != null) {
            predicates.add(cb.equal(root.get(Facility.Fields.isActive), filter.getIsActive()));
        }
        if(filter.getOrganizationId() != null) {
            predicates.add(cb.equal(
                    root.get(Facility.Fields.organization).get(Organization.Fields.id),
                    filter.getOrganizationId()));
        }
        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
