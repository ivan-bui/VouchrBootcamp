package com.example.ivanbui.twittersearch;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ViewGroup tweetContainer;
    private RecyclerView recyclerView;
    private EditText editText;
    private ProgressBar progressBar;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private MyAdapter adapter;

    private String searchTerm = "";
    private List<MyTweet> data;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private String maxId = "";
    private String sinceId = "";
    private boolean paging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.pb);
        data = new ArrayList<>();
        editText = (EditText) findViewById(R.id.editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        tweetContainer = (ViewGroup) findViewById(R.id.linearLayout);
        recyclerView = (RecyclerView) findViewById(R.id.drawerList);

        adapter = new MyAdapter(data);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        Log.v("...", "Last Item Wow !");
                        if (!paging) {
                            pageTwitter();
                            paging = true;
                        }
                    }
                }
            }
        });
    }

    /**
     * Queries against the indices of recent or popular Tweets using the editText value
     */
    private void searchTwitter() {
        data.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        searchTerm = editText.getText().toString();
        if (!searchTerm.equals("")) {
            TwitterServiceBuilder.getTwitterService().search(searchTerm).enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    maxId = response.body().getMetadata().getMaxId();
                    handleResults(response);
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    Log.d("Twitter", t.toString());
                }
            });
        }
    }

    /**
     * Request next set of results
     */
    private void pageTwitter() {
        progressBar.setVisibility(View.VISIBLE);

        retrofit2.Call v = TwitterServiceBuilder.getTwitterService().page(searchTerm, maxId, sinceId);
        TwitterServiceBuilder.getTwitterService().page(searchTerm, maxId, sinceId).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                String nextResults = response.body().getMetadata().getNextResults();
                Uri results = Uri.parse(nextResults);
                maxId = results.getQueryParameter("max_id");

                handleResults(response);
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d("Twitter", t.toString());
            }
        });
    }

    private void handleResults(Response<SearchResponse> response) {
        sinceId = response.body().getMetadata().getSinceId();
        for (Status status : response.body().getStatuses()) {
            data.add(getMyTweet(status));
            adapter.notifyDataSetChanged();
        }
        paging = false;
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Hides the keyboard when clicking away from editText field
     */
    public void clickHandler(View view) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        searchTwitter();
    }

    @NonNull
    private MyTweet getMyTweet(Status status) {
        return new MyTweet.Builder()
                        .text(status.getText())
                        .name(status.getUser().getName())
                        .screenName(status.getUser().getScreenName())
                        .image(status.getUser().getProfileImageUrl())
                        .build();
    }
}
