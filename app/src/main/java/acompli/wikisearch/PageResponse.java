package acompli.wikisearch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thakurprateek on 02-11-2015.
 */
public class PageResponse {

    private String  batchcomplete;

    @SerializedName("continue")
    private ContinueRes continueRes;

    private Query query;

    public String getBatchcomplete() {
        return batchcomplete;
    }

    public void setBatchcomplete(String batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
