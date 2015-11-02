package acompli.wikisearch;

import retrofit.http.*;
import rx.Observable;

/**
 * Created by thakurprateek on 02-11-2015.
 */
public interface WikiImageApi {

    @GET("/w/api.php/")
    Observable<PageResponse> items(@retrofit.http.Query("action") String action, @retrofit.http.Query("prop") String prop, @retrofit.http.Query("format") String format , @retrofit.http.Query("piprop") String piprop
            , @retrofit.http.Query("pithumbsize") String pithumbsize, @retrofit.http.Query("pilimit") String pilimit, @retrofit.http.Query("generator") String generator, @retrofit.http.Query("gpslimit") String gpslimit, @retrofit.http.Query("gpssearch") String gpssearch);
}
