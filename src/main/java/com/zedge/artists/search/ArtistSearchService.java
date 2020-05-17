package com.zedge.artists.search;

import com.zedge.artists.search.dto.FoundAlbum;
import com.zedge.artists.search.dto.FoundArtist;

import java.util.List;
import java.util.Optional;

public interface ArtistSearchService {

    List<FoundArtist> searchArtistsByName(String name, int limit);

    List<FoundAlbum> topArtistAlbums(int artistPlatformId, int limit);

    Optional<FoundArtist> fetchArtist(int artistPlatformId);
}
