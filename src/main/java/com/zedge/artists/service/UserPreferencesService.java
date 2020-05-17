package com.zedge.artists.service;

import com.zedge.artists.error.ResourceNotFound;
import com.zedge.artists.model.Artist;
import com.zedge.artists.persistence.UserPreferenceRepository;
import com.zedge.artists.search.ArtistSearchService;
import com.zedge.artists.search.mapper.FoundArtistMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserPreferencesService {

    private ArtistsService artistsService;
    private ArtistSearchService artistSearchService;
    private TopAlbumsService topAlbumsService;
    private UserPreferenceRepository userPreferenceRepository;

    public UserPreferencesService(ArtistsService artistsService,
                                  ArtistSearchService artistSearchService,
                                  TopAlbumsService topAlbumsService,
                                  UserPreferenceRepository userPreferenceRepository
    ) {
        this.artistsService = artistsService;
        this.artistSearchService = artistSearchService;
        this.userPreferenceRepository = userPreferenceRepository;
        this.topAlbumsService = topAlbumsService;
    }

    public Optional<Artist> findUserFavoriteArtist(UUID userId) {
        Optional<Artist> favoriteArtist = userPreferenceRepository.findUserFavoriteArtist(userId);

        favoriteArtist.ifPresent(topAlbumsService::ensureArtistTopAlbums);

        return favoriteArtist;
    }

    public void setUserFavoriteArtist(UUID userId, int artistPlatformId) {
        Optional<Artist> oldFavoriteArtist = userPreferenceRepository.findUserFavoriteArtist(userId);
        boolean isUserNew = oldFavoriteArtist.isEmpty();
        if (! isUserNew && oldFavoriteArtist.get().getPlatformId() == artistPlatformId) {
            return;
        }

        Optional<Artist> artist = artistsService.findByPlatformId(artistPlatformId);
        if (artist.isPresent()) {
            saveUseArtistPreference(userId, artist.get().getId(), isUserNew);
            return;
        }

        Artist newArtist = artistSearchService.fetchArtist(artistPlatformId)
                .map(FoundArtistMapper::toArtist)
                .orElseThrow(ResourceNotFound::new);

        Artist createdArtist = artistsService.create(newArtist);
        saveUseArtistPreference(userId, createdArtist.getId(), isUserNew);
        topAlbumsService.ensureArtistTopAlbums(createdArtist);
    }

    private void saveUseArtistPreference(UUID userId, UUID artistId, boolean isUserNew) {
        if (isUserNew) {
            userPreferenceRepository.setUserFavoriteArtist(userId, artistId);
        } else {
            userPreferenceRepository.updateUserFavoriteArtist(userId, artistId);
        }
    }
}
