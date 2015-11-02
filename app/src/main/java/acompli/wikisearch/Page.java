package acompli.wikisearch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thakurprateek on 30-10-2015.
 */
public class Page {

    private int pageid;

    private String title;

    private Thumbnail thumbnail;

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}

