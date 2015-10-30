package acompli.wikisearch;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;


public class ImageSearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageListAdapter imageListAdapter;
    List<Page> pages;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue queue;
    private static final String TAG = "requestTag";
    private TextView tvNoContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        tvNoContent = (TextView) findViewById(R.id.tvNoContent);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        enableSwipeRefresh(false);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                getResources().getInteger(R.integer.number_of_colums_in_grid), LinearLayoutManager.VERTICAL));

        pages = new ArrayList<>();

        imageListAdapter = new ImageListAdapter(this, pages);
        mRecyclerView.setAdapter(imageListAdapter);

        //Adding animation to elements
        mRecyclerView.setItemAnimator(new FlipInTopXAnimator());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Importing a collapasable search widget that will be used in image search
        getMenuInflater().inflate(R.menu.menu_image_search, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(
                        Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        //Listen to the changes in text
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!Utils.isEmpty(query)) {

                    searchImages(query);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!Utils.isEmpty(newText)) {

                    searchImages(newText);

                }

                return false;
            }
        });

        return true;
    }

    private void searchImages(String searchQuery) {
        //Create a volley request queue
        queue = Volley.newRequestQueue(ImageSearchActivity.this);

        //Cancel if a request is created, This will be called again and again so callback of old requests shouldn't be executed, hence cancel
        if (jsonObjectRequest != null) {
            jsonObjectRequest.cancel();
        }

        showLoading();

        //Generate Url

        String url = Utils.buildUrl(searchQuery);//"https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=100&pilimit=100&generator=prefixsearch&gpslimit=50&gpssearch=" + newText;

        //Create a get request

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {

                JSONObject pagesContainer = null;


                try {
                    if (response.has(Utils.RESPONSE_FIELD_QUERRY) && response.getJSONObject(Utils.RESPONSE_FIELD_QUERRY)
                            .has(Utils.RESPONSE_FIELD_PAGES)) {
                        pagesContainer = response.getJSONObject(Utils.RESPONSE_FIELD_QUERRY)
                                .getJSONObject(Utils.RESPONSE_FIELD_PAGES);

                        if (pagesContainer.keys().hasNext()) {
                            hideNoContent();
                        } else {
                            showNoContent();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    showNoContent();
                }

                imageListAdapter.refreshAdapter(pagesContainer);

                hideLoading();

            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {

                showNoContent(getResources().getString(R.string.errorOccured));

                imageListAdapter.refreshAdapter(null);

                hideLoading();

            }
        }

        );

        jsonObjectRequest.setTag(TAG);

        //Add to queue, also means execute request
        queue.add(jsonObjectRequest);

    }


    @Override
    protected void onStop () {
        super.onStop();
        //When activity stops cancel all request to avoid any pending callback that will be called
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    /**
     * Shows a progress circle below the action bar
     */
    public void showLoading(){
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    /**
     * Hides the progress circle if showing
     */
    public void hideLoading(){
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(false);
    }

    /**
     * Disables or enables swipe down to refresh UI. WIll normally be disabled in our case as such a functionality is not implemented
     * @param enabled to be enabled or disables
     */
    public void enableSwipeRefresh(boolean enabled){
        mSwipeRefreshLayout.setEnabled(false);
    }


    /**
     *Shows a message when there are no images to show on the screen
     */
    public void showNoContent(){
        tvNoContent.setText(R.string.nothingToShow);
        tvNoContent.setVisibility(View.VISIBLE);
    }

    /**
     * Shows a custom message when there are no images to shoe on the screen
     * @param message the message to be shown
     */
    public void showNoContent(String message){
        tvNoContent.setText(message);
        tvNoContent.setVisibility(View.VISIBLE);
    }

    public void hideNoContent(){
        tvNoContent.setVisibility(View.GONE);
    }


}
