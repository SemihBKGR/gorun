package com.semihbg.gorun.util;

import com.semihbg.gorun.AppConstants;
import com.semihbg.gorun.AppContext;
import com.semihbg.gorun.setting.AppSetting;
import com.semihbg.gorun.setting.ServerStateType;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.util.concurrent.ForkJoinPool;

public class StateUtils {

    public static boolean hasInternetConnection() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ServerStateType serverStateType() {
        Request request = new Request.Builder()
                .url(AppConstants.SERVER_CODE_STATE_URI)
                .method("GET", null)
                .build();
        try {
            ResponseBody responseBody = AppContext.httpClient.newCall(request).execute().body();
            String responseString = responseBody.string();
            return ServerStateType.of(responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerStateType.DOWN;
    }

}