package com.zedge.artists.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "artists")
public class Artist {

    @Id
    private UUID id;

    private String name;

    private Integer platformId;

    private Integer amgId;

    private String primaryGenreName;

    private Instant topAlbumsFetchedAt;

    @OneToMany(
            mappedBy = "artist",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private List<Album> topAlbums;
}
