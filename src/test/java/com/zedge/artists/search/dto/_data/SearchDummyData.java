package com.zedge.artists.search.dto._data;

import com.zedge.artists.search.dto.FoundAlbum;
import com.zedge.artists.search.dto.FoundArtist;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchDummyData {

    public static FoundArtist.FoundArtistBuilder baseFoundArtist() {
        return FoundArtist.builder()
                .platformId(1234)
                .amgId(5678)
                .name("Famous artist")
                .primaryGenreName("Pop");
    }

    public static FoundAlbum.FoundAlbumBuilder baseFoundAlbum() {
        return FoundAlbum.builder()
                .platformId(1234)
                .title("New lbum")
                .releaseDate(LocalDate.of(2009, 3, 22))
                .trackCount(12);
    }

    public static List<FoundAlbum> fullFoundAlbums(int howMany) {
        return IntStream.rangeClosed(1, howMany)
                .mapToObj(nr -> baseFoundAlbum()
                                    .title("Album #" + nr)
                                    .build())
                .collect(Collectors.toList());
    }

    public static List<FoundArtist> fullFoundArtists(int howMany) {
        return IntStream.rangeClosed(1, howMany)
                .mapToObj(nr -> baseFoundArtist()
                        .name("Artist #" + nr)
                        .build())
                .collect(Collectors.toList());
    }
}
