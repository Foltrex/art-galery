package com.scnsoft.art.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Art {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Builder.Default
    @OneToMany(mappedBy = "art", cascade = CascadeType.REMOVE)
    private List<ArtInfo> artInfos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "art", cascade = CascadeType.REMOVE)
    private List<Proposal> proposals = new ArrayList<>();
}
