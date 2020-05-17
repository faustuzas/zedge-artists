package com.zedge.artists.search.itunes.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "wrapperType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ItunesArtist.class, name = "artist"),
        @JsonSubTypes.Type(value = ItunesAlbum.class, name = "collection")
})
public abstract class ItunesEntity {

}
