package com.example.ivanbui.twittersearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ivanbui on 2017-05-03.
 */

public class SearchResponse {

    @SerializedName("statuses")
    private List<Status> statuses;

    @SerializedName("search_metadata")
    private SearchMetadata search_metadata;

    public SearchMetadata getMetadata() {return search_metadata;}

    public List<Status> getStatuses() {
        return statuses;
    }

}