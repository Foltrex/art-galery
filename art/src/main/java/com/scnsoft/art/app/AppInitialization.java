package com.scnsoft.art.app;

import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.repository.OrganizationRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppInitialization {

    private final OrganizationRoleRepository organizationRoleRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void postContextInitialization() {
        log.info("Initialization method has started");
        initRoles();
    }

    @Transactional
    public void initRoles() {
        log.info("Initialization organization roles");
        if (organizationRoleRepository.findByName(OrganizationRole.RoleType.CREATOR).isEmpty()) {
            OrganizationRole roleCreator = OrganizationRole.builder()
                    .name(OrganizationRole.RoleType.CREATOR)
                    .build();

            organizationRoleRepository.save(roleCreator);
        }
        if (organizationRoleRepository.findByName(OrganizationRole.RoleType.MODERATOR).isEmpty()) {
            OrganizationRole roleModerator = OrganizationRole.builder()
                    .name(OrganizationRole.RoleType.MODERATOR)
                    .build();

            organizationRoleRepository.save(roleModerator);
        }
        if (organizationRoleRepository.findByName(OrganizationRole.RoleType.MEMBER).isEmpty()) {
            OrganizationRole roleMember = OrganizationRole.builder()
                    .name(OrganizationRole.RoleType.MEMBER)
                    .build();

            organizationRoleRepository.save(roleMember);
        }
    }

}
