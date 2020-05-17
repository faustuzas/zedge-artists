package com.zedge.artists.search.mapper;

import com.zedge.artists.model.Album;
import com.zedge.artists.search.dto.FoundAlbum;

public class FoundAlbumMapper {

    public static Album toAlbum(FoundAlbum album) {
        return Album.builder()
                .platformId(album.getPlatformId())
                .title(album.getTitle())
                .trackCount(album.getTrackCount())
                .releaseDate(album.getReleaseDate())
                .build();
    }
}
