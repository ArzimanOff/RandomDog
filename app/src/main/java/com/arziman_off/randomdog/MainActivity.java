package com.arziman_off.randomdog;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private MainViewModel mainViewModel;

    private ImageView mainImageView;
    private ProgressBar progressBar;
    private MaterialButton btnNewImageLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initViews();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.generateDog();
        mainViewModel.getDogImageMutableLiveData().observe(
                this,
                new Observer<DogImage>() {
                    @Override
                    public void onChanged(DogImage dogImage) {
                        Glide
                                .with(MainActivity.this)
                                .load(dogImage.getMessage())
                                .centerCrop()
                                .into(mainImageView);
                    }
                });
    }

    private void initViews() {
        mainImageView = findViewById(R.id.mainImageView);
        progressBar = findViewById(R.id.newImageLoader);
        btnNewImageLoad = findViewById(R.id.btnNewImageLoad);
    }

}