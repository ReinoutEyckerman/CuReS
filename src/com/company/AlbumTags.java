package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reinout on 11/10/16.
 */
public class AlbumTags {
    public String Title=null;
    public String Performer=null;
    public String Songwriter=null;
    public String Genre=null;
    public String Date=null;
    public String DiscID=null;
    public String Comment=null;
    public String Catalog=null;
    public String FileLocation=null;

    public List<TrackTags> tracks=null;
    public AlbumTags(){
        this.tracks=new ArrayList<TrackTags>();
    }
    public void Clean(){
        if(Title!=null)
            Title = Title.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if(Performer!=null)
            Performer= Performer.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if(Songwriter!=null)
            Songwriter=Songwriter.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if(Genre!=null)
            Genre=Genre.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if(Date!=null)
            Date=Date.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if(DiscID!=null)
            DiscID=DiscID.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if(Comment!=null)
            Comment=Comment.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if(Catalog!=null)
            Catalog=Catalog.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if(FileLocation!=null)
            FileLocation=FileLocation.replaceAll("^\"+", "").replaceAll("\"+$", "");
        tracks.forEach(TrackTags::Clean);
    }
}
