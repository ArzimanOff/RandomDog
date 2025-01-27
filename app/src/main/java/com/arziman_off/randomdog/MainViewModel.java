package com.arziman_off.randomdog;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "MainViewModel";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<DogImage> dogImageMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> internetError = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<DogImage> getDogImageMutableLiveData() {
        return dogImageMutableLiveData;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<Boolean> getInternetError() {
        return internetError;
    }

    public void generateDog() {
        Disposable disposable = loadDogImageRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(
                        new Consumer<DogImage>() {
                            @Override
                            public void accept(DogImage dogImage) throws Throwable {
                                internetError.setValue(false);
                                dogImageMutableLiveData.setValue(dogImage);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                internetError.setValue(true);
                                Log.d(LOG_TAG, "чзх" + throwable.getMessage());
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private Single<DogImage> loadDogImageRx() {
        return ApiFactory.getApiService().loadDogImage();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
