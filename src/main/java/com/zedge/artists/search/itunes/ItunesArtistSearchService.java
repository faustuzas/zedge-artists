package com.zedge.artists.search.itunes;

import com.zedge.artists.search.ArtistSearchService;
import com.zedge.artists.search.dto.FoundAlbum;
import com.zedge.artists.search.dto.FoundArtist;
import com.zedge.artists.search.itunes.dto.ItunesAlbum;
import com.zedge.artists.search.itunes.dto.ItunesArtist;
import com.zedge.artists.search.itunes.dto.ItunesResponse;
import com.zedge.artists.search.itunes.mapper.ItunesAlbumMapper;
import com.zedge.artists.search.itunes.mapper.ItunesArtistMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItunesArtistSearchService implements ArtistSearchService {

    private ItunesHttpClient itunesHttpClient;

    public ItunesArtistSearchService(ItunesHttpClient itunesHttpClient) {
        this.itunesHttpClient = itunesHttpClient;
    }

    public List<FoundArtist> searchArtistsByName(String name, int limit) {
        return this.itunesHttpClient.searchArtistsByName(name, limit)
                .getResults()
                .stream()
                .filter(ItunesArtist.class::isInstance)
                .map(entity -> (ItunesArtist) entity)
                .map(ItunesArtistMapper::toFoundArtist)
                .collect(Collectors.toList());
    }

    @Override
    public List<FoundAlbum> topArtistAlbums(int itunesId, int limit) {
        return this.itunesHttpClient.topAlbumsById(itunesId, limit)
                .getResults()
                .stream()
                .filter(ItunesAlbum.class::isInstance)
                .map(entity -> (ItunesAlbum) entity)
                .map(ItunesAlbumMapper::toFoundAlbum)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FoundArtist> fetchArtist(int itunesId) {
        ItunesResponse response = this.itunesHttpClient.fetchArtist(itunesId);
        return Optional.ofNullable(response.getResultCount() == 0 ? null : response.getResults().get(0))
                .filter(ItunesArtist.class::isInstance)
                .map(artist -> (ItunesArtist) artist)
                .map(ItunesArtistMapper::toFoundArtist);
    }
}
