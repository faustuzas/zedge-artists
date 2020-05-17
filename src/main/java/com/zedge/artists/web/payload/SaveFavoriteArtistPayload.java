package com.zedge.artists.web.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveFavoriteArtistPayload {

    @NotNull
    private Integer artistPlatformId;
}
