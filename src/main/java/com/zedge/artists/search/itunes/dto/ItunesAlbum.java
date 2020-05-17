package com.zedge.artists.search.itunes.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ItunesAlbum extends ItunesEntity {

    private Integer collectionId;

    private String collectionName;

    private Integer trackCount;

    private LocalDate releaseDate;
}
