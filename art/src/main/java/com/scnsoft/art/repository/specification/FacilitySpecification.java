package com.scnsoft.art.repository.specification;

import java.util.UUID;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.City;
import com.scnsoft.art.entity.Facility;

public class FacilitySpecification {
    public static Specification<Facility> idEqual(UUID id) {
        return (facility, cq, cb) -> {
            return cb.equal(facility.get("id"), id);
        };
    }
    public static Specification<Facility> cityIdEqual(UUID id) {
        return (facility, cq, cb) -> {
            Join<Facility, Address> address = facility.join("address");
            Join<Address, City> city = address.join("city");
            return cb.equal(city.get("id"), id);
        };
    }
    public static Specification<Facility> facilityNameStartWith(String name) {
        return (facility, cq, cb) -> {
            String facilityNameStartWithRegex = name + "%";
            return cb.like(facility.get("name"), facilityNameStartWithRegex);
        };
    }
    public static Specification<Facility> statusEqual(boolean status) {
        return (facility, cq, cb) -> {
            return cb.equal(facility.get("isActive"), status);
        };
    }
}
