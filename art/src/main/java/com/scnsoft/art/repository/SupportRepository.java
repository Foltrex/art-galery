package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Support;
import com.scnsoft.art.entity.SupportThread;
import liquibase.pro.packaged.Q;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupportRepository extends JpaRepository<Support, UUID>, JpaSpecificationExecutor<Support> {

    @Query(nativeQuery = true, value = "select count(1) from (" +
            "select distinct on (support.thread_id) " +
            "support.account_id from support_thread " +
            "inner join support on support.thread_id = support_thread.id " +
            "where support_thread.status = 0 " +
            "order by support.thread_id, support.created_at desc) t1 where t1.account_id != :id")
    Integer getUnanswered(@Param("id") UUID id);

    @Query("select count(s) from SupportThread s where s.status = :status")
    Integer getOpen(@Param("status") SupportThread.SupportThreadStatus status);

    @Query("select s from Support s" +
            " where s.threadId in (:idList) " +
            " and s.type <> com.scnsoft.art.entity.SupportType.SYSTEM" +
            " order by s.createdAt")
    List<Support> findPosts(List<UUID> idList);

}
