package com.zedge.artists.search.itunes;

import com.zedge.artists.search.itunes.dto.ItunesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "itunes-client", url = "${app.itunes.api}")
public interface ItunesHttpClient {

    @PostMapping("/search?entity=allArtist&term={name}&limit={limit}")
    ItunesResponse searchArtistsByName(@PathVariable String name, @PathVariable int limit);

    @PostMapping(value = "/lookup?id={itunesId}&entity=album&limit={limit}")
    ItunesResponse topAlbumsById(@PathVariable int itunesId, @PathVariable int limit);

    @PostMapping(value = "/lookup?id={itunesId}")
    ItunesResponse fetchArtist(@PathVariable int itunesId);
}