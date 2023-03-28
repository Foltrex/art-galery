package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtTopic;
import com.scnsoft.art.entity.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtTopicRepository extends JpaRepository<ArtTopic, Integer> {

    @Query("select artTopic.id, artTopic.label from ArtTopic artTopic")
    List<Option> findAllInOptionModel();
}
