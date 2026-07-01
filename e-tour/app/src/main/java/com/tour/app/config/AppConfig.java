package com.tour.app.config;

import android.content.Context;
import android.content.res.Resources;

import com.tour.app.BuildConfig;
import com.tour.app.R;

public class AppConfig {

    private static AppConfig instance;
    private final Context context;
    private final Resources resources;

    private AppConfig(Context context) {
        this.context = context.getApplicationContext();
        this.resources = this.context.getResources();
    }

    public static synchronized AppConfig getInstance(Context context) {
        if (instance == null) {
            instance = new AppConfig(context);
        }
        return instance;
    }

    public String getApiBaseUrl() {
        if (BuildConfig.DEBUG) {
            return "http://10.0.2.2:8888/api/";
        }
        return "https://your-domain.com/api/";
    }

    public String getLocalApiUrl() {
        return "http://localhost:8888/api/";
    }

    public String getDeviceApiUrl() {
        return "http://192.168.3.21:8888/api/";
    }

    public String getProdApiUrl() {
        return "https://your-domain.com/api/";
    }

    public String getAmapApiKey() {
        return resources.getString(R.string.amap_api_key);
    }

    public String getModelScopeApiKey() {
        return resources.getString(R.string.modelscope_api_key);
    }

    public String getModelScopeApiUrl() {
        return resources.getString(R.string.modelscope_api_url);
    }

    public String getModelScopeModel() {
        return resources.getString(R.string.modelscope_model);
    }

    public String getLocalAiServiceUrl() {
        return resources.getString(R.string.local_ai_service_url);
    }
}
