package com.zedge.artists.search.itunes.mapper;

import com.zedge.artists.search.itunes.dto.ItunesAlbum;
import com.zedge.artists.search.dto.FoundAlbum;

public class ItunesAlbumMapper {

    public static FoundAlbum toFoundAlbum(ItunesAlbum album) {
        return FoundAlbum.builder()
                .title(album.getCollectionName())
                .platformId(album.getCollectionId())
                .releaseDate(album.getReleaseDate())
                .trackCount(album.getTrackCount())
                .build();
    }
}
