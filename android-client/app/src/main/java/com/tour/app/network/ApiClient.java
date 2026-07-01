package com.tour.app.network;

import android.content.Context;
import com.tour.app.config.AppConfig;
import com.tour.app.utils.AuthManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor authInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    String token = null;
                    if (context != null) {
                        token = AuthManager.getInstance(context).getAuthToken();
                    }

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Content-Type", "application/json");

                    if (token != null) {
                        requestBuilder.header("Authorization", "Bearer " + token);
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            };
            
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(authInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            
            String baseUrl = BASE_URL_EMULATOR;
            if (context != null) {
                baseUrl = AppConfig.getInstance(context).getApiBaseUrl();
            }
            
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    
    private static final String BASE_URL_EMULATOR = "http://10.0.2.2:8888/api/";
    private static final String BASE_URL_LOCAL = "http://localhost:8888/api/";
    private static final String BASE_URL_DEVICE = "http://192.168.3.21:8888/api/";
    private static final String BASE_URL_PROD = "https://your-domain.com/api/";
    
    public static AuthService getAuthService(Context context) {
        return getClient(context).create(AuthService.class);
    }
    
    public static AuthService getAuthService() {
        return getClient(null).create(AuthService.class);
    }
    
    public static UserService getUserService(Context context) {
        return getClient(context).create(UserService.class);
    }
    
    public static UserService getUserService() {
        return getClient(null).create(UserService.class);
    }
    
    public static SpotService getSpotService(Context context) {
        return getClient(context).create(SpotService.class);
    }
    
    public static SpotService getSpotService() {
        return getClient(null).create(SpotService.class);
    }
    
    public static StrategyService getStrategyService(Context context) {
        return getClient(context).create(StrategyService.class);
    }
    
    public static StrategyService getStrategyService() {
        return getClient(null).create(StrategyService.class);
    }
    
    public static TripService getTripService(Context context) {
        return getClient(context).create(TripService.class);
    }
    
    public static TripService getTripService() {
        return getClient(null).create(TripService.class);
    }
}
