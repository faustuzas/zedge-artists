package com.zedge.artists.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AlbumDto {

    private UUID id;

    private String title;

    private Integer platformId;

    private Integer trackCount;

    private LocalDate releaseDate;
}
