package com.scnsoft.art.listener;

import com.scnsoft.art.entity.Organization;
import jakarta.persistence.PrePersist;

public class OrganizationListener {

    @PrePersist
    private void beforePersist(Organization organization) {
        organization.setStatus(Organization.Status.NEW);
    }
}

