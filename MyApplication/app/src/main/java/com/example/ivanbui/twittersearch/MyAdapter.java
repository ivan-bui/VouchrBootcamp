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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    List<Status> data = Collections.emptyList();
    List<MyTweet> statusList;

    public MyAdapter(List<MyTweet> statusList){
        this.statusList = statusList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyTweet current = statusList.get(position);
        holder.text.setText(current.text);
        holder.name.setText(current.name);
        holder.screenName.setText(current.screenName);

        String url = current.imgUrl;
        Context context = holder.profilePic.getContext();
        Picasso.with(context).load(url).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        TextView name;
        TextView screenName;
        ImageView profilePic;

        public MyViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.tweet);
            name = (TextView) itemView.findViewById(R.id.name);
            screenName = (TextView) itemView.findViewById(R.id.screenname);
            profilePic = (ImageView) itemView.findViewById(R.id.list_image);
        }
    }

}


