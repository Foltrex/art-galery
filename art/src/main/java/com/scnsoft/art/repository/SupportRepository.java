package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface SupportRepository extends JpaRepository<Support, UUID>, JpaSpecificationExecutor<Support> {

    @Query("select s from" +
            " Support s" +
            " inner join SupportThread st on st.id = s.threadId" +
            " where s.threadId in (:threadIds)" +
            " and st.createdAt = s.createdAt" +
            " and s.type = com.scnsoft.art.entity.SupportType.TEXT")
    List<Support> findOpPosts(List<UUID> threadIds);
}
