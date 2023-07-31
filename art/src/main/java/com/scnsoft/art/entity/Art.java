package com.scnsoft.art.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Art {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    private String name;
    private String description;
    private Long price;

    private Date dateCreation;

    private UUID artistAccountId;

    @ManyToOne
    @JoinColumn(name = "art_size_id")
    private ArtSize artSize;

    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "art_art_style",
        joinColumns = @JoinColumn(name="art_id"),
        inverseJoinColumns = @JoinColumn(name="art_style_id")
    )
    private List<ArtStyle> artStyles = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "art_art_topic",
        joinColumns = @JoinColumn(name="art_id"),
        inverseJoinColumns = @JoinColumn(name="art_topic_id")
    )
    private List<ArtTopic> artTopics = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "art_format_id")
    private ArtSize artFormat;


    @ManyToOne
    @JoinColumn(name = "art_type_id")
    private ArtType artType;

    @Builder.Default
    @OneToMany(mappedBy = "art", cascade = CascadeType.REMOVE)
    private List<ArtInfo> artInfos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "art", cascade = CascadeType.REMOVE)
    private List<Proposal> proposals = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}
