package com.example.ivanbui.twittersearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by ivanbui on 2017-05-04.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.TweetHolder>{
    private LayoutInflater inflater;
    List<MyTweet> statusList;

    public MyAdapter(List<MyTweet> statusList){
        this.statusList = statusList;
    }

    @Override
    public TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new TweetHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetHolder holder, int position) {
        MyTweet current = statusList.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class TweetHolder extends RecyclerView.ViewHolder{

        TextView text;
        TextView name;
        TextView screenName;
        ImageView profilePic;

        public TweetHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.tweet);
            name = (TextView) itemView.findViewById(R.id.name);
            screenName = (TextView) itemView.findViewById(R.id.screenname);
            profilePic = (ImageView) itemView.findViewById(R.id.list_image);
        }

        public void bind(MyTweet tweet) {
            text.setText(tweet.text);
            name.setText(tweet.name);
            screenName.setText(tweet.screenName);

            String url = tweet.imgUrl;
            Context context = profilePic.getContext();
            Picasso.with(context).load(url).into(profilePic);
        }
    }

}


