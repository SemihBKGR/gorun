package com.semihbkgr.gorun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.gorun.AppConstants;
import com.semihbkgr.gorun.AppContext;
import com.semihbkgr.gorun.R;
import com.semihbkgr.gorun.snippet.CacheableSnippetService;
import com.semihbkgr.gorun.snippet.Snippet;
import com.semihbkgr.gorun.snippet.SnippetInfo;
import com.semihbkgr.gorun.util.http.ResponseCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {

    public static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        AppContext.initialize(getApplicationContext());

        new Handler(getMainLooper())
                .postDelayed(() -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }, AppConstants.Values.LOGO_ON_SCREEN_TIME_MS);

    }

    @Override
    protected void onStart() {
        super.onStart();

        AppContext.instance().snippetService.getAllSnippetInfosAsync(new ResponseCallback<List<SnippetInfo>>() {
            @Override
            public void onResponse(List<SnippetInfo> data) {
                AppContext.instance().executorService.execute(() -> {
                    Map<Integer, SnippetInfo> idSnippetInfoMap = new HashMap<>();
                    data.forEach(snippetInfo -> idSnippetInfoMap.put(snippetInfo.id, snippetInfo));
                    List<SnippetInfo> snippetInfoList = AppContext.instance().snippetService.getAllSavedSnippetInfos();
                    snippetInfoList.forEach(snippetInfo -> {
                        if (idSnippetInfoMap.containsKey(snippetInfo.id)) {
                            if (idSnippetInfoMap.get(snippetInfo.id).versionId != snippetInfo.versionId) {
                                Log.i(TAG, "onResponse: snippet version is old, snippetId: " + snippetInfo.id);
                                AppContext.instance().snippetService.getSnippetAsync(snippetInfo.id, new ResponseCallback<Snippet>() {
                                    @Override
                                    public void onResponse(Snippet data) {
                                        AppContext.instance().snippetService.update(data);
                                        Log.i(TAG, "onResponse: snippet is updated, snippetId: " + data.id);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        runOnUiThread(() -> Toast.makeText(StartActivity.this, "Request error", Toast.LENGTH_SHORT).show());
                                    }
                                });
                            } else
                                Log.i(TAG, "onResponse: snippet is up-to-date, snippetId: " + snippetInfo.id);
                        } else {
                            AppContext.instance().snippetService.delete(snippetInfo.id);
                            Log.i(TAG, "onResponse: snippet deleted, snippetId: " + snippetInfo.id);
                        }
                    });
                });
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> Toast.makeText(StartActivity.this, "Connection error", Toast.LENGTH_SHORT).show());
            }
        });

        if (AppContext.instance().snippetService instanceof CacheableSnippetService)
            AppContext.instance().scheduledExecutorService.scheduleWithFixedDelay(((CacheableSnippetService) AppContext.instance().snippetService)::clearExpiredCaches, AppConstants.Values.CACHE_EXPIRED_CLEAR_TIME_INTERVAL_MS, AppConstants.Values.CACHE_EXPIRED_CLEAR_TIME_INTERVAL_MS, TimeUnit.MILLISECONDS);


    }

}
