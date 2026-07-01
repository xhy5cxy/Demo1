package com.tour.app.network;

import com.tour.app.model.Strategy;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

/**
 * 攻略相关API接口
 * 匹配后端接口格式
 */
public interface StrategyService {
    
    /**
     * 获取攻略列表
     */
    @GET("strategies")
    Call<ApiResponse<List<Strategy>>> getStrategies(
            @Query("page") int page,
            @Query("size") int size,
            @Query("destination") String destination,
            @Query("status") Integer status
    );
    
    /**
     * 获取攻略详情
     */
    @GET("strategies/{id}")
    Call<ApiResponse<Strategy>> getStrategyById(@Path("id") Long id);
    
    /**
     * 搜索攻略
     */
    @GET("strategies/search")
    Call<ApiResponse<List<Strategy>>> searchStrategies(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("size") int size
    );
    
    /**
     * 获取热门攻略
     */
    @GET("strategies/hot")
    Call<ApiResponse<List<Strategy>>> getHotStrategies(@Query("limit") int limit);
    
    /**
     * 上传攻略图片
     */
    @Multipart
    @POST("strategies/upload-image")
    Call<ApiResponse<String>> uploadStrategyImage(
            @Part("strategyId") RequestBody strategyId,
            @Part MultipartBody.Part imageFile
    );
    
    /**
     * 创建攻略
     */
    @POST("strategies")
    Call<ApiResponse<Strategy>> createStrategy(@retrofit2.http.Body Strategy strategy);
}
