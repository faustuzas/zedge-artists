package com.zedge.artists.web;

import com.zedge.artists.error.ResourceNotFound;
import com.zedge.artists.model.Artist;
import com.zedge.artists.search.ArtistSearchService;
import com.zedge.artists.service.UserPreferencesService;
import com.zedge.artists.web.dto.AlbumDto;
import com.zedge.artists.web.dto.FoundArtistDto;
import com.zedge.artists.web.mapper.AlbumDtoMapper;
import com.zedge.artists.web.mapper.FoundArtistDtoMapper;
import com.zedge.artists.web.payload.SaveFavoriteArtistPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/artists")
public class ArtistsController {

    private ArtistSearchService artistSearchService;
    private UserPreferencesService userPreferencesService;

    public ArtistsController(
            ArtistSearchService artistSearchService,
            UserPreferencesService userPreferencesService
    ) {
        this.artistSearchService = artistSearchService;
        this.userPreferencesService = userPreferencesService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoundArtistDto>> searchArtists(
            @RequestParam String term,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<FoundArtistDto> foundArtists = artistSearchService.searchArtistsByName(term, limit)
                .stream()
                .map(FoundArtistDtoMapper::fromFoundArtist)
                .collect(Collectors.toList());

        return ResponseEntity.ok(foundArtists);
    }

    @GetMapping("/favorite")
    public ResponseEntity<List<AlbumDto>> getFavoriteArtistTopAlbums(@RequestParam UUID userId) {
        Artist userFavorite = userPreferencesService.findUserFavoriteArtist(userId)
                .orElseThrow(ResourceNotFound::new);

        List<AlbumDto> albums = userFavorite.getTopAlbums()
                .stream()
                .map(AlbumDtoMapper::fromAlbum)
                .collect(Collectors.toList());

        return ResponseEntity.ok(albums);
    }

    @PostMapping("/favorite")
    public ResponseEntity<Void> saveFavoriteArtist(
            @RequestParam UUID userId,
            @Valid @RequestBody SaveFavoriteArtistPayload payload
    ) {
        userPreferencesService.setUserFavoriteArtist(userId, payload.getArtistPlatformId());

        return ResponseEntity.noContent().build();
    }
}
