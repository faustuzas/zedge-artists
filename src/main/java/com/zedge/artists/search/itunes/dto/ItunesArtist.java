package com.zedge.artists.search.itunes.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ItunesArtist extends ItunesEntity {

    private Integer artistId;

    private Integer amgArtistId;

    private String artistName;

    private String primaryGenreName;
}
