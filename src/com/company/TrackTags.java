package com.company;

/**
 * Created by reinout on 11/10/16.
 */
public class TrackTags {
    public String Title=null;
    public String Performer=null;
    public String Songwriter=null;
    public int CutPoint=0;
    public String ISRC=null;
    public void SetCutPoint(String s){
        s = s.replaceAll("^\"+", "").replaceAll("\"+$", "");
        String[] splitTime=s.split(":");
        if(splitTime[2]!=null&&splitTime[2].length()<3)
            splitTime[2]=splitTime[2].concat("0");
        CutPoint=Integer.parseInt(splitTime[0])*60000+Integer.parseInt(splitTime[1])*1000+Integer.parseInt(splitTime[2]);

    }
    public void Clean() {
        if (Title != null)
            Title = Title.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if (Performer != null)
            Performer = Performer.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if (Songwriter != null)
            Songwriter = Songwriter.replaceAll("^\"+", "").replaceAll("\"+$", "");
        if (ISRC != null)
            ISRC = ISRC.replaceAll("^\"+", "").replaceAll("\"+$", "");
    }
}
