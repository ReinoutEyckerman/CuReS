package com.company;


import org.json.simple.JSONObject;
import org.musicbrainz.controller.Artist;
import org.musicbrainz.controller.Controller;
import org.musicbrainz.model.searchresult.ArtistResultWs2;

import javax.naming.directory.SearchResult;
import java.util.List;

/**
 * Created by reinout on 10/26/16.
 */
public class MusicBrainzAPI {
   public void GetMetadata(){
       Artist artist=new Artist();
       artist.search("Exodus");
       List<ArtistResultWs2> results;
        results = artist.getFullSearchResultList();

   }

}
