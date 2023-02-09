package com.scnsoft.user.entity;

import com.scnsoft.user.entity.constant.TemplateFile;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class EmailMessageCode {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(unique = true)
    @Enumerated(EnumType.ORDINAL)
    private TemplateFile templateFile;

    private Integer code;

    private Boolean isValid = true;

    private Integer countAttempts = 0;
}
