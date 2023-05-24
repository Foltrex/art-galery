package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class Support {


    @Id
    @GeneratedValue
    private UUID id;
    private UUID accountId;
    private UUID threadId;
    private SupportType type;
    private String message;
    private Date createdAt;
}
