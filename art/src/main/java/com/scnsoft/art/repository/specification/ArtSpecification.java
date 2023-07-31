package com.scnsoft.art.repository.specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.City;

import java.util.UUID;

public class ArtSpecification {

    public static Specification<Art> artInfosIsEmpty() {
        return (art, q, cb) -> cb.isEmpty(art.get("artInfos"));
    }

    public static Specification<Art> artNameContain(String name) {
        return (r, q, cb) -> {
            String nameRegex = "%" + name.toLowerCase() + "%";
            return cb.like(cb.lower(r.get(Art.Fields.name)), nameRegex);
        };
    }


    public static Specification<Art> cityIdEquals(UUID cityId) {
        return (art, q, cb) -> {
            Join<Art, ArtInfo> artInfos = art.join(Art.Fields.artInfos);
            Predicate isLastArtInfo = cb.isNull(artInfos.get(ArtInfo.Fields.expositionDateEnd));

            q.where(isLastArtInfo);

            Join<ArtInfo, Facility> facility = artInfos.join("facility");
            Join<Facility, Address> address = facility.join("address");
            Join<Address, City> city = address.join("city");

            return cb.equal(city.get(City.Fields.id), cityId);
        };
    }

    public static Specification<Art> descriptionContain(String description) {
        String decriptionRegex = "%" + description + "%";
        return (art, q, cb) -> cb.like(art.get(Art.Fields.description), decriptionRegex);
    }

    public static Specification<Art> artistIdEqual(UUID artistId) {
        return (art, q, cb) -> cb.equal(art.get(Art.Fields.artistAccountId), artistId);
    }
}
