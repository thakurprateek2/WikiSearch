package acompli.wikisearch;

import android.net.Uri;

/**
 * Created by thakurprateek on 30-10-2015.
 */
public class Utils {

    public static final String WIKI_DOMAIN = "en.wikipedia.org";

    public static final String PARAM_ACTION = "action";

    public static final String PARAM_PROP = "prop";
    public static final String PARAM_FORMAT = "format";

    public static final String PARAM_PIPROP = "piprop";
    public static final String PARAM_THUMBNAILSIZE = "pithumbsize";
    public static final String PARAM_PIIMIT = "pilimit";
    public static final String PARAM_GENERATOR = "generator";
    public static final String PARAM_GPSLIMIT = "gpslimit";
    public static final String PARAM_GPSSEARCH = "gpssearch";

    public static final String RESPONSE_FIELD_QUERRY ="query";
    public static final String RESPONSE_FIELD_PAGES ="pages";



    private static Uri.Builder builder;


    public static boolean isEmpty(String str) {
        if (str == null)
            return true;

        if (str.trim().length() == 0)
            return true;

        if (str.trim().equals("null"))
            return true;

        return false;
    }


    //String url = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=100&pilimit=100&generator=prefixsearch&gpslimit=50&gpssearch=" + newText;



    public static final String buildUrl(String searchText){

        if(builder == null) {

            builder = new Uri.Builder();
            builder.scheme("https")
                    .authority(WIKI_DOMAIN)
                    .appendPath("w")
                    .appendPath("api.php")
                    .appendQueryParameter(PARAM_ACTION, "query")
                    .appendQueryParameter(PARAM_PROP, "pageimages")
                    .appendQueryParameter(PARAM_FORMAT, "json")
                    .appendQueryParameter(PARAM_PIPROP, "thumbnail")
                    .appendQueryParameter(PARAM_THUMBNAILSIZE, "100")
                    .appendQueryParameter(PARAM_PIIMIT, "50")
                    .appendQueryParameter(PARAM_GENERATOR, "prefixsearch")
                    .appendQueryParameter(PARAM_GPSLIMIT, "50");
        }
        String myUrl = builder.appendQueryParameter(PARAM_GPSSEARCH, searchText).build().toString();

        return myUrl;

    }
}
