package com.scnsoft.art.app;

import com.scnsoft.art.entity.Currency;
import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.repository.CurrencyRepository;
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
    private static final String DOLLAR_CURRENCY_VALUE = "USD";
    private static final String DOLLAR_CURRENCY_LABEL = "$";

    private final OrganizationRoleRepository organizationRoleRepository;
    private final CurrencyRepository currencyRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void postContextInitialization() {
        initRoles();
        initCurrencies();
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

    @Transactional
    public void initCurrencies() {
        if (currencyRepository.findByValue(DOLLAR_CURRENCY_VALUE).isEmpty()) {
            Currency currency = Currency.builder()
                    .value(DOLLAR_CURRENCY_VALUE)
                    .label(DOLLAR_CURRENCY_LABEL)
                    .build();

            currencyRepository.save(currency);
        }
    }

}
