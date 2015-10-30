package acompli.wikisearch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thakurprateek on 30-10-2015.
 */
public class Page {

    public static final String JSON_FIELD_PAGE_ID = "pageid";
    public static final String JSON_FIELD_TITLE = "title";
    public static final String JSON_FIELD_THUMBNAIL_SOURCE = "source";
    public static final String JSON_FIELD_THUMBNAIL = "thumbnail";


    private int pageid;

    private String title;

    private String thumbnailSource;

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

    public String getThumbnailSource() {
        return thumbnailSource;
    }

    public void setThumbnailSource(String thumbnailSource) {
        this.thumbnailSource = thumbnailSource;
    }


    /**
     * Takes in a JSON object and returns usable page that will be used to build list on client side
     * @param jsonObject JSON object conatining the page
     * @return a page object
     * @throws JSONException
     */
    public static final Page getPageFromJSon(JSONObject jsonObject) throws JSONException {

        Page page = new Page();

        page.setPageid(jsonObject.getInt(JSON_FIELD_PAGE_ID));

        page.setTitle(jsonObject.getString(JSON_FIELD_TITLE));

        if(jsonObject.has(JSON_FIELD_THUMBNAIL)) {
            page.setThumbnailSource(jsonObject.getJSONObject(JSON_FIELD_THUMBNAIL)
                    .getString(JSON_FIELD_THUMBNAIL_SOURCE));
        }

        return page;
    }

}

