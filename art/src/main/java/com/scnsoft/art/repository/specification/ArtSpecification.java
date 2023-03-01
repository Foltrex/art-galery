package com.scnsoft.art.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.entity.Representative;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArtSpecification {

    public static Specification<Art> artInfosIsEmpty() {
        return new Specification<Art>() {
            @Override
            public Predicate toPredicate(Root<Art> art, CriteriaQuery<?> q, CriteriaBuilder cb) {
                return cb.isEmpty(art.get("artInfos"));
            }
        };
    }

    public static Specification<Art> nameIsEqualTo(String name) {
        return new Specification<Art>() {
            @Override
            public Predicate toPredicate(Root<Art> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
                return cb.equal(r.get("name"), name);
            }
        };
    }


    public static Specification<Art> proposalsWithRepresentativeIsNull(Representative representative) {
        return (art, cq, cb) -> {
            Join<Art, Proposal> proposals = art.join("proposals", JoinType.LEFT);

            Predicate proposalIdIsNull = cb.isNull(proposals.get("id"));
            Predicate facilityNotEqualCurrentRepresentativeFacility = cb.notEqual(proposals.get("facility"), representative.getFacility());
            return cb.or(proposalIdIsNull, facilityNotEqualCurrentRepresentativeFacility);
        };
    }
}
