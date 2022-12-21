package com.scnsoft.user.app;

import com.scnsoft.user.entity.Role;
import com.scnsoft.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@PropertySource("/application.properties")
@Slf4j
public class AppInitialization {

    private final RoleRepository roleRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void postContextInitialization() {
        log.info("Initialization method has started");
        initRoles();
    }

    @Transactional
    public void initRoles() {
        log.info("init roles");
        if (roleRepository.findByName(Role.RoleType.ROLE_ADMIN).isEmpty()) {
            Role roleAdmin = Role.builder().name(Role.RoleType.ROLE_ADMIN).build();
            roleRepository.save(roleAdmin);
        }
        if (roleRepository.findByName(Role.RoleType.ROLE_MODERATOR).isEmpty()) {
            Role roleModerator = Role.builder().name(Role.RoleType.ROLE_MODERATOR).build();
            roleRepository.save(roleModerator);
        }
        if (roleRepository.findByName(Role.RoleType.ROLE_USER).isEmpty()) {
            Role roleUser = Role.builder().name(Role.RoleType.ROLE_USER).build();
            roleRepository.save(roleUser);
        }
    }


}
