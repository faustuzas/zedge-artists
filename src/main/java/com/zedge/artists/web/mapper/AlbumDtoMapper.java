package com.zedge.artists.web.mapper;

import com.zedge.artists.model.Album;
import com.zedge.artists.web.dto.AlbumDto;

public class AlbumDtoMapper {
    public static AlbumDto fromAlbum(Album album) {
        return AlbumDto.builder()
                .id(album.getId())
                .platformId(album.getPlatformId())
                .title(album.getTitle())
                .releaseDate(album.getReleaseDate())
                .trackCount(album.getTrackCount())
                .build();
    }
}
