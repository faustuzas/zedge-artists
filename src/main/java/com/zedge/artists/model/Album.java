package com.zedge.artists.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "albums")
public class Album {

    @Id
    private UUID id;

    private String title;

    private Integer platformId;

    private Integer trackCount;

    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;
}
