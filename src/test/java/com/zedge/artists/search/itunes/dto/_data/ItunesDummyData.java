package com.zedge.artists.search.itunes.dto._data;

import com.zedge.artists.search.itunes.dto.ItunesAlbum;
import com.zedge.artists.search.itunes.dto.ItunesArtist;
import com.zedge.artists.search.itunes.dto.ItunesEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ItunesDummyData {

    public static ItunesArtist.ItunesArtistBuilder itunesArtistBase() {
        return ItunesArtist.builder()
                .artistId(1234)
                .amgArtistId(5678)
                .artistName("Famous singer")
                .primaryGenreName("Pop");
    }

    public static ItunesAlbum.ItunesAlbumBuilder itunesAlbumBase() {
        return ItunesAlbum.builder()
                .collectionId(12345)
                .collectionName("New album")
                .releaseDate(LocalDate.of(2017, 7, 4))
                .trackCount(12);
    }

    public static List<ItunesEntity> fullAlbums(int howMany) {
        return IntStream.rangeClosed(1, howMany)
                .mapToObj(nr -> itunesAlbumBase()
                                    .collectionName("Album #" + nr)
                                    .build())
                .collect(Collectors.toList());
    }
}
