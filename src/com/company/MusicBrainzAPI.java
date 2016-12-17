package com.company;


import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.musicbrainz.controller.Artist;
import org.musicbrainz.model.searchresult.ArtistResultWs2;

import java.util.List;

/**
 * Created by reinout on 10/26/16.
 */
public class MusicBrainzAPI {
   protected static final String USERAGENT
       = "CuReS https://github.com/ReinoutEyckerman/CuReS";

   public void GetMetadata(){
       DefaultHttpClient client=new DefaultHttpClient();
               HttpParams connectionParams = client.getParams();

       HttpConnectionParams.setConnectionTimeout(connectionParams, 60000);
        HttpConnectionParams.setSoTimeout(connectionParams, 60000);
        connectionParams.setParameter("http.useragent", USERAGENT);
        connectionParams.setParameter("http.protocol.content-charset", "UTF-8");

       Artist artist=new Artist();
       artist.search("Exodus");
       List<ArtistResultWs2> results;
        results = artist.getFullSearchResultList();
        System.out.println(results);
   }

}
