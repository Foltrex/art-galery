package com.scnsoft.user.repository.specification;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.Expression;

import org.springframework.data.jpa.domain.Specification;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Account.AccountType;

public class AccountSpecification {
    private static final String ORGANIZAITON_ID_KEY = "organizaiton_id";

    public static Specification<Account> idInList(List<UUID> accountIds) {
        return (account, cq, cb) -> {
            Expression<UUID> idExpression = account.get("id");
            return idExpression.in(accountIds);
        };
    }

    public static Specification<Account> firstnameOrLastnameStartWith(String username) {
        return (account, cq, cb) -> {
            String nameRegex = username + "%";
            return cb.or(
                cb.like(account.get("firstName"), nameRegex),
                cb.like(account.get("lastName"), nameRegex)
            );
        };
    } 


    public static Specification<Account> usertypeEqual(AccountType accountType) {
        return (account, cq, cb) -> cb.equal(account.get("accountType"), accountType);
    } 
}
