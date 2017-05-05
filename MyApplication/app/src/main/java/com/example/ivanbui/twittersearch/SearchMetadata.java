package com.example.ivanbui.twittersearch;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ivanbui on 2017-05-04.
 */

public class SearchMetadata {
    @SerializedName("max_id_str")
    private String max_id;
    @SerializedName("since_id_str")
    private String since_id;
    @SerializedName("next_results")
    private String next_results;

    public String getMaxId(){return max_id;}
    public String getSinceId(){return since_id;}

    public String getNextResults() {
        return next_results;
    }
}
