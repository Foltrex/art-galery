package com.scnsoft.art.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Getter
@ToString
public class User {

    @Size(min = 2)
    private String firstname;

    @Size(min = 2)
    private String lastname;

    @NotNull
    private UUID accountId;

}
