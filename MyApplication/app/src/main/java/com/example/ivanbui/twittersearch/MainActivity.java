package com.example.ivanbui.twittersearch;

import android.content.Context;
import android.net.Uri;
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

    private String searchTerm = "";
    private ViewGroup tweetContainer;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<MyTweet> data;
    private EditText editText;
    private ProgressBar progressBar;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private String maxId = "";
    private String sinceId = "";
    private boolean paging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        data = new ArrayList<MyTweet>();
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


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if(dy > 0){
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

    private String nextResults;

    private void pageTwitter(){
        progressBar.setVisibility(View.VISIBLE);
        try {
            TwitterServiceBuilder.getTwitterService().page(searchTerm, maxId, sinceId).enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    sinceId = response.body().getMetadata().getSinceId();
                    nextResults = response.body().getMetadata().getNextResults();

                    Uri results = Uri.parse(nextResults);
                    maxId = results.getQueryParameter("max_id");

                    for (Status status : response.body().getStatuses()) {
                        MyTweet myResult = new MyTweet();
                        myResult.text = status.getText();
                        myResult.name = status.getUser().getName();
                        myResult.screenName = "@" + status.getUser().getScreenName();
                        myResult.imgUrl = status.getUser().getProfileImageUrl();
                        data.add(myResult);
                        adapter.notifyDataSetChanged();
                    }
                    paging = false;
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    Log.d("Twitter", t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickHandler(View view) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        searchTwitter();
    }

    private void searchTwitter() {
        data.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        searchTerm = editText.getText().toString();
        if (!searchTerm.equals("")){
            TwitterServiceBuilder.getTwitterService().search(searchTerm).enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    maxId = response.body().getMetadata().getMaxId();
                    sinceId = response.body().getMetadata().getSinceId();
                    for (Status status : response.body().getStatuses()) {
                        MyTweet myResult = new MyTweet();
                        myResult.text = status.getText();
                        myResult.name = status.getUser().getName();
                        myResult.screenName = "@" + status.getUser().getScreenName();
                        myResult.imgUrl = status.getUser().getProfileImageUrl();
                        data.add(myResult);
                        adapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    Log.d("Twitter", t.toString());
                }
            });
        }
    }
}
