package com.example.ivanbui.twittersearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        TextView tweetMessageTextView;
        TextView nameTextView;
        TextView screenNameTextView;
        ImageView profilePicImageView;

        public TweetHolder(View itemView) {
            super(itemView);
            tweetMessageTextView = (TextView) itemView.findViewById(R.id.tweet);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            screenNameTextView = (TextView) itemView.findViewById(R.id.screenname);
            profilePicImageView = (ImageView) itemView.findViewById(R.id.list_image);
        }

        public void bind(MyTweet tweet) {
            tweetMessageTextView.setText(tweet.text);
            nameTextView.setText(tweet.name);
            screenNameTextView.setText(tweet.screenName);

            String url = tweet.imgUrl;
            Context context = profilePicImageView.getContext();
            Picasso.with(context).load(url).into(profilePicImageView);
        }
    }

}


