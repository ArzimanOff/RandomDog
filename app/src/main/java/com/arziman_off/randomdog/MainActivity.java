package com.arziman_off.randomdog;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        setContentView(R.layout.activity_main);
        initViews();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.generateDog();
        mainViewModel.getDogImageMutableLiveData()
                .observe(
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
                        }
                );
        mainViewModel.getIsLoading()
                .observe(
                        this,
                        new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean isLoading) {
                                if (isLoading) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    mainImageView.setVisibility(View.GONE);
                                } else {
                                    mainImageView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }
                );
        mainViewModel.getInternetError()
                .observe(this,
                        new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean internetError) {
                                if (internetError) {
                                    Toast toast = Toast.makeText(
                                            MainActivity.this,
                                            R.string.internet_error_toast_text,
                                            Toast.LENGTH_SHORT
                                    );
                                    toast.setGravity(Gravity.BOTTOM, 0, 250);
                                    toast.show();
                                }
                            }
                        }
                );
        btnNewImageLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.generateDog();
            }
        });
    }

    private void initViews() {
        mainImageView = findViewById(R.id.mainImageView);
        progressBar = findViewById(R.id.newImageLoader);
        btnNewImageLoad = findViewById(R.id.btnNewImageLoad);
    }
}