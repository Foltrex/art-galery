package com.scnsoft.art.repository.specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.user.entity.Address;
import com.scnsoft.user.entity.City;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArtSpecification {

    public static Specification<Art> artInfosIsEmpty() {
        return (art, q, cb) -> cb.isEmpty(art.get("artInfos"));
    }

    public static Specification<Art> artNameContain(String name) {
        return (r, q, cb) -> {
            String nameRegex = "%" + name + "%";
            return cb.like(r.get("name"), nameRegex);
        };
    }

    public static Specification<Art> cityNameContain(String name) {
        return (art, q, cb) -> {
            Join<Art, ArtInfo> artInfos = art.join("artInfos");
            Predicate isLastArtInfo = cb.isNull(artInfos.get("expositionDateEnd"));

            q.where(isLastArtInfo);

            Join<ArtInfo, Facility> facility = artInfos.join("facility");
            Join<Facility, Address> address = facility.join("address");
            Join<Address, City> city = address.join("city");

            String nameRegex = "%" + name + "%";
            return cb.like(city.get("name"), nameRegex);
        };
    }

    public static Specification<Art> descriptionContain(String description) {
        String decriptionRegex = "%" + description + "%";
        return (art, q, cb) -> cb.like(art.get("description"), decriptionRegex);
    }
}
