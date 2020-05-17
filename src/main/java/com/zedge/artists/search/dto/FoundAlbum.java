package com.zedge.artists.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoundAlbum {

    private Integer platformId;

    private String title;

    private Integer trackCount;

    private LocalDate releaseDate;
}
