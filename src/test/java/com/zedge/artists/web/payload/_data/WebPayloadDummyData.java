package com.zedge.artists.web.payload._data;

import com.zedge.artists.web.payload.SaveFavoriteArtistPayload;

public class WebPayloadDummyData {

    public static SaveFavoriteArtistPayload.SaveFavoriteArtistPayloadBuilder baseSaveFavoriteArtistPayload() {
        return SaveFavoriteArtistPayload.builder()
                .artistPlatformId(12345);
    }
}
