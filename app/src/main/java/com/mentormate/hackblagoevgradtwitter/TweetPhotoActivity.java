package com.mentormate.hackblagoevgradtwitter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TweetPhotoActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "TweetPhotoActivity";

    @Bind(R.id.img_tweet)
    ImageView imgTweet;
    @Bind(R.id.edt_tweet_text)
    EditText edtPost;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_photo);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_upload)
    public void dispatchTakePictureIntent() {
        if (downloadUrl == null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(this, "Wait for previous picture to upload", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgTweet.setImageBitmap(imageBitmap);
            uploadFile(imageBitmap);
        }

    }

    private void uploadFile(Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        StorageReference imageRef = FirebaseDatabaseHelper.getInstance()
                .getImageStorage("image" + timeStamp + ".png");

        byte[] byteArray = getBitmapBytes(bitmap);

        imageRef.putBytes(byteArray)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUrl = taskSnapshot.getDownloadUrl().getPath();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(TweetPhotoActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private byte[] getBitmapBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @OnClick(R.id.btn_tweet)
    public void onTweetPressed() {
        if (progressBar.getVisibility() == View.INVISIBLE && downloadUrl != null) {
            TwitterPost post = new TwitterPost(edtPost.getText().toString(), downloadUrl);
            FirebaseDatabaseHelper.getInstance().postTweet(post);
            finish();
        } else {
            Toast.makeText(this, "No image attached yet", Toast.LENGTH_SHORT).show();
        }
    }
}
