package com.example.ivanbui.twittersearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ivanbui on 2017-05-05.
 */

public class SearchMetadataResponse {

    @SerializedName("search_metadata")
    private String search_metadata;
    public String getMetadata() {return search_metadata;}
}
