package com.mentormate.hackblagoevgradtwitter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetHolder> {

    private List<TwitterPost> tweets;

    public TweetsAdapter() {
        this.tweets = new ArrayList<>();
    }

    public void setTweets(List<TwitterPost> tweets) {
        if (tweets != null) {
            this.tweets = tweets;
            notifyDataSetChanged();
        }
    }

    @Override
    public TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_tweet, parent, false);
        return new TweetHolder(v);
    }

    @Override
    public void onBindViewHolder(TweetHolder holder, int position) {
        TwitterPost post = tweets.get(position);
        holder.txtTweetSender.setText(post.sender);
        holder.txtTweetMessage.setText(post.message);
        if (post.picUrl != null) {
            downloadImage(holder, post.picUrl);
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    class TweetHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_tweet_sender)
        TextView txtTweetSender;
        @Bind(R.id.txt_tweet_message)
        TextView txtTweetMessage;
        @Bind(R.id.img_tweet)
        ImageView imgTweet;

        public TweetHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void downloadImage(final TweetHolder holder, String imageUrl) {
        String imageName = imageUrl.substring(imageUrl.lastIndexOf("/"));
        FirebaseDatabaseHelper.getInstance().getImageStorage(imageName).getBytes(4000000)
                .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        Bitmap bitmap = convertToBitmap(task.getResult());
                        holder.imgTweet.setImageBitmap(bitmap);
                    }
                });
    }

    private Bitmap convertToBitmap(byte[] bitmapdata) {
        return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }
}
