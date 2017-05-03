package com.example.ivanbui.twittersearch;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;

import java.util.List;

/**
 * Created by ivanbui on 2017-05-03.
 */

public class MyResponseClass {
    public List<Integer> statuses;
    public Metadata search_metadata;
}


