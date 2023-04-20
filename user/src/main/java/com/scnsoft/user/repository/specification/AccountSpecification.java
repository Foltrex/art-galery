package com.scnsoft.user.repository.specification;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import org.springframework.data.jpa.domain.Specification;

import com.scnsoft.user.entity.Account;
import com.scnsoft.user.entity.Account.AccountType;

public class AccountSpecification {

    public static Specification<Account> idInList(List<UUID> accountIds) {
        return (account, cq, cb) -> {
            Expression<UUID> idExpression = account.get(Account.Fields.id);
            return idExpression.in(accountIds);
        };
    }

    public static Specification<Account> inMetadata(String key, String value) {
        return (account, cq, cb) -> {
            Join<Object, Object> join = account.join(Account.Fields.metadata, JoinType.INNER);

            return cb.and(
                    cb.equal(join.get(Metadata.Fields.metadataId).get(MetadataId.Fields.key), key),
                    cb.equal(join.get(Metadata.Fields.value), value)
            );
        };
    }

    public static Specification<Account> firstnameOrLastnameStartWith(String username) {
        return (account, cq, cb) -> {
            String nameRegex = username + "%".toLowerCase();
            return cb.or(
                cb.like(cb.lower(account.get(Account.Fields.firstName)), nameRegex),
                cb.like(cb.lower(account.get(Account.Fields.lastName)), nameRegex)
            );
        };
    } 


    public static Specification<Account> usertypeEqual(AccountType accountType) {
        return (account, cq, cb) -> cb.equal(account.get(Account.Fields.accountType), accountType);
    } 
}
