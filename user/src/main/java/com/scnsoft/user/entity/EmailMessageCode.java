package com.scnsoft.user.entity;

import com.scnsoft.user.entity.constant.TemplateFile;
import com.scnsoft.user.listener.EmailMessageCodeListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(EmailMessageCodeListener.class)
public class EmailMessageCode {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private Integer code;

    private Boolean isValid = true;

    private Integer countAttempts;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
}
