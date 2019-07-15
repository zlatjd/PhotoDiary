package kr.effrot.photoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ShowFullImageActivity extends AppCompatActivity {

    ImageView iv_image_full_size;

    String image_uri;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_image);

        iv_image_full_size = findViewById(R.id.iv_image_full_size);

        Intent intent = getIntent();

        image_uri = intent.getStringExtra("image_uri");

        handler = new Handler();


        Glide.with(ShowFullImageActivity.this)
                .load(image_uri)
                .override(1000 , 1500)
                .into(iv_image_full_size);

    }

}
