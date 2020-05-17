package com.zedge.artists.model._data;

import com.zedge.artists.model.Album;
import com.zedge.artists.model.Artist;
import org.assertj.core.util.Lists;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DummyData {

    public static Artist.ArtistBuilder baseArtist() {
        return Artist.builder()
                .id(UUID.randomUUID())
                .platformId(12345)
                .amgId(67890)
                .name("Popular artist")
                .primaryGenreName("Pop")
                .topAlbums(Lists.emptyList())
                .topAlbumsFetchedAt(LocalDate.of(2000, 1, 1).atStartOfDay()
                        .toInstant(ZoneOffset.UTC));
    }

    public static Album.AlbumBuilder baseAlbum() {
        return Album.builder()
                .id(UUID.randomUUID())
                .platformId(12345)
                .title("New album")
                .trackCount(12)
                .releaseDate(LocalDate.of(2002, 5, 3));
    }

    public static List<Album> fullAlbums(int howMany) {
        return IntStream.rangeClosed(1, howMany)
                .mapToObj(nr -> baseAlbum()
                                    .title("Album #" + nr)
                                    .build())
                .collect(Collectors.toList());
    }
}
