package com.scnsoft.art.repository.specification;

import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Artist;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Proposal;
import com.scnsoft.art.entity.Representative;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

@RequiredArgsConstructor
public class ArtSpecification {
    // TODO: Add regex support

    public static Specification<Art> artInfosIsEmpty() {
        return (art, q, cb) -> cb.isEmpty(art.get("artInfos"));
    }

    public static Specification<Art> artNameContain(String name) {
        return (r, q, cb) -> {
            String nameRegex = "%" + name + "%";
            return cb.like(r.get("name"), nameRegex);
        };
    }

    public static Specification<Art> artistNameContain(String name) {
        return (art, q, cb) -> {
            Join<Art, Artist> artist = art.join("artist");
            String nameRegex = "%" + name + "%";
            Predicate artistFirstnameEqualName = cb.like(
                    artist.get("firstname"),
                    nameRegex
            );

            Predicate artistLastnameEqualName = cb.like(
                    artist.get("lastname"),
                    nameRegex
            );
            return cb.or(artistFirstnameEqualName, artistLastnameEqualName);
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

    public static Specification<Art> decriptionContain(String description) {
        String decriptionRegex = "%" + description + "%";
        return (art, q, cb) -> cb.like(art.get("description"), decriptionRegex);
    }

    public static Specification<Art> proposalsWithRepresentativeIsNull(Representative representative) {
        return (art, cq, cb) -> {
            Join<Art, Proposal> proposals = art.join("proposals", JoinType.LEFT);

            Predicate proposalIdIsNull = cb.isNull(proposals.get("id"));
            Predicate facilityNotEqualCurrentRepresentativeFacility = cb.notEqual(proposals.get("facility"),
                    representative.getFacility());
            return cb.or(proposalIdIsNull, facilityNotEqualCurrentRepresentativeFacility);
        };
    }
}
