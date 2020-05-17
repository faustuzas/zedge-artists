package com.zedge.artists.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoundArtist {

    private Integer platformId;

    private Integer amgId;

    private String name;

    private String primaryGenreName;
}
