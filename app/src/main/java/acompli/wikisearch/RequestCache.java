package acompli.wikisearch;

import rx.Observable;

/**
 * Created by thakurprateek on 03-11-2015.
 */
public class RequestCache {

    private Observable<PageResponse> observable;

    private String observableForQuery;

    private static RequestCache ourInstance = new RequestCache();

    public static RequestCache getInstance() {
        return ourInstance;
    }

    private RequestCache() {
    }

    public Observable<PageResponse> observableForQuery(String query){
        if(query.equals(observableForQuery)){
            return observable;
        }

        return null;
    }

    public void storeRequestInCache(Observable<PageResponse> observable, String observableForQuery){

        setObservable(observable);

        setObservableForQuery(observableForQuery);

    }

    public Observable<PageResponse> getObservable() {
        return observable;
    }

    public void setObservable(Observable<PageResponse> observable) {
        this.observable = observable;
    }

    public String getObservableForQuery() {
        return observableForQuery;
    }

    public void setObservableForQuery(String observableForQuery) {
        this.observableForQuery = observableForQuery;
    }
}
