package acompli.wikisearch;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static java.lang.String.format;

/**
 * A placeholder fragment containing a simple view.
 */
public class WikiSearchImageActivityFragment extends Fragment {

    ProgressWheel progressWheel;

    private RecyclerView mRecyclerView;
    private ImageListAdapter imageListAdapter;
    List<Page> pages;
    private EditText etSearch;

    private CompositeSubscription compositeSubscription;



    private WikiImageApi api;


    public WikiSearchImageActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = createWikiImageApi();
        pages = new ArrayList<>();

        compositeSubscription = new CompositeSubscription();


        imageListAdapter = new ImageListAdapter(getActivity(), pages);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wiki_search_image, container, false);

        progressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        etSearch = (EditText) view.findViewById(R.id.etSearch);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressWheel.stopSpinning();


        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                getResources().getInteger(R.integer.number_of_colums_in_grid), LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(imageListAdapter);

        mRecyclerView.setItemAnimator(new FlipInTopXAnimator());

        //This observer will only be fired when user has finished typing
        RxTextView.textChangeEvents(etSearch)//
                .debounce(400, TimeUnit.MILLISECONDS)// default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(_getSearchObserver());
    }


    private WikiImageApi createWikiImageApi() {

        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(
                "http://en.wikipedia.org/")
                .setConverter(new GsonConverter(new Gson()))
                .setLogLevel(RestAdapter.LogLevel.FULL);



        return builder.build().create(WikiImageApi.class);
    }


    /**
     * Creates a observer observe the results of images
     * @return the observer
     */
    private Observer<TextViewTextChangeEvent> _getSearchObserver() {
        return new Observer<TextViewTextChangeEvent>() {
            @Override
            public void onCompleted() {
                if(progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                }
            }

            @Override
            public void onError(Throwable e) {
                if(progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                }
            }

            @Override
            public void onNext(TextViewTextChangeEvent onTextChangeEvent) {
                Log.d("TAG", format("Searching for %s", onTextChangeEvent.text().toString()));
                String searchQuery = onTextChangeEvent.text().toString();

                //Cancels all subscription that are no longer valid
                compositeSubscription.clear();

                imageListAdapter.getPages().clear();

                imageListAdapter.notifyDataSetChanged();



                if(TextUtils.isEmpty(searchQuery)){
                    progressWheel.stopSpinning();
                    return;
                }

                progressWheel.spin();

                //Adds a subscription
                compositeSubscription.add(createSubscriptionForQuery(searchQuery));




            }
        };
    }

    public Subscription createSubscriptionForQuery(String searchQuery){
        //Logic for caching the request so that it doesnt fetch again on configuration change
        Observable<PageResponse> observable = RequestCache.getInstance().observableForQuery(searchQuery);

        if(observable == null) {

            observable = api.items("query", "pageimages", "json", "thumbnail", "100", "50", "prefixsearch", "50", searchQuery).cache();

            RequestCache.getInstance().storeRequestInCache(observable, searchQuery);

        }

        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<PageResponse, Observable<Page>>() {
                    @Override
                    public Observable<Page> call(PageResponse urls) {
                        return Observable.from(urls.getQuery().getPages().values());
                    }
                })
                .subscribe(new Observer<Page>() {
                    @Override
                    public void onCompleted() {

                        Log.d("TAG", "onCompleted CALLED");
                        if (progressWheel.isSpinning()) {
                            progressWheel.stopSpinning();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();

                        if (progressWheel.isSpinning()) {
                            progressWheel.stopSpinning();
                        }

                    }

                    @Override
                    public void onNext(Page response) {

                        //This will only be called once a image item is ready

                        imageListAdapter.addAt(imageListAdapter.getPages().size(), response);

                    }
                });

        return subscription;

    }
}
