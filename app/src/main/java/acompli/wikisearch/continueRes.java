package acompli.wikisearch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thakurprateek on 02-11-2015.
 */
public class ContinueRes {

    private String gpsoffset;

    @SerializedName("continue")
    private String continueString;

    public String getGpsoffset() {
        return gpsoffset;
    }

    public void setGpsoffset(String gpsoffset) {
        this.gpsoffset = gpsoffset;
    }

    public String getContinueString() {
        return continueString;
    }

    public void setContinueString(String continueString) {
        this.continueString = continueString;
    }
}
