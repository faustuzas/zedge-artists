package com.zedge.artists.web.dto._data;

import com.zedge.artists.web.dto.AlbumDto;
import com.zedge.artists.web.dto.FoundArtistDto;

import java.time.LocalDate;
import java.util.UUID;

public class WebDtoDummyData {

    public static FoundArtistDto.FoundArtistDtoBuilder baseFoundArtistDto() {
        return FoundArtistDto.builder()
                .platformId(12345)
                .amgId(678990)
                .name("Popular artist")
                .primaryGenreName("Pop");
    }

    public static AlbumDto.AlbumDtoBuilder baseAlbumDto() {
        return AlbumDto.builder()
                .id(UUID.randomUUID())
                .platformId(1234)
                .title("New album")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .trackCount(44);
    }
}
