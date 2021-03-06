package com.semihbkgr.gorun.snippet.client;

import android.util.Log;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.semihbkgr.gorun.AppConstants;
import com.semihbkgr.gorun.snippet.Snippet;
import com.semihbkgr.gorun.snippet.SnippetInfo;
import com.semihbkgr.gorun.util.http.ErrorResponseModel;
import com.semihbkgr.gorun.util.http.RequestException;
import com.semihbkgr.gorun.util.http.ResponseCallback;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SnippetClientImpl implements SnippetClient {

    private static final String TAG = SnippetClientImpl.class.getName();

    private final OkHttpClient httpClient;
    private final Gson gson;

    public SnippetClientImpl(OkHttpClient httpClient, Gson gson) {
        this.httpClient = httpClient;
        this.gson = gson;
    }

    @Nullable
    @Override
    public SnippetInfo[] getAllSnippetInfos() throws RequestException {
        Request request = new Request.Builder()
                .url(AppConstants.Nets.SERVER_SNIPPET_HTTP_URI)
                .build();
        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                try (ResponseBody body = response.body()) {
                    return gson.fromJson(response.body().string(), SnippetInfo[].class);
                }
            } else {
                try (ResponseBody body = response.body()) {
                    throw new RequestException(gson.fromJson(response.body().string(), ErrorResponseModel.class));
                }
            }
        } catch (Exception e) {
            throw new RequestException(e);
        }
    }

    @Override
    public void getAllSnippetInfosAsync(ResponseCallback<? super SnippetInfo[]> callback) {
        Request request = new Request.Builder().url(AppConstants.Nets.SERVER_SNIPPET_HTTP_URI).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: AllSnippetInfos request failed");
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "onResponse: AllSnippetInfos response received successfully");
                try {
                    if (response.code() == 200) {
                        SnippetInfo[] snippetInfos = gson.fromJson(response.body().string(), SnippetInfo[].class);
                        Log.i(TAG, "onResponse: AllSnippetInfos response deserialized successfully");
                        callback.onResponse(snippetInfos);
                    } else {
                        RequestException exception = new RequestException(gson.fromJson(response.body().string(), ErrorResponseModel.class));
                        Log.e(TAG, "onResponse: AllSnippetInfos response processing failed", exception);
                        callback.onFailure(exception);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: AllSnippetInfos response processing failed");
                    callback.onFailure(e);
                } finally {
                    response.close();
                }
            }
        });
    }

    @Override
    public Snippet getSnippet(int id) throws RequestException {
        String url = AppConstants.Nets.SERVER_SNIPPET_HTTP_URI + '/' + id;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                try (ResponseBody body = response.body()) {
                    return gson.fromJson(response.body().string(), Snippet.class);
                }
            } else {
                try (ResponseBody body = response.body()) {
                    throw new RequestException(gson.fromJson(response.body().string(), ErrorResponseModel.class));
                }
            }
        } catch (Exception e) {
            throw new RequestException(e);
        }
    }

    @Override
    public void getSnippetAsync(int id, ResponseCallback<? super Snippet> callback) {
        String url = AppConstants.Nets.SERVER_SNIPPET_HTTP_URI + '/' + id;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: Snippet request failed", e);
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "onResponse: Snippet response received successfully");
                try {
                    if (response.code() == 200) {
                        Snippet snippet = gson.fromJson(response.body().string(), Snippet.class);
                        Log.i(TAG, "onResponse: Snippet response deserialized successfully");
                        callback.onResponse(snippet);
                    } else {
                        RequestException exception = new RequestException(gson.fromJson(response.body().string(), ErrorResponseModel.class));
                        Log.e(TAG, "onResponse: AllSnippetInfos response processing failed", exception);
                        callback.onFailure(exception);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: Snippet response processing failed", e);
                    callback.onFailure(e);
                } finally {
                    response.close();
                }
            }
        });
    }

}
