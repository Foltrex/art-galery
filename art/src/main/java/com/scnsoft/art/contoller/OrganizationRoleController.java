package com.scnsoft.art.contoller;

import com.scnsoft.art.dto.OrganizationRoleDto;
import com.scnsoft.art.facade.OrganizationRoleServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("organization-roles")
@RequiredArgsConstructor
public class OrganizationRoleController {

    private final OrganizationRoleServiceFacade organizationRoleServiceFacade;

    @GetMapping
    public ResponseEntity<List<OrganizationRoleDto>> findAll() {
        return ResponseEntity.ok(organizationRoleServiceFacade.findAll());
    }
}
