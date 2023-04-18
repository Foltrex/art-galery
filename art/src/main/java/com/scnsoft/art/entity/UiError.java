package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class UiError {

    public enum UiErrorStatus {
        OPEN, FIXED, NOT_REPRODUCIBLE
    }

    @Id
    @GeneratedValue
    private Integer id;

    private LocalDateTime createdAt;
    private UiErrorStatus status;

    private UUID userId;
    private String url;
    private String errorName;
    private String errorMessage;
    private String errorTrace;
    private String componentStack;
}
